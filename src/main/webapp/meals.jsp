<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=insert">Add Meal</a></p>
<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
        padding: 4px;
    }
</style>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>DescriptionCalories</th>
        <th></th>
        <th></th>
    </tr>

    <c:set var="meals" value="${requestScope.meals}"/>
    <c:forEach var="meal" items="${meals}">
        <c:if test="${meal.isExcess()}">
            <tr style="color:red;">
        </c:if>
        <c:if test="${!meal.isExcess()}">
            <tr style="color:green;">
        </c:if>

        <td>
            <fmt:parseDate pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"
                           value="${meal.getDateTime()}"/>
            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/>
        </td>
        <td><c:out value="${meal.getDescription()}"/></td>
        <td><c:out value="${meal.getCalories()}"/></td>
        <td><a href="meals?action=edit&mealId=<c:out value="${meal.getId()}"/>">Update</a></td>
        <td><a href="meals?action=delete&mealId=<c:out value="${meal.getId()}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>