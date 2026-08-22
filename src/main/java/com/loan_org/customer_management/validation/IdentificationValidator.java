package com.loan_org.customer_management.validation;

import java.util.List;

import com.loan_org.customer_management.entity.Identification;

public interface IdentificationValidator {
    void validateIdentification(Identification identification);
    void validateDuplicateTypes(List<Identification> identifications);
}