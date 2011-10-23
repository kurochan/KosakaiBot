package kosakai.functions;

import kosakai.KosakaiBot;
import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class SetActiveFromTweet implements BotFunction {

	private KosakaiBot bot;

	public SetActiveFromTweet(KosakaiBot bot) {
		this.bot = bot;
	}

	@Override
	public StatusUpdate process(Status status) {
		long statusId = status.getId();
		boolean flag = false;
		String sendMessage = "";
		String userName = status.getUser().getScreenName();
		String message = status.getText();

		if (!userName.equals("kuro_m88")) {
			return null;
		}

		if (message.contains("起きろ")) {
			bot.setActive(true);
			sendMessage = "@" + userName + " botをアクティブにしました。";
			flag = true;
		} else if (message.contains("寝ろ")) {
			bot.setActive(false);
			sendMessage = "@" + userName + " botを停止しました。";
			flag = true;
		}

		if (flag) {
			return Twitter.ConvertToStatusUpdate(statusId, sendMessage);
		} else {
			return null;
		}
	}
}
