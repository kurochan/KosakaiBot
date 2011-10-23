package kosakai;

import kosakai.etc.Twitter;

public class Test {

	public static void main(String[] args) {
		KosakaiBot kosakai = new KosakaiBot();
		Twitter.isDebug = true;
		kosakai.setActive(true);
		kosakai.run();
	}
}
