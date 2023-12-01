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
<spring:message code="text.band.details.for"/> ${band.name}
<p>
    <img src="${band.imageURL}"/>
</p>
<p>${band.description}</p>
<p>
    <spring:message code="text.music.type.list"/>
</p>
<ul>
    <c:forEach var="genre" items="${band.genres}">
        <li>${genre}</li>
    </c:forEach>
</ul>
<c:if test="${not empty band.albums}">
    <p>
        <spring:message code="text.albums.list"/>
    </p>
    <table border="1">
        <thead>
        <tr>
            <td>
                <spring:message code="text.number"/>
            </td>
            <td>
                <spring:message code="text.name"/>
            </td>
            <td>
                <spring:message code="text.sales"/>
            </td>
            <td>
                <spring:message code="text.songs"/>
            </td>
        </tr>
        </thead>
        <c:forEach varStatus="ind" var="album" items="${band.albums}">
            <tr>
                <td>${ind.index + 1}</td>
                <td>${album.name}</td>
                <td>${album.albumSales}</td>
                <td>
                    <c:forEach var="song" items="${album.songs}">
                        <p>${song}</p>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<p>
    <spring:message code="text.tour.history"/>
</p>
<ul>
    <c:forEach var="tour" items="${band.tours}">
        <li>
            <a href="../tours/${tour.id}">${tour.tourName}</a>
            (<spring:message code="text.concerts.number"/> ${tour.numberOfConcerts})
        </li>
    </c:forEach>
</ul>
<a href="../bands">
    <spring:message code="text.back.bandList"/>
</a>
</body>
</html>