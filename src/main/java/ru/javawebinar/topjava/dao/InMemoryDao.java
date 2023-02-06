package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public class InMemoryDao implements IDao<Meal> {
    private static final Logger log = LoggerFactory.getLogger(InMemoryDao.class);
    private final MealsStorage storage;

    public InMemoryDao() {
        this.storage = MealsStorage.getMealStorage();
    }

    @Override
    public List<Meal> getAll() {
        return storage.getMeals();
    }

    @Override
    public void update(final Meal meal) {
        storage.replaceById(meal);
        log.debug("Meal updated:{}", meal);
    }

    @Override
    public void create(final Meal meal) {
        storage.addMeal(meal);
        log.debug("Meal created:{}", meal);
    }

    @Override
    public void delete(final Integer mealId) {
        storage.remove(mealId);
        log.debug("Meal deleted, id:{}", mealId);
    }

    @Override
    public Meal getById(final Integer mealId) {
        return storage.findById(mealId);
    }
}
