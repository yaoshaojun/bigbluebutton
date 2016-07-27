package org.bigbluebutton.core2x.json.handlers

import org.bigbluebutton.core2x.RedisMsgHdlrActor
import org.bigbluebutton.core2x.api.IncomingMsg.UserJoinMeetingInMessage
import org.bigbluebutton.core2x.json.{ BigBlueButtonInMessage, IncomingEventBus2x, ReceivedJsonMessage }
import org.bigbluebutton.core2x.domain._
import org.bigbluebutton.messages.UserJoinMeetingMessage

trait UserJoinMeetingMessageJsonHandler extends UnhandledJsonMsgHdlr
    with UserJoinMeetingMessageJsonHandlerHelper {
  this: RedisMsgHdlrActor =>

  val eventBus: IncomingEventBus2x

  override def handleReceivedJsonMsg(msg: ReceivedJsonMessage): Unit = {
    def publish(meetingId: IntMeetingId, userID: IntUserId, token: SessionToken, sessionID: SessionId,
      presenceID: ClientId, userAgent: ClientUserAgent): Unit = {
      log.debug(s"Publishing ${msg.name} [ $userID ]")
      eventBus.publish(
        BigBlueButtonInMessage(meetingId.value,
          new UserJoinMeetingInMessage(meetingId, userID, token, sessionID, presenceID, userAgent)))
      // TODO use senderId or userID?
    }

    if (msg.name == UserJoinMeetingMessage.NAME) {
      log.debug("Received JSON message [" + msg.name + "]")
      val m = UserJoinMeetingMessage.fromJson(msg.data)
      for {
        meetingId <- Option(m.header.meetingId)
        senderId <- Option(m.header.senderId)
        userID <- Option(m.body.userID)
        token <- Option(m.body.token)
        sessionID <- Option(m.body.sessionID)
        presenceID <- Option(m.body.presenceID)
        userAgent = convertPresenceUserAgent(m)
      } yield publish(IntMeetingId(meetingId), IntUserId(userID), SessionToken(token),
        SessionId(sessionID), ClientId(presenceID), userAgent)
    } else {
      super.handleReceivedJsonMsg(msg)
    }

  }
}

trait UserJoinMeetingMessageJsonHandlerHelper {
  def convertPresenceUserAgent(msg: UserJoinMeetingMessage): ClientUserAgent = {
    // TODO

    new ClientUserAgent {}
  }
}
