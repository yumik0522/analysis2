package net.mofancy.security.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.mofancy.security.admin.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-05
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> selectMemberByGroupId(int groupId);

    List<User> selectLeaderByGroupId(int groupId);
}
