package com.anxstra.services;

import com.anxstra.dao.repositories.UserRepository;
import com.anxstra.entities.User;
import com.anxstra.exceptions.UserNotFoundException;

import java.util.List;

public class UserService {

    private static final String ID_NOT_FOUND_MSG = "User with id %d not found";

    private static final String NAME_NOT_FOUND_MSG = "User with name %s not found";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllByIds(List<Integer> ids) {
        return ids.stream()
                  .map(id -> userRepository.findById(id)
                                           .orElseThrow(() -> new UserNotFoundException(String.format(ID_NOT_FOUND_MSG, id))))
                  .toList();
    }

    public List<User> findAllByNames(List<String> fullNames) {
        return fullNames.stream()
                        .map(name -> userRepository.findByName(name)
                                                   .orElseThrow(() -> new UserNotFoundException(
                                                           String.format(NAME_NOT_FOUND_MSG, name))))
                        .toList();
    }

}
