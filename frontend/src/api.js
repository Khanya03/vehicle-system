const API_BASE = "http://localhost:8080";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      Accept: "application/json",
      ...(options.body ? { "Content-Type": "application/json" } : {}),
    },
    ...options,
    body: options.body ? JSON.stringify(options.body) : undefined,
  });

  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    let code = "";
    try {
      const data = await response.json();
      if (data && data.message) message = data.message;
      if (data && data.code) code = data.code;
    } catch {
      // Non-JSON errors still surface the status.
    }
    const error = new Error(message);
    error.status = response.status;
    error.code = code;
    throw error;
  }

  if (response.status === 204) return null;
  return response.json();
}

export function getVehicles() {
  return request("/api/vehicles");
}

export function getCars() {
  return request("/api/cars");
}

export function createVehicle(vehicle) {
  return request("/api/vehicles", { method: "POST", body: vehicle });
}

export function payByCard(payment) {
  return request("/api/payments/card", { method: "POST", body: payment });
}

export function getReceipt(paymentId) {
  return request(`/api/payments/${encodeURIComponent(paymentId)}/receipt`);
}
