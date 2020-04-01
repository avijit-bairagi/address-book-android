package com.avijit.addressbook.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String phoneNumber;

}
