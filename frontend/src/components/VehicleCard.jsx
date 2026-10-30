import RemoteImage from "./RemoteImage.jsx";
import { imageForVehicle } from "../images.js";

function money(value) {
  const amount = Number(value);
  if (Number.isNaN(amount)) return "—";
  return `R${amount.toLocaleString("en-ZA", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

function agentLabel(agent) {
  const name = agent?.name;
  if (!name) return "";
  return [name.firstName, name.lastName].filter(Boolean).join(" ");
}

export default function VehicleCard({ vehicle, index, selected, paid, onPay }) {
  const model = vehicle.model || "Untitled vehicle";
  const doors = vehicle.numberOfDoors;
  const agent = agentLabel(vehicle.agent);

  return (
    <article className={`vehicle-card${selected ? " is-selected" : ""}`}>
      <RemoteImage
        className="vehicle-thumb"
        src={imageForVehicle(vehicle, index)}
        alt={`${model} photo`}
      />
      <div className="vehicle-card-body">
        <div className="vehicle-card-top">
          <h3>{model}</h3>
          <strong>{money(vehicle.price)}</strong>
        </div>
        <p className="vehicle-meta">
          <span>{vehicle.vehicleId || "No id"}</span>
          {typeof doors === "number" && <span>{doors} doors</span>}
          {typeof vehicle.loadCapacity === "number" && <span>{vehicle.loadCapacity} t load</span>}
          {typeof vehicle.engineCapacity === "number" && <span>{vehicle.engineCapacity} cc</span>}
          {agent && <span>{agent}</span>}
        </p>
        <button type="button" className="btn-secondary" onClick={() => onPay(vehicle)} disabled={paid || !vehicle.vehicleId}>
          {paid ? "Paid" : "Pay for this vehicle"}
        </button>
      </div>
    </article>
  );
}
