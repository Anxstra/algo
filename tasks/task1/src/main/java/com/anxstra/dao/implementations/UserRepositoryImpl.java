package com.anxstra.dao.implementations;

import com.anxstra.dao.repositories.UserRepository;
import com.anxstra.entities.User;
import com.anxstra.gson.config.DBReader;
import com.anxstra.gson.config.GsonConfigurer;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findById(Integer id) {
        return DBReader.getJsonArray("users")
                       .asList()
                       .stream()
                       .map(el -> GsonConfigurer.getGson()
                                                .fromJson(el, User.class))
                       .filter(user -> user.getId()
                                           .equals(id))
                       .findFirst();
    }

    @Override
    public Optional<User> findByName(String fullName) {
        return DBReader.getJsonArray("users")
                       .asList()
                       .stream()
                       .map(el -> GsonConfigurer.getGson()
                                                .fromJson(el, User.class))
                       .filter(user -> (user.getFullName()
                                            .equals(fullName)))
                       .findFirst();
    }
}
