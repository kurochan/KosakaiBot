package kosakai;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import kosakai.etc.*;
import kosakai.functions.*;

import twitter4j.Status;
import twitter4j.StatusUpdate;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class KosakaiBot {
	@SuppressWarnings("unused")
	@PrimaryKey
	private String id = "KosakaiBot";
	@Persistent
	private boolean isActive;
	@NotPersistent
	private Twitter twitter;
	@NotPersistent
	public static final int targetListId = 57330781;
	@Persistent(serialized = "true", defaultFetchGroup = "true")
	public List<UserLimitStatus> userLimitStatuses;
	@NotPersistent
	List<BotFunction> alwaysActiveList = new ArrayList<BotFunction>();
	@NotPersistent
	List<BotFunction> listFunctionList = new ArrayList<BotFunction>();
	@NotPersistent
	List<BotFunction> mentionFunctionList = new ArrayList<BotFunction>();

	public KosakaiBot() {
		if (userLimitStatuses == null) {
			userLimitStatuses = new ArrayList<UserLimitStatus>();
		}
		setActive(false);
	}

	public void run() {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Status> mentions = null;
		List<Status> lists = null;
		try {
			twitter = pm.getObjectById(Twitter.class, "Twitter");
		} catch (JDOObjectNotFoundException e) {
			twitter = new Twitter();
			pm.makePersistent(twitter);
		} catch (NullPointerException e) {
			twitter = new Twitter();
		} finally {
			// Botがアクティブでない時も取得しておいてsinceidを最新に保つ
			mentions = twitter.getMentions();
			lists = twitter.getListStatus();
			pm.close();
		}
		updateUserLimitStatus();

		// アクティブ/非アクティブに関する処理
		alwaysActiveList.add(new SetActiveFromTweet(this));
		doTask(alwaysActiveList, mentions);

		if (!isActive) {
			return;
		}
		if (mentions == null || lists == null) {
			return;
		}
		if (mentions.size() <= 0 && lists.size() <= 0) {
			return;
		}

		// リストに関する処理
		listFunctionList.add(new ReactionToList(this));
		doTask(listFunctionList, lists);
		// メンションに関する処理
		mentionFunctionList.add(new Ping());
		mentionFunctionList.add(new ReactionToMention(this));
		mentionFunctionList.add(new Omikuji());
		mentionFunctionList.add(new Janken());
		mentionFunctionList.add(new TweetRandom());
		mentionFunctionList.add(new RunBF());
		mentionFunctionList.add(new ResetUserLimitStatus(this));
		mentionFunctionList.add(new SaySyuji());
		doTask(mentionFunctionList, mentions);

		// userLimitStatusのリストの更新処理 これをしないとJDOが変更を検知できない
		List<UserLimitStatus> tmpList = userLimitStatuses;
		setUserLimitStatuses(null);
		setUserLimitStatuses(tmpList);
	}

	public void doTask(List<BotFunction> functionList, List<Status> statusList) {
		StatusUpdate statusUpdate;
		if (functionList == null || statusList == null) {
			return;
		}
		for (Status status : statusList) {
			for (BotFunction f : functionList) {
				statusUpdate = f.process(status);
				if (statusUpdate != null) {
					twitter.tweet(statusUpdate);
					break;
				}
			}
		}
	}

	public void updateUserLimitStatus() {

		UserLimitStatus status;
		Date now = new Date();

		// リストの後ろから削除しないと期待した動作ができない
		for (int i = userLimitStatuses.size() - 1; i >= 0; i--) {
			status = userLimitStatuses.get(i);
			if (status.getLimitUntil().getTime() <= now.getTime()) {
				userLimitStatuses.remove(status);
			}
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setUserLimitStatuses(List<UserLimitStatus> userLimitStatuses) {
		this.userLimitStatuses = userLimitStatuses;
	}
}
