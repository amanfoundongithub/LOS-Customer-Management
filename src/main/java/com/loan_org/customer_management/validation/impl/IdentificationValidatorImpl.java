package com.loan_org.customer_management.validation.impl;

import com.loan_org.customer_management.entity.Identification;
import com.loan_org.customer_management.validation.IdentificationValidator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentificationValidatorImpl implements IdentificationValidator {


    public void validateIdentification(
            Identification identification
    ) {

        if (identification == null) {
            throw new IllegalArgumentException(
                    "Identification cannot be null"
            );
        }

        if (identification.getType() == null) {
            throw new IllegalArgumentException(
                    "Identification type is required"
            );
        }

        if (identification.getValue() == null ||
                identification.getValue().isBlank()) {

            throw new IllegalArgumentException(
                    "Identification value is required"
            );
        }
    }


    public void validateDuplicateTypes(
            List<Identification> identifications
    ) {

        if (identifications == null ||
                identifications.isEmpty()) {
            return;
        }

        long distinctTypes =
                identifications.stream()
                        .map(Identification::getType)
                        .distinct()
                        .count();

        if (distinctTypes != identifications.size()) {

            throw new IllegalStateException(
                    "Duplicate identification types are not allowed"
            );
        }
    }
}