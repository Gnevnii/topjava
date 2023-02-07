package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements Dao<Meal> {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealDao.class);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final List<Meal> meals = new CopyOnWriteArrayList<>();

    public InMemoryMealDao() {
        final List<Meal> allMeal = Arrays.asList(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        for (Meal meal : allMeal) {
            addMeal(meal);
        }
    }

    private synchronized void addMeal(Meal meal) {
        meal.setId(counter.incrementAndGet());
        create(meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals);
    }

    @Override
    public Meal update(final Meal object) {
        meals.stream()
                .filter(meal -> Objects.equals(meal.getId(), object.getId()))
                .findFirst()
                .ifPresent(meals::remove);
        meals.add(object);
        log.debug("Method update is finished:{}", object);
        return getById(object.getId());
    }

    @Override
    public Meal create(final Meal object) {
        object.setId(counter.incrementAndGet());
        meals.add(object);
        log.debug("Method creat is finished:{}", object);
        return object;
    }

    @Override
    public void delete(int objectId) {
        meals.stream()
                .filter(meal -> Objects.equals(objectId, meal.getId()))
                .findFirst()
                .ifPresent(meals::remove);
        log.debug("Method delete is finished, id:{}", objectId);
    }

    @Override
    public Meal getById(int objectId) {
        final Meal meal = meals.stream()
                .filter(m -> Objects.equals(objectId, m.getId()))
                .findFirst()
                .orElse(null);
        log.debug("Method getById is finished, id:{}", meal);
        return meal;
    }
}
