package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {

    List<T> getAll();

    T update(T object);

    T create(T object);

    void delete(int id);

    T getById(int id);
}
