package com.demo.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    private long id;
    private long collageId;
    private String createDateTime;
    private boolean verified;
    private boolean active;

    @NotBlank(message = "Email is required!")
    @Pattern(regexp="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", message="Invalid email address!")
    @Size(min = 4,max = 255,message = "Email should have 4 to 255 characters")
    private String email;

    @NotBlank(message = "First name is required!")
    @Size(min = 4,max = 255,message = "First name should have 4 to 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    @Size(min = 4,max = 255,message = "Last name should have 4 to 255 characters")
    private String lastName;

    @NotBlank(message = "Role is required!")
    @Size(min = 4,max = 255,message = "Last name should have 4 to 255 characters")
    private String role;

    @NotBlank(message = "Password is required!")
    @Size(min = 4,max = 255,message = "Password should have 4 to 255 characters")
    private String password;
}
