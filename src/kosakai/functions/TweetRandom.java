package kosakai.functions;

import java.util.Random;

import kosakai.etc.Twitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class TweetRandom implements BotFunction {

	Random random = new Random();

	@Override
	public StatusUpdate process(Status status) {
		boolean flag = false;
		String sendMessage = "";
		String userName = status.getUser().getScreenName();
		String message = status.getText();
		long statusId = status.getId();

		if (message.contains("random") || message.contains("rand")
				|| message.contains("rnd")) {

			int n1 = 0;
			int n2 = 0;
			int rnd;
			boolean foundRnd = false;
			boolean foundN1 = false;
			boolean foundN2 = false;
			String messages[];

			messages = message.split(" ");
			for (int i = 0; i < messages.length; i++) {
				if (messages[i].equals("random") || messages[i].equals("rand")
						|| messages[i].equals("rnd") && !foundRnd) {
					foundRnd = true;
					continue;
				}
				if (foundRnd && !foundN1) {
					try {
						n1 = Integer.parseInt(messages[i]);
						foundN1 = true;
					} catch (NumberFormatException e) {
					}
					continue;
				}
				if (foundN1) {
					try {
						n2 = Integer.parseInt(messages[i]);
						foundN2 = true;
						break;
					} catch (NumberFormatException e) {
					}
					continue;
				}
			}
			if (foundN1) {
				if (!foundN2) {
					n2 = n1;
					n1 = 1;
				}
				rnd = getRandom(n1, n2);
				sendMessage = "@" + userName + " " + rnd + " [" + n1 + "～" + n2
						+ "]";
			} else {
				rnd = getRandom();
				sendMessage = "@" + userName + " " + rnd + " [int]";
			}
			flag = true;
		}
		if (flag) {
			return Twitter.ConvertToStatusUpdate(statusId, sendMessage);
		} else {
			return null;
		}
	}

	private int getRandom(int n1, int n2) {

		int num;
		num = random.nextInt(Math.abs(n2 - n1) + 1);
		if (n1 < n2) {
			num += n1;
		} else {
			num += n2;
		}
		return num;
	}

	private int getRandom() {
		return random.nextInt();
	}
}
