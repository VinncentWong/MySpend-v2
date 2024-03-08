package org.artwork.service;

import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.response.HttpResponse;
import centwong.utility.response.ServiceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.artwork.entity.ArtworkParam;
import org.artwork.constant.EndpointConstant;
import org.artwork.entity.Artwork;
import org.artwork.entity.CloudinaryResp;
import org.artwork.mapper.ArtworkMapper;
import org.artwork.repository.IRepository;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class Service implements IService {

    @Autowired
    private IRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private final ArtworkMapper artworkMapper = ArtworkMapper.INSTANCE;

    @SneakyThrows
    @Override
    @CircuitBreaker(name = "artworkSave")
    public ServiceData<Artwork> save(ArtworkParam param, MultipartFile file, Artwork artwork) {

        var httpHeader = new HttpHeaders();
        httpHeader.put(HttpHeaderConstant.USER_ID, List.of(String.valueOf(param.getId())));

        var httpEntity = new HttpEntity<HttpResponse>(
               httpHeader
        );

        var fetchUser = this.restTemplate
                .exchange(
                        String.format("http://%s/user/%s", EndpointConstant.USER_SERVICE, param.getId()),
                        HttpMethod.GET,
                        httpEntity,
                        HttpResponse.class
                )
                .getBody()
                .getData();

        log.info("finished fetch user-service get with value: {}", fetchUser);

        if(fetchUser == null){
            return ServiceData
                    .<Artwork>builder()
                    .build();
        }

        httpHeader = new HttpHeaders();
        httpHeader.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.MULTIPART_FORM_DATA_VALUE));

        var body = new LinkedMultiValueMap<String, Object>();
        body.put("image", List.of(file.getResource()));
        var httpEntityPhoto = new HttpEntity<MultiValueMap<String, Object>>(
            body, httpHeader
        );

        var uploadPhoto = this.restTemplate
                .exchange(
                        String.format("http://%s/cloudinary/post", EndpointConstant.IMAGE_SERVICE),
                        HttpMethod.POST,
                        httpEntityPhoto,
                        CloudinaryResp.class
                );

        var data = uploadPhoto.getBody();
        if(data == null){
            throw new RuntimeException("error when uploading photo into cloudinary");
        }

        log.info("accept image-service data: {}", data);

        artwork.setPhoto(data.getSecureUrl());
        artwork.setPublicIdImage(data.getPublicId());

        User user = this
                .mapper
                .readValue(
                        this.mapper.writeValueAsString(fetchUser),
                        User.class
                );

        artwork.setFkUserId(user.getId());

        var res = this.repository
                .save(artwork);

        return ServiceData
                .<Artwork>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<Artwork> get(ArtworkParam param) {
        var res = this.repository
                .get(param);

        return ServiceData
                .<Artwork>builder()
                .data(res.getData())
                .build();
    }

    @Override
    public ServiceData<List<Artwork>> getList(ArtworkParam param) {
        var res = this.repository
                .getList(param);

        return ServiceData
                .<List<Artwork>>builder()
                .data(res.getData())
                .pg(res.getPg())
                .build();
    }

    @Override
    public void update(ArtworkParam param, Artwork artwork) {
        log.info("accept update param: {} with data: {}", param, artwork);
        var res = this.repository
                .get(param)
                .getData();

        if(res == null){
            log.error("res with param: {} on update result nothing", param);
        } else {
            var updatedData = this.artworkMapper.update(artwork, res);
            this.repository.save(updatedData);
            log.info("successfully update data: {}", updatedData);
        }
    }

    @Override
    public void delete(ArtworkParam param) {
        log.info("accept delete param: {}", param);
        var res = this.repository
                .get(param)
                .getData();

        var dataUpdate = Artwork
                .builder()
                .deletedAt(LocalDateTime.now())
                .isActive(false)
                .build();

        if(res == null){
            log.error("res with param: {} on update result nothing", param);
        } else {
            var updatedData = this.artworkMapper.update(dataUpdate, res);
            this.repository.save(updatedData);
            log.info("successfully delete data: {}", updatedData);
        }
    }

    @Override
    public void activate(ArtworkParam param) {
        log.info("accept activate param: {}", param);
        var res = this.repository
                .get(param)
                .getData();

        var dataUpdate = Artwork
                .builder()
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
                .isActive(true)
                .build();

        if(res == null){
            log.error("res with param: {} on update result nothing", param);
        } else {
            var updatedData = this.artworkMapper.update(dataUpdate, res);
            this.repository.save(updatedData);
            log.info("successfully activate data: {}", updatedData);
        }
    }
}
