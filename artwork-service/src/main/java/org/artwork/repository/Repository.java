package org.artwork.repository;

import centwong.utility.response.HttpResponse;
import centwong.utility.response.RepositoryData;
import centwong.utility.util.QueryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.artwork.entity.ArtworkParam;
import org.artwork.entity.Artwork;
import org.artwork.mapper.ArtworkMapper;

import java.util.List;

@org.springframework.stereotype.Repository
@Slf4j
public class Repository implements IRepository {

    @PersistenceContext
    private EntityManager em;

    private final ArtworkMapper mapper = ArtworkMapper.INSTANCE;

    @Override
    public RepositoryData<Artwork> save(Artwork artwork) {
        log.info("accept artwork data: {}", artwork);
        this.em.persist(artwork);
        return RepositoryData.<Artwork>builder()
                .data(artwork)
                .build();
    }

    @Override
    public RepositoryData<Artwork> get(ArtworkParam param) {
        try{
            log.info("accepting get param: {}", param);
            var query = QueryUtil
                    .generateQuery(em, Artwork.class, param);

            var res = query.getSingleResult();

            return RepositoryData
                    .<Artwork>builder()
                    .data(res)
                    .build();
        } catch (NoResultException ex){
            return RepositoryData.<Artwork>builder().build();
        }
    }

    @Override
    public RepositoryData<List<Artwork>> getList(ArtworkParam param) {
        try{
            log.info("accepting getList param: {}", param);
            var query = QueryUtil
                    .generateQuery(em, Artwork.class, param);
            var queryCount = QueryUtil
                    .generateQueryCount(em, Artwork.class, param);

            var res = query.getResultList();
            var resCount = queryCount.getSingleResult();

            var isParamExist = param.getPgParam() != null && param.getPgParam().getLimit() != null && param.getPgParam().getOffset() != null;

            Long totalPage = null;

            if(isParamExist){
                totalPage = (resCount / param.getPgParam().getLimit()) + 1;
            }

            var pg = HttpResponse.Pagination
                    .builder()
                    .totalElements(resCount)
                    .totalPage(totalPage)
                    .currentPage(param.getPgParam().getOffset())
                    .currentElements((long)res.size())
                    .build();

            return RepositoryData
                    .<List<Artwork>>builder()
                    .data(res)
                    .pg(
                            isParamExist ? pg : null
                    )
                    .build();
        } catch(NoResultException ex){
            return RepositoryData.<List<Artwork>>builder().build();
        }
    }

    @Override
    public void update(ArtworkParam param, Artwork artwork) {
        log.info("accepting param: {} with value: {}", param, artwork);
        var data = this.get(param).getData();
        if(data == null){
            throw new NoResultException(String.format("no artwork found with param: %s", param));
        }

        var updatedData = this.mapper.update(artwork, data);

        this.em.persist(updatedData);
    }
}
