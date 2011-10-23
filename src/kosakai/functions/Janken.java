package kosakai.functions;

import java.util.Random;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class Janken implements BotFunction {

	@Override
	public StatusUpdate process(Status status) {
		boolean flag = false;
		String sendMessage = "";
		String userName = status.getUser().getScreenName();
		String message = status.getText();
		long statusId = status.getId();
		Random random =new Random();

		if (message.contains("じゃんけん")) {
			int num = random.nextInt(3);
			if (num == 0) {
				sendMessage = "グー";
			} else if (num == 1) {
				sendMessage = "チョキ";
			} else {
				sendMessage = "パー";
			}
			flag = true;
			sendMessage = "@" + userName + " " + sendMessage + "！";
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(statusId, sendMessage);
		} else {
			return null;
		}
	}
}
