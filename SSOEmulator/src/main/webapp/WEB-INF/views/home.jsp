<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<a href="/sample/authenticated">Authenticated</a>
<p>UserID:romi Password:password Role: ROLE_ADMIN</p>
<p>UserName: mahesh Password:password Role:ROLE_USER</p>

<P> Request Headers parameters that are set by SSO provider( WebSEAL/Siteminder):</P>
<p>iv-user:romi authorities:ROLE_ADMIN</P>

<p>
iv-user:mahesh authorities:ROLE_USER</P>

</body>
</html>
