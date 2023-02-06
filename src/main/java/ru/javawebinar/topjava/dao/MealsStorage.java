package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MealsStorage {
    public static final int CALORIES_PER_DAY = 2000;
    private static MealsStorage instance = null;
    private final List<Meal> meals;

    private MealsStorage() {
        List<Meal> mls = new ArrayList<>();
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mls.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals = Collections.synchronizedList(mls);
    }

    public static synchronized MealsStorage getMealStorage() {
        if (instance == null) {
            instance = new MealsStorage();
        }
        return instance;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public void replaceById(Meal meal) {
        remove(meal.getId());
        addMeal(meal);
    }

    public void addMeal(final Meal meal) {
        meals.add(meal);
    }

    public void remove(final Integer mealId) {
        meals.removeIf(ml -> Objects.equals(ml.getId(), mealId));
    }

    public Meal findById(final Integer mealId) {
        return meals.stream().filter(meal -> meal.getId().equals(mealId)).findFirst().orElse(null);
    }
}