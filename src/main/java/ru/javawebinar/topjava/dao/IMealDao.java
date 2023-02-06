package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public interface IMealDao {

    List<Meal> getAllMeals();

    void updateMeal(Meal meal);

    void createMeal(Meal meal);

    void deleteMeal(Integer mealId);

    Meal getById(Integer mealId);
}
