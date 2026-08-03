package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:filmrate",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
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

    @Disabled
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

    @Disabled
    @Test
    void updateUser_whenLoginIsDuplicated_get409() throws Exception {
        String response =  mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateDuplicated1\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        int userId = jsonNode.get("id").asInt();

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
                        "  \"id\": " + userId + "," +
                        "  \"login\": \"updateDuplicated2\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isConflict());
    }

    @Test
    void updateUser_whenLoginIsItsOwn_getOk() throws Exception {
        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"login\": \"updateOwn1\"," +
                                "  \"name\": \"Nick Name\"," +
                                "  \"email\": \"mail@mail.ru\"," +
                                "  \"birthday\": \"1946-08-20\"" +
                                "}")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        int userId = jsonNode.get("id").asInt();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"login\": \"updateOwn2\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"id\": " + userId + "," +
                        "  \"login\": \"updateOwn1\"," +
                        "  \"name\": \"Nick Name\"," +
                        "  \"email\": \"mail@mail.ru\"," +
                        "  \"birthday\": \"1946-08-20\"" +
                        "}")
        ).andExpect(status().isOk());
    }
}
