package ru.yandex.practicum.filmorate.storage.like;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Integer> getFilmLikes(Integer filmId);

    Map<Integer, HashSet<Integer>> getAllLikes();
}
