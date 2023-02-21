package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Before
    public void beforeEach() {
        MealTestData.reinit();
    }

    @Test
    public void get() {
        final Meal actual = mealService.get(MealTestData.GET_MEAL_ID, UserTestData.USER_ID);
        MealTestData.assertMatch(actual, MealTestData.GET_MEAL);
    }

    @Test
    public void delete() {
        mealService.delete(MealTestData.DELETE_MEAL_ID, UserTestData.USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.DELETE_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        final List<Meal> meals = new ArrayList<>(MealTestData.meals);
        final LocalDate maxDate = meals.get(0).getDateTime().toLocalDate();
        final LocalDate minDate = meals.get(meals.size() - 1).getDateTime().toLocalDate();

        List<Meal> actual = mealService.getBetweenInclusive(minDate, maxDate.plusDays(1), UserTestData.USER_ID);
        MealTestData.assertMatch(actual, meals);

        actual = mealService.getBetweenInclusive(minDate, minDate, UserTestData.USER_ID);
        meals.removeIf(meal -> !meal.getDateTime().toLocalDate().equals(minDate));
        MealTestData.assertMatch(actual, meals);
    }

    @Test
    public void getAll() {
        final List<Meal> actual = mealService.getAll(UserTestData.USER_ID);
        final List<Meal> expected = MealTestData.meals;
        expected.sort(Comparator.comparing(Meal::getDateTime).reversed());
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void update() {
        final Meal meal = mealService.get(MealTestData.GET_MEAL_ID, UserTestData.USER_ID);
        final Meal updated = MealTestData.getUpdated(meal);
        mealService.update(updated, UserTestData.USER_ID);
        final Meal actual = mealService.get(meal.getId(), UserTestData.USER_ID);
        MealTestData.assertMatch(actual, updated);
    }

    @Test
    public void create() {
        final Meal newMeal = MealTestData.getNew();
        final Meal meal = mealService.create(newMeal, UserTestData.USER_ID);
        newMeal.setId(meal.getId());
        MealTestData.assertMatch(meal, newMeal);
    }
}