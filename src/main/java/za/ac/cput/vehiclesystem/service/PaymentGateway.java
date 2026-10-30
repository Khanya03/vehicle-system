package za.ac.cput.vehiclesystem.service;

import za.ac.cput.vehiclesystem.dto.CardPaymentRequest;

public interface PaymentGateway {
    GatewayResult charge(CardPaymentRequest request);

    record GatewayResult(boolean approved, String cardBrand, String cardLast4,
                         String gatewayReference, String failureCode, String failureReason) {
    }
}
