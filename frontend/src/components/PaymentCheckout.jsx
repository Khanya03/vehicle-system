import { useMemo, useState } from "react";
import { getReceipt, payByCard } from "../api.js";
import {
  brandFor,
  formatCardNumber,
  friendlyPaymentError,
  newIdempotencyKey,
  receiptView,
  testCard,
  validateCard,
} from "../card.js";
import { imageForVehicle } from "../images.js";
import RemoteImage from "./RemoteImage.jsx";

function money(value) {
  const amount = Number(value);
  if (Number.isNaN(amount)) return "R0.00";
  return `R${amount.toLocaleString("en-ZA", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

function Field({ id, label, error, ...props }) {
  return (
    <label htmlFor={id}>
      {label}
      <input id={id} aria-invalid={Boolean(error)} {...props} />
      {error && <span className="field-error">{error}</span>}
    </label>
  );
}

export default function PaymentCheckout({ vehicle, onPaid }) {
  const [cardholderName, setCardholderName] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [expiry, setExpiry] = useState("");
  const [cvv, setCvv] = useState("");
  const [cvvFocus, setCvvFocus] = useState(false);
  const [showCard, setShowCard] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const [receipt, setReceipt] = useState(null);
  const [idempotencyKey, setIdempotencyKey] = useState(newIdempotencyKey);
  const [formVehicleId, setFormVehicleId] = useState(vehicle?.vehicleId ?? "");
  const cardBrand = useMemo(() => brandFor(cardNumber), [cardNumber]);
  const sample = testCard();
  const vehicleId = vehicle?.vehicleId ?? "";

  if (formVehicleId !== vehicleId) {
    setFormVehicleId(vehicleId);
    setReceipt(null);
    setError("");
    setFieldErrors({});
    setCardholderName("");
    setCardNumber("");
    setExpiry("");
    setCvv("");
    setIdempotencyKey(newIdempotencyKey());
  }

  function fillCard() {
    setCardNumber(formatCardNumber(sample.number));
    setCardholderName("Test Buyer");
    setExpiry("12/30");
    setCvv("123");
    setFieldErrors({});
    setError("");
  }

  async function submit(event) {
    event.preventDefault();
    if (busy || !vehicle?.vehicleId) return;

    const nextErrors = validateCard({ cardholderName, cardNumber, expiry, cvv });
    setFieldErrors(nextErrors);
    setError("");
    if (Object.keys(nextErrors).length) return;

    const digits = cardNumber.replace(/\D/g, "");
    const expiryDigits = expiry.replace(/\D/g, "");
    setBusy(true);
    try {
      const payment = await payByCard({
        vehicleId: vehicle.vehicleId,
        cardholderName: cardholderName.trim(),
        cardNumber: digits,
        expiryMonth: Number(expiryDigits.slice(0, 2)),
        expiryYear: 2000 + Number(expiryDigits.slice(2)),
        cvv,
        idempotencyKey,
      });
      const paymentReceipt = receiptView(await getReceipt(payment.paymentId));
      setReceipt(paymentReceipt);
      onPaid?.(paymentReceipt);
    } catch (requestError) {
      setError(friendlyPaymentError(requestError));
      setIdempotencyKey(newIdempotencyKey());
    } finally {
      setCardholderName("");
      setCardNumber("");
      setExpiry("");
      setCvv("");
      setBusy(false);
    }
  }

  if (!vehicle) {
    return (
      <section className="panel" id="checkout">
        <p className="eyebrow">Checkout</p>
        <h2>Pay for a vehicle</h2>
        <p className="panel-copy">Choose a vehicle from the list to open the simulated card payment.</p>
      </section>
    );
  }

  if (receipt) {
    const when = receipt.paidAt ? new Date(receipt.paidAt).toLocaleString() : "";
    return (
      <section className="panel payment-receipt" id="checkout">
        <div className="payment-success-mark" aria-hidden="true">✓</div>
        <p className="eyebrow">Payment successful</p>
        <h2>Receipt</h2>
        <p className="payment-receipt-number">{receipt.receiptNumber}</p>
        <p className="vehicle-meta">
          <span>{receipt.model || vehicle.model}</span>
          <span>{when}</span>
          <span>{receipt.status}</span>
        </p>
        <div className="payment-total-row">
          <span>Total</span>
          <strong>{money(receipt.amount)}</strong>
        </div>
        <p className="payment-card-line">
          {receipt.cardBrand || "Card"} ending {receipt.cardLast4 || "••••"}
        </p>
      </section>
    );
  }

  return (
    <section className="panel" id="checkout">
      <p className="eyebrow">Checkout</p>
      <h2>Pay for {vehicle.model || "this vehicle"}</h2>
      <div className="payment-summary-line">
        <RemoteImage
          className="payment-summary-thumb"
          src={imageForVehicle(vehicle, 0)}
          alt=""
        />
        <div>
          <strong>{money(vehicle.price)}</strong>
          <p className="vehicle-meta"><span>{vehicle.vehicleId}</span></p>
        </div>
      </div>
      <div className="payment-demo-banner">
        <strong>DEMO MODE</strong> — no real money is charged. A valid test card is approved.
        <button type="button" className="text-button" onClick={() => setShowCard((value) => !value)}>
          {showCard ? "Hide test card" : "Show test card"}
        </button>
        {showCard && (
          <div className="payment-test-card">
            <span>
              <strong>{sample.number}</strong>
              <small>{sample.result}. Expiry 12/30, CVV 123.</small>
            </span>
            <button type="button" onClick={fillCard}>Fill in</button>
          </div>
        )}
      </div>
      <div className={`demo-card${cvvFocus ? " is-flipped" : ""}`} aria-hidden="true">
        <span>{cardBrand || "DEMO"}</span>
        <strong>{cardNumber || "•••• •••• •••• ••••"}</strong>
        <small>{cardholderName.trim() || "Cardholder"} · {expiry || "MM/YY"}</small>
      </div>
      {error && <p className="banner banner-error" role="alert">{error}</p>}
      <form className="stack-form" onSubmit={submit} noValidate>
        <Field
          id="cardholderName"
          label="Cardholder name"
          autoComplete="cc-name"
          value={cardholderName}
          onChange={(event) => setCardholderName(event.target.value)}
          error={fieldErrors.cardholderName}
        />
        <Field
          id="cardNumber"
          label={cardBrand ? `Card number · ${cardBrand}` : "Card number"}
          inputMode="numeric"
          autoComplete="cc-number"
          value={cardNumber}
          onChange={(event) => setCardNumber(formatCardNumber(event.target.value))}
          error={fieldErrors.cardNumber}
        />
        <div className="split-fields">
          <Field
            id="expiry"
            label="Expiry MM/YY"
            inputMode="numeric"
            autoComplete="cc-exp"
            placeholder="MM/YY"
            value={expiry}
            onChange={(event) => {
              const digits = event.target.value.replace(/\D/g, "").slice(0, 4);
              setExpiry(digits.length > 2 ? `${digits.slice(0, 2)}/${digits.slice(2)}` : digits);
            }}
            error={fieldErrors.expiry}
          />
          <Field
            id="cvv"
            label="CVV"
            type="password"
            inputMode="numeric"
            autoComplete="cc-csc"
            maxLength={4}
            value={cvv}
            onFocus={() => setCvvFocus(true)}
            onBlur={() => setCvvFocus(false)}
            onChange={(event) => setCvv(event.target.value.replace(/\D/g, "").slice(0, 4))}
            error={fieldErrors.cvv}
          />
        </div>
        <button className="btn-primary" type="submit" disabled={busy}>
          {busy ? "Processing payment…" : `Pay ${money(vehicle.price)}`}
        </button>
      </form>
    </section>
  );
}
