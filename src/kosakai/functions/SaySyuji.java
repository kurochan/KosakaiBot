package kosakai.functions;

import java.util.Calendar;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class SaySyuji implements BotFunction {

	@Override
	public StatusUpdate process(Status status) {
		String message = status.getText();

		if (message.contains("何周目") || message.contains("何週目")) {
			return createStatus(status);
		} else {
			return null;
		}
	}

	private StatusUpdate createStatus(Status status) {
		String userName = status.getUser().getScreenName();
		String sendMessage = "";

		Calendar koki_start = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		Calendar today_start = Calendar.getInstance();
		koki_start.set(2011, Calendar.SEPTEMBER, 18, 0, 0, 0);
		today_start.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
				today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		long week = ((today_start.getTimeInMillis() - koki_start
				.getTimeInMillis()) / (24 * 60 * 60 * 1000) + 1) / 7 + 1;
		sendMessage = "@" + userName + " 今週は、後期第" + week + "週目です。";
		return Twitter.ConvertToStatusUpdate(status.getId(), sendMessage);
	}
}
