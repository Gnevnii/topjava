package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_EXIST_ID = START_SEQ - 16;

    public static final LocalDateTime LATEST_MEAL_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0);
    public static final LocalDateTime FIRST_MEAL_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);

    public static final Meal userBreakfast = new Meal(START_SEQ + 3, FIRST_MEAL_DATE_TIME, "Завтрак", 500);
    public static final Meal adminLunch = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    private static final LocalDateTime CONSTANT_CASE = LocalDateTime.of(1985, Month.NOVEMBER, 18, 19, 0);
    public static Map<Integer, List<Meal>> userMeals = new HashMap<>();

    static {
        reinit();
    }

    public static void reinit() {
        userMeals.clear();
        userMeals.put(UserTestData.USER_ID, Arrays.asList(
                new Meal(START_SEQ + 9, LATEST_MEAL_DATE_TIME, "Ужин", 410),
                new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                userBreakfast
        ));

        userMeals.put(UserTestData.ADMIN_ID, Arrays.asList(
                adminLunch,
                new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500)));
    }

    public static Meal getNew() {
        return new Meal(CONSTANT_CASE, "newDescription", 1111);
    }

    public static Meal getUpdated(Meal meal) {
        final Meal updated = new Meal(meal);
        updated.setId(meal.getId());
        updated.setDescription("updatedDescription");
        updated.setCalories(meal.getCalories() + 7);
        updated.setDateTime(CONSTANT_CASE);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
