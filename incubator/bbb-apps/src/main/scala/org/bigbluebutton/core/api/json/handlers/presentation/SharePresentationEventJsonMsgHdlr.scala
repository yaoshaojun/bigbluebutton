package org.bigbluebutton.core.api.json.handlers.presentation

import org.bigbluebutton.core.api.IncomingMsg.SharePresentationEventInMessage
import org.bigbluebutton.core.api.RedisMsgHdlrActor
import org.bigbluebutton.core.apps.presentation.domain.PresentationId
import org.bigbluebutton.core.domain.{ IntMeetingId, IntUserId }
import org.bigbluebutton.core.api.json.{ BigBlueButtonInMessage, InHeaderAndJsonBody, IncomingEventBus2x, ReceivedJsonMessage }
import org.bigbluebutton.core.api.json.handlers.UnhandledJsonMsgHdlr
import org.bigbluebutton.messages.presentation.SharePresentationEventMessage

trait SharePresentationEventJsonMsgHdlr extends UnhandledJsonMsgHdlr {
  this: RedisMsgHdlrActor =>

  val eventBus: IncomingEventBus2x

  override def handleReceivedJsonMsg(msg: InHeaderAndJsonBody): Unit = {
    def publish(meetingId: IntMeetingId, senderId: IntUserId, presentationId: PresentationId, share: Boolean): Unit = {
      log.debug(s"Publishing ${msg.header.name} [ $presentationId $senderId]")
      eventBus.publish(
        BigBlueButtonInMessage(meetingId.value,
          new SharePresentationEventInMessage(meetingId, senderId, presentationId, share)))
    }

    if (msg.header.name == SharePresentationEventMessage.NAME) {
      log.debug("Received JSON message [" + msg.header.name + "]")
    } else {
      super.handleReceivedJsonMsg(msg)
    }

  }
}