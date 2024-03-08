package org.artwork.repository;

import centwong.utility.response.RepositoryData;
import org.artwork.entity.Artwork;
import org.artwork.entity.ArtworkParam;

import java.util.List;

public interface IRepository {
    RepositoryData<Artwork> save(Artwork artwork);
    RepositoryData<Artwork> get(ArtworkParam param);
    RepositoryData<List<Artwork>> getList(ArtworkParam param);
    void update(ArtworkParam param, Artwork artwork);
}
