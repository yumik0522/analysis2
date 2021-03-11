package net.mofancy.security.admin.mapper;

import net.mofancy.security.admin.entity.ResourceAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-05
 */
public interface ResourceAuthorityMapper extends BaseMapper<ResourceAuthority> {
    public void deleteByAuthorityIdAndResourceType(@Param("authorityId")String authorityId, @Param("resourceType") String resourceType);

}
