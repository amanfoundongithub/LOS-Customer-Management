package com.loan_org.customer_management.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

import com.loan_org.customer_management.entity.enums.Gender;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformation {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "First name contains invalid characters"
    )
    private String firstName;

    @Size(max = 50)
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "Middle name contains invalid characters"
    )
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "Last name contains invalid characters"
    )
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Gender gender;

    @Size(min = 2, max = 3)
    @Pattern(
            regexp = "^[A-Za-z]{2,3}$",
            message = "Invalid nationality"
    )
    private String nationality;
}