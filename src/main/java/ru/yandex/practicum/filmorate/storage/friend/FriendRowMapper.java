package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendLink;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendRowMapper implements RowMapper<FriendLink> {
    @Override
    public FriendLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendLink(rs.getInt("friend_id"), rs.getBoolean("is_confirmed"));
    }
}
