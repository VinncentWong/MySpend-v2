package org.artwork.service;

import centwong.utility.response.ServiceData;
import org.artwork.entity.ArtworkParam;
import org.artwork.entity.Artwork;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IService {
    ServiceData<Artwork> save(ArtworkParam param, MultipartFile file, Artwork artwork);
    ServiceData<Artwork> get(ArtworkParam param);
    ServiceData<List<Artwork>> getList(ArtworkParam param);
    void update(ArtworkParam param, Artwork artwork);
    void delete(ArtworkParam param);
    void activate(ArtworkParam param);
}
