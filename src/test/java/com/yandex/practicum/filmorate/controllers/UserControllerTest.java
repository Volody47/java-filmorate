package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import com.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import com.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserControllerTest {
    private MockMvc restMvc;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        this.restMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void validateEmail() {
        User user = new User();
        user.setEmail("");
        assertThrows(InvalidEmailException.class, () -> validateUser(user) );
    }

    @Test
    void validateLogin() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore ullamco");
        assertThrows(InvalidLoginException.class, () -> validateUser(user) );
    }

    @Test
    void validateBirthday() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setBirthday(LocalDate.MAX);
        assertThrows(InvalidBirthdayException.class, () -> validateUser(user) );
    }

    @Test
    public void shouldFindAllTest() throws Exception {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick");
        user.setBirthday(LocalDate.now().minusYears(1));

        when(userService.findAll()).thenReturn(List.of(user));

        restMvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("mail@mail.ru"));
    }

    @Test
    public void shouldCreateUserTest() throws Exception {
        restMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nick Name\", \"email\": \"mail@mail.ru\", \"login\": \"dolore\", \"birthday\": \"1946-08-20\"}")
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).createUser(any(User.class));
    }


    @Test
    void shouldUpdateUserTest() throws Exception {
        restMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nick Update\", \"email\": \"mail@mail.ru\", \"login\": \"dolore\", \"birthday\": \"1946-08-20\"}")
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).updateUser(any(User.class));
    }

    @Test
    void shouldGetUserTest() throws Exception {
        int id = 35;
        User user = new User();
        user.setId(35);
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick");
        user.setBirthday(LocalDate.now().minusYears(1));

        when(userService.getUser(id)).thenReturn(user);

        restMvc.perform(
                get("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(35));
    }

    @Test
    void shouldAddFriendTest() throws Exception {
        int userId = 1;
        int friendId = 2;
        restMvc.perform(
                put("/users/{userId}/friends/{friendId}", userId, friendId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldRemoveFriendTest() throws Exception {
        int userId = 1;
        int friendId = 2;
        restMvc.perform(
                delete("/users/{userId}/friends/{friendId}", userId, friendId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserFriendsTest() throws Exception {
        int userId = 1;
        int friendId = 2;
        User user1 = new User();
        user1.setId(userId);
        user1.setEmail("mail@mail.ru");
        user1.setLogin("dolore");
        user1.setName("Nick");
        user1.setBirthday(LocalDate.now().minusYears(1));
        User user2 = new User();
        user2.setId(friendId);
        user2.setEmail("mail@mail.ru");
        user2.setLogin("dolore");
        user2.setName("Friend");
        user2.setBirthday(LocalDate.now().minusYears(1));

        when(userService.getUserFriends(userId)).thenReturn(List.of(user2));
        restMvc.perform(
                get("/users/{userId}/friends", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Friend"));
    }

    @Test
    void shouldGetCommonFriendsWitOtherUserTest() throws Exception {
        int userId = 1;
        int otherId = 2;
        int commonFriendId = 3;
        User user1 = new User();
        user1.setId(userId);
        user1.setEmail("mail@mail.ru");
        user1.setLogin("dolore");
        user1.setName("Nick");
        user1.setBirthday(LocalDate.now().minusYears(1));
        User user2 = new User();
        user2.setId(otherId);
        user2.setEmail("mail@mail.ru");
        user2.setLogin("dolore");
        user2.setName("Friend");
        user2.setBirthday(LocalDate.now().minusYears(1));
        User user3 = new User();
        user3.setId(commonFriendId);
        user3.setEmail("mail@mail.ru");
        user3.setLogin("dolore");
        user3.setName("Common Friend");
        user3.setBirthday(LocalDate.now().minusYears(1));

        when(userService.getCommonFriendsWitOtherUser(userId, otherId)).thenReturn(List.of(user3));
        restMvc.perform(
                get("/users/{userId}/friends/common/{otherId}", userId, otherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Common Friend"));
    }
}