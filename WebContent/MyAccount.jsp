<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.sun.javafx.binding.StringFormatter"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Account</title>
</head>
<body>

	<form method= "GET" action="editProfile" >
	<% User user = (User) session.getAttribute("USER"); %>
	<h3>My Account</h3><br><br>
	First name: <%= user.getFirstName()%><br>
	Last name: <%= user.getLastName() %><br>
	Username: <%= user.getUsername() %><br>
	Email: <%= user.getEmail() %><br>
	Phone: <%= user.getPhone() %><br>
	Date of registration: <%= user.getRegistrationDate() %><br>
	Last login: <%= user.getLastLogin().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) %><br>
	Money: <%= user.getMoney() %><br>
	<input type="submit" value="Edit"><br>
	Back To<a href="MainPage.html">Main Page</a>
	</form>
</body>
</html>