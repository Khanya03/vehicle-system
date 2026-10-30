package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Simulated card payment. Only the card brand and last four digits are stored.
 * The full card number and CVV are not fields on this entity.
 */
@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(
        name = "uk_payment_vehicle_idempotency",
        columnNames = {"vehicle_id", "idempotency_key"}))
public class Payment {

    @Id
    private String paymentId;
    private String receiptNumber;
    @Column(name = "vehicle_id")
    private String vehicleId;
    private BigDecimal amount;
    private String currency;
    private String method;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String cardBrand;
    private String cardLast4;
    private String gatewayReference;
    private String failureCode;
    private String failureReason;
    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    protected Payment() {
    }

    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.receiptNumber = builder.receiptNumber;
        this.vehicleId = builder.vehicleId;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.method = builder.method;
        this.status = builder.status;
        this.cardBrand = builder.cardBrand;
        this.cardLast4 = builder.cardLast4;
        this.gatewayReference = builder.gatewayReference;
        this.failureCode = builder.failureCode;
        this.failureReason = builder.failureReason;
        this.idempotencyKey = builder.idempotencyKey;
        this.createdAt = builder.createdAt == null ? LocalDateTime.now() : builder.createdAt;
        this.paidAt = builder.paidAt;
    }

    public String getPaymentId() { return paymentId; }
    public String getReceiptNumber() { return receiptNumber; }
    public String getVehicleId() { return vehicleId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getCardBrand() { return cardBrand; }
    public String getCardLast4() { return cardLast4; }
    public String getGatewayReference() { return gatewayReference; }
    public String getFailureCode() { return failureCode; }
    public String getFailureReason() { return failureReason; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }

    public static class Builder {
        private String paymentId;
        private String receiptNumber;
        private String vehicleId;
        private BigDecimal amount;
        private String currency;
        private String method;
        private PaymentStatus status;
        private String cardBrand;
        private String cardLast4;
        private String gatewayReference;
        private String failureCode;
        private String failureReason;
        private String idempotencyKey;
        private LocalDateTime createdAt;
        private LocalDateTime paidAt;

        public Builder setPaymentId(String paymentId) { this.paymentId = paymentId; return this; }
        public Builder setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
        public Builder setVehicleId(String vehicleId) { this.vehicleId = vehicleId; return this; }
        public Builder setAmount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder setCurrency(String currency) { this.currency = currency; return this; }
        public Builder setMethod(String method) { this.method = method; return this; }
        public Builder setStatus(PaymentStatus status) { this.status = status; return this; }
        public Builder setCardBrand(String cardBrand) { this.cardBrand = cardBrand; return this; }
        public Builder setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; return this; }
        public Builder setGatewayReference(String gatewayReference) { this.gatewayReference = gatewayReference; return this; }
        public Builder setFailureCode(String failureCode) { this.failureCode = failureCode; return this; }
        public Builder setFailureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public Builder setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; return this; }
        public Builder setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

        public Payment build() {
            return new Payment(this);
        }
    }
}
