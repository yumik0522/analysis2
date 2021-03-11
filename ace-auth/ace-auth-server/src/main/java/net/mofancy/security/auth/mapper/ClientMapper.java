package net.mofancy.security.auth.mapper;

import net.mofancy.security.auth.entity.Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-04
 */
public interface ClientMapper extends BaseMapper<Client> {
    List<String> selectAllowedClient(int serviceId);

    List<Client> selectAuthorityServiceInfo(int clientId);
}
