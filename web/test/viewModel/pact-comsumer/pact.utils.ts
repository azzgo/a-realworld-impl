import path from 'path';
import { PactV3 } from '@pact-foundation/pact';

export const provider = new PactV3({
    consumer: 'realworldWeb',
    provider: 'realworldServer',
    dir: path.resolve(__dirname, '../../../..', 'contacts', 'pacts'),
    cors: true,
});
