package org.bigbluebutton.core2x.handlers

import org.bigbluebutton.core.OutMessageGateway
import org.bigbluebutton.core2x.api.IncomingMessage.RegisterUserRequestInMessage
import org.bigbluebutton.core2x.api.OutGoingMessage.UserRegisteredEvent2x
import org.bigbluebutton.core2x.models.{ MeetingStateModel, PinNumberGenerator, RegisteredUsersModel }

trait RegisterUserCommandHandler {
  val state: MeetingStateModel
  val outGW: OutMessageGateway

  def handleRegisterUser2x(msg: RegisterUserRequestInMessage): Unit = {
    val pinNumber = PinNumberGenerator.generatePin(state.props.voiceConf, state.status.get)
    val regUser = RegisteredUsersModel.create(
      msg.userId,
      msg.extUserId,
      msg.name,
      msg.roles,
      msg.authToken,
      msg.avatar,
      msg.logoutUrl,
      msg.welcome,
      msg.dialNumbers,
      pinNumber,
      msg.config,
      msg.extData)

    state.registeredUsersModel.add(regUser)
    outGW.send(new UserRegisteredEvent2x(state.props.id, state.props.recordingProp.recorded, regUser))
  }
}
