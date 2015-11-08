package de.vivistra.telegrambot;

import java.io.Closeable;

import org.apache.logging.log4j.Logger;

import de.vivistra.telegrambot.client.BotRequest;
import de.vivistra.telegrambot.client.ConnectionHandler;
import de.vivistra.telegrambot.model.message.Message;
import de.vivistra.telegrambot.receiver.IReceiverService;
import de.vivistra.telegrambot.receiver.Receiver;

public class TelegramBot implements Closeable {
	private String apiUrl = "https://api.telegram.org/bot";
	private String apiToken = "";
	private Logger logger;
	private Receiver receiver;
	private ConnectionHandler connectionHandler;
	
	public String getApiUrlWithToken() {
		return apiUrl + apiToken + "/";
	}

	public boolean isEmptyApiToken() {
		return apiToken.isEmpty();
	}

	
	public TelegramBot(String apiToken, Logger logger) {
		this.apiToken = apiToken;
		this.logger = logger;
		this.connectionHandler = new ConnectionHandler(this);
		this.receiver = new Receiver(this);
	}
	
	public String getApiToken() {
		return apiToken;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}
	
	public void subscribe(IReceiverService service) {
		receiver.subscribe(service);
	}

	public void unsubscribe(IReceiverService service) {
		receiver.unsubscribe(service);
	}
	
	public void send(Message message) {
		BotRequest request = new BotRequest(message);
		getConnectionHandler().post(request);
	}

	@Override
	public void close() {
		connectionHandler.close();
	}
	
}
