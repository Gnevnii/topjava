<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Edit Meal</h2>
<script>
    $(function() {
        $('#datetime').datepicker();
    });
</script>

<form method="POST" action='meals' name="frmAddMeal">
    <fmt:parseDate pattern="yyyy-MM-dd'T'HH:mm" var="date" type="both"
                   value="${meal.getDateTime()}"/>
    <input type="hidden" name="mealId" value="${meal.getId()}" />
    DateTime: <input type="text" id="datetime" name="datetime" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${date}"/>" /> <br />
    Description: <input type="text" name="description" value="<c:out value="${meal.getDescription()}" />" /> <br />
    Calories: <input type="text" name="calories" value="<c:out value="${meal.getCalories()}" />" /> <br />

    <input type="submit" value="Save"/>
    <input type="submit" value="Cancel"/>
</form>
</body>
</html>