package com.avijit.addressbook.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
}
