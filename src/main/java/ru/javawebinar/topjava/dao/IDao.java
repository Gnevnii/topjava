package ru.javawebinar.topjava.dao;

import java.util.List;

public interface IDao<T> {

    List<T> getAll();

    void update(T meal);

    void create(T meal);

    void delete(Integer mealId);

    T getById(Integer mealId);
}
