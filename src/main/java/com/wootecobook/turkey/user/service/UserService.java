package com.wootecobook.turkey.user.service;

import com.wootecobook.turkey.user.domain.User;
import com.wootecobook.turkey.user.domain.UserRepository;
import com.wootecobook.turkey.user.service.dto.UserRequest;
import com.wootecobook.turkey.user.service.dto.UserResponse;
import com.wootecobook.turkey.user.service.exception.NotFoundUserException;
import com.wootecobook.turkey.user.service.exception.SignUpException;
import com.wootecobook.turkey.user.service.exception.UserDeleteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserResponseById(Long id) {
        return UserResponse.from(findById(id));
    }

    public UserResponse save(UserRequest userRequest) {
        try {
            return UserResponse.from(userRepository.save(userRequest.toEntity()));
        } catch (Exception e) {
            throw new SignUpException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new UserDeleteException();
        }
    }
}
