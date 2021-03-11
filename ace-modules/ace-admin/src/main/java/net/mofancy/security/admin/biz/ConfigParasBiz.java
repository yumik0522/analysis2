package net.mofancy.security.admin.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mofancy.security.admin.entity.ConfigParas;
import net.mofancy.security.admin.mapper.ConfigParasMapper;
import net.mofancy.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>
    * 常量配置表 服务实现类
    * </p>
*
* @author zwq
* @since 2019-12-06
*/
@Service
public class ConfigParasBiz extends BaseBiz<ConfigParasMapper, ConfigParas>  {

    /**
     * 获取常量配置列表
     * @author zwq
     * @date 2019/12/6 0006
     * @param [moudleId, groupId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> getConfigParasList(String moudleId, String groupId) {
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        if(StringUtils.isEmpty(moudleId)) {
            params.put("moudleId",moudleId);
        }
        params.put("groupId",groupId);
        List<Map<String,Object>> list = mapper.getConfigParasList(params);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 功能描述
     * @author zwq
     * @date 2020/1/2 0002
     * @param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> getGroupIdList(String moudleId) {
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        params.put("moudleId",moudleId);
        List<Map<String,Object>> list = mapper.getGroupIdList(params);
        resultMap.put("list",list);
        return  resultMap;
    }

    public void updateConfigParas(ConfigParas configParas) {

        ConfigParas entity = new ConfigParas();
//        entity.setGroupId(configParas.getGroupId());
//        entity.setMoudleId(configParas.getMoudleId());
        entity.setParaId(configParas.getParaId());
        if(!StringUtils.isEmpty(configParas.getParaName())) {
            entity.setParaId(configParas.getParaName());
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("group_id",configParas.getGroupId());
        queryWrapper.eq("moudle_id",configParas.getMoudleId());

        mapper.update(entity,queryWrapper);
    }
}
