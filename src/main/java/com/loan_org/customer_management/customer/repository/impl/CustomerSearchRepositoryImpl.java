package com.loan_org.customer_management.customer.repository.impl;

import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import com.loan_org.customer_management.customer.repository.CustomerSearchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerSearchRepositoryImpl
        implements CustomerSearchRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<CustomerDocument> searchCustomers(
            String search,
            CustomerStatus status,
            CustomerType customerType,
            Instant createdFrom,
            Instant createdTo,
            Pageable pageable
    ) {

        Query query = new Query();

        List<Criteria> criteriaList = new ArrayList<>();

        /*
         * ---------------------------------------------------------
         * Free-text search
         * ---------------------------------------------------------
         *
         * Searches:
         * - customer number
         * - IAM user ID
         * - first name
         * - last name
         * - email
         * - mobile number
         */
        if (StringUtils.hasText(search)) {

            String escapedSearch =
                    java.util.regex.Pattern
                            .quote(search.trim());

            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("customerNumber")
                            .regex(escapedSearch, "i"),

                    Criteria.where("iamUserId")
                            .regex(escapedSearch, "i"),

                    Criteria.where("personalInformation.firstName")
                            .regex(escapedSearch, "i"),

                    Criteria.where("personalInformation.lastName")
                            .regex(escapedSearch, "i"),

                    Criteria.where("contactInformation.email")
                            .regex(escapedSearch, "i"),

                    Criteria.where("contactInformation.mobileNumber")
                            .regex(escapedSearch, "i")
            );

            criteriaList.add(searchCriteria);
        }

        /*
         * ---------------------------------------------------------
         * Status filter
         * ---------------------------------------------------------
         */
        if (status != null) {
            criteriaList.add(
                    Criteria.where("status").is(status)
            );
        }

        /*
         * ---------------------------------------------------------
         * Customer type filter
         * ---------------------------------------------------------
         */
        if (customerType != null) {
            criteriaList.add(
                    Criteria.where("customerType").is(customerType)
            );
        }

        /*
         * ---------------------------------------------------------
         * Created date range
         * ---------------------------------------------------------
         */
        if (createdFrom != null) {
            criteriaList.add(
                    Criteria.where("createdAt")
                            .gte(createdFrom)
            );
        }

        if (createdTo != null) {
            criteriaList.add(
                    Criteria.where("createdAt")
                            .lte(createdTo)
            );
        }

        /*
         * ---------------------------------------------------------
         * Combine filters
         * ---------------------------------------------------------
         */
        if (!criteriaList.isEmpty()) {

            query.addCriteria(
                    new Criteria().andOperator(
                            criteriaList.toArray(new Criteria[0])
                    )
            );
        }

        /*
         * ---------------------------------------------------------
         * Count before pagination
         * ---------------------------------------------------------
         */
        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                CustomerDocument.class
        );

        /*
         * ---------------------------------------------------------
         * Apply pagination and sorting
         * ---------------------------------------------------------
         */
        query.with(pageable);

        List<CustomerDocument> customers =
                mongoTemplate.find(
                        query,
                        CustomerDocument.class
                );

        return new PageImpl<>(
                customers,
                pageable,
                total
        );
    }
}