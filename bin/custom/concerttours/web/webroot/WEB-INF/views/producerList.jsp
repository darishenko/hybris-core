<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html>
<title>
    <spring:message code="text.producers"/>
</title>
<body>
<h1>
    <spring:message code="text.producers"/>
</h1>
<ul>
    <c:forEach var="producer" items="${producers}">
        <li>
            <a href="./producers/${producer.id}">
                    ${producer.firstName} ${producer.lastName}
            </a>
        </li>
    </c:forEach>
</ul>
</body>
</html>
