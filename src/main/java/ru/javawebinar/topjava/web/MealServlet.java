package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsStorage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        final List<Meal> meals = MealsStorage.getMealStorage().getMeals();
        final List<MealTo> mealTos = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsStorage.CALORIES_PER_DAY);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
        log.debug("doGet finished successfully");
    }
}
