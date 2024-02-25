package org.example.service;

import centwong.utility.response.ServiceData;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.entity.UserParam;
import org.example.exception.ConflictException;
import org.example.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Slf4j
public class Service implements IService{

    @Autowired
    private IRepository repository;

    @Override
    public ServiceData<User> save(User user) {
        var data = this.repository
                .get(
                        UserParam
                                .builder()
                                .email(user.getEmail())
                                .build()
                );

        if(data != null){
            throw new ConflictException("user email already exist");
        }

        var insertData = this.repository.save(user);

        return ServiceData
                .<User>builder()
                .data(insertData.getData())
                .build();
    }

    @Override
    public ServiceData<User> get(UserParam param) {

        var data = this.repository.get(param);

        return ServiceData
                .<User>builder()
                .data(data.getData())
                .build();
    }

    @Override
    public ServiceData<List<User>> getList(UserParam param) {
        var data = this.repository.getList(param);

        return ServiceData
                .<List<User>>builder()
                .data(data.getData())
                .pg(data.getPg())
                .build();
    }

    @Override
    public void update(UserParam param, User data) {
        this.repository.update(param, data);
    }

    @Override
    public void delete(UserParam param) {
        this.repository.update(
                param,
                User
                .builder()
                .deletedAt(LocalDateTime.now())
                .isActive(false)
                .build()
        );
    }

    @Override
    public void activate(UserParam param) {
        this.repository.update(
                param,
                User
                        .builder()
                        .updatedAt(LocalDateTime.now())
                        .deletedAt(null)
                        .isActive(true)
                        .build()
        );
    }
}
