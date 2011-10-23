package kosakai;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kosakai.etc.PMF;

@SuppressWarnings("serial")
public class KosakaiBotServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().println("Nothing.");
	}

	// メールが来たときに呼び出される
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		KosakaiBot kosakai;
		try {
			kosakai = pm.getObjectById(KosakaiBot.class,
					"KosakaiBot");
			kosakai.run();
		} catch (NullPointerException e) {
			kosakai = new KosakaiBot();
			pm.makePersistent(kosakai);
		} finally {
			pm.close();
		}
	}

	// エンティティの新規作成
	public void makeEntities() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			KosakaiBot kosakai = new KosakaiBot();
			// Twitter twitter = new Twitter();
			pm.makePersistent(kosakai);
			// pm.makePersistent(twitter);
		} finally {
			pm.close();
		}
	}
}
