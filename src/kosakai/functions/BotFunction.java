package kosakai.functions;

import twitter4j.Status;
import twitter4j.StatusUpdate;

public interface BotFunction {
	public StatusUpdate process(Status status);
}
