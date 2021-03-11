package net.mofancy.security.admin.rest;


import net.mofancy.security.admin.biz.GateLogBiz;
import net.mofancy.security.admin.entity.GateLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import net.mofancy.security.common.rest.BaseController;

/**
* <p>
    *  前端控制器
    * </p>
*
* @author zhangweiqi
* @since 2019-11-04
*/
@RestController
@RequestMapping("gateLog")
public class GateLogController extends BaseController<GateLogBiz, GateLog> {

}
