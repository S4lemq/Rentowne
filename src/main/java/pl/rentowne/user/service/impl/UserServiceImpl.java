package pl.rentowne.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.repository.UserRepository;
import pl.rentowne.user.service.UserService;

import java.util.Optional;

@Service("USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
    }

    @Override
    public UserBasicDto getLoggedUser() throws RentowneBusinessException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        if (loggedInUser == null || !(loggedInUser.getPrincipal() instanceof UserDetails)) {
            throw new RentowneBusinessException(RentowneErrorCode.USER_IS_NOT_LOGGED, "");
        }

        UserDetails userDetails = (UserDetails) loggedInUser.getPrincipal();
        String email = userDetails.getUsername();

        return userRepository.getByEmail(email);
    }
}
