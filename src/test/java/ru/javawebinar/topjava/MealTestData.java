package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int GET_MEAL_ID = START_SEQ + 3;
    public static final int DELETE_MEAL_ID = START_SEQ + 7;

    public static final Meal GET_MEAL = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal DELETE_MEAL = new Meal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
    public static final LocalDateTime UPDATED_DATE = LocalDateTime.now();

    public static List<Meal> meals;

    static {
        reinit();
    }

    public static void reinit() {
        meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                DELETE_MEAL,
                GET_MEAL
        );
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.now(), "newDescription", 1111);
    }

    public static Meal getUpdated(Meal meal) {
        final Meal updated = new Meal(meal);
        updated.setId(meal.getId());
        updated.setDescription("updatedDescription");
        updated.setCalories(meal.getCalories() + 7);
        updated.setDateTime(UPDATED_DATE);
        return updated;
    }


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").isEqualTo(expected);
    }
}
