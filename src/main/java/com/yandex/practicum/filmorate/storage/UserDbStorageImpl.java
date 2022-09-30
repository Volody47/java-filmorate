package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.yandex.practicum.filmorate.utils.Validator.validateUser;


@Repository
@Slf4j
public class UserDbStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User createUser(User user) {
        validateUser(user);
        String sqlQuery = "insert into USERS (USER_FULL_NAME, USER_EMAIL," +
                " USER_LOGIN, USER_BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", user.getId());
        if (!userRows.next()) {
            log.debug("User with id={} not found", user.getId());
            return null;
        }
        String sqlQuery = "UPDATE USERS SET USER_FULL_NAME = ?, USER_EMAIL = ?," +
                " USER_LOGIN = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        log.debug("User with id={} updated", user.getId());
        return user;
    }

    @Override
    public void remove(User user) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        boolean deleted = jdbcTemplate.update(sqlQuery, user.getId()) > 0;
        if (deleted) {
            log.debug("User with id={} removed", user.getId());
        } else {
            log.debug("User with id={} haven't found", user.getId());
        }
    }

    public List<User> findAll() {
        String sqlQuery = "select * from USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        if (users.size() == 0) {
            return null;
        }
        return users;
    }

    public User getUser(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
        if(userRows.next()) {
            int user_id = userRows.getInt("USER_ID");
            String email = userRows.getString("USER_EMAIL");
            String login = userRows.getString("USER_LOGIN");
            String name = userRows.getString("USER_FULL_NAME");
            LocalDate birthday = userRows.getDate("USER_BIRTHDAY").toLocalDate();
            //log.info("User found: {} {}", user.getId(), user.getName());
            return new User(user_id, email, login, name, birthday);
        } else {
            log.info("User with id= {} haven't found.", id);
            return null;
        }
    }

    public void addFriend(User user, User friend) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select * from FRIENDS" +
                " where USER_ID = ? AND FRIEND_ID = ?", user.getId(), friend.getId());
        if (friendsRows.next()) {
            String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID, REQUEST_STATUS," +
                    " FRIEND_RELATIONSHIP_STATUS) values (?, ?, 1, true)";
            jdbcTemplate.update(sqlQuery
                    , user.getId()
                    , friend.getId());
        } else {
            String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID, REQUEST_STATUS," +
                    " FRIEND_RELATIONSHIP_STATUS) values (?, ?, 1, false)";
            jdbcTemplate.update(sqlQuery
                    , user.getId()
                    , friend.getId());
        }
    }

    public void removeFriend(User user, User friend) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        boolean deleted = jdbcTemplate.update(sqlQuery, user.getId(), friend.getId()) > 0;
        if (deleted) {
            log.debug("Friend with id={} removed", friend.getId());
        } else {
            log.debug("Friend with id={} haven't found", friend.getId());
        }
    }

    public List<User> getUserFriends(User user) {
        String sqlQuery = "select *" +
                " from USERS as u" +
                " RIGHT JOIN FRIENDS as fr ON u.USER_ID = fr.FRIEND_ID" +
                " WHERE fr.USER_ID = ? AND fr.REQUEST_STATUS = 1";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, user.getId());
        return users;
    }

    public List<User> getCommonFriendsWitOtherUser(User user, User otherUser) {
        String sqlQuery = "SELECT *" +
                " FROM USERS AS u" +
                " WHERE u.USER_ID in (select FRIEND_ID from FRIENDS as fr" +
                "                    WHERE (fr.USER_ID = ? OR fr.USER_ID = ?)" +
                "                      AND (fr.FRIEND_ID not in (?, ?)))";
        List<User> listOfCommonFriends = jdbcTemplate.query(sqlQuery, this::mapRowToUser, user.getId(),
                                                            otherUser.getId(), user.getId(), otherUser.getId());
        return listOfCommonFriends;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("USER_ID");
        String email = resultSet.getString("USER_EMAIL");
        String login = resultSet.getString("USER_LOGIN");
        String name = resultSet.getString("USER_FULL_NAME");
        LocalDate birthday = resultSet.getDate("USER_BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
