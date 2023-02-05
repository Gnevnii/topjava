<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
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
<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
        padding: 4px;
    }
</style>

<c:set var="meals" value="${requestScope.meals}"/>

<table>
    <tr bordercolor="red">
        <th>Date</th>
        <th>Description</th>
        <th>DescriptionCalories</th>
        <th></th>
        <th></th>
    </tr>

    <c:forEach var="meal" items="${meals}">
        <c:set var="isExcess" value="${meal.isExcess()}"/>

        <c:if test="${isExcess}">
            <tr style="color:red;">
        </c:if>
        <c:if test="${!isExcess}">
            <tr style="color:green;">
        </c:if>

        <td>
            <fmt:parseDate pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"
                           value="${meal.getDateTime()}"/>
            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/>
        </td>
        <td>${meal.getDescription()}</td>
        <td>${meal.getCalories()}</td>
        <td></td>
        <td></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>