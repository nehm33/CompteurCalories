<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Sign in</title>
    <link rel="stylesheet" type="text/css" href="css/login.css" />
</head>
<meta charset="UTF-8">
</head>
<body>
    <div class="conteneur">
        <div class="head">
        	<h1>Sign in to CompteurCalories</h1>
        	<c:if test="${ !empty error }">
            	<div class="error">
            		<p><c:out value="${ error }" /></p>
            	</div>
            </c:if>
        </div>
        <div class="form">
            <form class="formulaire" method="post" action="">
                <label for="username">Username</label><br>
                <input type="text" name="username" id="username" placeholder="Username" required><br>
                <label for="password">Password</label><br>
                <input type="password" name="password" id="password" placeholder="Password" required><br>
                <input type="submit" id="sub-btn" value="Sign in">
            </form>
        </div>
    </div>
</body>
</html>