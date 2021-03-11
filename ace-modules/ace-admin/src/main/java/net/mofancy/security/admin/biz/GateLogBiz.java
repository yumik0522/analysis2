package net.mofancy.security.admin.biz;

import net.mofancy.security.admin.entity.GateLog;
import net.mofancy.security.admin.mapper.GateLogMapper;
import net.mofancy.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

/**
* <p>
    *  服务实现类
    * </p>
*
* @author zhangweiqi
* @since 2019-11-05
*/
@Service
public class GateLogBiz extends BaseBiz<GateLogMapper, GateLog>  {
    @Override
    public void insert(GateLog entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insert(entity);
    }

}
