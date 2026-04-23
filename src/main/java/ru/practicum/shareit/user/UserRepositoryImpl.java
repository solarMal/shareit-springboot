package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorhandler.exception.UserNotFoundException;


import java.util.*;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long dynamicId = 1;

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        user.setId(dynamicId++);
        users.put(user.getId(), user);
        log.info("пользователь добавлен {}", user);
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser  = users.get(id);

        if (existingUser == null) {
            throw new UserNotFoundException("пользователь не найден");
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        log.info("пользователь обновлён {}", user);
        return existingUser;
    }

    @Override
    public void deleteUserById(Long id) {
        User removed = users.remove(id);

        if (removed == null) {
            throw new UserNotFoundException("пользователь не найден");
        }

        log.info("пользователь с id={} удалён", id);
    }
}
