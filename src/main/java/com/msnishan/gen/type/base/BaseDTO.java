package com.msnishan.gen.type.base;


import com.msnishan.gen.type.request.RequestContext;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDTO {
    private Long id;
    private RequestContext context;
    private LocalDateTime requestDateTime;

}
