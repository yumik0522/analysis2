package net.mofancy.security.admin.rest;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import net.mofancy.security.common.rest.BaseController;
import net.mofancy.security.admin.entity.GroupType;
import net.mofancy.security.admin.biz.GroupTypeBiz;
/**
* <p>
    *  前端控制器
    * </p>
*
* @author zhangweiqi
* @since 2019-11-04
*/
@RestController
@RequestMapping("groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz, GroupType> {

}
