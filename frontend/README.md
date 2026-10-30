# Vehicle system frontend

Vite + React (JavaScript) UI for the vehicle-system Spring Boot API.

## Run

The API is expected at http://localhost:8080. Start Spring Boot first, then:

```bash
npm install
npm run dev
```

Open http://localhost:5173.

The page loads vehicles from `GET /api/vehicles` and cars from `GET /api/cars`, and adds a car with `POST /api/vehicles` (`model`, `price`, `numberOfDoors`).

Photos are loaded from `images.unsplash.com`. No image files are stored in this folder.

## Payments

Checkout calls `POST /api/payments/card`, the same kind of simulated card payment used by MediSync. Enter card details only to run the demo charge. The page then clears them. The API stores and returns the card brand and last four digits. It does not store the full card number or CVV.

Demo mode approves a valid card. Try Visa `4242 4242 4242 4242`, expiry `12/30`, CVV `123`.
