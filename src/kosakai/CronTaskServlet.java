package kosakai;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kosakai.etc.PMF;

//cronで呼び出される
@SuppressWarnings("serial")
public class CronTaskServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		KosakaiBot kosakai;
		try {
			kosakai = pm.getObjectById(KosakaiBot.class, "KosakaiBot");
			kosakai.run();
		} catch (JDOObjectNotFoundException e) {
			kosakai = new KosakaiBot();
			pm.makePersistent(kosakai);
		} catch (NullPointerException e) {
			kosakai = new KosakaiBot();
		} finally {
			pm.close();
		}
		resp.getWriter().println("Run.");
	}
}
