package ru.isu.productsaccounting.converter;

import org.springframework.core.convert.converter.Converter;
import ru.isu.productsaccounting.model.User;
import ru.isu.productsaccounting.repository.UserRepository;

import java.util.Optional;

public class UserConverter implements Converter<Long, Optional<User>> {

    private final UserRepository userRepository;

    public UserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> convert(Long id) {
        return userRepository.findById(id);
    }
}
