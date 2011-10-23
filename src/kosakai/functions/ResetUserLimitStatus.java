package kosakai.functions;

import java.util.ArrayList;

import kosakai.KosakaiBot;
import kosakai.etc.Twitter;
import kosakai.etc.UserLimitStatus;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class ResetUserLimitStatus implements BotFunction {

	private KosakaiBot bot;

	public ResetUserLimitStatus(KosakaiBot bot) {
		this.bot = bot;
	}

	@Override
	public StatusUpdate process(Status status) {
		boolean flag = false;
		String message = status.getText();
		String userName = status.getUser().getScreenName();
		String sendMessage = "";

		if (!userName.equals("kuro_m88")) {
			return null;
		}

		if (message.contains("忘れろ")) {
			if (!message.contains(Twitter.footer)) {
				bot.setUserLimitStatuses(new ArrayList<UserLimitStatus>());
				sendMessage = "@" + userName + " LimitListを初期化しました。";
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
