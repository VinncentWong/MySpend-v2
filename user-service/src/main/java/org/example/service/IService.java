package org.example.service;

import centwong.utility.response.ServiceData;
import org.example.entity.User;
import org.example.entity.UserParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IService {
    ServiceData<User> save(User user);
    ServiceData<User> get(UserParam param);
    ServiceData<List<User>> getList(UserParam param);
    void update(UserParam param, User data);
    void delete(UserParam param);
    void activate(UserParam param);
}
