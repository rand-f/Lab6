package com.example.employeesystem.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "ID can not be empty")
    @Size(min = 3, message = "ID must be more than 2 characters")
    private String ID;

    @NotEmpty(message = "name can not be empty")
    @Size(min=5, message = "name must be more than 4 characters")
    @Pattern(regexp = "^[A-Za-z]+$",message = "name must not contain numbers")
    private String name;

    @NotEmpty(message = "email can not be empty")
    @Email(message = "must be a valid email")
    private String email;

    @NotEmpty(message = "phone number can not be empty")
    //@Size(min = 10, max = 10, message = "phone number must be 10 digits")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits long")
    private String phoneNumber;

    @NotNull(message = "age can not be empty")
    @Min(value = 25,message = "age must be 25 or greater" )
    private int age;

    @NotEmpty(message = "positon can not be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$",
            message = "Status must be one of: not started, in progress, completed")
    private String position;

    private boolean onLeave = false;

    @NotNull(message = "hireDate can not be empty")
    @PastOrPresent(message = "hire date must be in past or present")
    private LocalDate hireDate;

    @NotNull(message = "annual leave can not be empty")
    @PositiveOrZero(message = "annual leave must be a positive number")
    private int annualLeave;

}
