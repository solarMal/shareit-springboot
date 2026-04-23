package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorhandler.exception.CriticalException;
import ru.practicum.shareit.errorhandler.exception.UserNotFoundException;
import ru.practicum.shareit.errorhandler.exception.ValidateException;


import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepositoryImpl") UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        validation(user);
        return repository.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return repository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("пользователь с id" + id + " не найден"));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public User updateUser(Long id, User user) {
        existingEmailValidator(user);
        return repository.updateUser(id, user);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteUserById(id);
    }


    private void validation(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidateException("имейл не может быть пустым");
        }
        existingEmailValidator(user);
    }

    private void existingEmailValidator(User user) {
        for (User currentUser : repository.getAllUsers()) {
            if (Objects.equals(currentUser.getEmail(), user.getEmail())) {
                throw new CriticalException("имейл уже существует");
            }
        }
    }
}
