package kosakai.etc;

import java.util.Date;
import java.io.Serializable;

public class UserLimitStatus implements Serializable {

	private static final long serialVersionUID = 7207567370386066391L;
	private long userId;
	private Date limitUntil;
	private String data;

	public UserLimitStatus(long userId, String data, Date limitUntil) {
		this.userId = userId;
		this.data = data;
		this.limitUntil = limitUntil;
	}

	public long getUserId() {
		return userId;
	}

	public Date getLimitUntil() {
		return limitUntil;
	}

	public String getData() {
		return data;
	}
}
