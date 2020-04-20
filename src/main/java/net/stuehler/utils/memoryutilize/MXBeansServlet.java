package net.stuehler.utils.memoryutilize;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MXBeansServlet
 */
@WebServlet("/MXBeansServlet")
public class MXBeansServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MXBeansServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter out = response.getWriter()) {
			List<GarbageCollectorMXBean> gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
			out.println("== Garbage Collectors ==");
			for (GarbageCollectorMXBean gcMXBean : gcMXBeans) {
				out.print("Name: ");
				out.println(gcMXBean.getName());
				out.print("\tCollection Count: ");
				out.println(gcMXBean.getCollectionCount());
				out.print("\tCollection Time: ");
				out.println(gcMXBean.getCollectionTime());
				out.print("\tMemory Pools:");
				for (String pool : gcMXBean.getMemoryPoolNames()) {
					out.print(" \"" + pool + "\"");
				}
				out.println();
			}

			List<MemoryPoolMXBean> memPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
			out.println("== Memory Pools ==");
			for (MemoryPoolMXBean memPoolMXBean : memPoolMXBeans) {
				out.print("Name: ");
				out.println(memPoolMXBean.getName());
				out.print("\tType: ");
				out.println(memPoolMXBean.getType());
				out.print("\tUsage: ");
				out.println(memPoolMXBean.getUsage());
				out.print("\tPeak Usage: ");
				out.println(memPoolMXBean.getPeakUsage());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
