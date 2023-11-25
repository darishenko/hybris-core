<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<title>Band Details</title>
<body>
<h1>Band Details</h1>
Band Details for ${band.name}
<p>${band.description}</p>
<p>Music type:</p>
<ul>
    <c:forEach var="genre" items="${band.genres}">
        <li>${genre}</li>
    </c:forEach>
</ul>
<c:if test="${not empty band.albums}">
    <p>Albums:</p>
    <table border="1">
        <thead>
        <tr>
            <td>Number</td>
            <td>Name</td>
            <td>Sales</td>
            <td>Songs</td>
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
<p>Tour History:</p>
<ul>
    <c:forEach var="tour" items="${band.tours}">
        <li><a href="../tours/${tour.id}">${tour.tourName}</a>(number of concerts: ${tour.numberOfConcerts})</li>
    </c:forEach>
</ul>
<a href="../bands">Back to Band List</a>
</body>
</html>