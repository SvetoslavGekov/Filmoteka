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
input[type=text]:focus, input[type=number]:focus, input[type=email]:focus , div > input:focus{
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

input[type=text],input[type=email], input[type=number], div > input {
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
	
	 First name: <input type="text" placeholder="<%= user.getFirstName() %>" name="firstname" 
		 pattern="[A-Za-z]{1,32}" title="Not including numbers or special characters" style="text-transform: capitalize;"><br>
	 Last name: <input type="text" placeholder="<%= user.getLastName() %>" name="lastname" 
		 pattern="[A-Za-z]{1,32}" title="Not including numbers or special characters" style="text-transform: capitalize;"><br>
	 Email: <input type="email" placeholder="<%= user.getEmail() %>" name="email" 
	 	 pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"><br>
	 Phone number: <input type="text" placeholder="<%= user.getPhone() %>" name="phone" 
	 	 pattern="08\d{8}" title="Starting with 08 and 10 symbols long"><br>
	
	 <div style="display:none" id="passwordfield">
	 	 Current password: <input type="password" id="cpass" placeholder="Enter your password" name="currentPass" ><br>
	 	 New Password: <input type="password" id="npass"  placeholder="Enter the new passsword" name="newPass1"><br>
	 	 <input type="password" id="npass2"  placeholder="Repeat the new password" name="newPass2"><br>
	 </div>
	 
	 <input type="button" id="change_pass_btn" onclick="openPassFields()" value="Change Password">
	 <button type="submit" class="savebtn">Save Changes</button>
	 <button type="reset"  class="resetbtn">Reset</button>
	</form>
	
	<script type="text/javascript">
		function openPassFields(){
			document.getElementById("cpass").setAttribute("required", "");
			document.getElementById("cpass").setAttribute("pattern","^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
			document.getElementById("cpass").setAttribute("title", "At least 6 characters containing: 1 Uppercase letter, 1 Lowercase letter and 1 number");
			
			document.getElementById("npass").setAttribute("required", "");
			document.getElementById("npass").setAttribute("pattern","^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
			document.getElementById("npass").setAttribute("title", "At least 6 characters containing: 1 Uppercase letter, 1 Lowercase letter and 1 number");
			
			document.getElementById("npass2").setAttribute("required", "");
			document.getElementById("npass2").setAttribute("pattern","^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
			document.getElementById("npass2").setAttribute("title", "At least 6 characters containing: 1 Uppercase letter, 1 Lowercase letter and 1 number");
			
			document.getElementById("passwordfield").style.display = "block";
		}
	</script>
</body>
</html>
