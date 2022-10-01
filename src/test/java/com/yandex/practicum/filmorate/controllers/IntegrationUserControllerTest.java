package com.yandex.practicum.filmorate.controllers;

import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.UserService;
import com.yandex.practicum.filmorate.storage.UserDbStorageImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;



@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationUserControllerTest {
    private final UserDbStorageImpl userStorage;

//    @Autowired
//    public IntegrationUserControllerTest(UserDbStorageImpl userStorage) {
//        this.userStorage = userStorage;
//    }

    @BeforeEach
    public void setUp() {
        User user = new User(0, "mail@mail.ru", "user_login",
                "user_full_name", LocalDate.now());
        userStorage.createUser(user);
    }

    @Test
    public void shouldGetUserIntegrTest() {

        User user = userStorage.getUser(2);

        assertEquals(2, user.getId());
        assertEquals("mail@mail.ru", user.getEmail());
    }

    @Test
    public void shouldFindAllIntegrTest() {
        List<User> users = userStorage.findAll();

        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void shouldCreateUserIntegrTest() {
        User user = new User(0, "new_mail@mail.ru", "new_user_login",
                "new_user_name", LocalDate.now());
        userStorage.createUser(user);

        //assertEquals(2, user.getId());
        assertEquals("new_user_name", user.getName());
    }

    @Test
    public void shouldUpdateUserIntegrTest() {
        User user = new User(1, "update_mail@mail.ru", "update_user_login",
                "update_user_name", LocalDate.now());
        User updated_user = userStorage.updateUser(user);

        assertEquals(1, updated_user.getId());
        assertEquals("update_user_name", user.getName());
    }

    @Test
    public void shouldAddFriendIntegrTest() {
        User user1 = new User(0, "mail@mail.ru", "user_login",
                "user_with_friend_name", LocalDate.now());
        User friend = new User(0, "mail@mail.ru", "user_login",
                "user_friend_name", LocalDate.now());
        userStorage.createUser(user1);
        userStorage.createUser(friend);

        userStorage.addFriend(user1, friend);
        List<User> userFriends = userStorage.getUserFriends(user1);

        assertEquals("user_friend_name", userFriends.get(0).getName());
        assertEquals(1, userStorage.getUserFriends(user1).size());
    }

    @Test
    public void shouldGetUserFriendsIntegrTest() {
        User user1 = new User(0, "mail@mail.ru", "user_login",
                "user_with_friend_name", LocalDate.now());
        User friend = new User(0, "mail@mail.ru", "user_login",
                "user_friend_name", LocalDate.now());
        userStorage.createUser(user1);
        userStorage.createUser(friend);

        userStorage.addFriend(user1, friend);

        assertEquals(1, userStorage.getUserFriends(user1).size());
    }

    @Test
    public void shouldRemoveFriendIntegrTest() {
        User user1 = new User(0, "mail@mail.ru", "user_login",
                "user_with_friend_name", LocalDate.now());
        User friend = new User(0, "mail@mail.ru", "user_login",
                "user_friend_name", LocalDate.now());
        userStorage.createUser(user1);
        userStorage.createUser(friend);
        userStorage.addFriend(user1, friend);
        assertEquals(1, userStorage.getUserFriends(user1).size());

        userStorage.removeFriend(user1, friend);
        assertEquals(0, userStorage.getUserFriends(user1).size());
    }

    @Test
    public void shouldGetCommonFriendsWitOtherUserIntegrTest() {
        User user1 = new User(0, "mail@mail.ru", "user_login",
                "user_1_name", LocalDate.now());
        User user2 = new User(0, "mail@mail.ru", "user_login",
                "user_2_name", LocalDate.now());
        User commonUser = new User(0, "mail@mail.ru", "user_login",
                "common_user_name", LocalDate.now());
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(commonUser);
        userStorage.addFriend(user1, commonUser);
        userStorage.addFriend(user2, commonUser);

        List<User> commonFriendsWitOtherUser = userStorage.getCommonFriendsWitOtherUser(user1, user2);

        assertEquals("common_user_name", commonFriendsWitOtherUser.get(0).getName());
    }
}
