package net.mofancy.security.auth.controller;


import net.mofancy.security.auth.biz.ClientBiz;
import net.mofancy.security.auth.entity.Client;
import net.mofancy.security.auth.entity.ClientService;
import net.mofancy.security.common.msg.ObjectRestResponse;
import net.mofancy.security.common.rest.BaseController;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-04
 */
@RestController
@RequestMapping("service")
public class ServiceController extends BaseController<ClientBiz,Client> {
    @RequestMapping(value = "/{id}/client", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable int id, String clients){
        baseBiz.modifyClientServices(id, clients);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ClientService> getUsers(@PathVariable int id){
        return new ObjectRestResponse<ClientService>().rel(true).data(baseBiz.getClientServices(id));
    }
}
