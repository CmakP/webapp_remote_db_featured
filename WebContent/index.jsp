<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COMP 3613 Assignment02</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>

<!-- JSP init() -->
<%! 
    private String headerGreeting, bodyGreeting, instruction;

	public void jspInit() {		
		ServletConfig config = getServletConfig();
		headerGreeting = config.getInitParameter("headerGreeting");
		bodyGreeting = config.getInitParameter("bodyGreeting");
		instruction = config.getInitParameter("instruction");
    }
%> 

<body>
<div>

 <div class="header">
  <h1>COMP 3613 Assignment02</h1>
  <h2>Siamak Pourian, A00977249</h2>
  
<%
  if(request.getSession().getAttribute("choice") == null) {
%>
 <h3><%=headerGreeting%></h3>
 </div>
 
 <div class="center">
  <form action="LanChanged" method="post">
   <div>
    <h2><%=bodyGreeting%><br><%=instruction%><br></h2>
    <input type="radio" name="choice" value="English" checked> English  
    <input type="radio" name="choice" value="French"> Français
    <input type="radio" name="choice" value="Spanish"> Español<br><br>
    <input type="submit" value="Submit" class="button">
   </div>
  </form>
 </div>
 
  <div class="footer" style="margin-top: 243px">
  <img src="Images/flag.png" alt="Flag">
   Copyright © Siamak Pourian
 </div>

<%
  } else {
%>
 <h3>${requestScope.greeting}</h3>
 </div>

 <div class="center">
  <form action="About" method="post">
   <div>
    <h1>${requestScope.welcome}</h1>
    <h3>${requestScope.about}<br>${requestScope.changeLanguage}</h3>
      <input type="submit" value="About" class="button">
   </div>
  </form>
 </div>

 <div class="center">
  <form action="Table" method="post">
   <div>
   <h3>${requestScope.table}</h3>  
    <input type="submit" value="View Tables" class="button">
   </div>
  </form>
 </div>

 <div class="center">
  <form action="SQLCommand" method="post">
   <div>
   <h3>${requestScope.command}</h3>  
    <input type="submit" value="View SQL Commands" class="button">
   </div>
  </form>
 </div>
  
 <div class="footer" style="margin-top: 40px">
  <img src="Images/flag.png" alt="Flag">
   Copyright © Siamak Pourian
 </div>

<%
  }
%>
 	
</body>
</html>