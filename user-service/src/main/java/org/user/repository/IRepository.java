package org.user.repository;

import centwong.utility.response.RepositoryData;
import org.user.entity.User;
import org.user.entity.UserParam;

import java.util.List;

public interface IRepository {
    RepositoryData<User> save(User user);
    RepositoryData<User> get(UserParam param);
    RepositoryData<List<User>> getList(UserParam param);
    void update(UserParam param, User user);
}
