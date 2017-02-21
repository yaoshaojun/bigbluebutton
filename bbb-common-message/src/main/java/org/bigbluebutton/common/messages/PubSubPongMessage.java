package org.bigbluebutton.common.messages;

import org.bigbluebutton.common.messages.payload.PubSubPingMessagePayload;

public class PubSubPongMessage implements IBigBlueButtonMessage {

	public static final String PUBSUB_PONG = "BbbPubSubPongMessage";
	
	public MessageHeader header;		
	public PubSubPingMessagePayload payload;

	public String toJson() {
		// TODO
		return "FIX MEE!!!";
	}

	public String getChannel() {
		// TODO
		return "FIX MEE!!!";
	}
}
