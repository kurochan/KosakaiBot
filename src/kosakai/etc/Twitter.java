package kosakai.etc;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import kosakai.KosakaiBot;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Twitter {

	@SuppressWarnings("unused")
	@PrimaryKey
	private String id = "Twitter";
	@NotPersistent
	public static boolean isDebug = false;
	@Persistent
	private long mSinceid;
	@Persistent
	private long lSinceid;
	@NotPersistent
	private twitter4j.Twitter twitter = new TwitterFactory().getInstance();
	@NotPersistent
	public static final String footer = "[自動投稿]";

	public Twitter() {
		setlSinceid(-1);
		setmSinceid(-1);
		twitter = new TwitterFactory().getInstance();
	}

	public void setmSinceid(long sinceid) {
		mSinceid = sinceid;
	}

	public void setlSinceid(long sinceid) {
		lSinceid = sinceid;
	}

	public void tweet(String message) {
		tweet(-1, message);
	}

	public static StatusUpdate ConvertToStatusUpdate(long inReplyToStatusId,
			String message) {
		if (message.length() > 140 - footer.length()) {
			message = message.substring(0, 141 - footer.length());
		}
		message += " " + footer;
		StatusUpdate status = new StatusUpdate(message);
		status.setInReplyToStatusId(inReplyToStatusId);
		return status;
	}

	public void tweet(long inReplyToStatusId, String message) {
		if (message.length() > 140 - footer.length()) {
			message = message.substring(0, 141 - footer.length());
		}
		message += " " + footer;
		StatusUpdate status = new StatusUpdate(message);
		status.setInReplyToStatusId(inReplyToStatusId);
		tweet(status);
	}

	public void tweet(StatusUpdate status) {
		if (isDebug) {
			System.out.println("tweet: " + status.toString());
		} else {
			try {
				twitter.updateStatus(status);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Status> getListStatus() {

		List<Status> statuses = null;
		Paging p = new Paging();

		if (this.lSinceid < 1) {
			setlSinceid(1);
		}
		p.setSinceId(this.lSinceid);
		try {
			statuses = twitter.getUserListStatuses(KosakaiBot.targetListId, p);
			if (statuses != null) {
				if (statuses.size() > 0) {
					if (statuses.get(0).getId() > this.lSinceid) {
						setlSinceid(statuses.get(0).getId());
					}
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		if (statuses == null) {
			return null;
		}
		// Botが反応する必要のないツイートを削除
		// リストの後ろから削除しないと期待した動作ができない
		for (int i = statuses.size() - 1; i >= 0; i--) {
			Status status = statuses.get(i);
			if (status.isRetweet() || status.getText().startsWith("RT")) {
				statuses.remove(status);
				continue;
			}
			if (status.getInReplyToStatusId() > 1
					|| status.getText().startsWith("@")) {
				if (!status.getInReplyToScreenName().equals("YmdrSakai")) {
					statuses.remove(status);
					continue;
				}
			}
		}
		return statuses;
	}

	public List<Status> getMentions() {

		List<Status> statuses = null;
		Paging p = new Paging();

		if (this.mSinceid < 1) {
			setmSinceid(1);
		}
		p.sinceId(this.mSinceid);
		try {
			statuses = twitter.getMentions(p);
			// tweet(Long.toString(sinceid)+" "+Integer.toString(statuses.size()));
			if (statuses != null) {
				if (statuses.size() > 0) {
					if (statuses.get(0).getId() > this.mSinceid) {
						this.setmSinceid(statuses.get(0).getId());
					}
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		return statuses;
	}
}
