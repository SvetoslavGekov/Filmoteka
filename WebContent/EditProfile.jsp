<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<style>

body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box}


/* Add a background color when the inputs get focus */
input[type=text]:focus, input[type=number]:focus, input[type=email]:focus {
    background-color: #ddd;
    outline: none;
}

/* Set a style for all buttons */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
    opacity: 0.9;
}

button:hover {
    opacity:0.6;
}

/* Extra styles for the reset button */
.resetbtn {
    padding: 14px 20px;
    background-color: #f44336;
}

body {font-family: Arial, Helvetica, sans-serif;}
form {border: 8px solid #f1f1f1;}

input[type=text],input[type=email], input[type=number] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
}

.resetbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
}

</style>

<title>My Account</title>
</head>
<body>
	<h1>Edit Profile</h1>
	<form method= "POST" action="updateProfile" >
	<% User user = (User) session.getAttribute("USER"); %>
	
	 First name: <input type="text" placeholder="<%= user.getFirstName() %>" name="firstname" maxlength="15"><br>
	 Last name: <input type="text" placeholder="<%= user.getLastName() %>" name="lastname" maxlength="15"><br>
	 Email: <input type="email" placeholder="<%= user.getEmail() %>" name="email" maxlength="15"><br>
	 Phone number: <input type="number" placeholder="<%= user.getPhone() %>" name="phone" maxlenght="10" minlenght="10"><br>
	 
	 <button type="submit" class="savebtn">Save Changes</button>
	 <button type="reset"  class="resetbtn">Reset</button>
	</form>
</body>
</html>
