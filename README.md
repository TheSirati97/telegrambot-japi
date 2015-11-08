# TelegramBot JavaAPI

Hey guys, i am sirati97 and I am a github noob. I normally only have private repos on BitBucket.
I made this fork because [marzn's Telegram Bot Api](https://github.com/marzn/telegrambot-japi) only allows you to run one telegram bot per java classloader (or program in normal environment). (WIP)

If you have any question feel free to contact me via [Telegram](http://telegram.me/thesirati97).

If anyone wants to help me get used to github, please also contact me via [Telegram](http://telegram.me/thesirati97).

## Project state

This software is still under development and need some more time to be finished. But it is already working nice, so we want to share the software with you. If you want, feel free to contribute.

No changelog yet!

## HowTo get the software

### Just download the jar

You can download the first pre release [here](https://github.com/TheSirati97/telegrambot-japi/blob/master-mcn/releases/MCN-Telegram-Bot-API-0.9.0.1.jar?raw=true). Warning i did not tested it yet!.

## HowTo use the software

### Send a message and receive a message

```java
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
```
