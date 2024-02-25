package org.example.repository;

import centwong.utility.response.RepositoryData;
import org.example.entity.User;
import org.example.entity.UserParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepository {
    RepositoryData<User> save(User user);
    RepositoryData<User> get(UserParam param);
    RepositoryData<List<User>> getList(UserParam param);
    void update(UserParam param, User user);
}
