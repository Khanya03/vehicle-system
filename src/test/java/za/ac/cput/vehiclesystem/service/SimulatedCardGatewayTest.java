package za.ac.cput.vehiclesystem.service;

import org.junit.jupiter.api.Test;
import za.ac.cput.vehiclesystem.dto.CardPaymentRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulatedCardGatewayTest {

    @Test
    void chargeApprovesDemoVisaAndKeepsLast4Only() {
        SimulatedCardGateway gateway = new SimulatedCardGateway();
        CardPaymentRequest request = visaRequest("123");

        PaymentGateway.GatewayResult result = gateway.charge(request);

        assertTrue(result.approved());
        assertEquals("Visa", result.cardBrand());
        assertEquals("4242", result.cardLast4());
        assertNull(result.failureCode());
        assertFalse(result.toString().contains("4242424242424242"));
    }

    @Test
    void chargeRejectsBadCvvWithoutReturningFullPan() {
        SimulatedCardGateway gateway = new SimulatedCardGateway();
        CardPaymentRequest request = visaRequest("12");

        PaymentGateway.GatewayResult result = gateway.charge(request);

        assertFalse(result.approved());
        assertEquals("INVALID_CARD", result.failureCode());
        assertEquals("4242", result.cardLast4());
        assertFalse(result.toString().contains("4242424242424242"));
    }

    private CardPaymentRequest visaRequest(String cvv) {
        CardPaymentRequest request = new CardPaymentRequest();
        request.setCardholderName("Lisa Mpahla");
        request.setCardNumber("4242 4242 4242 4242");
        request.setExpiryMonth(12);
        request.setExpiryYear(2030);
        request.setCvv(cvv);
        return request;
    }
}
