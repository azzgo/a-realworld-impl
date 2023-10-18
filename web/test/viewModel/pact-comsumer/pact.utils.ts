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
 *  "exp": 4105008000, // 2100-01-01
 *  "user_id": "id",
 *  "username": "jake",
 *  "email": "jake"
 * }
 */
export const jwtToken =
  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTc0NTkyMzQsImV4cCI6NDEwNTAwODAwMCwic3ViIjoiamFrZV9pZCIsInVzZXJuYW1lIjoiamFrZSIsImVtYWlsIjoiamFrZUBqYWtlLmpha2UifQ.ZDKKQ0noCRU3cHs9B2EW1GllM6lj5Wr07GBoJVf2v3g";
