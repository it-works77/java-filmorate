package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.user.DbUserStorageQueries.*;

@Slf4j
@Repository
@Qualifier("dbUserStorage")
public class DbUserStorage extends BaseDbStorage<User> implements UserStorage {

    public DbUserStorage(JdbcTemplate jdbc, RowMapper<User> userRowMapper) {
        super(jdbc, userRowMapper);
    }

    @Override
    public User add(User user) {
        int id = insert(INSERT_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        Optional<User> user = get(newUser.getId());
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Не найден пользователь с id=" + newUser.getId());
        }

        int rowsUpdated = update(UPDATE_BY_ID_QUERY,
                newUser.getLogin(),
                newUser.getEmail(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );

        if (rowsUpdated != 1) {
            String msg = "Количество обновленных пользователей по id=%d не равно 1!".formatted(newUser.getId());
            log.debug(msg);
            throw new InternalServerException(msg);
        }
        return newUser;
    }

    @Override
    public Optional<User> get(Integer id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public boolean remove(Integer id) {
        return delete(DELETE_BY_ID_QUERY, id);
    }
}
