package kosakai.functions;

import java.util.Date;
import java.util.Random;

import kosakai.KosakaiBot;
import kosakai.etc.Twitter;
import kosakai.etc.UserLimitStatus;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public abstract class ReactionWord implements BotFunction {

	KosakaiBot bot;

	public ReactionWord(KosakaiBot bot) {
		this.bot = bot;
	}

	public StatusUpdate process(Status status, Reaction reaction) {

		Random random = new Random();
		int rnd = random.nextInt(reaction.templates.length);
		boolean flag = false;
		String sendMessage = "";
		String message = status.getText();

		if (reaction.reactionRate == 0) {
			return null;
		}

		for (String word : reaction.words) {

			if (message.contains(word)) {
				if (!message.contains(Twitter.footer)) {

					for (UserLimitStatus limitStatus : bot.userLimitStatuses) {
						if (status.getUser().getId() == limitStatus.getUserId()) {
							for (String str : reaction.words) {
								if (limitStatus.getData().equals(str)) {
									return null;
								}
							}
						}
					}

					if (random.nextInt(100) >= reaction.reactionRate * 100) {
						return null;
					}

					sendMessage = "@" + status.getUser().getScreenName() + " "
							+ reaction.templates[rnd];

					UserLimitStatus limitStatus = new UserLimitStatus(status
							.getUser().getId(), word, new Date(status
							.getCreatedAt().getTime()
							+ 3600000L
							* reaction.noReactionTime));
					bot.userLimitStatuses.add(limitStatus);

					flag = true;
					break;
				}
			}
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(status.getId(), sendMessage);
		}
		return null;
	}

	@Override
	public abstract StatusUpdate process(Status status);
}
