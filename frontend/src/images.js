const HERO_IMAGE =
  "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?auto=format&fit=crop&w=1800&q=80";

const CAR_IMAGES = [
  "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=800&q=80",
  "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80",
  "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80",
  "https://images.unsplash.com/photo-1542362567-b07e54358753?auto=format&fit=crop&w=800&q=80",
  "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?auto=format&fit=crop&w=800&q=80",
];

const TRUCK_IMAGE =
  "https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?auto=format&fit=crop&w=800&q=80";

const MOTORCYCLE_IMAGE =
  "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80";

export function heroImage() {
  return HERO_IMAGE;
}

export function imageForVehicle(vehicle, index) {
  if (vehicle && typeof vehicle.loadCapacity === "number") return TRUCK_IMAGE;
  if (vehicle && typeof vehicle.engineCapacity === "number") return MOTORCYCLE_IMAGE;
  return CAR_IMAGES[Math.abs(index) % CAR_IMAGES.length];
}
