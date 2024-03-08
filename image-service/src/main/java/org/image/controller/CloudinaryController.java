package org.image.controller;

import org.image.entity.CloudinaryResp;
import org.image.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    private IService service;

    @PostMapping(
            value = "/post",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CloudinaryResp> save(
            @RequestPart("image") MultipartFile file
            ){
        return ResponseEntity.ok(
                this.service.save(file)
        );
    }
}
