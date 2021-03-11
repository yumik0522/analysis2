package net.mofancy.security.admin.rest;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mofancy.security.admin.biz.ResourceAuthorityBiz;
import net.mofancy.security.admin.constant.AdminCommonConstant;
import net.mofancy.security.admin.vo.AuthorityMenuTree;
import net.mofancy.security.admin.vo.GroupTree;
import net.mofancy.security.admin.vo.GroupUsers;
import net.mofancy.security.common.msg.ObjectRestResponse;
import net.mofancy.security.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.mofancy.security.common.rest.BaseController;
import net.mofancy.security.admin.entity.Group;
import net.mofancy.security.admin.biz.GroupBiz;

import java.util.ArrayList;
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
@RequestMapping("group")
public class GroupController extends BaseController<GroupBiz, Group> {
    @Autowired
    private ResourceAuthorityBiz resourceAuthorityBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Group> list(String name, String groupType) {
        if(StringUtils.isBlank(name)&& StringUtils.isBlank(groupType)) {
            return new ArrayList<Group>();
        }
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            queryWrapper.eq("groupType", groupType);
        }
        return baseBiz.selectByExample(queryWrapper);
    }



    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifiyUsers(@PathVariable int id, String members, String leaders){
        baseBiz.modifyGroupUsers(id, members, leaders);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<GroupUsers> getUsers(@PathVariable int id){
        return new ObjectRestResponse<GroupUsers>().rel(true).data(baseBiz.getGroupUsers(id));
    }

    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthority(@PathVariable  int id, String menuTrees){
        String [] menus = menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id, menus);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthority(@PathVariable  int id){
        return new ObjectRestResponse().data(baseBiz.getAuthorityMenu(id)).rel(true);
    }

    @RequestMapping(value = "/{id}/authority/element/add", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse addElementAuthority(@PathVariable  int id,int menuId, int elementId){
        baseBiz.modifyAuthorityElement(id,menuId,elementId);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/authority/element/remove", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse removeElementAuthority(@PathVariable int id,int menuId, int elementId){
        baseBiz.removeAuthorityElement(id,menuId,elementId);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/authority/element", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Integer>> getElementAuthority(@PathVariable  int id){
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id)).rel(true);
    }


    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<GroupTree> tree(String name, String groupType) {
        if(StringUtils.isBlank(name)&&StringUtils.isBlank(groupType)) {
            return new ArrayList<GroupTree>();
        }
        Group group = new Group();
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            group.setGroupType(Integer.parseInt(groupType));
        }
        queryWrapper.setEntity(group);
        return  getTree(baseBiz.selectByExample(queryWrapper), AdminCommonConstant.ROOT);
    }


    private List<GroupTree> getTree(List<Group> groups,int root) {
        List<GroupTree> trees = new ArrayList<GroupTree>();
        GroupTree node = null;
        for (Group group : groups) {
            node = new GroupTree();
            node.setLabel(group.getName());
            BeanUtils.copyProperties(group, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees,root) ;
    }


}
