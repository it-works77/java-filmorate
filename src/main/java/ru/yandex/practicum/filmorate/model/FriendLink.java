package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendLink {
    private Integer friendId;
    private boolean isConfirmed;

    public FriendLink(Integer friendId) {
        this.friendId = friendId;
        this.isConfirmed = false;
    }
}
