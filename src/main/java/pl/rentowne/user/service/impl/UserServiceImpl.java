package pl.rentowne.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.user.model.User;
import pl.rentowne.user.repository.UserRepository;
import pl.rentowne.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
    }
}
