package net.mofancy.security.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mofancy.security.auth.bean.ClientInfo;
import net.mofancy.security.auth.entity.Client;
import net.mofancy.security.auth.mapper.ClientMapper;
import net.mofancy.security.auth.service.AuthClientService;
import net.mofancy.security.auth.util.client.ClientTokenUtil;
import net.mofancy.security.common.exception.auth.ClientInvalidException;
import net.mofancy.security.common.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ace on 2017/9/10.
 */
@Service
public class DBAuthClientService implements AuthClientService {
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ClientTokenUtil clientTokenUtil;
    @Autowired
    private DiscoveryClient discovery;
    private ApplicationContext context;

    @Autowired
    public DBAuthClientService(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public String apply(String clientId, String secret) throws Exception {
        Client client = getClient(clientId, secret);
        return clientTokenUtil.generateToken(new ClientInfo(client.getCode(),client.getName(),client.getId().toString()));
    }

    private Client getClient(String clientId, String secret) {
        Client client = new Client();
        client.setCode(clientId);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(client);
        client = clientMapper.selectOne(queryWrapper);
        if(client==null||!client.getSecret().equals(secret)){
            throw new ClientInvalidException("Client not found or Client secret is error!");
        }
        return client;
    }

    @Override
    public void validate(String clientId, String secret) throws Exception {
        Client client = new Client();
        client.setCode(clientId);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(client);
        client = clientMapper.selectOne(queryWrapper);
        if(client==null||!client.getSecret().equals(secret)){
            throw new ClientInvalidException("Client not found or Client secret is error!");
        }
    }

    @Override
    public List<String> getAllowedClient(String clientId, String secret) {
        Client info = this.getClient(clientId, secret);
        List<String> clients = clientMapper.selectAllowedClient(info.getId());
        if(clients==null) {
            new ArrayList<String>();
        }
        return clients;
    }

    @Override
    public List<String> getAllowedClient(int serviceId) {
        Client info = getClient(serviceId);
        List<String> clients = clientMapper.selectAllowedClient(info.getId());
        if(clients==null) {
            new ArrayList<String>();
        }
        return clients;
    }

    private Client getClient(int clientId) {
        Client client = new Client();
        client.setCode(clientId+"");
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(client);
        client = clientMapper.selectOne(queryWrapper);
        return client;
    }

    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void registryClient() {
        // 自动注册节点
        discovery.getServices().forEach((name) ->{
            Client client = new Client();
            client.setName(name);
            client.setCode(name);
            QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(client);
            Client dbClient = clientMapper.selectOne(queryWrapper);
            if(dbClient==null) {
                client.setSecret(UUIDUtils.generateShortUuid());
                clientMapper.insert(client);
            }
        });
    }
}
