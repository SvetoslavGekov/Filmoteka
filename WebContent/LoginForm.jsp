<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<style>
body {font-family: Arial, Helvetica, sans-serif;}
form {border: 3px solid #f1f1f1;}

input[type=text], input[type=password] {
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

button:hover {
    opacity: 0.8;
}

.cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
}

.imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
}

img.avatar {
    width: 19%;
    border-radius: 50%;
}

.container {
    padding: 16px;
}

</style>
</head>
<body>

<h2>Login Form</h2>

<form method ="POST" action= "LoginServlet">
  <div class="imgcontainer">
    <img src="icons/avatar.png" alt="Avatar" class="avatar">
  </div>

  <div class="container">
    <label for="username"><b>Username</b></label>
    <input type="text" placeholder="Enter Username" name="username" required>

    <label for="password"><b>Password</b></label>
    <input type="password" placeholder="Enter Password" name="password" required>
        
    <button type="submit">Login</button>
    <label>
      <input type="checkbox" checked="checked" name="remember"> Remember me
    </label>
    
    <%	String login_msg=(String)request.getAttribute("error");  
    		
		if(login_msg!=null){
			out.println("<font color=red size=4px><center><i>"+login_msg+"</i></center></font>");
   		}
	%>
	
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="register">Don't have an account?  <a href="RegistrationForm.html" style="color:red"><b><i>Sign up now.</i></b></a></span>
  </div>
</form>

</body>
</html>