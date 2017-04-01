<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>About page</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">

</head>

<body>
 <div>
  
  <div class="header">
   <h1>COMP 3613 Assignment02</h1>
   <h2>Siamak Pourian, A00977249</h2>
   <h3>${requestScope.greeting}</h3>
  </div>
  
  <form action="assignment02" method="post">
   <div class="center">
    <input type="submit" value="Goto Homepage" class="button">
   </div>
  </form>
   
  <div>
   <h3>${requestScope.introductionHeader}</h3>
   <p>${requestScope.introduction}</p><br>
   <h3>${requestScope.howToUseHeader}</h3>
   <p>${requestScope.howToUse}</p><br>
   <h3>${requestScope.crudHeader}</h3>
   <p>${requestScope.crud}</p><br>
   
   <div class="center">
    <h4>${requestScope.lanOption}</h4>
    <form action="LanChanged" method="post">
      <div>
       <input type="radio" name="choice" value="English" checked> English  
       <input type="radio" name="choice" value="French"> Français
       <input type="radio" name="choice" value="Spanish"> Español<br><br>
       <input type="submit" value="Change Language" class="button">
      </div>
    </form>
   </div>
     
  </div>
   
   <div class="footer" style="margin-top: 40px">
    <img src="Images/flag.png" alt="Flag">
     Copyright © Siamak Pourian
   </div>

 </div>
</body>
</html>