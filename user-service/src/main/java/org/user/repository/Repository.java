package org.user.repository;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.user.constant.UserConstant;
import org.user.entity.User;
import org.user.entity.UserParam;
import org.user.mapper.UserMapper;

import java.time.Duration;
import java.util.List;

@org.springframework.stereotype.Repository
@Slf4j
public class Repository implements IRepository{

    @PersistenceContext
    private EntityManager em;

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public RepositoryData<User> save(User user) {
        log.info("catch save data: {}", user);
        em.persist(user);
        var keys = this.redisTemplate
                .keys(UserConstant.ALL);
        if(keys != null){
            this.redisTemplate.delete(keys);
            log.info("delete keys: {}", keys);
        }
        return RepositoryData
                .<User>builder()
                .data(user)
                .build();
    }

    @Override
    @SneakyThrows
    public RepositoryData<User> get(UserParam param) {
        log.info("catch get param: {}", param);
        try{
            var ops = this.redisTemplate.opsForValue();
            var redisResult = ops.get(String.format(UserConstant.GET, param));
            if(redisResult != null){
                var user = this.objMapper
                        .readValue(redisResult, User.class);
                log.info("cache hit with data: {}", user);
                return RepositoryData
                        .<User>builder()
                        .data(user)
                        .build();
            }
            var query = QueryUtil.generateQuery(em, User.class, param);

            var res = query.getSingleResult();

            ops.set(String.format(UserConstant.GET, param), this.objMapper.writeValueAsString(res));

            return RepositoryData
                    .<User>builder()
                    .data(res)
                    .build();
        } catch(NoResultException ex){
            return RepositoryData
                    .<User>builder()
                    .build();
        }
    }

    @Override
    @SneakyThrows
    public RepositoryData<List<User>> getList(UserParam param) {
        log.info("catch getList param: {}", param);
        try{
            var ops = this.redisTemplate.opsForValue();
            var redisResult = ops.get(String.format(UserConstant.GET_LIST, param));
            if(redisResult != null){
                var listData = this.objMapper.readValue(redisResult, new TypeReference<List<User>>() {});
                var pgStr = ops.get(String.format(UserConstant.GET_LIST_PG, param));
                HttpResponse.Pagination pg = null;
                if(pgStr != null){
                    pg = this.objMapper.readValue(pgStr, HttpResponse.Pagination.class);
                }
                log.info("cache hit with value: {}, pg: {}", listData, pg);
                return RepositoryData
                        .<List<User>>builder()
                        .data(listData)
                        .pg(pg)
                        .build();
            }
            var query = QueryUtil.generateQuery(em, User.class, param);
            var queryCount = QueryUtil.generateQueryCount(em, User.class, param);

            var res = query.getResultList();
            var n = queryCount.getSingleResult();

            Long totalPage = null;

            var isPgExist = param.getPg() != null && param.getPg().getLimit() != null && param.getPg().getOffset() != null;

            if(isPgExist){
                totalPage = (n / param.getPg().getLimit()) + 1;
            }

            var pg = HttpResponse.Pagination
                    .builder()
                    .currentElements((long)res.size())
                    .currentPage(param.getPg().getOffset())
                    .totalPage(totalPage)
                    .totalElements(n)
                    .build();

            ops.set(
                    String.format(UserConstant.GET_LIST, param),
                    this.objMapper.writeValueAsString(res),
                    Duration.ofMinutes(1)
            );

            if(isPgExist){
                ops.set(
                        String.format(UserConstant.GET_LIST, param),
                        this.objMapper.writeValueAsString(pg),
                        Duration.ofMinutes(1)
                );
            }
            return RepositoryData
                    .<List<User>>builder()
                    .data(res)
                    .pg(isPgExist? pg: null)
                    .build();
        } catch(NoResultException ex){
            return  RepositoryData
                    .<List<User>>builder()
                    .build();
        }
    }

    @Override
    public void update(UserParam param, User user) {

        log.info("catch update param: {} with data {}", param, user);

        var data = this.get(param).getData();
        if(data == null){
            throw new NoResultException("no user data found");
        }

        var updatedData = this.mapper.update(user, data);

        this.em.persist(updatedData);

        var keys = this.redisTemplate.keys(UserConstant.ALL);
        if(keys != null){
            this.redisTemplate.delete(keys);
            log.info("delete keys: {}", keys);
        }
    }
}
