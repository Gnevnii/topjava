package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.regex.Pattern;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final static String INSERT_OR_EDIT = "/mealForm.jsp";
    private final static String LIST_MEAL = "/meals.jsp";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("uuuu-MM-dd HH:mm")
            .toFormatter();
    private final static Pattern NUMBER_ONLY_PATTER = Pattern.compile("^[0-9]+$");

    private Dao<Meal> mealDao;

    @Override
    public void init() {
        mealDao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start method doGet");

        String forward = "";
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        switch (action) {
            case "delete": {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                mealDao.delete(mealId);
                log.debug("Action delete is finished, meal id:{}", mealId);
                break;
            }
            case "edit": {
                forward = INSERT_OR_EDIT;
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = mealDao.getById(mealId);
                request.setAttribute("meal", meal);
                log.debug("Start action edit, meal id:{}", mealId);
                break;
            }
            case "insert": {
                forward = INSERT_OR_EDIT;
                log.debug("Start action insert");
                break;
            }
            default: {
                forward = LIST_MEAL;
                request.setAttribute("meals", getAllMeal());
                log.debug("Start action show all meals");
                break;
            }
        }

        if (action.equals("delete")) {
            request.setAttribute("meals", getAllMeal());
            response.sendRedirect("meals");
            log.debug("Finish method doGet, redirect to: {}", forward);
            return;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        log.debug("Finish method doGet, forwarded to: {}", forward);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Start method doPost");
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("cancel") != null) {
            redirectToMealList(request, response);
            return;
        }

        final String caloriesValue = request.getParameter("calories");
        final boolean isNumber = NUMBER_ONLY_PATTER.matcher(caloriesValue).matches();
        if (!isNumber) {
            log.error("Error parsing input value for calories: {}", caloriesValue);
            redirectToMealList(request, response);
            return;
        }

        final Meal newMeal = new Meal(parseDateTimeParameter(request.getParameter("datetime")),
                request.getParameter("description"),
                Integer.parseInt(caloriesValue));
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            mealDao.create(newMeal);
        } else {
            newMeal.setId(Integer.parseInt(mealId));
            mealDao.update(newMeal);
        }
        redirectToMealList(request, response);
    }

    private void redirectToMealList(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", getAllMeal());
        response.sendRedirect("meals");
        log.debug("doPost finished successfully");
    }

    private LocalDateTime parseDateTimeParameter(final String datetime) {
        return LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
    }

    private List<MealTo> getAllMeal() {
        return MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
    }
}
