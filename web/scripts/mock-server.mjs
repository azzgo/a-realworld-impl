import pactum from 'pactum';
import path from "path";
import fs from 'fs/promises';
import process from "process";
import _ from 'lodash';

const pactJsonFile = await fs.readFile(path.resolve(process.cwd(), '../contacts/pacts/realworldWeb-realworldServer.json'))

try {
  console.log("start mock server...");
  pactum.mock.addInteraction(fromContract());
  await pactum.mock.start(8080);
} catch (err) {
  console.error(err);
  process.exit(1);
}

function fromContract() {
  const interactions = JSON.parse(pactJsonFile).interactions;

  return interactions?.map(interaction => {
    if (interaction.request.method === 'GET' && interaction.request.query != null) {
      const queryParamsPair = Object.entries(interaction.request.query).map(([key, value]) => {
        return [key, value?.[0]]
      })
      return {
        ...interaction,
        request: {
          ..._.omit(interaction.request, 'query'),
          queryParams: _.fromPairs(queryParamsPair)
        }
      }
    }
    return interaction;
  })
}
