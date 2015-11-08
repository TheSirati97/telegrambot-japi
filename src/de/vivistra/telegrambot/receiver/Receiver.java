package de.vivistra.telegrambot.receiver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import de.vivistra.telegrambot.TelegramBot;
import de.vivistra.telegrambot.client.ConnectionHandler;
import de.vivistra.telegrambot.client.BotRequest;
import de.vivistra.telegrambot.client.BotResponse;
import de.vivistra.telegrambot.model.message.Message;
import de.vivistra.telegrambot.model.message.MessageType;

public class Receiver {
	private final TelegramBot telegramBot;
	
	public Receiver(TelegramBot telegramBot) {
		this.telegramBot = telegramBot;
	}
	
	private final Queue<Message> messageQueue = new LinkedList<>();

	private final Set<IReceiverService> receiverServices = new HashSet<>();

	private final ReceiverThread receiver = new ReceiverThread();

	private void notifyServices(Message message) {
		for (IReceiverService service : receiverServices) {
			service.received(message);
		}
	}

	public void subscribe(IReceiverService service) {
		receiverServices.add(service);

		if (!receiver.isAlive()) {
			receiver.start();
		}
	}

	public void unsubscribe(IReceiverService service) {
		receiverServices.remove(service);

		if (receiverServices.isEmpty()) {
			receiver.interrupt();
		}
	}

	private class ReceiverThread extends Thread {

		@Override
		public void run() {

			if (telegramBot.isEmptyApiToken()) {
				telegramBot.getLogger().error("API token is not set. Plase use BotSettings.setApiToken(<Your bots API token>);");
				return;
			}

			ConnectionHandler connectionHandler = telegramBot.getConnectionHandler();

			UpdateRequest updateRequest;
			BotResponse botResponse;

			int nextExpectedMsg = 0;

			while (!isInterrupted()) {
				try {
					updateRequest = new UpdateRequest(nextExpectedMsg++);

					botResponse = connectionHandler.post(new BotRequest(updateRequest));

					switch (botResponse.getStatusCode()) {
					case 200:
						for (Message message : botResponse.getMessages()) {

							if (message.getMessageType() == MessageType.UNHANDLED_MESSAGE) {
								continue;
							}

							messageQueue.add(message);

							notifyServices(message);
						}

						nextExpectedMsg = botResponse.getUpdateID() + 1;

						break;
					case 409:
						telegramBot.getLogger().error("There is already a bot with this token connected");
						break;
					default:
						telegramBot.getLogger().error("Unknown answer, request failed.");
						break;
					}
				} catch (Exception e) {
					telegramBot.getLogger().error("Error while posting: ", e);
				}
				
				
			}
		}
	}
}
