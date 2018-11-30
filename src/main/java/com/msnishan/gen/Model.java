package com.msnishan.gen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 *
 *
 */
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Model
{
    @JsonProperty("package")
    private String packageName;
    private String name;
    private String type;
    private List<Attribute> attributes;
}
