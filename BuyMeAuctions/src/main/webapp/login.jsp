<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>BuyMe User Login</title>
		<style>
			body {
				font-family: Calibri, sans-serif;
				background-color: #424957;
				color: white;
			}
		</style>
	</head>
	<body>
		<div align="center">
			<h1>BuyMe User Login</h1>
			<form action =LoginServlet method="post">
				<table>
				
					<tr><td>User Name: </td><td><input type="text" name="username"></td></tr>
					<tr><td>Password: </td><td><input type="password" name="password"></td></tr>
					
					<tr><td></td><td><input type="submit" name="loginbutton" value="Log In"></td></tr>
					
					<tr><td><br>New to BuyMe? Create an account: </td><td>
					<tr><td>User Name: </td><td><input type="text" name="newusername"></td></tr>
					<tr><td>Password: </td><td><input type="password" name="newpassword"></td></tr>
					<tr><td>Email: </td><td><input type="text" name="newemail"></td></tr>
					<tr><td>Home Address: </td><td><input type="text" name="newaddress"></td></tr>
					
					<tr><td></td><td><input type="submit" name="createbutton" value="Create Account"></td></tr>
					
				</table>
			</form>
		</div>
	</body>
</html>