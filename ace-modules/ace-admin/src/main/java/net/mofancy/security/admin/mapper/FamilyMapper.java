package net.mofancy.security.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface FamilyMapper extends BaseMapper {

    List<Map<String,Object>> getHierList(Integer projectId);

    List<Map<String,Object>> getDigitalList(Integer projectId);

    List<Map<String,Object>> getRecommList(Map<String,Object> params);

    List<Map<String,Object>> getRecommSaleList(Map<String,Object> params);

    List<Map<String,Object>> getSkuList(Map<String, Object> params);

    List<Map<String,Object>> getTopGoodsList(Map<String, Object> params);

    List<Map<String,Object>> getDistributionList(Map<String, Object> params);

    List<Map<String,Object>> getSolutionsList(Map<String, Object> params);

    int updateGroupName(Map<String, Object> params);

    int deleteClusterProjectParam(Map<String, Object> params);

    int deleteProdClusteredGroup(Map<String, Object> params);

    int deleteProdClusterSku(Map<String, Object> params);

    int deleteProdClusterSku2(Map<String, Object> params);

    int deleteClusterProjectParam2(Map<String, Object> params);

    int getClusterProjectParam(Map<String, Object> params);

    Integer getClusterKeySeq();

    int saveClusterProjectParam(Map<String, Object> params);

    int saveProdClusteredGroup(Map<String, Object> params);

    int saveProdClusterSku(Map<String, Object> params);

    int saveClusterProjectParam2(Map<String, Object> params);
}
