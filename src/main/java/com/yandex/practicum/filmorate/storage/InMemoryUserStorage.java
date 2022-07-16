package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import com.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateUser;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer identificator = 0;

    public int generateId() {
        return ++identificator;
    }

    public List<User> findAll() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        for (Integer user : users.keySet()) {
            listOfUsers.add(users.get(user));
        }
        log.debug("Users quantity: {}", listOfUsers.size());
        return listOfUsers;
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("New user created with id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = null;
        for (Integer userId : users.keySet()) {
            if (userId.equals(user.getId())) {
                validateUser(user);
                int id = user.getId();
                updatedUser = users.put(id, user);
                log.debug("User with id={} updated", user.getId());
            }
        }
        //log.error("Unknown user.");
        return updatedUser;
    }

    @Override
    public void remove(User user) {
        users.remove(user.getId());
        log.debug("User with id={} removed", user.getId());
    }

    public User getUser(int id) {
        User user = null;
        for (Integer userId : users.keySet()) {
            if (userId == id) {
                user = users.get(id);
            }
        }
        return user;
    }

    public List<User> getUserFriends(User user) {
        ArrayList<User> listOfFriends = new ArrayList<>();
        for (Integer friendIds : user.getFriendIds()) {
            if (user.getId() != friendIds) {
                listOfFriends.add(users.get(friendIds));
            }
        }
        return listOfFriends;
    }

    public void addFriend(User user, User friend) {
        user.getFriendIds().add(user.getId());
        user.getFriendIds().add(friend.getId());
    }

    public void removeFriend(User user, User friend) {
        user.getFriendIds().remove(user.getId());
        user.getFriendIds().remove(friend.getId());
    }

    public List<User> getCommonFriendsWitOtherUser(User user, User otherUser) {
        ArrayList<User> listOfCommonFriends = new ArrayList<>();
        for (Integer userFriendIds : user.getFriendIds()) {
            for (Integer otherUserFriendIds : otherUser.getFriendIds()) {
                if (userFriendIds.equals(otherUserFriendIds)) {
                    listOfCommonFriends.add(users.get(userFriendIds));
                }
            }
        }
        return listOfCommonFriends;
    }
}
