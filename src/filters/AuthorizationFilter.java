package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthorizationFilter
 */
@WebFilter("/auth/*")
public class AuthorizationFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Cast request and response to HTTP equivalents
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// Get the session object
		HttpSession session = req.getSession(false);

		// If there is no session at all -> nobody has logged in
		if (session == null) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Check if the session is newly created
		if (session.isNew()) {
			// Set the IP of the request which called the server
			session.setAttribute("ip", req.getRemoteAddr());
		}
		// If the session is old (not invalidated)
		else {
			// Check if the stored IP corresponds to the current caller IP or if there is an
			// set user
			String sessionIp = (String) session.getAttribute("ip");

			if (sessionIp == null || !sessionIp.equals(req.getRemoteAddr()) || (session.getAttribute("USER") == null)) {
				// If not (potential session highjacking) -> invalidate session and set status
				// as unauthorized access
				System.out.println("No set IP (nobody logged), no user or cookie");
				session.invalidate();
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		// If the IP is the same as the one stored and there is a logged in user ->continue with the chain
		// Set caching options
		res.setHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
