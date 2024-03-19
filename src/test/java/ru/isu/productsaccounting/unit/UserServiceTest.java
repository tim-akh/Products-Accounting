package ru.isu.productsaccounting.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.isu.productsaccounting.model.User;
import ru.isu.productsaccounting.repository.UserRepository;
import ru.isu.productsaccounting.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = new ArrayList<>();

        users.add(new User(1L, "fn1", "ln1", "email1@ya.ru", "pass", "ROLE_MANAGER"));
        users.add(new User(2L, "fn2", "ln2", "email2@ya.ru", "pass", "ROLE_MANAGER"));
        users.add(new User(3L, "fn3", "ln3", "email3@ya.ru", "pass", "ROLE_MANAGER"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> expected = underTest.getAllUsers();
        Assertions.assertEquals(users, expected);
    }

    @Test
    void shouldGetUserByEmail() {
        final String email = "email1@ya.ru";
        final User user = new User(1L, "fn1", "ln1", "email1@ya.ru", "pass", "ROLE_MANAGER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        final Optional<User> expectedOptional = underTest.findUserByEmail(email);

        Assertions.assertNotNull(expectedOptional);
    }

    @Test
    void shouldSaveUserSuccessfully() {

        final User user = new User(1L, "fn1", "ln1", "email1@ya.ru", "pass", "ROLE_MANAGER");

        underTest.saveUser(user);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void shouldDeleteUser() {
        final User user = new User(1L, "fn1", "ln1", "email1@ya.ru", "pass", "ROLE_MANAGER");

        underTest.deleteUser(user);

        verify(userRepository).delete(user);
    }
}
