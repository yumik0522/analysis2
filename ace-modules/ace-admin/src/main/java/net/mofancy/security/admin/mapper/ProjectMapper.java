package net.mofancy.security.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.mofancy.security.admin.vo.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectMapper extends BaseMapper {

	List<Map<String,Object>> getProjectList(Map<String, Object> params);

    int getProjectId();

    void deleteProjectParam(int projectId);

    void saveNewProjectParam(Map<String, Object> params);

    int checkProjectExist(String projName);

    List<Map<String,Object>> getProjectById(Map<String, Object> params);

    List<Map<String,Object>> getDeptList(Page<Map<String, Object>> page, @Param("projectId") Integer projectId);

    List<Map<String,Object>> getGoodsList(Page<Map<String, Object>> page,@Param("goods") Goods godos);

    Map<String,Object> getProjectParam(Map<String, Object> map);
}
