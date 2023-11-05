import pactum from 'pactum';
import path from "path";
import fs from 'fs/promises';
import process from "process";

const pactJsonFile = await fs.readFile(path.resolve(process.cwd(), '../contacts/pacts/realworldWeb-realworldServer.json'))

try {
  console.log("start mock server...");
  pactum.mock.addInteraction(JSON.parse(pactJsonFile).interactions);
  await pactum.mock.start(8080);
} catch (err) {
  console.error(err);
  process.exit(1);
}
