package kosakai.functions;

public class ReactionFactory {

	public static Reaction getNullPo() {
		return new Reaction(2, 1.0f,
				new String[] { "ぬるぽ", "ガッ", "NullPointer" }, new String[] {
						"ヽ( ﾟ∀ﾟ)ﾉ┌┛)`Дﾟ)･;'ｶﾞｯ!!", "■━⊂( ﾟ∇ﾟ) 彡 ガッ☆｀◇´)ﾉ" });
	}

	public static Reaction getHelp() {
		return new Reaction(0, 1.0f, new String[] { "Help", "help" },
				new String[] { "see <Kosakai page name>" });
	}
}
