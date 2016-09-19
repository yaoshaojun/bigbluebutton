package org.bigbluebutton.common.messages2x.presentation;

import org.bigbluebutton.common.messages2x.AbstractEventMessage;
import org.bigbluebutton.common.messages2x.objects.MessageKey;
import org.bigbluebutton.common.messages2x.objects.PresentationCode;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

public class PresentationConversionErrorMessage2x extends AbstractEventMessage {
    public static final String NAME = "PresentationConversionErrorMessage";
    public final Payload payload;

    public PresentationConversionErrorMessage2x(String meetingID, String presentationID,
                                              PresentationCode code, MessageKey messageKey,
                                              String presentationName, Integer numPages,
                                                Integer maxNumPages) {
        super();
        header.name = NAME;

        this.payload = new Payload();
        payload.meetingID = meetingID;
        payload.presentationID = presentationID;
        payload.code = code;
        payload.messageKey = messageKey;
        payload.presentationName = presentationName;
        payload.numPages = numPages;
        payload.maxNumPages = maxNumPages;
    }

    public static PresentationConversionErrorMessage2x fromJson(String message) {
        ObjectMapper mapper = JsonFactory.create();
        return mapper.readValue(message, PresentationConversionErrorMessage2x.class);
    }

    public class Payload {
        public String meetingID;
        public PresentationCode code;
        public MessageKey messageKey;
        public String presentationID;
        public String presentationName;
        public Integer numPages;
        public Integer maxNumPages;
    }
}