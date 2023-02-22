package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = new BeanPropertyRowMapper<>(Meal.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("meals");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(jdbcInsert.executeAndReturnKey(getSqlParameters(meal, userId)).intValue());
        } else if (namedParameterJdbcTemplate.update("UPDATE meals SET description=:description," +
                " calories=:calories, date_time=:dateTime where meals.id=:id and user_id=:user_id", getSqlParameters(meal, userId)) == 0) {
            return null;
        }
        return meal;
    }

    private SqlParameterSource getSqlParameters(final Meal meal, final int userId) {
        final MapSqlParameterSource result = new MapSqlParameterSource();
        result.addValue("id", meal.getId());
        result.addValue("user_id", userId);
        result.addValue("description", meal.getDescription());
        result.addValue("dateTime", meal.getDateTime());
        result.addValue("calories", meal.getCalories());
        return result;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        final List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE id=? " +
                "AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time desc", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("from", startDateTime);
        params.addValue("to", endDateTime);
        params.addValue("userId", userId);
        return namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE user_id=:userId AND date_time >= :from AND date_time < :to" +
                        " ORDER BY date_time desc",
                params, ROW_MAPPER);
    }
}
