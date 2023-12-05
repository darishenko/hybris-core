<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html>
<title>
    <spring:message code="text.producer.details"/>
</title>
<body>
<h1>
    <spring:message code="text.producer.details"/>
</h1>
<h3>${producer.firstName} ${producer.lastName}</h3>
<p>
    <spring:message code="text.tours.list"/>
</p>
<ul>
    <c:forEach var="tour" items="${producer.tours}">
        <li>
            <a href="../tours/${tour.id}">
                    ${tour.tourName}
            </a>
        </li>
    </c:forEach>
</ul>
<a href="../bands">
    <spring:message code="text.back.bandList"/>
</a>
</body>
</html>