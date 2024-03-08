package org.artwork.controller;

import centwong.utility.constant.ContextConstant;
import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.artwork.dto.ArtworkDto;
import org.artwork.entity.ArtworkParam;
import org.artwork.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.context.Context;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/artwork")
public class ArtworkController {

    @Autowired
    private IService service;

    @PostMapping(
            value = "/save",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> save(
            HttpServletRequest req,
            @RequestPart("image") MultipartFile file,
            @RequestPart("dto") @Valid ArtworkDto.Create dto
            ){
        var id = Long.parseLong(req.getHeader(HttpHeaderConstant.USER_ID));
        var res = this.service.save(
                ArtworkParam
                        .builder()
                        .id(id)
                        .build(),
                file,
                dto.toArtwork()
        );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully save artwork",
                        res.getData(),
                        null,
                        null
                ));
    }
}
