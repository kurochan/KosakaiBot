package kosakai.functions;

import java.util.ArrayList;
import java.util.List;

import kosakai.KosakaiBot;
import twitter4j.Status;
import twitter4j.StatusUpdate;

public class ReactionToList extends ReactionWord {


	public ReactionToList(KosakaiBot bot) {
		super(bot);
	}

	@Override
	public StatusUpdate process(Status status) {
		StatusUpdate statusUpdate = null;
		List<Reaction> list = new ArrayList<Reaction>();
		list.add(ReactionFactory.getNullPo());
		for (Reaction reaction : list) {
			statusUpdate = process(status, reaction);
			if (statusUpdate != null) {
				break;
			}
		}
		return statusUpdate;
	}
}
