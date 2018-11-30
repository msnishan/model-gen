package com.msnishan.gen;

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
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class ModelList
{
    private String basePackage;
    private List<Model> models;
}
