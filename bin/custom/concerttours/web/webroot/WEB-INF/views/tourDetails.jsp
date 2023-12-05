<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html>
<title>
    <spring:message code="text.tour.details"/>
</title>
<body>
<h1>
    <spring:message code="text.tour.details"/>
</h1>
<spring:message code="text.tour.details.for"/> ${tour.tourName}
<p>${tour.description}</p>
<c:if test="${not empty tour.producer.id}">
    <p>
        <spring:message code="text.producer"/>
        <a href="../producers/${tour.producer.id}">
                ${tour.producer.firstName} ${tour.producer.lastName}
        </a>
    </p>
</c:if>
<p>
    <spring:message code="text.schedule.list"/>
</p>
<c:if test="${not empty tour.concerts}">
    <table border="1">
        <tr>
            <th>
                <spring:message code="text.venue"/>
            </th>
            <th>
                <spring:message code="text.type"/>
            </th>
            <th>
                <spring:message code="text.date"/>
            </th>
            <th>
                <spring:message code="text.days.until.concert"/>
            </th>
        </tr>
        <c:forEach var="concert" items="${tour.concerts}">
            <tr>
                <td>${concert.venue}</td>
                <td>${concert.type}</td>
                <td>
                    <fmt:formatDate pattern="dd MMM yyyy" value="${concert.date}"/>
                </td>
                <td>${concert.countDown}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<a href="../bands">
    <spring:message code="text.back.bandList"/>
</a>
</body>
</html>