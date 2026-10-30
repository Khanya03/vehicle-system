package za.ac.cput.vehiclesystem.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.vehiclesystem.domain.Payment;
import za.ac.cput.vehiclesystem.domain.PaymentStatus;
import za.ac.cput.vehiclesystem.domain.Vehicle;
import za.ac.cput.vehiclesystem.dto.CardPaymentRequest;
import za.ac.cput.vehiclesystem.dto.CardPaymentResponse;
import za.ac.cput.vehiclesystem.exception.PaymentException;
import za.ac.cput.vehiclesystem.repository.PaymentRepository;
import za.ac.cput.vehiclesystem.repository.VehicleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentGateway gateway;

    public PaymentService(PaymentRepository paymentRepository, VehicleRepository vehicleRepository,
                          PaymentGateway gateway) {
        this.paymentRepository = paymentRepository;
        this.vehicleRepository = vehicleRepository;
        this.gateway = gateway;
    }

    @Transactional(noRollbackFor = PaymentException.class)
    public CardPaymentResponse payByCard(CardPaymentRequest request) {
        try {
            if (request == null
                    || request.getVehicleId() == null || request.getVehicleId().isBlank()
                    || request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()) {
                throw new IllegalArgumentException("Vehicle ID and idempotency key are required");
            }

            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "NOT_FOUND",
                            "Vehicle not found: " + request.getVehicleId()));

            Payment previous = paymentRepository.findByVehicleIdAndIdempotencyKey(
                    vehicle.getVehicleId(), request.getIdempotencyKey()).orElse(null);
            if (previous != null) {
                if (previous.getStatus() == PaymentStatus.FAILED) {
                    throw new PaymentException(HttpStatus.PAYMENT_REQUIRED,
                            previous.getFailureCode(), previous.getFailureReason());
                }
                return toResponse(previous);
            }
            if (paymentRepository.existsByVehicleIdAndStatus(vehicle.getVehicleId(), PaymentStatus.PAID)) {
                throw new PaymentException(HttpStatus.CONFLICT, "ALREADY_PAID", "This vehicle is already paid");
            }

            BigDecimal amount = BigDecimal.valueOf(vehicle.getPrice()).setScale(2, RoundingMode.HALF_UP);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PaymentException(HttpStatus.CONFLICT, "NOTHING_TO_PAY",
                        "There is nothing to pay for this vehicle");
            }

            PaymentGateway.GatewayResult result = gateway.charge(request);
            LocalDateTime now = LocalDateTime.now();
            String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Payment payment = new Payment.Builder()
                    .setPaymentId(paymentId)
                    .setReceiptNumber(receiptNumber(now, paymentId))
                    .setVehicleId(vehicle.getVehicleId())
                    .setAmount(amount)
                    .setCurrency("ZAR")
                    .setMethod("CARD")
                    .setStatus(result.approved() ? PaymentStatus.PAID : PaymentStatus.FAILED)
                    .setCardBrand(result.cardBrand())
                    .setCardLast4(result.cardLast4())
                    .setGatewayReference(result.gatewayReference())
                    .setFailureCode(result.failureCode())
                    .setFailureReason(result.failureReason())
                    .setIdempotencyKey(request.getIdempotencyKey())
                    .setCreatedAt(now)
                    .setPaidAt(result.approved() ? now : null)
                    .build();
            paymentRepository.save(payment);

            if (!result.approved()) {
                throw new PaymentException(HttpStatus.PAYMENT_REQUIRED,
                        result.failureCode(), result.failureReason());
            }
            return toResponse(payment);
        } finally {
            if (request != null) {
                request.clearCardData();
            }
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getReceipt(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(HttpStatus.NOT_FOUND, "NOT_FOUND",
                        "Payment not found: " + paymentId));
        Vehicle vehicle = vehicleRepository.findById(payment.getVehicleId()).orElse(null);
        Map<String, Object> receipt = new LinkedHashMap<>();
        receipt.put("paymentId", payment.getPaymentId());
        receipt.put("receiptNumber", payment.getReceiptNumber());
        receipt.put("vehicleId", payment.getVehicleId());
        receipt.put("model", vehicle == null ? null : vehicle.getModel());
        receipt.put("amount", payment.getAmount());
        receipt.put("total", payment.getAmount());
        receipt.put("currency", payment.getCurrency());
        receipt.put("status", payment.getStatus() == null ? null : payment.getStatus().name());
        receipt.put("cardBrand", payment.getCardBrand());
        receipt.put("cardLast4", payment.getCardLast4());
        receipt.put("paidAt", payment.getPaidAt());
        receipt.put("date", payment.getPaidAt());
        return receipt;
    }

    private CardPaymentResponse toResponse(Payment payment) {
        return new CardPaymentResponse(
                payment.getPaymentId(),
                payment.getReceiptNumber(),
                payment.getVehicleId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus() == null ? null : payment.getStatus().name(),
                payment.getCardBrand(),
                payment.getCardLast4(),
                payment.getPaidAt());
    }

    private String receiptNumber(LocalDateTime when, String paymentId) {
        String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(when);
        return "RCT-" + day + "-" + paymentId.substring(4);
    }
}
