package org.artwork.repository;

import centwong.utility.response.HttpResponse;
import centwong.utility.response.RepositoryData;
import centwong.utility.util.QueryUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.artwork.constant.ArtworkConstant;
import org.artwork.entity.ArtworkParam;
import org.artwork.entity.Artwork;
import org.artwork.mapper.ArtworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;

@org.springframework.stereotype.Repository
@Slf4j
public class Repository implements IRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objMapper;

    private final ArtworkMapper mapper = ArtworkMapper.INSTANCE;

    @Override
    public RepositoryData<Artwork> save(Artwork artwork) {
        var allKeys = redisTemplate.keys(ArtworkConstant.ALL);
        log.info("accept artwork data: {}", artwork);
        this.em.persist(artwork);

        if(allKeys != null){
            redisTemplate.delete(allKeys);
            log.info("delete all keys: {}", allKeys);
        }

        return RepositoryData.<Artwork>builder()
                .data(artwork)
                .build();
    }

    @Override
    @SneakyThrows
    public RepositoryData<Artwork> get(ArtworkParam param) {
        log.info("accepting get param: {}", param);
        try{
            var ops = this.redisTemplate.opsForValue();
            var redisData = ops.get(String.format(ArtworkConstant.GET, param));
            if(redisData != null){
                log.info("cache hit with res: {}", redisData);
                return RepositoryData
                        .<Artwork>builder()
                        .data(objMapper.readValue(redisData, Artwork.class))
                        .build();
            }

            var query = QueryUtil
                    .generateQuery(em, Artwork.class, param);

            var res = query.getSingleResult();

            ops.set(
                    String.format(ArtworkConstant.GET, param),
                    this.objMapper.writeValueAsString(res),
                    Duration.ofMinutes(1)
            );

            return RepositoryData
                    .<Artwork>builder()
                    .data(res)
                    .build();
        } catch (NoResultException ex){
            return RepositoryData.<Artwork>builder().build();
        }
    }

    @Override
    @SneakyThrows
    public RepositoryData<List<Artwork>> getList(ArtworkParam param) {
        log.info("accepting getList param: {}", param);
        try{
            var ops = this.redisTemplate.opsForValue();
            var redisData = ops.get(String.format(ArtworkConstant.GET_LIST, param));
            if(redisData != null){
                var listData = this.objMapper
                        .readValue(redisData, new TypeReference<List<Artwork>>() {});
                var pgStr = ops
                        .get(String.format(ArtworkConstant.GET_LIST_PG, param));
                HttpResponse.Pagination pg = null;
                if(pgStr != null){
                    pg = this.objMapper.readValue(pgStr, HttpResponse.Pagination.class);
                }
                log.info("cache hit with value: {}, pg: {}", listData, pg);
                return RepositoryData
                        .<List<Artwork>>builder()
                        .data(listData)
                        .pg(pg)
                        .build();
            }
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

            ops.set(
                    String.format(ArtworkConstant.GET_LIST, param),
                    this.objMapper.writeValueAsString(res),
                    Duration.ofMinutes(1)
            );

            if(isParamExist){
                ops.set(
                        String.format(ArtworkConstant.GET_LIST_PG, param),
                        this.objMapper.writeValueAsString(pg),
                        Duration.ofMinutes(1)
                );
            }

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

        var keys = this.redisTemplate
                .keys(ArtworkConstant.ALL);
        if(keys != null){
            this.redisTemplate.delete(keys);
        }
    }
}
