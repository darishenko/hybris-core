<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html>
<title>
    <spring:message code="text.band.details"/>
</title>
<body>
<h1>
    <spring:message code="text.band.details"/>
</h1>
<ul>
    <c:forEach var="band" items="${bands}">
        <li>
            <a href="./bands/${band.id}">${band.name}</a>
            <img src="${band.imageURL}"/>
        </li>
    </c:forEach>
</ul>
</body>
</html>