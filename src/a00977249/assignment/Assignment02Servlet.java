package a00977249.assignment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import a00977249.assignment.data.DataManager;
import a00977249.assignment.data.Database;
import a00977249.assignment.data.product.Product;
import a00977249.assignment.data.product.ProductDao;
import a00977249.assignment.decode.Decoder;
import a00977249.assignment.util.CookieUtilities;
import a00977249.assignment.util.Utility;
import a00977249.assignment.view.HTMLManager;
import tribble.util.Base64Decoder;

/**
 * Servlet implementation class Controller
 */
public class Assignment02Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String PRICE_ERROR = "Enter product price in C$.  Example: ##.#";
	public static final String WEIGHT_ERROR = "Enter product weight in pounds.  Example: ##.#";
	public static final String CODE_ERROR = "Enter the 5 digit product code.  Example: #####";
	public static final String CODE_PATERN = "\\d{5}";
	
	public static final String SQLCOMMAND_URI = "/WEB-INF/JSP/sqlCommands.jsp";
	public static final String TABLES_URI = "/WEB-INF/JSP/tables.jsp";
	public static final String ABOUT_URI = "/WEB-INF/JSP/about.jsp";
    public static final String INDEX_URI = "/index.jsp";
   
    public static final String ABOUT_REFERER_URI = "/About";
    public static final String TABLE_REFERER_URI = "/Table";
    public static final String SQLCOMMAND_REFERER_URI = "/SQLCommand";
    public static final String INDEX_REFERER_URI = "/assignment02";
    public static final String LANCHANGED_REFERER_URI = "/LanChanged";
    
	public static final String RESOURCE_BUNDLES_PATH = "/a00977249/assignment/resourcebundles/MyResource";
	public static final int COOKIE_MAX_AGE = 60 * 60;
	
	private ResourceBundle resourceBundle;	
	private Properties decryptedDBProps;
	private ProductDao dao;
	private DataManager dm;
	private Locale locale;
    
	private String dbPropsPath, pwPropsPath, userPassword, action;
	   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Assignment02Servlet() {
        super();
    }
    
    /**
     * Establishes connection to the remote db and instantiates dao object
     * 
     * @param config contains deploy time info
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
    	try {
    		super.init(config);
        	resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLES_PATH);
        	dm = new DataManager();
         	dbPropsPath = config.getServletContext().getRealPath("/") + config.getInitParameter("encryptedDbPropPath");
         	pwPropsPath = config.getServletContext().getRealPath("/") + config.getInitParameter("encryptedPWPropPath");
    	} catch (java.util.MissingResourceException e) {
			e.printStackTrace();
		}
   	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		try {
			HttpSession session = request.getSession();
			String requestingURI = request.getRequestURI();
	
			Cookie cookie = CookieUtilities.getCookie(request.getCookies(), "languageChoice"); 
			action = INDEX_URI;
 		
 			if(requestingURI.contains(ABOUT_REFERER_URI)) {
    			action = ABOUT_URI;
      			setAboutI18N(request);
			} else if(requestingURI.contains(TABLE_REFERER_URI)) {
				String userStatus = CookieUtilities.getCookieValue(request.getCookies(), "userStatus", "");
				
				boolean isAuthentic = false;
				// user not authorized, or user authorized but server restarted during the same session
				// or it could be a new session
				if(userStatus.equals("") || (userStatus.equals("authorized") && decryptedDBProps == null)) {		
					if(authenticateSession(request, response)) {
						Database.config(decryptedDBProps);
						Database.connect(decryptedDBProps);
					    dao = new ProductDao(getStatement(getConnection()));
				    	response.reset();
						response.addCookie(new Cookie("userStatus", "authorized"));
					    isAuthentic = true;
					} else {
						response.addCookie(new Cookie("userStatus", ""));
					}
				} else if(getConnection().isClosed()) {
					Database.connect(decryptedDBProps);
					dao = new ProductDao(getStatement(getConnection()));
				    isAuthentic = true;
				} else {
					isAuthentic = true;
				}
				if(isAuthentic) {
					setTablePageParams(request,session, null);
				}
			} else if(request.getParameter("insert") != null) {	
					setTablePageParams(request, session, dao.insert(getProduct(request)));
		 	} else if(request.getParameter("update") != null) {
		 			setTablePageParams(request, session, dao.update(getProduct(request)));
		    } else if(request.getParameter("delete") != null) {
		 	    setTablePageParams(request, session, dao.delete(Integer.parseInt(request.getParameter("id"))));		
			} else if(requestingURI.contains(SQLCOMMAND_REFERER_URI)) {
				action = SQLCOMMAND_URI;
				HTMLManager hm = new HTMLManager();
				setSQLCommandsI18N(request);
				request.setAttribute("sqlCommandsTable", hm.getSQLCommandsTable(dm.getSqlCollection(), dm.getCommandCreationTime(), resourceBundle));    		
			} else if(cookie != null || requestingURI.contains(LANCHANGED_REFERER_URI)) { 
				String choice = request.getParameter("choice");
				if(choice == null) {
					choice = cookie.getValue();
				}
				setLocale(choice);
				cookie = new Cookie("languageChoice", choice);
			    cookie.setMaxAge(COOKIE_MAX_AGE);
	            response.addCookie(cookie);   
	            if(!choice.equals((String) session.getAttribute("choice"))) {
	                session.setAttribute("choice", choice);
	            }
    	    }
 			if(action.equals(INDEX_URI)) {
	 		    if(locale != null) {
	 		    	setHomeI18N(request);		
	 		    }
 				if("no".equals(request.getParameter("logoff"))) {
	 		    	closeConnection();
	 		    }
			}
 			response.setContentType("text/html");
	 		response.setIntHeader("Refresh", 120);
	 		response.setHeader("Cache-Control","no-cache");
	 		response.setHeader("Pragma","co-cache");
	 		request.setAttribute("greeting", resourceBundle.getString("Greeting"));
			request.getRequestDispatcher(action).forward(request, response);		
			
	 	} catch (java.lang.NumberFormatException e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	    } catch (IllegalArgumentException e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	    } catch (SQLException e) {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Handles the insert statement
	 * 
	 * @param request
	 * @throws SQLException
	 */
	public Product getProduct(HttpServletRequest request) throws SQLException {

		Product product = new Product();
	    String paramName;
		String paramValue;
		
		Enumeration<String> paramNames = request.getParameterNames();
	    while(paramNames.hasMoreElements()) {   
		  	paramName = paramNames.nextElement();
		    paramValue = request.getParameter(paramName);
		    switch (paramName) {
		    
		        case "Id" :
		    	    product.setId(Integer.parseInt(paramValue));
		        	break;
		        	
		        case "Name" : 
		        	
		    	    if(!Utility.validateInput(paramValue)) {
		    		    throw new IllegalArgumentException("Name is not a nullable field. Enter product name.");
		            } else {
		                product.setName(paramValue);
		            }
     		    break;
     		    
	     	    case "Price" :
	     	    	
		        	if(!Utility.validateInput(paramValue)) {
	    	            throw new IllegalArgumentException(String.format("Price is not a nullable field. %s", PRICE_ERROR));               
	        	    }
		        	double price;
		        	try {
		    	        price = Double.parseDouble(paramValue);
		        	} catch (java.lang.NumberFormatException e) {
		    	    	throw new java.lang.NumberFormatException(String.format("Invalid Entry: '%s'. %s", paramValue, PRICE_ERROR));
		        	}
		    	    if(!Utility.isPositive(price)) {
		    		    throw new IllegalArgumentException(String.format("Invalid Entry: '%s'. Price must be positive.", paramValue));
		        	} else {
		        		product.setPrice(Utility.twoDecimals(price));
		    	    }
		            break;
		            
	    	    case "Weight" :
	    	    	
		        	if(!Utility.validateInput(paramValue)) {
		        	    throw new IllegalArgumentException(String.format("Weight is not a nullable field. %s", WEIGHT_ERROR));
		    	    } 
	    	    	double weight;
		        	try {
		         	    weight = Double.parseDouble(paramValue);
		    	    }  catch (java.lang.NumberFormatException e) {
		        		throw new java.lang.NumberFormatException(String.format("Invalid Input: '%s'. %s", paramValue, WEIGHT_ERROR));
		        	}
		    	    if(!Utility.isPositive(weight)) {
		    		    throw new IllegalArgumentException(String.format("Invalid input: '%s'. Weight must be positive. %s", paramValue, WEIGHT_ERROR));    
		            } else {
		                product.setWeight(Utility.twoDecimals(weight));
		            }           
		            break;
		            
		        case "Code" : 
		        	
		        	if(!Utility.validateInput(paramValue)) {
		    	    	throw new IllegalArgumentException(String.format("Code is not a nullable field. %s", CODE_ERROR));
		        	} else if (!Utility.checkPattern(paramValue, CODE_PATERN)) {
		        		throw new IllegalArgumentException(String.format("Invalid Entry: '%s'. %s", paramValue, CODE_ERROR));  
		    	    } else if(request.getParameter("update") == null && dao.validateCode(Integer.parseInt(paramValue))) {
		    	        throw new IllegalArgumentException("Duplicate code. A product with code '" + paramValue + "' already exists in the table. "
		    					+ "Check your poduct code and try again with a valid code.");
		            } else {
		        		product.setCode(Integer.parseInt(paramValue));
		            }
		        	break;
		        	
		        case "Manufacturer" : 
		        	
		    	    product.setManufacturer(paramValue);
		        	break;
		        	
		        case "MadeIn" : 
		        	
		    	    product.setMadeIn(paramValue);
		        	break;
		        	
		        case "Description" : 
		        	
		        	product.setDescription(paramValue);
		         	break;
		    }
		}
	    return product;
	}
	
	public void loadAppParams() {
	}
	
	/**
	 * Configures the parameters needed to load the t.jsp
	 * 
	 * @param request the user request
	 * @param session the session object
	 * @param sqlCommand the SQL command to be added to the session collection
	 * @throws SQLException
	 */
	public void setTablePageParams(HttpServletRequest request,HttpSession session, String sqlCommand) throws SQLException {
		action = TABLES_URI;
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLES_PATH, locale);
     	request.setAttribute("tableHeader1", resourceBundle.getString("Table_Header1"));
      	request.setAttribute("tableHeader2", resourceBundle.getString("Table_Header2"));
      	request.setAttribute("Id", resourceBundle.getString("Id_Header"));
      	request.setAttribute("Name", resourceBundle.getString("Name_Header"));
      	request.setAttribute("Price", resourceBundle.getString("Price_header"));
      	request.setAttribute("Weight", resourceBundle.getString("Weight_Header"));
      	request.setAttribute("Code", resourceBundle.getString("Code_Header"));
      	request.setAttribute("Manufacturer", resourceBundle.getString("Manufacturer_Header"));
      	request.setAttribute("MadeIn", resourceBundle.getString("MadeIn_Header"));
      	request.setAttribute("Description", resourceBundle.getString("Description_Header"));
      	request.setAttribute("Action", resourceBundle.getString("Action_Header"));
      	request.setAttribute("DisInstruction", resourceBundle.getString("Disconnect_Instructions"));
      	request.setAttribute("Connected", resourceBundle.getString("Stau_Connected"));
      	request.setAttribute("Disconnected", resourceBundle.getString("Disconnect"));
      	request.setAttribute("metadata", getMetaData());
      	request.setAttribute("products", loadTable().getOutputData());
    	request.setAttribute("protocol", request.getProtocol());
    	if(sqlCommand != null) {
    		dm.addSQLCommand(sqlCommand);
    		dm.addDate(new java.util.Date());	
    	} else {
    		dm.setSqlCollection(getSQLSessionCollection(session));
			dm.setCommandCreationTime(getSQLSessionCreation(session));
    	}
	}
	
	/**
	 * Loads the data from remote db and sets them with the request scope variables
	 * 
	 * @return HTML manager reference
	 * @throws SQLException
	 */
	public HTMLManager loadTable() throws SQLException {
		HTMLManager hm = new HTMLManager();
		ArrayList<Product> productsList = new ArrayList<Product>();
		dm.setProductList(productsList);
		dao.loadData(productsList);
		for(Product product : productsList) {
			hm.addRowToOutputData(product);
		}
		return hm;
	}
	
	/**
	 * Connects to the remote db
	 * 
	 * @return connection to the remote db
	 * @throws SQLException
	 */
	private synchronized Connection getConnection() throws SQLException {
		return Database.getConnection();
	}
	
	/**
	 * Creates a statement using the DB connection
	 * 
	 * @param Connection established connection to the DB
	 * @return created statement 
	 * @throws SQLException
	 */
	public Statement getStatement(Connection con) throws SQLException {
		return con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
	}
	
	/**
	 * Closes the connects to the remote db
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		Database.closeConnection();
	}
		
	/**
	 * Sets the home page presentation language based on the chosen lacale
	 *
	 * @param request the user request
	 */
	public void setHomeI18N(HttpServletRequest request) {
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLES_PATH, locale);
		request.setAttribute("welcome", resourceBundle.getString("Welcome"));
		request.setAttribute("about", resourceBundle.getString("About"));
		request.setAttribute("changeLanguage", resourceBundle.getString("ChangeLanguage"));
		request.setAttribute("table", resourceBundle.getString("Table"));
	    request.setAttribute("command", resourceBundle.getString("SQL"));
	}
	
	/**
	 * Sets the about page presentation language based on the chosen locale
	 * 
	 * @param request the user request
	 */
	public void setAboutI18N(HttpServletRequest request) {
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLES_PATH, locale);
     	request.setAttribute("introductionHeader", resourceBundle.getString("Introduction_Header"));
		request.setAttribute("introduction", resourceBundle.getString("Introduction"));
	    request.setAttribute("howToUseHeader", resourceBundle.getString("How_To_Use_Header"));
	    request.setAttribute("howToUse", resourceBundle.getString("How_To_Use"));
	    request.setAttribute("crudHeader", resourceBundle.getString("CRUD_Header"));
	    request.setAttribute("crud", resourceBundle.getString("CRUD"));
	    request.setAttribute("lanOption", resourceBundle.getString("Language_Option"));
	}

	/**
	 * Sets the SQLCommands page presentation language based on the chosen locale
	 * 
	 * @param request the user request
	 */
	public void setSQLCommandsI18N(HttpServletRequest request) {
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLES_PATH, locale);
     	request.setAttribute("SQLHeader", resourceBundle.getString("SQL_Header"));
	}
	
	/**
	 * Retrieves the SQL commands execution date collection from the session object
	 * 
	 * @param session the session object
	 * @return a collection of SQL commands execution date
	 */
	public ArrayList<String> getSQLSessionCollection(HttpSession session) {
		@SuppressWarnings("unchecked")
		ArrayList<String> sqlCommands = (ArrayList<String>) session.getAttribute("sqlCollection");
	    if (sqlCommands == null) {
	        sqlCommands = new ArrayList<String>();
	        session.setAttribute("sqlCollection", sqlCommands);
	    }
	    return sqlCommands;
	}
	
	/**
	 * Retrieves the SQL commands collection from the session object
	 * 
	 * @param session the session object
	 * @return a collection of SQL commands
	 */
	public ArrayList<Date> getSQLSessionCreation(HttpSession session) {
		@SuppressWarnings("unchecked")
		ArrayList<Date> sqlTimes = (ArrayList<Date>) session.getAttribute("sqlCreation");
	    if (sqlTimes == null) {
	        sqlTimes = new ArrayList<Date>();
	        session.setAttribute("sqlCreation", sqlTimes);
	    }
	    return sqlTimes;
	}
	
	/**
	 * Returns true if the user is authorized and false otherwise 
	 * 
	 * @return true if the user is authenticated and false otherwise
	 */
	public boolean authenticateSession(HttpServletRequest request, HttpServletResponse response) {
		boolean result = true;
		String authorization = request.getHeader("Authorization");
		if (authorization == null) {
			result = false;
		} else if(authorization != null) {
			String userInfo = authorization.substring(6).trim();
			Base64Decoder decoder = new Base64Decoder();
			String nameAndPassword = new String(decoder.decodeBuffer(userInfo));
			int index = nameAndPassword.indexOf(":");
			@SuppressWarnings("unused")
			String user = nameAndPassword.substring(0, index);
			userPassword = nameAndPassword.substring(index + 1);
			try {
				decryptAndLoadPropFile(userPassword);
			} catch (Exception e) {
				result = false;
			} 
	    }
		return result;
	}
	
	/**
	 * Throws IOException if the decipher key is not valid to decrypt and load the
	 * DB properties file  
	 * 
	 * @param realPassword the pass key for deciphering
	 * @throws Exception 
	 */
	public void decryptAndLoadPropFile(String password) throws Exception {
		decryptedDBProps = new Properties();
		String decipher = Decoder.getDecipherCode(pwPropsPath, password);
		InputStream inputStream = new ByteArrayInputStream(decipher.getBytes(StandardCharsets.UTF_8));
		decryptedDBProps.load(inputStream);
		String realPassword = decryptedDBProps.getProperty("user");
		if ((realPassword != null) && (realPassword.equals(password))) {
            decipher = Decoder.getDecipherCode(dbPropsPath, password);
		    inputStream = new ByteArrayInputStream(decipher.getBytes(StandardCharsets.UTF_8));
		    decryptedDBProps.load(inputStream);
		}		
	}
	
    /**
     * Closes the connection to the DB only if it is still open
     */
	public void destroy() {
	    try {
	    	if(!getConnection().isClosed()) {
	    		closeConnection();
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Returns database meta data
	 * 
	 * @return an array of DB meta data
	 * @throws SQLException
	 */
	public String[] getMetaData() throws SQLException {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		String[] dbInfo = { 
				dbMetaData.getURL(), dbMetaData.getDatabaseProductName(), 
				dbMetaData.getDatabaseProductVersion(), 
				dbMetaData.getDriverName(), dbMetaData.getDriverVersion() };
		return dbInfo;
	}
	
	/**
	 * Sets the locale based on the choice
	 * 
	 * @param choice language option
	 */
	public void setLocale(String choice) {
		if("English".equals(choice)) {
	    	locale = Locale.CANADA;
	    } else if("French".equals(choice)) {
	    	locale = Locale.CANADA_FRENCH;
	    } else if("Spanish".equals(choice)) {
	    	locale = new Locale("sp", "ES");
	    }
	}
}
