/**
 * Project: COMP3613_A00977249Assignment02
 * File: DataManager.java
 * Date: Oct 14, 2016
 * Time: 9:45:29 PM
 */
package a00977249.assignment.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;

/**
 * @author Siamak Pourian
 *
 * CookieUtilities Class
 */
public class CookieUtilities {
	
	/** Given the cookie objects array, a name, and a default value,
	   *  this method tries to find the value of the cookie with
	   *  the given name. If no cookie matches the name,
	   *  the default value is returned.
	 * @throws UnsupportedEncodingException 
	   */
	  
	  public static String getCookieValue
	                           (Cookie[] cookies,
	                            String cookieName,
	                            String defaultValue) {
	    if (cookies != null) {
	      for(int i=0; i<cookies.length; i++) {
	        Cookie cookie = cookies[i];
	        if (cookieName.equals(cookie.getName())) {
	        	return (cookie.getValue());
	        }
	      }
	    }
	    return(defaultValue);
	  }

	  /** Given the cookie objects array and a name, this method tries
	   *  to find and return the cookie that has the given name.
	   *  If no cookie matches the name, null is returned.
	   */
	  
	  public static Cookie getCookie(Cookie[] cookies,
	                                 String cookieName) {
	    if (cookies != null) {
	      for(int i=0; i<cookies.length; i++) {
	        Cookie cookie = cookies[i];
	        if (cookieName.equals(cookie.getName())) {
	          return(cookie);
	        }
	      }
	    }
	    return(null);
	  }
}
