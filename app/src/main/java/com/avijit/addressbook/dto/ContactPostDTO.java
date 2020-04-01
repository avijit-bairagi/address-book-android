package com.avijit.addressbook.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPostDTO {

    private String name;
    private String address;
    private String phoneNumber;

    public ContactPostDTO() {
    }

    public ContactPostDTO(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
