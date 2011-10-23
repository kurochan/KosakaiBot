package kosakai.functions;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class RunBF implements BotFunction {

	@Override
	public StatusUpdate process(Status status) {

		long statusId = status.getId();
		boolean flag = false;
		String sendMessage = "";
		String userName = status.getUser().getScreenName();
		String message = status.getText();

		if (!message.contains("_bf ")) {
			return null;
		}
		if (!message.startsWith("@")) {
			return null;
		}
		char messageArray[] = message.toCharArray();
		for (int i = 0; i < messageArray.length - 1; i++) {
			if (messageArray[i] == ' ') {
				message = message.substring(i + 1, messageArray.length);
				break;
			}
		}
		if (!message.startsWith("_bf ")) {
			return null;
		}
		message = message.substring(4, message.length());
		for (int i = 0; i < message.length() - 1; i++) {
			messageArray = message.toCharArray();
			if (messageArray[i] == ' ' || messageArray[i] == '\n'
					|| messageArray[i] == '\t') {
				message = message.substring(i + 1, messageArray.length);
				i = -1;
			} else {
				break;
			}
		}
		BFInterpreter bfi = new BFInterpreter();
		bfi.run(message);
		sendMessage = bfi.printData;
		if (!sendMessage.contains("@")) {
			sendMessage = "@" + userName + " " + sendMessage;
			flag = true;
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(statusId, sendMessage);
		} else {
			return null;
		}
	}
}
