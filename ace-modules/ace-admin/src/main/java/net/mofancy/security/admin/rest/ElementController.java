package net.mofancy.security.admin.rest;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mofancy.security.admin.biz.UserBiz;
import net.mofancy.security.common.msg.ObjectRestResponse;
import net.mofancy.security.common.msg.TableResultResponse;
import net.mofancy.security.admin.biz.ElementBiz;
import net.mofancy.security.admin.entity.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.mofancy.security.common.rest.BaseController;

import java.util.List;

/**
* <p>
    *  前端控制器
    * </p>
*
* @author zhangweiqi
* @since 2019-11-04
*/
@RestController
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz, Element> {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "1") int offset, String name, @RequestParam(defaultValue = "0") int menuId) {
        QueryWrapper<Element> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", menuId);
        if(StringUtils.isNotBlank(name)){
            queryWrapper.like("name", "%" + name + "%");
        }
        List<Element> elements = baseBiz.selectByExample(queryWrapper);
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Element> getAuthorityElement(String menuId) {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "",menuId);
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

    @RequestMapping(value = "/user/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Element> getAuthorityElement() {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "");
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

}
