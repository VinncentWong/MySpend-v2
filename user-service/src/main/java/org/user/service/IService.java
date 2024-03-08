package org.user.service;

import centwong.utility.response.ServiceData;
import org.user.dto.UserDto;
import org.user.entity.User;
import org.user.entity.UserParam;

import java.util.List;

public interface IService {
    ServiceData<User> save(User user);
    ServiceData<User> login(UserDto.Login dto);
    ServiceData<User> get(UserParam param);
    ServiceData<List<User>> getList(UserParam param);
    void update(UserParam param, User data);
    void delete(UserParam param);
    void activate(UserParam param);
}
