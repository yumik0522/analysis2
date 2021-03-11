package net.mofancy.security.admin.biz;

import CustomJobs.RunYulesQJob;
import QueueManager.RunMultiSqlJob;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.mofancy.security.admin.config.DataSourceConfig;
import net.mofancy.security.admin.config.DataSourceContextHolder;
import net.mofancy.security.admin.jqueue.JobDBSegment;
import net.mofancy.security.admin.jqueue.JqueueSegment;
import net.mofancy.security.admin.mapper.ProjectMapper;
import net.mofancy.security.admin.util.JobUtils;
import net.mofancy.security.admin.vo.Goods;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
* <p>
    * 仪表盘表 服务实现类
    * </p>
*
* @author zwq
* @since 2019-12-06
*/
@Service
public class ProjectBiz {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private ProjectMapper projectMapper;


    public IPage<Map<String,Object>> getProjectList(Integer id, Integer pageNo,Integer pageSize,String projectName) {

        Page< Map<String,Object>> page = new Page<>(pageNo, pageSize);

        List<Map<String,Object>> list = new ArrayList<>();

        Set<String> set=DataSourceConfig.dbMap.keySet();
        for (String s : set) {
            if(id!=null&&!id.toString().equals(s)) {
                continue;
            }
            DataSourceContextHolder.setDataSource(DataSourceConfig.dbMap.get(s));
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("datasetKey", s);
            params.put("datasetName", DataSourceConfig.dbMap.get(s).toString());
            if(!StringUtils.isEmpty(projectName)) {
                params.put("projectName", projectName);
            }
            list.addAll(projectMapper.getProjectList(params));
        }

        long total = 0;
        if(id==null) {
            total = list.size();
            int start = (pageNo-1)*pageSize;
            start = start<0?0:start;
            int end = pageNo*pageSize;
            list = list.subList(start, end>list.size()?list.size():end);
        }

        page.setRecords(list);
        page.setTotal(total);

        return page;
    }

    public void saveProject(Map<String, String> map, Integer datasetKey) {
        String projName = map.get("projName").toString(); //相当于newProjFromPoolNameTextBox的值
        String projDesc = map.get("projDesc").toString(); //相当于newProjFromPoolDescTextBox的值
        String minCust = "0";//map.get("minCust").toString();   //相当于newProjFromPoolMinCustDropDownList的值
        String minSale = "0.001";//map.get("minSale").toString();   //相当于newProjFromPoolMinSaleDropDownList的值
        String period = map.get("period").toString();
        String goodsLevel = map.get("goodsLevel").toString();
        //检测该project_name是否存在
        if(projectMapper.checkProjectExist(projName)>0) {
            //该沙盘名字已存在!
            throw new ParameterIllegalException("该沙盘名字已存在!");
        }

        //新增沙盘
        Map<String,Object> params = new HashMap<>();
        int projectId = projectMapper.getProjectId();

        ArrayList<String[]> p = new ArrayList<>();
        p = JobUtils.AddNewProjParam(p, "Period", "", period, "");
//		p = JobUtils.AddNewProjParam(p, "", "Customers", "Customers", "All cusomters");
        p = JobUtils.AddNewProjParam(p, "PseudoDept", "TRIPPS department", goodsLevel, "Hier1");
        p = JobUtils.AddNewProjParam(p, "PseudoProd", "TRIPPS product", "1", "SKUs");
        p = JobUtils.AddNewProjParam(p, "Restrict", "Restrict by", "#ALL", "All");
        p = JobUtils.AddNewProjParam(p, "Store", "Stores", "#ALL", "All Stores");
        p = JobUtils.AddNewProjParam(p, "MinCust", "Product matrix threshold: customers", minCust, minCust);
        p = JobUtils.AddNewProjParam(p, "MinSale", "Product matrix threshold: sales", minSale, minSale);
        projectMapper.deleteProjectParam(projectId);
        for (String[] entry : p) {
            params = new HashMap<>();
            params.put("createdBy", "Qi");
            params.put("projectId", projectId);
            if(!StringUtils.isEmpty(entry[3])) {
                params.put("paramType", "PROJ_SETUP");
                params.put("paramName", entry[1]);
                params.put("charVal", entry[3]);
                projectMapper.saveNewProjectParam(params);
            }
            if(!StringUtils.isEmpty(entry[0])) {
                params.put("paramType", "NEW_PROJ");
                params.put("paramName", entry[0]);
                params.put("charVal", entry[2]);
                projectMapper.saveNewProjectParam(params);
            }
        }

        List<Map<String,Object>> dataPoolList = dataSourceConfig.getDataPoolList(datasetKey);

        int jobID = dataSourceConfig.getJobId();
        int multiProcess = -1;
        RunMultiSqlJob job = new RunMultiSqlJob(jobID);
        JqueueSegment jqueue = new JqueueSegment("Qi", dataPoolList, projectId+"", "createNewProjectProdSummary", "0",job);
        JobDBSegment jobDBSegment = new JobDBSegment(jqueue);

        jobDBSegment.createNewProjectBase(projectId,"LFS");
        jobDBSegment.createNewProjectLFSFromTran(projectId, projName, projDesc, multiProcess);
        jobDBSegment.createNewProject(projectId, multiProcess);
        jobDBSegment.createProdSummary(projectId, multiProcess);

        String prod_matrix = "prod_matrix_" + projectId;
        String deptCol = "dept_sys_key";
        String skuCol = "prod_sys_key";
        String deptType = "C_P" + projectId;
        String skuType = "PP_P" + projectId;
        String xml = "<projID>" + projectId + "</projID>";
        xml += "<deptID>-1</deptID>";
        xml += "<deptCode>-1</deptCode>";
        xml += "<sample>ALL</sample>";
        xml += "<minCust>" + minCust + "</minCust>";
        xml += "<minSale>" + minSale + "</minSale>";
        xml += "<transaction>tran_proj" + projectId + "_#dept_key#</transaction>";
        xml += "<prod_matrix>" + prod_matrix + "</prod_matrix>";
        xml += "<deptType>" + deptType + "</deptType>";
        xml += "<skuType>" + skuType + "</skuType>";
        xml += "<deptCol>" + deptCol + "</deptCol>";
        xml += "<skuCol>" + skuCol + "</skuCol>";
        xml += "<tempPath>temp</tempPath>";
        xml += "<user>Qi</user>";

        jobID = dataSourceConfig.getJobId();
        RunYulesQJob yq_job = new RunYulesQJob(jobID);
        yq_job.setDesc("Run Product Matrix for All");
        yq_job.setCommand(xml);

        jqueue = new JqueueSegment("Qi",dataPoolList, projectId+"", projName, "0",yq_job);
        jqueue.runJQueueJob();
    }

