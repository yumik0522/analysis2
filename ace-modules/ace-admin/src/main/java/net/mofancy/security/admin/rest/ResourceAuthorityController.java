package net.mofancy.security.admin.rest;


import net.mofancy.security.admin.biz.ResourceAuthorityBiz;
import net.mofancy.security.admin.entity.ResourceAuthority;
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
@RequestMapping("resourceAuthority")
public class ResourceAuthorityController extends BaseController<ResourceAuthorityBiz, ResourceAuthority> {

}
