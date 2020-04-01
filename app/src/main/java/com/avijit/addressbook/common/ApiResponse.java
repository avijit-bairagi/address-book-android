package com.avijit.addressbook.common;

import com.avijit.addressbook.entity.Contact;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse implements Serializable {

    private String code;

    private String message;

    private Object data;
}