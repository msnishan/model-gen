package com.msnishan.gen.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msnishan.gen.type.request.RequestContext;
import com.msnishan.gen.type.request.RequestEnvelope;
import com.msnishan.gen.type.response.ResponseContext;
import com.msnishan.gen.type.response.ResponseEnvelope;
import com.msnishan.gen.type.response.ResponseError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EnvelopeConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T extractFromEnvelop(RequestEnvelope envelope, Class<T> clazz) {
        Map<String, Object> data = envelope.getData();
        return OBJECT_MAPPER.convertValue(data, clazz);
    }

    public static ResponseEnvelope createResponseEnvelop(RequestContext requestContext, List<ResponseError> errors,
                                                  String responseCode, Object data) {
        return new ResponseEnvelope(
                    new ResponseContext(requestContext.getRequestId(), requestContext.getClientId()),
                    LocalDateTime.now(), errors, responseCode,
                    OBJECT_MAPPER.convertValue(data, new TypeReference<Map<String, Object>>() {}));
    }
}
