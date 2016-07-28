package org.bigbluebutton.core.api.json.handlers.whiteboard

import org.bigbluebutton.core.api.IncomingMsg.SendWhiteboardAnnotationRequest
import org.bigbluebutton.core.api.RedisMsgHdlrActor
import org.bigbluebutton.core.domain.{ AnnotationVO, IntMeetingId, IntUserId }
import org.bigbluebutton.core.api.json.{ BigBlueButtonInMessage, IncomingEventBus2x, ReceivedJsonMessage }
import org.bigbluebutton.core.api.json.handlers.UnhandledJsonMsgHdlr
import org.bigbluebutton.messages.vo.AnnotationBody
import org.bigbluebutton.messages.whiteboard.SendWhiteboardAnnotationRequestEventMessage

trait SendWhiteboardAnnotationRequestEventJsonMsgHdlr
    extends UnhandledJsonMsgHdlr
    with SendWhiteboardAnnotationRequestEventJsonMessageHandlerHelper {

  this: RedisMsgHdlrActor =>

  val eventBus: IncomingEventBus2x

  override def handleReceivedJsonMsg(msg: ReceivedJsonMessage): Unit = {
    def publish(meetingId: IntMeetingId, senderId: IntUserId, annotation: AnnotationVO): Unit = {
      log.debug(s"Publishing ${msg.name} [$senderId]")
      eventBus.publish(
        BigBlueButtonInMessage(meetingId.value,
          SendWhiteboardAnnotationRequest(meetingId, senderId, annotation)))
    }

    if (msg.name == SendWhiteboardAnnotationRequestEventMessage.NAME) {
      log.debug(s"Received JSON message [ ${msg.name}]")
      val m = SendWhiteboardAnnotationRequestEventMessage.fromJson(msg.data)
      for {
        meetingId <- Option(m.header.meetingId)
        senderId <- Option(m.header.senderId)
        annotation <- convertAnnotationBody(m.body.annotation)
        requesterId <- Option(m.body.requesterId)
      } yield publish(IntMeetingId(meetingId), IntUserId(senderId), annotation)
    } else {
      super.handleReceivedJsonMsg(msg)
    }

  }
}

trait SendWhiteboardAnnotationRequestEventJsonMessageHandlerHelper {
  def convertAnnotationBody(body: AnnotationBody): Option[AnnotationVO] = {
    for {
      id <- Option(body.id)
      status <- Option(body.status)
      shapeType <- Option(body.shapeType)
      shape = extractInnerShape(body.shape)
      wbId <- Option(body.wbId)
    } yield AnnotationVO(id, status, shapeType, shape, wbId)
  }

  def extractInnerShape(obj: java.util.Map[String, Object]): scala.collection.immutable.Map[String, Object] = {

    import scala.collection.JavaConversions.mapAsScalaMap
    val a = mapAsScalaMap(obj)

    a.toMap
  }

}
