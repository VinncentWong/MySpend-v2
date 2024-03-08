package org.user.service;

import centwong.utility.exception.ForbiddenException;
import centwong.utility.jwt.JwtUtil;
import centwong.utility.response.ServiceData;
import centwong.utility.util.BcryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.user.constant.Constant;
import org.user.dto.UserDto;
import org.user.entity.User;
import org.user.entity.UserParam;
import org.user.exception.ConflictException;
import org.user.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class Service implements IService{

    @Autowired
    private IRepository repository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public ServiceData<User> save(User user) {
        var data = this.repository
                .get(
                        UserParam
                                .builder()
                                .email(user.getEmail())
                                .build()
                );

        if(data.getData() != null){
            throw new ConflictException("user email already exist");
        }

        var insertData = this.repository.save(user);

        return ServiceData
                .<User>builder()
                .data(insertData.getData())
                .build();
    }

    @Override
    public ServiceData<User> login(UserDto.Login dto) {
        var data = this.repository
                .get(
                        UserParam
                                .builder()
                                .email(dto.toUser().getEmail())
                                .build()
                );
        if(data.getData() == null){
            throw new RuntimeException("user data not found");
        }

        var user = data.getData();

        if(BcryptUtil.isMatch(dto.password(), user.getPassword())){
            var jwtToken = JwtUtil.generateJwtToken(
                    jwtSecret,
                    user.getRole().equals(Constant.ARTIST) ? Constant.ARTIST : Constant.USER,
                    user.getId()
            );
            return ServiceData
                    .<User>builder()
                    .data(user)
                    .metadata(jwtToken)
                    .build();
        } else {
            throw new ForbiddenException("email/password doesn't match");
        }
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
