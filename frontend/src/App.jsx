import { useEffect, useState } from "react";
import { getCars, getVehicles } from "./api.js";
import { heroImage } from "./images.js";
import AddVehicleForm from "./components/AddVehicleForm.jsx";
import PaymentCheckout from "./components/PaymentCheckout.jsx";
import RemoteImage from "./components/RemoteImage.jsx";
import VehicleCard from "./components/VehicleCard.jsx";

function offlineMessage(error) {
  if (error?.message === "Failed to fetch") {
    return "Could not reach the API at http://localhost:8080. Start Spring Boot and try again.";
  }
  return error?.message || "Something went wrong.";
}

function VehicleList({ title, eyebrow, vehicles, loading, error, selectedId, paidIds, onPay }) {
  return (
    <section className="list-section">
      <div className="section-heading">
        <p className="eyebrow">{eyebrow}</p>
        <h2>{title}</h2>
      </div>
      {loading && <p className="status-line" role="status">Loading {title.toLowerCase()}…</p>}
      {error && <p className="banner banner-error" role="alert">{error}</p>}
      {!loading && !error && vehicles.length === 0 && (
        <p className="status-line">Nothing here yet.</p>
      )}
      <div className="vehicle-grid">
        {vehicles.map((vehicle, index) => (
          <VehicleCard
            key={vehicle.vehicleId || `${title}-${index}`}
            vehicle={vehicle}
            index={index}
            selected={vehicle.vehicleId === selectedId}
            paid={paidIds.has(vehicle.vehicleId)}
            onPay={onPay}
          />
        ))}
      </div>
    </section>
  );
}

export default function App() {
  const [vehicles, setVehicles] = useState([]);
  const [cars, setCars] = useState([]);
  const [vehiclesLoading, setVehiclesLoading] = useState(true);
  const [carsLoading, setCarsLoading] = useState(true);
  const [vehiclesError, setVehiclesError] = useState("");
  const [carsError, setCarsError] = useState("");
  const [selected, setSelected] = useState(null);
  const [paidIds, setPaidIds] = useState(() => new Set());

  async function load() {
    setVehiclesLoading(true);
    setCarsLoading(true);
    setVehiclesError("");
    setCarsError("");

    const vehicleRequest = getVehicles()
      .then((data) => setVehicles(Array.isArray(data) ? data : []))
      .catch((error) => setVehiclesError(offlineMessage(error)))
      .finally(() => setVehiclesLoading(false));

    const carRequest = getCars()
      .then((data) => setCars(Array.isArray(data) ? data : []))
      .catch((error) => setCarsError(offlineMessage(error)))
      .finally(() => setCarsLoading(false));

    await Promise.all([vehicleRequest, carRequest]);
  }

  useEffect(() => {
    let active = true;
    getVehicles()
      .then((data) => { if (active) setVehicles(Array.isArray(data) ? data : []); })
      .catch((error) => { if (active) setVehiclesError(offlineMessage(error)); })
      .finally(() => { if (active) setVehiclesLoading(false); });
    getCars()
      .then((data) => { if (active) setCars(Array.isArray(data) ? data : []); })
      .catch((error) => { if (active) setCarsError(offlineMessage(error)); })
      .finally(() => { if (active) setCarsLoading(false); });
    return () => { active = false; };
  }, []);

  function chooseVehicle(vehicle) {
    setSelected(vehicle);
    document.getElementById("checkout")?.scrollIntoView({ behavior: "smooth", block: "start" });
  }

  function handleCreated(created) {
    load();
    if (created?.vehicleId) setSelected(created);
  }

  function handlePaid(receipt) {
    if (!receipt?.vehicleId) return;
    setPaidIds((current) => new Set(current).add(receipt.vehicleId));
  }

  return (
    <div className="page">
      <header className="hero">
        <RemoteImage className="hero-image" src={heroImage()} alt="" />
        <div className="hero-copy">
          <p className="eyebrow">Khanya vehicle system</p>
          <h1>Find a vehicle, then pay the demo way.</h1>
          <p>Stock from the Spring Boot API on localhost:8080. Card checkout keeps the brand and the last four digits.</p>
        </div>
      </header>

      <main>
        <div className="toolbar">
          <button type="button" className="btn-secondary" onClick={load}>Refresh lists</button>
        </div>
        <VehicleList
          title="Vehicles"
          eyebrow="GET /api/vehicles"
          vehicles={vehicles}
          loading={vehiclesLoading}
          error={vehiclesError}
          selectedId={selected?.vehicleId}
          paidIds={paidIds}
          onPay={chooseVehicle}
        />
        <VehicleList
          title="Cars"
          eyebrow="GET /api/cars"
          vehicles={cars}
          loading={carsLoading}
          error={carsError}
          selectedId={selected?.vehicleId}
          paidIds={paidIds}
          onPay={chooseVehicle}
        />
        <div className="workspace">
          <AddVehicleForm onCreated={handleCreated} />
          <PaymentCheckout vehicle={selected} onPaid={handlePaid} />
        </div>
      </main>
    </div>
  );
}
