package com.loan_org.customer_management.api.dto.response;

import lombok.*;

import java.time.LocalDate;

import com.loan_org.customer_management.entity.enums.Gender;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformationResponse {

    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String nationality;
}