    public List<Map<String,Object>> getProjectById(Integer datasetKey, Integer projectId) {

        String datasetName = DataSourceConfig.dbMap.get(datasetKey.toString()).toString();

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("datasetName", datasetName);
        params.put("projectId", projectId);
        return projectMapper.getProjectById(params);
    }

    public IPage<Map<String,Object>> getDeptList(Integer pageNo, Integer pageSize, Integer projectId) {

        Page< Map<String,Object>> page = new Page<>(pageNo, pageSize);

        List< Map<String,Object>> list = projectMapper.getDeptList(page,projectId);

        page.setRecords(list);

        return page;
    }

    public IPage<Map<String,Object>> getGoodsList(Integer projectId,Integer pageNo, Integer pageSize,String prodCode,String prodDesc,String parentCode,String parentDesc,String sort) {

        Page< Map<String,Object>> page = new Page<>(pageNo, pageSize);

        Goods goods = new Goods();

        if(!StringUtils.isEmpty(prodCode)) {
            goods.setProdCode(prodCode);
        }
        if(!StringUtils.isEmpty(prodDesc)) {
            goods.setProdDesc(prodDesc);
        }
        if(!StringUtils.isEmpty(parentCode)) {
            goods.setParentCode(parentCode);
        }
        if(!StringUtils.isEmpty(parentDesc)) {
            goods.setParentDesc(parentDesc);
        }
        if(!StringUtils.isEmpty(projectId)) {
            goods.setProjectId(projectId);
        }

        if(!StringUtils.isEmpty(sort)) {
            goods.setSort(sort);
        } else {
            goods.setSort("1");
        }

        List< Map<String,Object>> list = projectMapper.getGoodsList(page,goods);

        page.setRecords(list);

        return page;
    }

    public void deleteProject(Integer projectId, Integer datasetKey) {

        String[] params = {"CustomerSegmentsTable","CustomerClusterSummaryTable","CustomerNormalProfileTable","CustomerSummaryTable","ProductMatrixTable","ProductSegmentSummaryTable","ProdClusterSKUTable","CustomerDeploySegmentsTable"};
        String[] proj_dt = new String[params.length];
        for (int i = 0;i<params.length; i++) {
            String charVal = null;
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("paramName",params[i]);
            map.put("projectId",projectId);
            map = projectMapper.getProjectParam(map);
            if(map!=null) {
                charVal = map.get("char_val").toString();
            }
            proj_dt[i] = charVal;
        }

        int jobID = dataSourceConfig.getJobId();
        int multiProcess = 1;
        RunMultiSqlJob job = new RunMultiSqlJob(jobID);

        List<Map<String,Object>> dataPoolList = dataSourceConfig.getDataPoolList(datasetKey);

        JqueueSegment jqueue = new JqueueSegment("Qi", dataPoolList, projectId+"", "deleteProject", "0",job);
        JobDBSegment jobDBSegment = new JobDBSegment(jqueue);

        jobDBSegment.deleteProject(projectId,proj_dt,multiProcess);
    }
}
