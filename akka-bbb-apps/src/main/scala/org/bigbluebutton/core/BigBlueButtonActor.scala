package org.bigbluebutton.core

import akka.actor._
import akka.actor.ActorLogging
import akka.util.Timeout
import scala.concurrent.duration._
import org.bigbluebutton.core.bus._
import org.bigbluebutton.core.api._
import org.bigbluebutton.SystemConfiguration
import org.bigbluebutton.core.domain._

object BigBlueButtonActor extends SystemConfiguration {
  def props(system: ActorSystem,
    eventBus: IncomingEventBus,
    outGW: OutMessageGateway): Props =
    Props(classOf[BigBlueButtonActor], system, eventBus, outGW)
}

class BigBlueButtonActor(val system: ActorSystem,
    eventBus: IncomingEventBus, outGW: OutMessageGateway) extends Actor with ActorLogging {

  implicit def executionContext = system.dispatcher
  implicit val timeout = Timeout(5 seconds)

  private var meetings = new collection.immutable.HashMap[String, RunningMeeting]

  def receive = {
    case msg: CreateMeeting => handleCreateMeeting(msg)
    case msg: DestroyMeeting => handleDestroyMeeting(msg)
    case msg: KeepAliveMessage => handleKeepAliveMessage(msg)
    case msg: PubSubPing => handlePubSubPingMessage(msg)
    case msg: ValidateAuthToken => handleValidateAuthToken(msg)
    case msg: GetAllMeetingsRequest => handleGetAllMeetingsRequest(msg)
    // case _ => // do nothing
  }

  private def handleValidateAuthToken(msg: ValidateAuthToken) {
    meetings.get(msg.meetingId.value) foreach { m =>
      m.actorRef ! msg

      //      val future = m.actorRef.ask(msg)(5 seconds)
      //      future onComplete {
      //        case Success(result) => {
      //          log.info("Validate auth token response. meetingId=" + msg.meetingID + " userId=" + msg.userId + " token=" + msg.token)
      //          /**
      //           * Received a reply from MeetingActor which means hasn't hung!
      //           * Sometimes, the actor seems to hang and doesn't anymore accept messages. This is a simple
      //           * audit to check whether the actor is still alive. (ralam feb 25, 2015)
      //           */
      //        }
      //        case Failure(failure) => {
      //          log.warning("Validate auth token timeout. meetingId=" + msg.meetingID + " userId=" + msg.userId + " token=" + msg.token)
      //          outGW.send(new ValidateAuthTokenTimedOut(msg.meetingID, msg.userId, msg.token, false, msg.correlationId))
      //        }
      //      }
    }
  }

  private def handleKeepAliveMessage(msg: KeepAliveMessage): Unit = {
    outGW.send(new KeepAliveMessageReply(msg.aliveID))
  }

  private def handlePubSubPingMessage(msg: PubSubPing): Unit = {
    outGW.send(new PubSubPong(msg.system, msg.timestamp))
  }

  private def handleDestroyMeeting(msg: DestroyMeeting) {
    log.info("Received DestroyMeeting message for meetingId={}", msg.meetingId)
    meetings.get(msg.meetingId.value) match {
      case None => log.info("Could not find meetingId={}", msg.meetingId.value)
      case Some(m) => {
        meetings -= msg.meetingId.value
        log.info("Kick everyone out on meetingId={}", msg.meetingId)
        if (m.mProps.isBreakout) {
          log.info("Informing parent meeting {} that a breakout room has been ended{}", m.mProps.extId, m.mProps.id)
          eventBus.publish(BigBlueButtonEvent(m.mProps.extId.value,
            BreakoutRoomEnded(m.mProps.extId.value, m.mProps.id.value)))
        }
        outGW.send(new EndAndKickAll(msg.meetingId, m.mProps.recorded))
        outGW.send(new DisconnectAllUsers(msg.meetingId))
        log.info("Destroyed meetingId={}", msg.meetingId)
        outGW.send(new MeetingDestroyed(msg.meetingId))

        /** Unsubscribe to meeting and voice events. **/
        eventBus.unsubscribe(m.actorRef, m.mProps.id.value)
        eventBus.unsubscribe(m.actorRef, m.mProps.voiceConf.value)

        // Stop the meeting actor.
        context.stop(m.actorRef)
      }
    }
  }

  private def handleCreateMeeting(msg: CreateMeeting): Unit = {
    meetings.get(msg.meetingId.value) match {
      case None =>
        log.info("Create meeting request. meetingId={}", msg.mProps.id)

        var m = RunningMeeting(msg.mProps, outGW, eventBus)

        /** Subscribe to meeting and voice events. **/
        eventBus.subscribe(m.actorRef, m.mProps.id.value)
        eventBus.subscribe(m.actorRef, m.mProps.voiceConf.value)

        meetings += m.mProps.id.value -> m
        outGW.send(new MeetingCreated(m.mProps.id, m.mProps.extId, m.mProps.recorded, m.mProps.name.value,
          m.mProps.voiceConf.value, msg.mProps.duration, msg.mProps.moderatorPass,
          msg.mProps.viewerPass, msg.mProps.createTime, msg.mProps.createDate))

        m.actorRef ! new InitializeMeeting(m.mProps.id, m.mProps.recorded)

      case Some(m) =>
        log.info("Meeting already created. meetingID={}", msg.mProps.id)
      // do nothing

    }
  }

  private def handleGetAllMeetingsRequest(msg: GetAllMeetingsRequest) {

    var len = meetings.keys.size
    println("meetings.size=" + meetings.size)
    println("len_=" + len)

    val set = meetings.keySet
    val arr: Array[String] = new Array[String](len)
    set.copyToArray(arr)
    val resultArray: Array[MeetingInfo] = new Array[MeetingInfo](len)

    for (i <- 0 until arr.length) {
      val id = arr(i)
      val duration = meetings.get(arr(i)).head.mProps.duration
      val name = meetings.get(arr(i)).head.mProps.name
      val recorded = meetings.get(arr(i)).head.mProps.recorded
      val voiceBridge = meetings.get(arr(i)).head.mProps.voiceConf

      var info = new MeetingInfo(id, name.value, recorded.value, voiceBridge.value, duration)
      resultArray(i) = info

      //send the users
      self ! new GetUsers(IntMeetingId(id), IntUserId("nodeJSapp"))

      //send the presentation
      self ! new GetPresentationInfo(IntMeetingId(id), IntUserId("nodeJSapp"), "nodeJSapp")

      //send chat history
      self ! new GetChatHistoryRequest(IntMeetingId(id), IntUserId("nodeJSapp"), "nodeJSapp")

      //send lock settings
      self ! new GetLockSettings(IntMeetingId(id), IntUserId("nodeJSapp"))
    }

    outGW.send(new GetAllMeetingsReply(resultArray))
  }

}
