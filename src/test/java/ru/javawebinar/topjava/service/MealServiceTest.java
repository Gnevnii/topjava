package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
        final Meal actual = mealService.get(MealTestData.userBreakfast.getId(), UserTestData.USER_ID);
        MealTestData.assertMatch(actual, MealTestData.userBreakfast);
    }

    @Test
    public void getNotMy() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.userBreakfast.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.NOT_EXIST_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void delete() {
        mealService.delete(MealTestData.userBreakfast.getId(), UserTestData.USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.userBreakfast.getId(), UserTestData.USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(MealTestData.NOT_EXIST_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void deleteNotMy() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.delete(MealTestData.userBreakfast.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        final List<Meal> meals = new ArrayList<>(MealTestData.userMeals.get(UserTestData.USER_ID));
        final LocalDate latestMealDT = MealTestData.LATEST_MEAL_DATE_TIME.toLocalDate();
        final LocalDate beforeFirstMealDT = MealTestData.FIRST_MEAL_DATE_TIME.minusDays(1).toLocalDate();

        List<Meal> actual = mealService.getBetweenInclusive(beforeFirstMealDT, latestMealDT.plusDays(1), UserTestData.USER_ID);
        MealTestData.assertMatch(actual, meals);

        actual = mealService.getBetweenInclusive(beforeFirstMealDT, beforeFirstMealDT, UserTestData.USER_ID);
        meals.removeIf(meal -> !meal.getDateTime().toLocalDate().equals(beforeFirstMealDT));
        MealTestData.assertMatch(actual, meals);
    }

    @Test
    public void getAll() {
        final List<Meal> actual = mealService.getAll(UserTestData.USER_ID);
        final List<Meal> expected = MealTestData.userMeals.get(UserTestData.USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void update() {
        final Meal updated = MealTestData.getUpdated(MealTestData.userBreakfast);
        mealService.update(updated, UserTestData.USER_ID);
        MealTestData.assertMatch(mealService.get(updated.getId(), UserTestData.USER_ID), MealTestData.getUpdated(MealTestData.userBreakfast));
    }

    @Test
    public void updateNotMy() {
        final Meal updated = MealTestData.getUpdated(MealTestData.userBreakfast);
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(updated, UserTestData.GUEST_ID));
    }

    @Test
    public void create() {
        final Meal meal = mealService.create(MealTestData.getNew(), UserTestData.USER_ID);
        final Meal expected = MealTestData.getNew();
        expected.setId(meal.getId());
        MealTestData.assertMatch(meal, expected);
        MealTestData.assertMatch(mealService.get(meal.getId(), UserTestData.USER_ID), expected);
    }

    @Test
    public void createDuplicateDateMeal() {
        final Meal duplicate = MealTestData.getNew();
        duplicate.setDateTime(MealTestData.userBreakfast.getDateTime());
        Assert.assertThrows(DuplicateKeyException.class, () -> mealService.create(duplicate, UserTestData.USER_ID));
    }
}