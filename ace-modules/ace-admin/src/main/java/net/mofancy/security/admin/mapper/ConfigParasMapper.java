package net.mofancy.security.admin.mapper;

import net.mofancy.security.admin.entity.ConfigParas;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 常量配置表 Mapper 接口
 * </p>
 *
 * @author zwq
 * @since 2019-12-06
 */
public interface ConfigParasMapper extends BaseMapper<ConfigParas> {

    List<Map<String,Object>> getConfigParasList(Map<String,Object> params);

    List<Map<String,Object>> getGroupIdList(Map<String, Object> params);
}
