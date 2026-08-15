package com.loan_org.customer_management.customer.entity;

import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
@CompoundIndex(
        name = "customer_status_created_idx",
        def = "{'status': 1, 'createdAt': -1}"
)
@CompoundIndex(
        name = "customer_type_status_idx",
        def = "{'customerType': 1, 'status': 1}"
)
public class CustomerDocument {

    @Id
    private String id;

    /**
     * MongoDB optimistic locking version.
     */
    @Version
    private Long version;

    /**
     * Business-facing customer identifier.
     *
     * Example:
     * CUST-00000001
     */
    @NotBlank
    @Size(max = 30)
    @Pattern(
            regexp = "^CUST-[0-9]{8,}$",
            message = "Invalid customer number"
    )
    @Indexed(unique = true)
    private String customerNumber;

    /**
     * Reference to the user managed by IAM.
     *
     * IAM owns authentication and authorization.
     * Customer Management owns the customer profile.
     */
    @Size(max = 100)
    @Indexed(unique = true, sparse = true)
    private String iamUserId;

    /**
     * Customer personal information.
     */
    @Valid
    @NotNull
    private PersonalInformation personalInformation;

    /**
     * Customer contact information.
     */
    @Valid
    @NotNull
    private ContactInformation contactInformation;

    /**
     * Customer addresses.
     */
    @Valid
    @Builder.Default
    @Size(max = 10)
    private List<Address> addresses = new ArrayList<>();

    /**
     * Customer identification records.
     */
    @Valid
    @Builder.Default
    @Size(max = 10)
    private List<Identification> identifications = new ArrayList<>();

    /**
     * Customer type.
     */
    @NotNull
    @Builder.Default
    private CustomerType customerType = CustomerType.INDIVIDUAL;

    /**
     * Customer lifecycle status.
     */
    @NotNull
    @Builder.Default
    private CustomerStatus status = CustomerStatus.PROSPECT;

    /**
     * Customer communication preferences.
     */
    @Valid
    private CustomerPreferences preferences;

    /**
     * Flexible customer-specific attributes.
     *
     * Business-critical fields should not be stored here.
     */
    @Builder.Default
    @Size(max = 50)
    private Map<
            @Size(max = 50) String,
            @Size(max = 500) String
            > attributes = new HashMap<>();

    // -------------------------------------------------------------------------
    // Audit fields
    // -------------------------------------------------------------------------

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}