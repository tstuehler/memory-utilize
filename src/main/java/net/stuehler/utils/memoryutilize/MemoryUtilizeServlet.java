package net.stuehler.utils.memoryutilize;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MemoryUtilizeServlet
 */
@WebServlet("/MemoryUtilizeServlet")
public class MemoryUtilizeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MemoryUtilizeServlet.class.getName());

	@Inject
	private ShortLifespanMemoryBean memoryBean;

	@Inject
	private MemoryLeakBean memoryLeakBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemoryUtilizeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String size = request.getParameter("size");
		String num = request.getParameter("num");
		String lifespan = request.getParameter("lifespan");
		try (PrintWriter out = response.getWriter()) {
			final String CLEAR_PARAM = "clear";
			boolean isClear = false;
			Enumeration<String> params = request.getParameterNames();
			while (params.hasMoreElements()) {
				if (params.nextElement().equals(CLEAR_PARAM)) {
					isClear = true;
				}
			}

			if (isClear) {
				long bytes = memoryLeakBean.clear();
				out.println("Freed " + bytes + " bytes.");
			} else {
				if (size == null || size.isEmpty() || num == null || num.isEmpty()) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing arguments.");
					return;
				}
				int arraySize = Integer.parseInt(size);
				int numArrays = Integer.parseInt(num);
				int lifespanSeconds = lifespan == null || lifespan.isEmpty() ? 0 : Integer.parseInt(lifespan);
				byte[][] byteArrays = new byte[numArrays][];
				Random random = new Random();
				for (int i = 0; i < numArrays; i++) {
					byteArrays[i] = new byte[arraySize];
					random.nextBytes(byteArrays[i]);
				}
				if (lifespanSeconds > 0) {
					memoryBean.addMemory(new ShortLifespanMemory(lifespanSeconds, byteArrays));
				} else {
					memoryLeakBean.addArrays(byteArrays);
				}
				out.println("Successfully created " + numArrays + " byte arrays of size " + arraySize + ".");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
