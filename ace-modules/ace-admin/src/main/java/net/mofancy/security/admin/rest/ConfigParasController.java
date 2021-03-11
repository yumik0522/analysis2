package net.mofancy.security.admin.rest;


import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.web.ApiResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import net.mofancy.security.common.rest.BaseController;
import net.mofancy.security.admin.entity.ConfigParas;
import net.mofancy.security.admin.biz.ConfigParasBiz;

import java.util.Map;

/**
* <p>
    * 常量配置表 前端控制器
    * </p>
*
* @author zwq
* @since 2019-12-06
*/
@RestController
@RequestMapping("configparas")
public class ConfigParasController extends BaseController<ConfigParasBiz, ConfigParas> {

    /**
     * 获取常量配置列表
     * @author zwq
     * @date 2019/12/6 0006
     * @param [moudleId, groupId]
     * @return net.mofancy.security.common.web.ApiResponse
     */
    @RequestMapping(value = "/getConfigParasList", method = RequestMethod.GET)
    public ApiResponse getConfigParasList(String moudleId,String groupId) {
        if(StringUtils.isEmpty(groupId)) {
            throw new ParameterIllegalException("groupId不能为空");
        }
        Map<String,Object> resultMap = baseBiz.getConfigParasList(moudleId,groupId);
        return ApiResponse.buildSuccess(resultMap);
    }

    /**
     * 获取常量组groupId
     * @author zwq
     * @date 2020/1/2 0002
     * @param [moudleId]
     * @return net.mofancy.security.common.web.ApiResponse
     */
    @RequestMapping(value = "/getGroupIdList", method = RequestMethod.GET)
    public ApiResponse getGroupIdList(String moudleId) {
        Map<String,Object> resultMap = baseBiz.getGroupIdList(moudleId);
        return ApiResponse.buildSuccess(resultMap);
    }


    @RequestMapping(value = "/updateConfigParas", method = RequestMethod.PUT)
    public ApiResponse updateConfigParas(@RequestBody ConfigParas configParas) {

        if(StringUtils.isEmpty(configParas.getGroupId())) {
            throw new ParameterIllegalException("groupId不能为空！");
        }

        if(StringUtils.isEmpty(configParas.getMoudleId())) {
            throw new ParameterIllegalException("moudleId不能为空！");
        }

        if(StringUtils.isEmpty(configParas.getParaId())) {
            throw new ParameterIllegalException("paraId不能为空！");
        }

        baseBiz.updateConfigParas(configParas);
        return ApiResponse.buildSuccess();
    }

}
