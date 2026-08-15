package com.loan_org.customer_management.customer.dto.request;

import com.loan_org.customer_management.customer.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformationRequest {

    @Size(min = 1, max = 50)
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

    @Size(min = 1, max = 50)
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