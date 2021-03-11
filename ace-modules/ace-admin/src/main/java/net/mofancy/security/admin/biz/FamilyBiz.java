package net.mofancy.security.admin.biz;

import CustomJobs.RunClusterJob;
import CustomJobs.RunYulesQJob;
import QueueManager.RunMultiSqlJob;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.mofancy.security.admin.config.DataSourceConfig;
import net.mofancy.security.admin.config.DataSourceContextHolder;
import net.mofancy.security.admin.jqueue.JobDBSegment;
import net.mofancy.security.admin.jqueue.JqueueSegment;
import net.mofancy.security.admin.mapper.FamilyMapper;
import net.mofancy.security.admin.mapper.ProjectMapper;
import net.mofancy.security.admin.mapper.UserMapper;
import net.mofancy.security.admin.util.JobUtils;
import net.mofancy.security.admin.vo.Goods;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
* <p>
    * 数据池表 服务实现类
    * </p>
*
* @author zwq
* @since 2019-12-06
*/
@Service
@Slf4j
public class FamilyBiz {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private FamilyMapper familyMapper;


    public Map<String,Object> getHierList(Integer projectId) {

        Map<String,Object> resultMap = new HashMap<>();

        List<Map<String,Object>> list = familyMapper.getHierList(projectId);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getDigitalList(Integer projectId) {

        Map<String,Object> resultMap = new HashMap<>();

        List<Map<String,Object>> list = familyMapper.getDigitalList(projectId);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getRecommList(Integer projectId, Integer parentKey) {


        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        params.put("projectId",projectId);
        params.put("parentKey",parentKey);

        List<Map<String,Object>> list = familyMapper.getRecommList(params);

        double sum = 0.0;
        for (Map<String, Object> map : list) {
            sum += Integer.parseInt(map.get("pseudot2_ind").toString());
        }

        for (int i = 0;i<list.size();i++) {
            Map<String,Object> obj = list.get(i);
            BigDecimal bigDecimal = new BigDecimal(obj.get("pseudot2_ind").toString());
            obj.put("pseudot2_ind", bigDecimal.divide(new BigDecimal(sum),3,BigDecimal.ROUND_FLOOR).doubleValue());
            list.set(i, obj);
        }

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getRecommSaleList(Integer projectId, Integer parentKey, Integer g) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        params.put("projectId",projectId);
        params.put("parentKey",parentKey);
        params.put("g",g);

        List<Map<String,Object>> list = familyMapper.getRecommSaleList(params);

        resultMap.put("list",list);

        return resultMap;

    }

    public Map<String,Object> getSkuList(Integer projectId, Integer solutionKey, Integer groupKey) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        if(!StringUtils.isEmpty(projectId)) {
            params.put("projectId", projectId);
        }
        if(!StringUtils.isEmpty(solutionKey)) {
            params.put("solutionKey", solutionKey);
        }
        if(!StringUtils.isEmpty(groupKey)) {
            params.put("groupKey", groupKey);
        }

        List<Map<String,Object>> list = familyMapper.getSkuList(params);

        resultMap.put("list",list);

        return resultMap;
    }

    public void runClusterAll(Integer projectId, Integer datasetKey, Integer minSale) {
        List<Map<String,Object>> list = familyMapper.getHierList(projectId);
        for (Map<String, Object> map : list) {
            Integer deptKey = Integer.parseInt(map.get("prod_sys_key").toString());
            String deptName = map.get("prod_code").toString();
            runCluster(projectId,deptKey,deptName,datasetKey,minSale);
        }
    }

    public void runCluster(Integer projectId,Integer deptKey,String deptName,Integer datasetKey,Integer minSale) {

        String userName = "Qi";
        String checkJob = "0";
        int jobID = dataSourceConfig.getJobId();

        RunClusterJob job = new RunClusterJob(jobID);
        job.setFinishAction("FinishJobAction(" + jobID + ",'Done','runClusterButton','"+deptKey+"')");
        job.setDesc("Cluster " + deptName);
        String proj = projectId.toString();
        String xml = "<projID>" + proj + "</projID>";
        xml += "<dept>" + deptKey + "</dept>";//department的key
        xml += "<matrixDistance>distance</matrixDistance>";//用矩阵里的distance列还是phi_stats列
        xml += "<min>" + 0.0 + "</min>";//最低消费数
        xml += "<max>" + (minSale==0?"":minSale) + "</max>";//最高消费数
        xml += "<prod_cluster_sku>prod_cluster_sku_"+ proj + "</prod_cluster_sku>";
        xml += "<sku_type>PP_P" + proj  + "</sku_type>";
        xml += "<tempPath>temp</tempPath>";//可以输出的temp文件夹（比如C:\temp）
        xml += "<method>auto</method>";
        xml += "<user>" + userName + "</user>";//提交的用户名
        job.setCommand(xml);
        log.info(xml);

        List<Map<String,Object>> dataPoolList = dataSourceConfig.getDataPoolList(datasetKey);

        JqueueSegment jqueue = new JqueueSegment(userName, dataPoolList, projectId+"", "RunCluster", checkJob,job);
        jqueue.runJQueueJob();

    }

    public Map<String,Object> getTopGoodsList(Integer projectId, Integer parentKey) {
        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        params.put("projectId",projectId);
        params.put("parentKey",parentKey);

        List<Map<String,Object>> list = familyMapper.getTopGoodsList(params);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getDistributionList(Integer projectId, Integer parentKey) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        params.put("projectId",projectId);
        params.put("parentKey",parentKey);

        List<Map<String,Object>> list = familyMapper.getDistributionList(params);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getSolutionsList(Integer projectId, Integer solutionKey) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();

        params.put("projectId",projectId);
        params.put("solutionKey",solutionKey);

        List<Map<String,Object>> list = familyMapper.getSolutionsList(params);

        resultMap.put("list",list);

        return resultMap;

    }

    public void updateGroupName(Integer projectId, Integer groupKey, String name) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId", projectId);
        params.put("groupKey", groupKey);
        params.put("name", name);
        familyMapper.updateGroupName(params);
    }

