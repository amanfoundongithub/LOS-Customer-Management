package com.loan_org.customer_management.repository.impl;

import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.entity.enums.CustomerType;
import com.loan_org.customer_management.repository.CustomerSearchRepository;

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
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class CustomerSearchRepositoryImpl implements CustomerSearchRepository {

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

        List<Criteria> filters = new ArrayList<>();

        addSearchCriteria(filters, search);
        addStatusCriteria(filters, status);
        addCustomerTypeCriteria(filters, customerType);
        addDateRangeCriteria(filters, createdFrom, createdTo);

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])
                    )
            );
        }

        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                CustomerDocument.class
        );

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

    private void addSearchCriteria(
            List<Criteria> filters,
            String search
    ) {

        if (!StringUtils.hasText(search)) {
            return;
        }

        String escapedSearch =
                Pattern.quote(search.trim());

        filters.add(
                new Criteria().orOperator(
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
                )
        );
    }

    private void addStatusCriteria(
            List<Criteria> filters,
            CustomerStatus status
    ) {

        if (status != null) {
            filters.add(
                    Criteria.where("status").is(status)
            );
        }
    }

    private void addCustomerTypeCriteria(
            List<Criteria> filters,
            CustomerType customerType
    ) {

        if (customerType != null) {
            filters.add(
                    Criteria.where("customerType").is(customerType)
            );
        }
    }

    private void addDateRangeCriteria(
            List<Criteria> filters,
            Instant createdFrom,
            Instant createdTo
    ) {

        if (createdFrom != null) {
            filters.add(
                    Criteria.where("createdAt").gte(createdFrom)
            );
        }

        if (createdTo != null) {
            filters.add(
                    Criteria.where("createdAt").lte(createdTo)
            );
        }
    }
}