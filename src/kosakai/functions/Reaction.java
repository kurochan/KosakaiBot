package kosakai.functions;

public class Reaction {
	public int noReactionTime;
	public float reactionRate;
	public String words[];
	public String templates[];

	Reaction(int noReactionTime, float reactionRate, String words[],
			String templates[]) {
		this.noReactionTime = noReactionTime;
		this.reactionRate = reactionRate;
		this.words = words;
		this.templates = templates;
	}
}
