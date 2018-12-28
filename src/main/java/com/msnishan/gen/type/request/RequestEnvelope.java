package com.msnishan.gen.type.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnvelope {
    private RequestContext context;
    private LocalDateTime requestDateTime;
    @JsonUnwrapped
    private Map<String, Object> data;
}
