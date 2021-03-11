package net.mofancy.security.auth.biz;

import net.mofancy.security.auth.entity.Client;
import net.mofancy.security.auth.entity.ClientService;
import net.mofancy.security.auth.mapper.ClientMapper;
import net.mofancy.security.auth.mapper.ClientServiceMapper;
import net.mofancy.security.common.biz.BaseBiz;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* <p>
    *  服务实现类
    * </p>
*
* @author zhangweiqi
* @since 2019-11-04
*/
@Service
public class ClientBiz extends BaseBiz<ClientMapper, Client>  {
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(int id) {
        return mapper.selectAuthorityServiceInfo(id);
    }

    public void modifyClientServices(int id, String clients) {
        clientServiceMapper.deleteById(id);
        if (!StringUtils.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id+"");
                clientServiceBiz.insertSelective(clientService);
            }
        }
    }

}
