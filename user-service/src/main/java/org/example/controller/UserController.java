package org.example.controller;

import centwong.utility.constant.ContextConstant;
import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.entity.UserParam;
import org.example.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.context.Context;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IService service;

    @PostMapping(
            value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> save(
            HttpServletRequest req,
            @RequestBody UserDto.Create dto
    ){
        var initialTime = LocalDateTime.now();
        var data = this.service.save(dto.toUser());
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.CREATED,
                        "successfully create user",
                        data.getData(),
                        null,
                        null
                ));
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> get(
            HttpServletRequest req
    ){
        var initialTime = LocalDateTime.now();
        var id = Long.parseLong(req.getHeader(HttpHeaderConstant.USER_ID));
        var data = this.service
                .get(
                        UserParam
                                .builder()
                                .id(id)
                                .build()
                );
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully get user",
                        data.getData(),
                        null,
                        null
                ));
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> getList(
            HttpServletRequest req,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "ids") List<Long> ids,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "emails") List<String> emails,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "names") List<String> names,
            @RequestParam(name = "isActive") Boolean isActive,
            @RequestParam(name = "limit") Long limit,
            @RequestParam(name = "offset") Long offset
            ){
        var initialTime = LocalDateTime.now();
        var data = this.service
                .getList(
                        UserParam
                                .builder()
                                .id(id)
                                .build()
                );
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully get list user",
                        data.getData(),
                        null,
                        null
                ));
    }

    @PutMapping(
            value = "/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> save(
            HttpServletRequest req,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "ids") List<Long> ids,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "emails") List<String> emails,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "names") List<String> names,
            @RequestParam(name = "isActive") Boolean isActive,
            @RequestBody User user
    ){
        var initialTime = LocalDateTime.now();
        var param = UserParam
                .builder()
                .id(id)
                .ids(ids)
                .names(names)
                .email(email)
                .emails(emails)
                .name(name)
                .isActive(isActive)
                .build();
        this.service.update(param, user);
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully update user",
                        null,
                        null,
                        null
                ));
    }

    @DeleteMapping(
            value = "/delete",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> delete(
            HttpServletRequest req,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "ids") List<Long> ids,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "emails") List<String> emails,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "names") List<String> names,
            @RequestParam(name = "isActive") Boolean isActive
    ){
        var initialTime = LocalDateTime.now();
        var param = UserParam
                .builder()
                .id(id)
                .ids(ids)
                .names(names)
                .email(email)
                .emails(emails)
                .name(name)
                .isActive(isActive)
                .build();
        this.service.delete(param);
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully delete user",
                        null,
                        null,
                        null
                ));
    }

    @PatchMapping(
            value = "/activate",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> activate(
            HttpServletRequest req,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "ids") List<Long> ids,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "emails") List<String> emails,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "names") List<String> names,
            @RequestParam(name = "isActive") Boolean isActive
    ){
        var initialTime = LocalDateTime.now();
        var param = UserParam
                .builder()
                .id(id)
                .ids(ids)
                .names(names)
                .email(email)
                .emails(emails)
                .name(name)
                .isActive(isActive)
                .build();
        this.service.delete(param);
        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully delete user",
                        null,
                        null,
                        null
                ));
    }
}
