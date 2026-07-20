package ru.yandex.practicum.filmorate.storage.like;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryLikeStorageTest {
    private InMemoryLikeStorage inMemoryLikeStorage;

    @BeforeEach
    void setUp() {
        inMemoryLikeStorage = new InMemoryLikeStorage();
    }

    @Test
    void addLike() {
        inMemoryLikeStorage.addLike(1, 1);
        assertEquals(1, inMemoryLikeStorage.getFilmLikes(1).size());
        assertEquals(List.of(1), inMemoryLikeStorage.getFilmLikes(1));
    }

    @Test
    void getLikes_whenHasTwoFilmLikes_removeOne_getOk() {
        inMemoryLikeStorage.addLike(1, 1);
        inMemoryLikeStorage.addLike(2, 2);

        inMemoryLikeStorage.removeLike(1, 1);
        assertEquals(0, inMemoryLikeStorage.getFilmLikes(1).size());
        assertEquals(1, inMemoryLikeStorage.getFilmLikes(2).size());
    }

    @Test
    void removeOneLike_whenHasOneFilmTwoLikes_getOk() {
        inMemoryLikeStorage.addLike(1, 1);
        inMemoryLikeStorage.addLike(1, 2);

        inMemoryLikeStorage.removeLike(1, 1);
        assertEquals(1, inMemoryLikeStorage.getFilmLikes(1).size());
        assertEquals(List.of(2), inMemoryLikeStorage.getFilmLikes(1));
    }

    @Test
    void removeLike_whenWrongFilmId_getThrowNoSuchElement() {
        inMemoryLikeStorage.addLike(1, 1);
        assertThrows(NoSuchElementException.class, () -> inMemoryLikeStorage.removeLike(1, 2));
    }

    @Test
    void getLike_whenWrongFilmId_getThrowNoSuchElement() {
        inMemoryLikeStorage.addLike(1, 1);
        assertThrows(NoSuchElementException.class, () -> inMemoryLikeStorage.getFilmLikes(2));
    }
}