package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendLink;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.friend.DbFriendStorageQueries.*;

@Repository
@Qualifier("dbFriendStorage")
public class DbFriendStorage extends BaseDbStorage<FriendLink> implements FriendStorage {
    public DbFriendStorage(JdbcTemplate jdbc, RowMapper<FriendLink> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        insert(INSERT_QUERY, userId, friendId, false);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        delete(DELETE_QUERY, userId, friendId);
    }

    @Override
    public List<Integer> getFriends(Integer userId) {
        return findMany(FIND_BY_ID_QUERY, userId).stream()
                .map(FriendLink::getFriendId)
                .sorted()
                .toList();
    }
}
