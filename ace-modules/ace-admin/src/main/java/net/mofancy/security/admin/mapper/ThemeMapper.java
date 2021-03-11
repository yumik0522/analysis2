package net.mofancy.security.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ThemeMapper extends BaseMapper {

    List<Map<String,Object>> getOperationList(Integer projectId);

    List<Map<String,Object>> purchaseThemes(Map<String, Object> map);

    @Select("SELECT nextval('sub_proj_seq')")
    Integer getAnalysisKey();

    List<Map<String,Object>> getNumFactorList(Map<String,Object> params);

    List<Map<String,Object>> getDistributionList(Integer projectId);

    List<Map<String,Object>> getSavedSolutions(Integer projectId);

    List<Map<String,Object>> getFactorResult(Map<String, Object> params);

    List<String> getFactorName(Map<String, Object> params);

    int checkFactorSolution(Map<String, Object> params);

    void deleteCustomerSegments(Map<String, Object> params);

    void deleteCustomerNormalProfile(Map<String, Object> params);

    void deleteProjectParam(Map<String, Object> params);

    void deleteProjectParam2(Map<String, Object> params);

    int checkSavedSolutions(Map<String, Object> params);

    void deleteCorrelationVector(Map<String, Object> params);

    void deleteProdClusteredGroup(Map<String, Object> params);

    void deleteProdAssociatedGroup(Map<String, Object> params);

    void dropCustomerClusterSummary(Map<String, Object> params);

    void deleteCustomerClusterSummary(Map<String, Object> params);

    void deleteClusterFactor2(Map<String, Object> params);

    void deleteProjectParam3(Map<String, Object> params);

    void deleteProjectParam4(Map<String, Object> params);

    void deleteProjectParam5(Map<String, Object> params);

    List<Map<String,Object>> getClusterDistribution(Map<String, Object> params);

    List<Map<String,Object>> getDeptDistribution(Map<String, Object> params);

    List<Map<String,Object>> getPurchaseThemes(Map<String, Object> params);

    int getSubProjSeq();

    int checkFactorSolution2(Map<String, Object> params);

    void saveFactorSolution(Map<String, Object> params);

    void deleteClusterFactor(Map<String, Object> params);

    void saveClusterFactor(Map<String, Object> params);

    int checkFactorName(Map<String, Object> params);

    int updateFactorName(Map<String, Object> params);

    void saveFactorName(Map<String, Object> params);
}
