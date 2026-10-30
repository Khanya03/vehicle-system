const TEST_CARD = {
  number: "4242 4242 4242 4242",
  result: "Success",
};

export function testCard() {
  return TEST_CARD;
}

export function brandFor(number) {
  const digits = number.replace(/\D/g, "");
  if (/^4/.test(digits)) return "Visa";
  if (/^(5[1-5]|2[2-7])/.test(digits)) return "Mastercard";
  if (/^3[47]/.test(digits)) return "Amex";
  return "";
}

export function formatCardNumber(value) {
  return value.replace(/\D/g, "").slice(0, 19).replace(/(.{4})/g, "$1 ").trim();
}

export function newIdempotencyKey() {
  return globalThis.crypto?.randomUUID?.() || `${Date.now()}-${Math.random().toString(36).slice(2)}`;
}

export function friendlyPaymentError(error) {
  const messages = {
    INVALID_CARD: "The card details are invalid. Please check and try again.",
    EXPIRED_CARD: "This card has expired. Check the expiry date or use another card.",
    ALREADY_PAID: "This vehicle is already paid.",
    NOTHING_TO_PAY: "There is nothing to pay for this vehicle.",
    NOT_FOUND: "That vehicle could not be found.",
  };
  if (error?.code && messages[error.code]) return messages[error.code];
  if (error?.status === 429) return "Too many failed attempts. Please wait before trying again.";
  if (error?.status === 409) return error.message || "This vehicle can no longer be paid.";
  if (error?.message === "Failed to fetch") {
    return "Could not reach the API at http://localhost:8080. Start Spring Boot and try again.";
  }
  return error?.message || "Payment failed.";
}

export function validateCard({ cardholderName, cardNumber, expiry, cvv }) {
  const digits = cardNumber.replace(/\D/g, "");
  const expiryDigits = expiry.replace(/\D/g, "");
  const errors = {};

  if (!cardholderName.trim()) errors.cardholderName = "Enter the name on the card.";
  if (!/^\d{12,19}$/.test(digits)) errors.cardNumber = "Enter a card number (12 to 19 digits).";
  if (expiryDigits.length !== 4) {
    errors.expiry = "Enter expiry as MM/YY.";
  } else {
    const month = Number(expiryDigits.slice(0, 2));
    const year = 2000 + Number(expiryDigits.slice(2));
    const now = new Date();
    if (month < 1 || month > 12 || year < now.getFullYear()
      || (year === now.getFullYear() && month < now.getMonth() + 1)) {
      errors.expiry = "This card has expired.";
    }
  }
  if (!/^\d{3,4}$/.test(cvv)) errors.cvv = "CVV must be 3 or 4 digits.";
  return errors;
}

export function receiptView(data) {
  if (!data) return null;
  return {
    paymentId: data.paymentId,
    receiptNumber: data.receiptNumber,
    vehicleId: data.vehicleId,
    model: data.model,
    amount: data.amount ?? data.total,
    currency: data.currency || "ZAR",
    status: data.status,
    cardBrand: data.cardBrand,
    cardLast4: data.cardLast4,
    paidAt: data.paidAt || data.date,
  };
}
