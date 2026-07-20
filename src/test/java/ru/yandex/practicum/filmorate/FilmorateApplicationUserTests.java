package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationUserTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createThreeUser_GetAllWithSequentialIds() throws Exception {

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                         "  \"login\": \"dolore1\"," +
                         "  \"name\": \"Nick Name\"," +
                         "  \"email\": \"mail@mail.ru\"," +
                         "  \"birthday\": \"1946-08-20\"" +
                         "}")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                         "  \"login\": \"dolore2\"," +
                         "  \"name\": \"Nick Name\"," +
                         "  \"email\": \"mail@mail.ru\"," +
                         "  \"birthday\": \"1946-08-20\"" +
                         "}")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                         "  \"login\": \"dolore3\"," +
                         "  \"name\": \"Nick Name\"," +
                         "  \"email\": \"mail@mail.ru\"," +
                         "  \"birthday\": \"1946-08-20\"" +
                         "}")
        ).andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].id").value(3));

    }

    @Test
    void createUser_whenLoginIsDuplicated_get409() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"dolores\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"dolores\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isConflict());
    }


    @Test
    void updateUser_whenLoginIsDuplicated_get409() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateDuplicated1\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateDuplicated2\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"id\": 1," +
                        "  \"login\": \"updateDuplicated2\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isConflict());
    }

    @Test
    void updateUser_whenLoginIsItsOwn_getOk() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateDuplicated1\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateDuplicated2\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"id\": 1," +
                        "  \"login\": \"updateDuplicated1\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());
    }
}
