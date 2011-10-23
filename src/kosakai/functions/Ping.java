package kosakai.functions;

import java.util.Date;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class Ping implements BotFunction {

	@Override
	public StatusUpdate process(Status status) {
		boolean flag = false;
		long time;
		String message = status.getText();
		String userName = status.getUser().getScreenName();
		String sendMessage = "";
		Date now;

		if (message.contains("ping")) {
			if (!message.contains(Twitter.footer)) {
				now = new Date();
				time = now.getTime() - status.getCreatedAt().getTime();
				sendMessage = "@" + userName + " " + "ping received! time="
						+ time + "(ms)";
				flag = true;
			}
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(status.getId(), sendMessage);
		} else {
			return null;
		}
	}
}
