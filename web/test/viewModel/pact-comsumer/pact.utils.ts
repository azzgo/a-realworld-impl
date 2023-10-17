import path from "path";
import { PactV3 } from "@pact-foundation/pact";

export const provider = new PactV3({
  consumer: "realworldWeb",
  provider: "realworldServer",
  dir: path.resolve(__dirname, "../../../..", "contacts", "pacts"),
  cors: true,
});
/**
 * payload with {
 *  "iat": 1697459234,
 *  "exp": 1697545634,
 *  "user_id": "id",
 *  "username": "jake",
 *  "email": "jake"
 * }
 */
export const jwtToken =
  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTc0NTkyMzQsImV4cCI6MTY5NzU0NTYzNCwic3ViIjoiamFrZV9pZCIsInVzZXJuYW1lIjoiamFrZSIsImVtYWlsIjoiamFrZUBqYWtlLmpha2UifQ.FDd_DGCRIp_ivOEToH6rWbGbnnTQ3hufAT-RLYC4ttM";
