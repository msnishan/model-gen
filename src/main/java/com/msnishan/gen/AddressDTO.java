package com.msnishan.gen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



import javax.annotation.Generated;

@Generated("Generated from model-gen framework")
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AddressDTO {
    @JsonProperty("PinCode")
    private long pinCode;
    
    @JsonProperty("AddressLine1")
    private String addressLine1;
    
    @JsonProperty("AddressLine2")
    private String addressLine2;
    
}
