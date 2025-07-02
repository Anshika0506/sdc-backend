package com.sdc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactModel {
    private String name;
    private String email;
    private String contactNo;
    private String query;
    private String message;

}