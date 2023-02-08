<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>${meal.id == null ? 'Add meal' : 'Edit meal'}</h2>

<form method="POST" action='meals' name="frmAddMeal">
    <fmt:parseDate pattern="yyyy-MM-dd'T'HH:mm" var="date" type="both"
                   value="${meal.dateTime}"/>
    <input type="hidden" name="mealId" value="${meal.id}" />
    DateTime: <input type="datetime-local" id="datetime" name="datetime" value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${date}"/>" /> <br />
    Description: <input type="text" name="description" value="${meal.description}" /> <br />
    Calories: <input type="number" name="calories" value="${meal.calories}" /> <br />

    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>