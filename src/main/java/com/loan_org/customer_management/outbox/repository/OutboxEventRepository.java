package com.loan_org.customer_management.outbox.repository;

import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository
        extends MongoRepository<OutboxEventDocument, String> {

    List<OutboxEventDocument> findByStatusOrderByCreatedAtAsc(
            OutboxEventStatus status,
            Pageable pageable
    );

    Optional<OutboxEventDocument> findByEventId(
            String eventId
    );
}