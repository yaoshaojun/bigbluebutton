package org.bigbluebutton.common.messages2x.presentation;

import org.bigbluebutton.common.messages2x.AbstractEventMessage;
import org.bigbluebutton.common.messages2x.objects.MessageKey;
import org.bigbluebutton.common.messages2x.objects.PresentationCode;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

public class PresentationConversionProgressMessage2x extends AbstractEventMessage{

    public static final String PRESENTATION_CONVERSION_PROGRESS =
            "PresentationConversionProgressMessage";
    public final Payload payload;

    public PresentationConversionProgressMessage2x(String meetingID, String presentationID,
                                              PresentationCode code, MessageKey messageKey,
                                              String presentationName) {
        super();
        header.name = PRESENTATION_CONVERSION_PROGRESS;

        this.payload = new Payload();
        payload.meetingID = meetingID;
        payload.presentationID = presentationID;
        payload.code = code;
        payload.messageKey = messageKey;
        payload.presentationName = presentationName;
    }

    public static PresentationConversionProgressMessage2x fromJson(String message) {
        ObjectMapper mapper = JsonFactory.create();
        return mapper.readValue(message, PresentationConversionProgressMessage2x.class);
    }

    public class Payload {
        public String meetingID;
        public PresentationCode code;
        public MessageKey messageKey;
        public String presentationID;
        public String presentationName;
    }
}
