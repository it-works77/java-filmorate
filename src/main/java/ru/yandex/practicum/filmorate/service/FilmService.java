package ru.yandex.practicum.filmorate.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getAll() {
        return films.values();
    }
}
