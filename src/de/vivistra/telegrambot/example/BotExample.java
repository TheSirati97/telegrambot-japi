package de.vivistra.telegrambot.example;

import org.apache.logging.log4j.LogManager;

import de.vivistra.telegrambot.TelegramBot;
import de.vivistra.telegrambot.model.message.Message;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.receiver.IReceiverService;

public class BotExample {
	TelegramBot telegramBot;

	public BotExample() {
		// Set API token
		telegramBot = new TelegramBot("<Your TelegramAPI token here>",
				LogManager.getLogger("Telegram Bot 1"));
	}

	// Send a message to you. (You have to message the bot first to allow him to
	// send you messages)
	public void sendMessageToMe() {

		// A Telegram ID. It is a negative Integer for bots and a positive
		// Integer for humans.
		int recipient = 123456;// <Your TelegramID here>;

		// Create a message
		Message message = new TextMessage(recipient, "Hello =)");

		// Send the message
		telegramBot.send(message);
	}

	// First you need to implement the `IReceiverService` interface.
	public class GetMessage implements IReceiverService {

		@Override
		public void received(Message message) {
			switch (message.getMessageType()) {
			case TEXT_MESSAGE:
				String sender = message.getSender().toString();

				String text = message.getMessage().toString();

				System.out.println(sender + " wrote: " + text);

				break;
			default:
				System.out.println("Ignore received message.");
			}
		}
	}

	// Now we will create a object of our `GetMessage` and subscribe the
	// `Receiver`. Our method `received` will be called every time a `Message`
	// was received.
	public void subscribe() {
		// Create an IReceiverService object
		GetMessage getMessage = new GetMessage();

		// Subscribe the receiver
		telegramBot.subscribe(getMessage);
	}
}