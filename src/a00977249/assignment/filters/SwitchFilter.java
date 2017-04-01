package a00977249.assignment.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import a00977249.assignment.util.CookieUtilities;

/**
 * Servlet Filter implementation class Switch
 */
public class SwitchFilter implements Filter {

	public static final String TABLE_REFERER_URI = "/Table";
	
	/**
     * Default constructor. 
     */
    public SwitchFilter() {}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String userStatus = CookieUtilities.getCookieValue(req.getCookies(), "userStatus", "");
		
		String requestingURI = req.getRequestURI();
		if(requestingURI.contains(TABLE_REFERER_URI)) {
		    if (req.getHeader("Authorization") == null || !userStatus.equals("authorized")) {
				askForPassword(res);		
			}
		}
		chain.doFilter(request, response);
	}
	
	/**
	 * Prompts the user for Authorization header if none provided in the request
	 * 
	 * @param response 
	 * @param response the HttpServletResponse
	 */
	private void askForPassword(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Ie 401
		// Instruction code lines or Headers
		response.setHeader("WWW-Authenticate", "BASIC realm=\"privileged-few\"");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {}

}
