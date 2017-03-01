package org.bigbluebutton.common


object PubSubChannels {
  val FROM_BBB_APPS_CHANNEL: String = "bigbluebutton:from-bbb-apps"
  val FROM_BBB_APPS_PATTERN: String = FROM_BBB_APPS_CHANNEL + ":*"
  val FROM_SYSTEM_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":system"
  val FROM_MEETING_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":meeting"
  val FROM_PRESENTATION_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":presentation"
  val FROM_POLLING_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":polling"
  val FROM_USERS_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":users"
  val FROM_CHAT_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":chat"
  val FROM_WHITEBOARD_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":whiteboard"
  val FROM_CAPTION_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":caption"
  val FROM_DESK_SHARE_CHANNEL: String = FROM_BBB_APPS_CHANNEL + ":deskshare"
  val TO_BBB_APPS_CHANNEL: String = "bigbluebutton:to-bbb-apps"
  val TO_BBB_APPS_PATTERN: String = TO_BBB_APPS_CHANNEL + ":*"
  val TO_MEETING_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":meeting"
  val TO_SYSTEM_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":system"
  val TO_PRESENTATION_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":presentation"
  val TO_POLLING_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":polling"
  val TO_USERS_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":users"
  val TO_CHAT_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":chat"
  val TO_VOICE_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":voice"
  val TO_WHITEBOARD_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":whiteboard"
  val TO_CAPTION_CHANNEL: String = TO_BBB_APPS_CHANNEL + ":caption"
  val BBB_APPS_KEEP_ALIVE_CHANNEL: String = "bigbluebutton:from-bbb-apps:keepalive"
  val TO_BBB_HTML5_CHANNEL: String = "bigbluebutton:to-bbb-html5"
  val TO_VOICE_CONF_CHANNEL: String = "bigbluebutton:to-voice-conf"
  val TO_VOICE_CONF_PATTERN: String = TO_VOICE_CONF_CHANNEL + ":*"
  val TO_VOICE_CONF_SYSTEM_CHAN: String = TO_VOICE_CONF_CHANNEL + ":system"
  val FROM_VOICE_CONF_CHANNEL: String = "bigbluebutton:from-voice-conf"
  val FROM_VOICE_CONF_PATTERN: String = FROM_VOICE_CONF_CHANNEL + ":*"
  val FROM_VOICE_CONF_SYSTEM_CHAN: String = FROM_VOICE_CONF_CHANNEL + ":system"
}