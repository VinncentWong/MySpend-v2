package org.example.repository;

import centwong.utility.response.HttpResponse;
import centwong.utility.response.RepositoryData;
import centwong.utility.util.QueryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.entity.UserParam;
import org.example.mapper.UserMapper;

import java.util.List;

@org.springframework.stereotype.Repository
@Slf4j
public class Repository implements IRepository{

    @PersistenceContext
    private EntityManager em;

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public RepositoryData<User> save(User user) {
        log.info("catch save data: {}", user);
        em.persist(user);
        return RepositoryData
                .<User>builder()
                .data(user)
                .build();
    }

    @Override
    public RepositoryData<User> get(UserParam param) {
        try{
            log.info("catch get param: {}", param);
            var query = QueryUtil.generateQuery(em, User.class, param);

            var res = query.getSingleResult();

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
    public RepositoryData<List<User>> getList(UserParam param) {
        try{
            log.info("catch getList param: {}", param);
            var query = QueryUtil.generateQuery(em, User.class, param);
            var queryCount = QueryUtil.generateQueryCount(em, User.class, param);

            var res = query.getResultList();
            var n = queryCount.getSingleResult();

            Long totalPage = null;

            var isPgExist = param.getPg().getLimit() != null && param.getPg().getOffset() != null;

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
        var updatedData = this.mapper.update(user, data);

        this.em.persist(updatedData);
    }
}
