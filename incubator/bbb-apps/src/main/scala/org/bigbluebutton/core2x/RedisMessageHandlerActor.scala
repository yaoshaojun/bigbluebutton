package org.bigbluebutton.core2x

import akka.actor.{ Actor, ActorLogging, Props }
import org.bigbluebutton.core2x.bus.handlers.{ CreateMeetingRequestMessageJsonHandler, RegisterUserRequestMessageJsonHandler, UnhandledReceivedJsonMessageHandler, ValidateAuthTokenRequestMessageJsonHandler }
import org.bigbluebutton.core2x.bus.{ IncomingEventBus2x, IncomingJsonMessageBus, ReceivedJsonMessage }

object RedisMessageHandlerActor {
  def props(eventBus: IncomingEventBus2x, incomingJsonMessageBus: IncomingJsonMessageBus): Props =
    Props(classOf[RedisMessageHandlerActor], eventBus, incomingJsonMessageBus)
}

class RedisMessageHandlerActor(
  val eventBus: IncomingEventBus2x,
  val incomingJsonMessageBus: IncomingJsonMessageBus)
    extends Actor with ActorLogging
    with UnhandledReceivedJsonMessageHandler
    with ValidateAuthTokenRequestMessageJsonHandler
    with CreateMeetingRequestMessageJsonHandler
    with RegisterUserRequestMessageJsonHandler {

  def receive = {
    case msg: ReceivedJsonMessage => handleReceivedJsonMessage(msg)
    case _ => // do nothing
  }

}