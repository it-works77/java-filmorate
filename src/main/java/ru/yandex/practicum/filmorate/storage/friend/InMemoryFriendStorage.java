package ru.yandex.practicum.filmorate.storage.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final Map<Integer, HashSet<Integer>> friends = new HashMap<>();

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.debug("add friend id={} to user id={}", friendId, userId);

        addFriendToUser(userId, friendId);
        addFriendToUser(friendId, userId);
    }

    private void addFriendToUser(Integer userId, Integer friendId) {
        if (friends.containsKey(userId)) {
            HashSet<Integer> userFriends = friends.get(userId);
            userFriends.add(friendId);
            log.debug("Friend id={} added to user id={}. New friends: {}", friendId, userId, userFriends);
        } else {
            friends.put(userId, new HashSet<>(Set.of(friendId)));
            log.debug("First friend id={} was added to user id={}", friendId, userId);
        }
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        log.debug("removeFriend id={} from user id={}", friendId, userId);

        removeFriendFromUser(userId, friendId);
        removeFriendFromUser(friendId, userId);

    }

    private void removeFriendFromUser(Integer userId, Integer friendId) {
        if (friends.containsKey(userId)) {
            Set<Integer> userFriends = friends.get(userId);
            if (userFriends.contains(friendId)) {
                userFriends.remove(friendId);
                log.debug("Friend id={} removed from user id={}. New friends: {}", friendId, userId, userFriends);
            } else {
                String msg = "No friend id=%d of user id=%d in friends: %s".formatted(friendId, userId, friends);
                log.warn(msg);
                throw new NoSuchElementException(msg);
            }
        } else {
            String msg = "No such user id=%d in friends storage".formatted(userId);
            log.warn(msg);
            throw new NoSuchElementException(msg);
        }
    }

    @Override
    public List<Integer> getFriends(Integer userId) {
        log.debug("Get friends for user id={}", userId);
        if (friends.containsKey(userId)) {
            return friends.get(userId).stream().toList();
        } else {
            String msg = "No such user id=%d in friends storage".formatted(userId);
            log.info(msg);
            return List.of();
        }
    }
}
