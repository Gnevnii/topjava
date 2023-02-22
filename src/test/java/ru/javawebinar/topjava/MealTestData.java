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
    public static final int USER_BREAKFAST_ID = START_SEQ + 3;
    public static final int NOT_EXIST_ID = START_SEQ - 16;

    public static final Meal USER_BREAKFAST = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal USER_DINNER = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal ADMIN_LUNCH = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    private static final LocalDateTime someDateTime = LocalDateTime.of(1985, Month.NOVEMBER, 18, 19, 0);
    public static Map<Integer, List<Meal>> userMeals = new HashMap<>();

    static {
        reinit();
    }

    public static void reinit() {
        userMeals.clear();
        userMeals.put(UserTestData.USER_ID, Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                USER_DINNER,
                USER_BREAKFAST));

        userMeals.put(UserTestData.ADMIN_ID, Arrays.asList(
                ADMIN_LUNCH,
                new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500)));
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.now(), "newDescription", 1111);
    }

    public static Meal getUpdated(Meal meal) {
        final Meal updated = new Meal(meal);
        updated.setId(meal.getId());
        updated.setDescription("updatedDescription");
        updated.setCalories(meal.getCalories() + 7);
        updated.setDateTime(someDateTime);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").isEqualTo(expected);
    }
}
