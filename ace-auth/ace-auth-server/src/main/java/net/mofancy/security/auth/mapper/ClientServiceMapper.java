package net.mofancy.security.auth.mapper;

import net.mofancy.security.auth.entity.ClientService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-04
 */
public interface ClientServiceMapper extends BaseMapper<ClientService> {
    void deleteByServiceId(int id);
}
