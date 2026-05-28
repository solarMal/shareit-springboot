package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errorhandler.exception.UserNotFoundException;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return UserMapper.toUserDto(
                repository.save(UserMapper.toUser(userDto))
        );
    }

    @Override
    public UserDto getUserById(Long id) {
         User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("пользователь с id" + id + " не найден"));
         return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User current = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("пользователь с id" + id + " не найден"));

        if (userDto.getName() != null) {
            current.setName(userDto.getName());
        }

        if (current.getEmail() != null) {
            current.setName(userDto.getEmail());
        }

        User savedUser = repository.save(current);

        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }

        repository.deleteById(id);
    }
}
