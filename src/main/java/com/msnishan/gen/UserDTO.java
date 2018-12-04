package com.msnishan.gen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.List;


import javax.annotation.Generated;

@Generated("Generated from model-gen framework")
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserDTO {
    @JsonProperty("addressSet")
    private Set<AddressDTO> addressSet;
    
    private String lastName;
    
    private String firstName;
    
    @JsonProperty("addressList")
    private List<AddressDTO> addresses;
    
    private String password;
    
    private String mailId;
    
}
