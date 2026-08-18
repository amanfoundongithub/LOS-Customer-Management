package com.loan_org.customer_management.customer.enums;

/**
 * Represents the lifecycle status of a customer within the
 * Customer Management Service.
 */
public enum CustomerStatus {

    /**
     * Customer has been identified or registered as a potential customer,
     * but has not yet completed the onboarding process.
     */
    PROSPECT,

    /**
     * Customer onboarding has been initiated, but required verification
     * such as KYC or identity verification is still pending.
     */
    PENDING_VERIFICATION,

    /**
     * Customer has successfully completed the required onboarding and
     * verification processes and is currently active.
     */
    ACTIVE,

    /**
     * Customer is no longer actively using the service but has not been
     * permanently closed or terminated.
     */
    INACTIVE,

    /**
     * Customer has been temporarily restricted from using the service
     * due to a security, compliance, risk, or administrative action.
     */
    SUSPENDED,

    /**
     * Customer relationship has been permanently closed.
     * The customer record should generally be retained for historical,
     * regulatory, and audit purposes.
     */
    CLOSED,

    /**
     * Customer has been intentionally deactivated by the system or an
     * authorized administrator and cannot currently use the service.
     */
    DEACTIVATED
}