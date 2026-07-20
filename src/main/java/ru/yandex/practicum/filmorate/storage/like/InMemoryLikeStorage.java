package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryLikeStorage implements LikeStorage {
    private final Map<Integer, HashSet<Integer>> filmLikes = new HashMap<>();

    @Override
    public void addLike(Integer filmId, Integer userId) {
        log.debug("add like to film {} for user {}", filmId, userId);

        if (filmLikes.containsKey(filmId)) {
            HashSet<Integer> likes = filmLikes.get(filmId);
            likes.add(userId);
            log.debug("Likes added to film {}. New likes: {}", filmId, likes);
        } else {
            filmLikes.put(filmId, new HashSet<>(Set.of(userId)));
            log.debug("First like of user {} was added to film {}", userId, filmId);
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        log.debug("removeLike for film {}", filmId);

        if (filmLikes.containsKey(filmId)) {
            Set<Integer> likes = filmLikes.get(filmId);
            if (likes.contains(userId)) {
                likes.remove(userId);
                log.debug("like for user {} removed for film {}", userId, filmId);
            } else {
                log.warn("No like of user {} for film {} film in filmLikes", userId, filmId);
                throw new NoSuchElementException("No like of user %d for film %d film in filmLikes"
                        .formatted(userId, filmId));
            }
        } else {
            log.warn("No such film in filmLikes {}", filmId);
            throw new NoSuchElementException("No such film in filmLikes %d".formatted(filmId));
        }
    }

    @Override
    public List<Integer> getFilmLikes(Integer filmId) {
        log.debug("getFilmLikes for film {}", filmId);
        if (filmLikes.containsKey(filmId)) {
            return filmLikes.get(filmId).stream().toList();
        } else {
            String msg = "No such film id=%d in filmLikes".formatted(filmId);
            log.info(msg);
            return List.of();
        }
    }

    @Override
    public Map<Integer, HashSet<Integer>> getAllLikes() {
        return Map.copyOf(filmLikes);
    }


}
