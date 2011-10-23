package kosakai.functions;

import java.util.Random;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class Omikuji implements BotFunction {

	public StatusUpdate process(Status status) {
		boolean flag = false;
		String sendMessage = "";
		String userName = status.getUser().getScreenName();
		String message = status.getText();
		long statusId = status.getId();

		if (message.contains("おみくじ")) {
			Random random = new Random();
			int num = random.nextInt(10);
			if (num == 0) {
				sendMessage = "大吉";
			} else if (num == 1 || num == 2) {
				sendMessage = "中吉";
			} else if (num == 3 || num == 4) {
				sendMessage = "小吉";
			} else if (num == 5 || num == 6) {
				sendMessage = "吉";
			} else if (num == 7 || num == 8) {
				sendMessage = "末吉";
			} else {
				sendMessage = "凶";
			}
			sendMessage = "@" + userName + " " + sendMessage + "です。";
			flag = true;
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(statusId, sendMessage);
		} else {
			return null;
		}
	}
}
