package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public class MealDao implements IMealDao {
    private final MealsStorage storage;

    public MealDao() {
        this.storage = MealsStorage.getMealStorage();
    }

    @Override
    public List<Meal> getAllMeals() {
        return storage.getMeals();
    }

    @Override
    public void updateMeal(final Meal meal) {
        storage.replaceById(meal);
    }

    @Override
    public void createMeal(final Meal meal) {
        storage.addMeal(meal);
    }

    @Override
    public void deleteMeal(final Integer mealId) {
        storage.remove(mealId);
    }

    @Override
    public Meal getById(final Integer mealId) {
        return storage.findById(mealId);
    }
}
