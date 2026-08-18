package com.loan_org.customer_management.outbox.repository;

import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository extends MongoRepository<OutboxEventDocument, String> {

    @Query("""
            {
                'status': ?0,
                '$or': [
                    { 'nextAttemptAt': null },
                    { 'nextAttemptAt': { '$lte': ?1 } }
                ]
            }
            """)
    List<OutboxEventDocument> findEligibleEvents(
            OutboxEventStatus status,
            Instant now,
            Pageable pageable
    );

    Optional<OutboxEventDocument> findByEventId(
            String eventId
    );
}