    public void deleteSolution(Integer projectId, Integer solutionKey) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId", projectId);
        params.put("solutionKey", solutionKey);
        familyMapper.deleteClusterProjectParam(params);
        familyMapper.deleteProdClusteredGroup(params);
        familyMapper.deleteProdClusterSku(params);
        familyMapper.deleteProdClusterSku2(params);
        familyMapper.deleteClusterProjectParam2(params);
    }

    public void saveClusterSolution(Integer projectId, Integer deptKey, String name, Integer numCluster) {

        Map<String,Object> params = new HashMap<>();
        params.put("name", name);
        params.put("deptKey", deptKey);
        params.put("numCluster", numCluster);
        params.put("projectId", projectId);
        int count = familyMapper.getClusterProjectParam(params);
        if(count>0) {
            throw new ParameterIllegalException("该商品组合名字已经存在!");
        } else {
            Integer clusterKey = familyMapper.getClusterKeySeq();
            params.put("clusterKey", clusterKey);
            params.put("username", "Qi");
            int result = familyMapper.saveClusterProjectParam(params);
            if(result==0) {
                log.error("保存失败saveClusterProjectParam");
                throw new ParameterIllegalException("保存失败!");
            }
            result = familyMapper.saveProdClusteredGroup(params);
            if(result==0) {
                log.error("保存失败saveProdClusteredGroup");
                throw new ParameterIllegalException("保存失败!");
            }
            result = familyMapper.saveProdClusterSku(params);
            if(result==0) {
                log.error("保存失败saveProdClusterSku");
                throw new ParameterIllegalException("保存失败!");
            }
            result = familyMapper.saveClusterProjectParam2(params);
            if(result==0) {
                log.error("保存失败saveClusterProjectParam2");
                throw new ParameterIllegalException("保存失败!");
            }
        }
    }
}
