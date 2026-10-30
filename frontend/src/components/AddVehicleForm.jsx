import { useState } from "react";
import { createVehicle } from "../api.js";

const EMPTY = { model: "", price: "", numberOfDoors: "4" };

export default function AddVehicleForm({ onCreated }) {
  const [form, setForm] = useState(EMPTY);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  function update(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  async function submit(event) {
    event.preventDefault();
    setError("");
    setNotice("");

    const model = form.model.trim();
    const price = Number(form.price);
    const numberOfDoors = Number(form.numberOfDoors);
    if (!model) {
      setError("Enter a model name.");
      return;
    }
    if (!Number.isFinite(price) || price < 0) {
      setError("Enter a price of zero or more.");
      return;
    }
    if (!Number.isInteger(numberOfDoors) || numberOfDoors < 1) {
      setError("Enter the number of doors as a whole number.");
      return;
    }

    setBusy(true);
    try {
      const created = await createVehicle({ model, price, numberOfDoors });
      setForm(EMPTY);
      setNotice(`Saved ${created.model || model}.`);
      onCreated(created);
    } catch (requestError) {
      const offline = requestError.message === "Failed to fetch";
      setError(offline
        ? "Could not reach the API at http://localhost:8080. Start Spring Boot and try again."
        : requestError.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <section className="panel">
      <p className="eyebrow">Stock</p>
      <h2>Add a car</h2>
      <p className="panel-copy">
        Posts a car to <code>POST /api/vehicles</code> with model, price, and numberOfDoors.
      </p>
      {error && <p className="banner banner-error" role="alert">{error}</p>}
      {notice && <p className="banner banner-ok" role="status">{notice}</p>}
      <form className="stack-form" onSubmit={submit}>
        <label htmlFor="model">
          Model
          <input id="model" name="model" value={form.model} onChange={update} placeholder="Civic" />
        </label>
        <label htmlFor="price">
          Price (ZAR)
          <input id="price" name="price" inputMode="decimal" value={form.price} onChange={update} placeholder="20000" />
        </label>
        <label htmlFor="numberOfDoors">
          Number of doors
          <input id="numberOfDoors" name="numberOfDoors" inputMode="numeric" value={form.numberOfDoors} onChange={update} />
        </label>
        <button className="btn-primary" type="submit" disabled={busy}>
          {busy ? "Saving…" : "Add vehicle"}
        </button>
      </form>
    </section>
  );
}
