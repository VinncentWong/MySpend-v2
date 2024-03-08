package org.artwork.controller;

import centwong.utility.constant.ContextConstant;
import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.artwork.dto.ArtworkDto;
import org.artwork.entity.Artwork;
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
import java.util.List;

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

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> getList(
            HttpServletRequest req,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "names", required = false) List<String> names,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "stock", required = false) Long stock,
            @RequestParam(value = "stocks", required = false) List<Long> stocks,
            @RequestParam(value = "weight", required = false) Double weight,
            @RequestParam(value = "weights", required = false) List<Double> weights,
            @RequestParam(value = "dimensionX", required = false) Double dimensionX,
            @RequestParam(value = "dimensionXs", required = false) List<Double> dimensionXs,
            @RequestParam(value = "dimensionY", required = false) Double dimensionY,
            @RequestParam(value = "dimensionYs", required = false) List<Double> dimensionYs,
            @RequestParam(value = "dimensionZ", required = false) Double dimensionZ,
            @RequestParam(value = "dimensionZs", required = false) List<Double> dimensionZs,
            @RequestParam(value = "fkUserId", required = false) Long fkUserId,
            @RequestParam(value = "fkUserIds", required = false) List<Long> fkUserIds,
            @RequestParam(value = "isPreorder", required = false) Boolean isPreorder,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Long offset
    ){
        var res = this.service
                .getList(
                        ArtworkParam
                                .builder()
                                .id(id)
                                .ids(ids)
                                .name(name)
                                .names(names)
                                .dimensionX(dimensionX)
                                .dimensionXs(dimensionXs)
                                .dimensionYs(dimensionYs)
                                .categories(categories)
                                .category(category)
                                .description(description)
                                .descriptions(descriptions)
                                .dimensionY(dimensionY)
                                .dimensionZ(dimensionZ)
                                .dimensionZs(dimensionZs)
                                .isActive(isActive)
                                .isPreorder(isPreorder)
                                .stock(stock)
                                .stocks(stocks)
                                .weight(weight)
                                .weights(weights)
                                .fkUserIds(fkUserIds)
                                .fkUserId(fkUserId)
                                .pgParam(
                                        HttpResponse.PaginationParam
                                                .builder()
                                                .limit(limit)
                                                .offset(offset)
                                                .build()
                                )
                                .build()
                );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully get list artwork",
                        res.getData(),
                        res.getPg(),
                        res.getMetadata()
                ));
    }

    @GetMapping(
            value = "/get",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> get(
            HttpServletRequest req,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "names", required = false) List<String> names,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "stock", required = false) Long stock,
            @RequestParam(value = "stocks", required = false) List<Long> stocks,
            @RequestParam(value = "weight", required = false) Double weight,
            @RequestParam(value = "weights", required = false) List<Double> weights,
            @RequestParam(value = "dimensionX", required = false) Double dimensionX,
            @RequestParam(value = "dimensionXs", required = false) List<Double> dimensionXs,
            @RequestParam(value = "dimensionY", required = false) Double dimensionY,
            @RequestParam(value = "dimensionYs", required = false) List<Double> dimensionYs,
            @RequestParam(value = "dimensionZ", required = false) Double dimensionZ,
            @RequestParam(value = "dimensionZs", required = false) List<Double> dimensionZs,
            @RequestParam(value = "fkUserId", required = false) Long fkUserId,
            @RequestParam(value = "fkUserIds", required = false) List<Long> fkUserIds,
            @RequestParam(value = "isPreorder", required = false) Boolean isPreorder,
            @RequestParam(value = "isActive", required = false) Boolean isActive
    ){
        var res = this.service
                .get(
                        ArtworkParam
                                .builder()
                                .id(id)
                                .ids(ids)
                                .name(name)
                                .names(names)
                                .dimensionX(dimensionX)
                                .dimensionXs(dimensionXs)
                                .dimensionYs(dimensionYs)
                                .categories(categories)
                                .category(category)
                                .description(description)
                                .descriptions(descriptions)
                                .dimensionY(dimensionY)
                                .dimensionZ(dimensionZ)
                                .dimensionZs(dimensionZs)
                                .isActive(isActive)
                                .isPreorder(isPreorder)
                                .stock(stock)
                                .stocks(stocks)
                                .weight(weight)
                                .weights(weights)
                                .fkUserId(fkUserId)
                                .fkUserIds(fkUserIds)
                                .pgParam(
                                        HttpResponse.PaginationParam
                                                .builder()
                                                .limit(1)
                                                .build()
                                )
                                .build()
                );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully get artwork",
                        res.getData(),
                        res.getPg(),
                        res.getMetadata()
                ));
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> update(
            HttpServletRequest req,
            @PathVariable("id") Long id,
            @RequestBody Artwork artwork
            ){
        this.service
                .update(
                        ArtworkParam
                                .builder()
                                .id(id)
                                .build(),
                        artwork
                );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully update artwork",
                        null, null, null
                ));
    }

    @PatchMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> activate(
            HttpServletRequest req,
            @PathVariable("id") Long id
    ){
        this.service
                .activate(
                        ArtworkParam
                                .builder()
                                .id(id)
                                .build()
                );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully activate artwork",
                        null, null, null
                ));
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<HttpResponse> delete(
            HttpServletRequest req,
            @PathVariable("id") Long id
    ){
        this.service
                .delete(
                        ArtworkParam
                                .builder()
                                .id(id)
                                .build()
                        );
        var initialTime = LocalDateTime.now();

        return ResponseEntity
                .ok(HttpResponse.sendSuccessResponse(
                        Context
                                .of(ContextConstant.TIME_START, initialTime)
                                .put(ContextConstant.REQUEST_PATH, req.getRequestURI()),
                        HttpStatus.OK,
                        "successfully delete artwork",
                        null, null, null
                ));
    }
}
