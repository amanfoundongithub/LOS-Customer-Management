package com.loan_org.customer_management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loan_org.customer_management.entity.CustomerDocument;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String>, CustomerSearchRepository {
    Optional<CustomerDocument> findByCustomerNumber(String customerNumber);
    Optional<CustomerDocument> findByIamUserId(String iamUserId);
    boolean existsByCustomerNumber(String customerNumber);
    boolean existsByIamUserId(String iamUserId);
}