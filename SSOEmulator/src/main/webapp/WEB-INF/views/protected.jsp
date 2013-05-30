<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %>
<html>
<head>
	<title>Protected</title>
</head>
<body>
<h1>
	Protected Resources 
</h1>
<p><a href="/sample" > back</a></p> <p><a href="<c:url value="j_spring_security_logout" />" > Logout</a></p>
<P> <h2>Loggedin UserName:</h2> <sec:authentication property="principal.username"/> </P>
<sec:authorize access="hasRole('ROLE_ADMIN')">
         This session will be visible to an admin only.<br/>
         You are an Administrator.<br/>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_USER')">
         This session will be visible to an Customer only.<br/>
         You are an Customer.<br/>
        </sec:authorize>


</body>
</html>
