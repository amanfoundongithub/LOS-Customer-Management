package com.loan_org.customer_management.customer.validation;

import com.loan_org.customer_management.customer.entity.Identification;

import java.util.List;

public interface IdentificationValidator {
    void validateIdentification(Identification identification);
    void validateDuplicateTypes(List<Identification> identifications);
}