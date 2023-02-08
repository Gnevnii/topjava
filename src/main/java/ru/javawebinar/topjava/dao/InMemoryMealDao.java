package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements Dao<Meal> {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealDao.class);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> meals;

    public InMemoryMealDao() {
        final List<Meal> meals = Arrays.asList(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        this.meals = new ConcurrentHashMap<>(meals.size());
        meals.forEach(this::create);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal update(final Meal object) {
        meals.computeIfPresent(object.getId(), (integer, meal) -> meal);
        log.debug("Method update is finished:{}", object);
        return object;
    }

    @Override
    public Meal create(final Meal object) {
        object.setId(counter.incrementAndGet());
        meals.put(object.getId(), object);
        log.debug("Method creat is finished:{}", object);
        return object;
    }

    @Override
    public void delete(int objectId) {
        meals.remove(objectId);
        log.debug("Method delete is finished, id:{}", objectId);
    }

    @Override
    public Meal getById(int objectId) {
        final Meal meal = meals.get(objectId);
        log.debug("Method getById is finished, id:{}", meal);
        return meal;
    }
}
