package com.loan_org.customer_management.customer.repository;

import com.loan_org.customer_management.customer.entity.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String>, CustomerSearchRepository {
    Optional<CustomerDocument> findByCustomerNumber(String customerNumber);
    Optional<CustomerDocument> findByIamUserId(String iamUserId);
    boolean existsByCustomerNumber(String customerNumber);
    boolean existsByIamUserId(String iamUserId);
}