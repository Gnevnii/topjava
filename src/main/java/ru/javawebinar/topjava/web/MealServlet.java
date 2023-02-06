package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.InMemoryDao;
import ru.javawebinar.topjava.dao.MealsStorage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final static String INSERT_OR_EDIT = "/mealform.jsp";
    private final static String LIST_MEAL = "/meals.jsp";

    private InMemoryDao mealDao;

    @Override
    public void init() throws ServletException {
        super.init();
        mealDao = new InMemoryDao();
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        String forward;
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealDao.delete(mealId);
            forward = LIST_MEAL;
            request.setAttribute("meals", getAllMeal());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealDao.getById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listmeal")) {
            forward = LIST_MEAL;
            request.setAttribute("meals", getAllMeal());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        log.debug("doGet finished successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        final String description = request.getParameter("description");
        final String datetime = request.getParameter("datetime").trim();
        final int calories = Integer.parseInt(request.getParameter("calories"));
        final LocalDateTime dateTime = getDateParameter(datetime);

        String param = request.getParameter("mealId");
        if (param == null || param.isEmpty()) {
            mealDao.create(new Meal(dateTime, description, calories));
        } else {
            final Meal dbMeal = mealDao.getById(Integer.parseInt(param));
            dbMeal.setCalories(calories);
            dbMeal.setDescription(description);
            dbMeal.setDateTime(dateTime);
            mealDao.update(dbMeal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("meals", getAllMeal());
        view.forward(request, response);
    }

    private LocalDateTime getDateParameter(final String datetime) {
        final LocalDateTime dateTime;
        try {
            final Date dateParsed = new SimpleDateFormat("MM/dd/yyyy").parse(datetime);
            dateTime = dateParsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (ParseException e) {
            log.error("Error parsing date string:{}", datetime);
            throw new RuntimeException(e);
        }
        return dateTime;
    }

    private List<MealTo> getAllMeal() {
        final List<Meal> meals = mealDao.getAll();
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsStorage.CALORIES_PER_DAY)
                .stream()
                .sorted(Comparator.comparing(MealTo::getDateTime))
                .collect(Collectors.toList());
    }
}
