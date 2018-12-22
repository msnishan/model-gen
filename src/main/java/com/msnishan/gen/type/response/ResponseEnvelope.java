package com.msnishan.gen.type.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEnvelope {
    private ResponseContext context;
    private LocalDateTime responseDateTime;
    private List<ResponseError> errors;
    private String responseCode;
    private Map<String, Object> data;
}
