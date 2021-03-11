package net.mofancy.security.admin.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mofancy.security.admin.entity.User;
import net.mofancy.security.admin.mapper.MenuMapper;
import net.mofancy.security.admin.mapper.UserMapper;
import net.mofancy.security.common.biz.BaseBiz;
import net.mofancy.security.common.constant.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
* <p>
    *  服务实现类
    * </p>
*
* @author zhangweiqi
* @since 2019-11-05
*/
@Service
public class UserBiz extends BaseBiz<UserMapper, User>  {
    @Autowired
    private MenuMapper menuMapper;
//    @Autowired
//    private UserAuthUtil userAuthUtil;
    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(pre="user{1.username}")
    public void updateSelectiveById(User entity) {
        super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}")
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        return mapper.selectOne(queryWrapper);
    }

}
