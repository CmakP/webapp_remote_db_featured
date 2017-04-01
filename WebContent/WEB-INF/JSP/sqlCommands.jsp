<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQL commands</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">

</head>

<!-- JSP init() -->
<%! 
    private String tableName;

	public void jspInit() {
		tableName = getServletContext().getInitParameter("tableName");
    }
%>

<body>

 <div class="header">
  <h1>COMP 3613 Assignment02</h1>
  <h2>Siamak Pourian, A00977249</h2>
  <h3>${requestScope.greeting}</h3>
 </div>
  
  <div class="center">
   <h3>${requestScope.SQLHeader} '<%=tableName%>' table,<br>HTTPSession id: <%=request.getSession().getId()%></h3>
   <br>${requestScope.sqlCommandsTable}
  </div>
 <br>
 <div class="center">
  <form action="assignment02" method="post">
   <div>
    <input type="submit" value="Goto Homepage" class="button">
   </div>
  </form>
 </div>
 
 <div class="footer" style="margin-top: 270px">
  <img src="Images/flag.png" alt="Flag">
   Copyright © Siamak Pourian
 </div>
 
</body>
</html>