import { EventExecutor } from '../../interfaces/Event'

export const executor: EventExecutor = async (client) => {
    if (client.user != null) {
        console.log(`Logged in as ${client.user.username}!`);
    }
}

export const name: string = 'ready'