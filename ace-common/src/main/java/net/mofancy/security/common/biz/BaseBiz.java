package net.mofancy.security.common.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.mofancy.security.common.msg.TableResultResponse;
import net.mofancy.security.common.util.EntityUtils;
import net.mofancy.security.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/11/4 0004.
 */
public abstract class BaseBiz<M extends BaseMapper<T>, T> {
    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(Query query) {
        QueryWrapper<T> queryWrapper =  new QueryWrapper<>();
        if(query.entrySet().size()>0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                queryWrapper.eq(entry.getKey(), entry.getValue().toString());
            }
        }
        return mapper.selectOne(queryWrapper);
    }

    public T selectById(Serializable id) {
        return mapper.selectById(id);
    }

    public List<T> selectList(T entity) {
        QueryWrapper<T> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(entity);
        return mapper.selectList(queryWrapper);
    }


    public List<T> selectListAll() {
        return mapper.selectList(new QueryWrapper<T>());
    }

    public Long selectCount(T entity) {
        QueryWrapper<T> queryWrapper =  new QueryWrapper<>();
        queryWrapper.setEntity(entity);
        return new Long(mapper.selectCount(queryWrapper));
    }

    public void insert(T entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insert(entity);
    }


    public void insertSelective(T entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insert(entity);
    }

    public void delete(Query query) {
        QueryWrapper<T> queryWrapper =  new QueryWrapper<>();
        if(query.entrySet().size()>0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                queryWrapper.eq(entry.getKey(), entry.getValue().toString());
            }
        }
        mapper.delete(queryWrapper);
    }


    public void deleteById(Serializable id) {
        mapper.deleteById(id);
    }

    public void updateById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        mapper.updateById(entity);
    }

    public void updateSelectiveById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        mapper.updateById(entity);

    }

    public List<T> selectByExample(QueryWrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    public int selectCountByExample(QueryWrapper<T> queryWrapper) {
        return mapper.selectCount(queryWrapper);
    }



    public TableResultResponse<T> selectByQuery(Query query) {

        QueryWrapper<T> queryWrapper =  new QueryWrapper<>();

        if(query.entrySet().size()>0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if(entry.getKey().equals("orderByDesc")) {
                    queryWrapper.orderByDesc(entry.getValue().toString());
                } else if(entry.getKey().equals("orderByAsc")) {
                    queryWrapper.orderByAsc(entry.getValue().toString());
                } else {
                    if(entry.getKey().lastIndexOf("id")>0||entry.getKey().lastIndexOf("Id")>0) {
                        queryWrapper.eq(entry.getKey(), entry.getValue().toString());
                    } else {
                        queryWrapper.like(entry.getKey(), "%" + entry.getValue().toString() + "%");
                    }
                }

            }
        }

        queryWrapper.orderByDesc("id");

        Page<T> page = new Page<>(query.getPage(), query.getLimit());  // 查询第n页，每页返回x条
        IPage<T> iPage = mapper.selectPage(page,queryWrapper);

        return new TableResultResponse<T>(iPage.getTotal(), iPage.getRecords());

    }


}
