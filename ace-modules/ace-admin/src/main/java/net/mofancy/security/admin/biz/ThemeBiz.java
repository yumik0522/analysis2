package net.mofancy.security.admin.biz;

import CustomJobs.RunFactorAnalysisJob;
import FactorAnalysis.FactorAnalysis;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import global.util.Util;
import lombok.extern.slf4j.Slf4j;
import net.mofancy.security.admin.config.DataSourceConfig;
import net.mofancy.security.admin.jqueue.JqueueSegment;
import net.mofancy.security.admin.mapper.ThemeMapper;
import net.mofancy.security.admin.util.JobUtils;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.vo.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
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
public class ThemeBiz {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private ThemeMapper themeMapper;

    /**
     * 主题运算列表
     * @author zwq
     * @date 2020/3/14 0014
     * @param [projectId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> getOperationList(Integer projectId) {

        Map<String,Object> resultMap = new HashMap<>();

        List<Map<String,Object>> list = themeMapper.getOperationList(projectId);

        resultMap.put("list",list);

        return resultMap;
    }

    /**
     * 运算主题分析
     * @author zwq
     * @date 2020/3/14 0014
     * @param [datasetKey, projectId, analysisName, minSpend, maxSpend, params]
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void runFactorAnalysis(Integer datasetKey,Integer projectId,String analysisName,Integer minSpend,Integer maxSpend,String params) {

        JSONObject arr = JSONObject.parseObject(params);
        String keys = arr.getString("keys");
        String codes = arr.getString("codes");
        String groups = arr.getString("groups");
        Map<String,Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("analysisName", analysisName);

        List<Map<String,Object>> list = themeMapper.purchaseThemes(map);
        if(list!=null&&list.size()>0) {
            throw new ParameterIllegalException("运算失败，该主题分析名称已经存在!");
        }
        map.put("analysisName", null);
        map.put("codes", codes);
        map.put("groups", groups);
        list = themeMapper.purchaseThemes(map);
        if(list!=null&&list.size()>0) {
            throw new ParameterIllegalException("运算失败，该主题分析已经存在!");
        }

        int jobID = dataSourceConfig.getJobId();
        RunFactorAnalysisJob job = new RunFactorAnalysisJob(jobID);
        Integer analysisKey = themeMapper.getAnalysisKey();
        String xml = "<projID>" + projectId + "</projID>";
        xml += "<analysis_name>" + analysisName + "</analysis_name>"; //名字从correlationTextBox读取，用户输入
        xml += "<analysis_key>"+analysisKey+"</analysis_key>";//数据库得到nextval('sub_proj_seq')值

        //下面三个是逗号分隔的参数，从product family 列表中选取打钩的。每个department只能有一个product family solution商品簇组合选择。所以如果有很多方案时，命名比较关键，这样可以用过滤filter把想要的全部选出来。表格表头的checkbox是自动·选取每个department的第一商品簇组合
        xml += "<dept_keys>" + keys + "</dept_keys>";//每个department的系统生成的数字key
        xml += "<dept_codes>" + codes + "</dept_codes>";//每个department的原有code
        xml += "<solution_keys>" + groups + "</solution_keys>";//每个选取的商品簇组合的key
        //以上选择全部加到一起有时候可能超过project_param表中param_att或char_val的预设的200字符长度，如果在建库时department过多或code过长。最好办法就是手动把该schema下的这两个字段长度加长。
        xml += "<minSpend>" + minSpend + "</minSpend>";//minSpendTextBox中读取，用户输入，可空，设置消费者最低消费下限
        xml += "<maxSpend>" + maxSpend + "</maxSpend>";//maxSpendTextBox中读取，用户输入，可空，设置消费者最高消费上限
        xml += "<skuCol>prod_sys_key</skuCol>";
        xml += "<deptCol>dept_sys_key</deptCol>";
        xml += "<transaction>tran_proj"+projectId+"</transaction>";
        //只有这个消费范围内的，含边界，才会计算customer profile, 从而得到factore loading table
        xml += "<multi>1</multi>";//是否多线程处理，数据池参数

        job.setCommand(xml);

        List<Map<String,Object>> dataPoolList = dataSourceConfig.getDataPoolList(datasetKey);

        JqueueSegment jqueue = new JqueueSegment("Qi",dataPoolList, projectId+"", "RunFactorAnalysisJob", "0",job);
        jqueue.runJQueueJob();
    }


    public Map<String,Object> getNumFactorList(Integer projectId, Integer analysisKey) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);

        List<Map<String,Object>> list = themeMapper.getNumFactorList(params);

        resultMap.put("list",list);

        return resultMap;

    }

    public Map<String,Object> getDistributionList(Integer projectId, Integer analysisKey) {

        Map<String,Object> resultMap = new HashMap<>();

        List<Map<String,Object>> list = themeMapper.getDistributionList(projectId);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getSaveSolutions(Integer projectId) {

        Map<String,Object> resultMap = new HashMap<>();

        List<Map<String,Object>> list = themeMapper.getSavedSolutions(projectId);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> purchaseThemes(Integer projectId) {

        Map<String,Object> resultMap = new HashMap<>();

        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);

        List<Map<String,Object>> list = themeMapper.purchaseThemes(params);

        resultMap.put("list",list);

        return resultMap;
    }

    public Map<String,Object> getLoadFactorResult(Integer projectId, Integer numFactor, Integer analysisKey, String rotation, String includeGroups) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("numFactor", numFactor);
        params.put("analysisKey", analysisKey);
        params.put("rotation", "ORIGINAL");
        params.put("includeGroups", includeGroups);
        String factorList = "";
        for(int i = 1;i<=numFactor;i++) {
            factorList += "f.factor_"+i+",";
        }
        if(factorList.length()>0) {
            factorList = factorList.substring(0, factorList.length()-1);
        }

        params.put("factorList", factorList);
        List<Map<String,Object>> list = themeMapper.getFactorResult(params);
        if(list!=null&&list.size()<1) {
            throw new ParameterIllegalException("No product families found for this theme analysis");
        }
        int numGroup = list.size();
        List<String> categories = themeMapper.getFactorName(params);


        //把矩阵转到二维数组中，准备做旋转
        double[][] m = new double[numGroup][numFactor];
        double check = 0;
        boolean rotationFlag = true;


        //检查是否有整行都是0的数据，否则无法标准化加旋转
        for (int i = 0;i<list.size();i++) {
            check = 0;
            Map<String,Object> map = list.get(i);
            for(int j=0;j<numFactor;j++) {
                m[i][j] = Util.ntod(map.get("factor_"+(j+1)));
                check += Math.abs(m[i][j]);
            }
            if (check == 0) {
                rotationFlag = false;
            }
        }

        if (rotationFlag) {
            //可旋转时，从rotationDropDownList中读取旋转方式，调用java 类FactorAnalysis
            if ((!Util.strEmpty(rotation)) && (!rotation.equals("ORIGINAL"))){
                FactorAnalysis loadOrig = new FactorAnalysis(m);
                //通过url外部重新给迭代上线次数，默认1000，极偶尔的时候迭代次数会超过1000
                int maxTries = Util.atoi("0");
                if (maxTries > 0) {
                    loadOrig.setMaxTries(maxTries);
                }
                //运行旋转
                loadOrig.Rotations(rotation, numGroup);
                m = loadOrig.getMatrix();

            }
        } else {
            throw new ParameterIllegalException("Rotation not possible as some values are nil");
        }
        List<List<Double>> series = new ArrayList<>();
        for (int i = 0; i < numFactor; i++) {

            List<Double> arrs = new ArrayList<>();

            for (int j = 0; j < numGroup; j++) {
                m[j][i] = (double)Math.round(m[j][i]*100*100)/100;
                if(m[j][i]<20) {
                    continue;
                }
                arrs.add(m[j][i]);
                if(arrs.size()>5) {
                    Collections.sort(arrs,Collections.reverseOrder());
                    arrs.remove(5);

                } else {
                    for(int k = arrs.size();k<5;k++) {
                        arrs.add(0.0);
                    }
                }
            }
            series.add(arrs);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("categories", categories);
        map.put("series", series);

        return map;
    }

    public PageData getFactorResult(Integer projectId, Integer numFactor, Integer analysisKey, String rotation, String includeGroups, Integer pageNo, Integer pageSize) {

        PageHelper.startPage(pageNo, pageSize);

        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("numFactor", numFactor);
        params.put("analysisKey", analysisKey);
        params.put("rotation", "ORIGINAL");
        params.put("includeGroups", includeGroups);
        String factorList = "";
        for(int i = 1;i<=numFactor;i++) {
            factorList += "round(f.factor_"+i+",3)*100 AS factor_"+i+",";
        }
        if(factorList.length()>0) {
            factorList = factorList.substring(0, factorList.length()-1);
        }
        params.put("factorList", factorList);
        List<Map<String,Object>> list = themeMapper.getFactorResult(params);

        PageData pageData=new PageData();
        PageInfo<Map<String,Object>> pageInfo=new PageInfo<>(list);
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPageNum(pageInfo.getPageNum());
        pageData.setPageSize(pageInfo.getPageSize());
        pageData.setDataList(list);

        return pageData;
    }

    public Map<String,Object> getFamilyList(Integer projectId, Integer numFactor, Integer analysisKey, String rotation, String includeGroups, Integer factorInd) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("numFactor", numFactor);
        params.put("analysisKey", analysisKey);
        params.put("rotation", "ORIGINAL");
        params.put("includeGroups", includeGroups);
        String factorList = "";
        for(int i = 1;i<=numFactor;i++) {
            factorList += "f.factor_"+i+",";
        }
        if(factorList.length()>0) {
            factorList = factorList.substring(0, factorList.length()-1);
        }

        params.put("factorList", factorList);
        List<Map<String,Object>> list = themeMapper.getFactorResult(params);
        if(list!=null&&list.size()<1) {
            throw new ParameterIllegalException("No product families found for this theme analysis");
        }
        int numGroup = list.size();


        //把矩阵转到二维数组中，准备做旋转
        double[][] m = new double[numGroup][numFactor];
        double check = 0;
        boolean rotationFlag = true;


        //检查是否有整行都是0的数据，否则无法标准化加旋转
        for (int i = 0;i<list.size();i++) {
            check = 0;
            Map<String,Object> map = list.get(i);
            for(int j=0;j<numFactor;j++) {
                m[i][j] = Util.ntod(map.get("factor_"+(j+1)));
                check += Math.abs(m[i][j]);
            }
            if (check == 0) {
                log.info("check == 0");
                rotationFlag = false;
            }
        }

        if (rotationFlag) {
            //可旋转时，从rotationDropDownList中读取旋转方式，调用java 类FactorAnalysis
            if ((!Util.strEmpty(rotation)) && (!rotation.equals("ORIGINAL"))){
                FactorAnalysis loadOrig = new FactorAnalysis(m);
                //运行旋转
                loadOrig.Rotations(rotation, numGroup);
                m = loadOrig.getMatrix();

            }
        } else {
            throw new ParameterIllegalException("Rotation not possible as some values are nil");
        }
        Set<Double> series = new TreeSet<>();

        List<Double> arrs = new ArrayList<>();
        List<Double> a = new ArrayList<>();
        List<Double> b = new ArrayList<>();
        Map<Double,Integer> seat = new HashMap<>();
        for (int j = 0; j < numGroup; j++) {
            m[j][factorInd] = (double)Math.round(m[j][factorInd]*100*100)/100;
            arrs.add(m[j][factorInd]);
            seat.put(m[j][factorInd], j);
            if(m[j][factorInd]>20) {
                a.add(m[j][factorInd]);
            }
            if(m[j][factorInd]<-20) {
                b.add(m[j][factorInd]);
            }
        }
        Collections.sort(arrs,Collections.reverseOrder());
        series.addAll(a);
        series.addAll(arrs.subList(0, 5));
        series.addAll(b);
        List<Double> results = new ArrayList<>();
        results.addAll(series);
        Collections.sort(results,Collections.reverseOrder());
        Map<String,Object> map = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Integer> groupKeys = new ArrayList<>();
        for (Double d : results) {
            Integer index = seat.get(d);
            categories.add((String)list.get(index).get("group_name"));
            groupKeys.add(Integer.parseInt(list.get(index).get("group_key").toString()));
        }
        map.put("groupKeys", groupKeys);
        map.put("categories", categories);
        map.put("series", results);

        return map;
    }

    public void deleteFactorSolution(Integer projectId, Integer analysisKey) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);
        int count = themeMapper.checkFactorSolution(params);

        if(count<1) {
            themeMapper.deleteCustomerSegments(params);
            themeMapper.deleteCustomerNormalProfile(params);
            themeMapper.deleteProjectParam(params);
            themeMapper.deleteProjectParam2(params);
        }
    }

    public void deleteFactorAnalysis(Integer projectId, Integer analysisKey) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);
        int count = themeMapper.checkSavedSolutions(params);
        if(count>0) {
            throw new ParameterIllegalException("删除失败,该主题分析存在已保存的主题方案！");
        }
        themeMapper.deleteCorrelationVector(params);
        themeMapper.deleteProdClusteredGroup(params);
        themeMapper.deleteProdAssociatedGroup(params);
        try {
            themeMapper.dropCustomerClusterSummary(params);
        } catch (Exception e) {
            themeMapper.deleteCustomerClusterSummary(params);
        }

        themeMapper.deleteClusterFactor2(params);
        themeMapper.deleteProjectParam3(params);
        themeMapper.deleteProjectParam4(params);
        themeMapper.deleteProjectParam5(params);
    }

    public List<Map<String,Object>> getSavedSolutions(Integer projectId) {
        return themeMapper.getSavedSolutions(projectId);
    }

    public List<Map<String,Object>> getClusterDistribution(Integer projectId, Integer analysisKey) {

        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);
        List<Map<String,Object>> list = themeMapper.getClusterDistribution(params);
        List<Map<String,Object>> resultList = new ArrayList<>();
        if(list!=null&&list.size()>0) {
            Map<String,Object> map = list.get(list.size()-1);
            Integer maxNumCust = Integer.parseInt(map.get("num_cust").toString());
            for (int i = 0 ; i<list.size();i++) {
                Map<String, Object> obj = list.get(i);
                Map<String,Object> item = new HashMap<>();
                item.put("stats", ">="+obj.get("stats"));
                item.put("num_cust", obj.get("num_cust"));
                Integer numCust = Integer.parseInt(obj.get("num_cust").toString());
                Integer cust = 0;
                if(i>0) {
                    cust = Integer.parseInt(list.get(i-1).get("num_cust").toString());
                }
                DecimalFormat df = new DecimalFormat("#.00");
                item.put("cust_spend", df.format(numCust*1.0/maxNumCust));
                item.put("spend", df.format(((numCust-cust)*1.0)/maxNumCust));
                resultList.add(item);
            }
        }
        return resultList;
    }

    public List<Map<String,Object>> getDeptDistribution(Integer projectId, Integer analysisKey) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);
        List<Map<String,Object>> list = themeMapper.getDeptDistribution(params);
        List<Map<String,Object>> resultList = new ArrayList<>();
        if(list!=null&&list.size()>0) {
            Map<String,Object> map = list.get(list.size()-1);
            Integer maxNumCust = Integer.parseInt(map.get("num_cust").toString());
            for (int i = 0 ; i<list.size();i++) {
                Map<String, Object> obj = list.get(i);
                Map<String,Object> item = new HashMap<>();
                item.put("stats", ">="+obj.get("stats"));
                item.put("num_cust", obj.get("num_cust"));
                Integer numCust = Integer.parseInt(obj.get("num_cust").toString());
                Integer cust = 0;
                if(i>0) {
                    cust = Integer.parseInt(list.get(i-1).get("num_cust").toString());
                }
                DecimalFormat df = new DecimalFormat("#.00");
                item.put("cust_spend", df.format(numCust*1.0/maxNumCust));
                item.put("spend", df.format(((numCust-cust)*1.0)/maxNumCust));
                resultList.add(item);
            }
        }
        return resultList;
    }

    public List<Map<String,Object>> getPurchaseThemes(Integer projectId, Integer parentKey, Integer numFactor) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("parentKey", parentKey);
        params.put("numFactor", numFactor);
        return themeMapper.getPurchaseThemes(params);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFactorSolution(Integer projectId, Integer numFactor, Integer analysisKey, String rotation, String includeGroups) {
        String factorName =((numFactor < 10) ? " " : "") + numFactor +  " 主题 "+rotation;

        int paramKey = themeMapper.getSubProjSeq();
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("analysisKey", analysisKey);
        params.put("paramKey", paramKey);
        params.put("numFactor", numFactor);
        params.put("rotation", rotation);
        params.put("factorName", factorName);
        params.put("username", "Qi");

        int count = themeMapper.checkFactorSolution2(params);

        if(count>0) {
            return;
        }

        themeMapper.saveFactorSolution(params);

        themeMapper.deleteClusterFactor(params);

        //save the matrix
        params.put("rotation", "ORIGINAL");
        params.put("includeGroups", includeGroups);
        String factorList = "";
        for(int i = 1;i<=numFactor;i++) {
            factorList += "f.factor_"+i+",";
        }
        if(factorList.length()>0) {
            factorList = factorList.substring(0, factorList.length()-1);
        }

        params.put("factorList", factorList);
        List<Map<String,Object>> list = themeMapper.getFactorResult(params);
        int num_group = list.size();
        double[][] m = new double[num_group][numFactor];
        List<Integer> groupKeys = new ArrayList<>();

        for (int j = 0; j < num_group; j++) {
            Map<String,Object> map = list.get(j);
            groupKeys.add(Integer.parseInt(map.get("group_key").toString()));
            for (int i = 0; i < numFactor; i++)
            {

                m[j][i] = Util.ntod((map.get("factor_" + (i + 1)).toString())) / 100d;
            }
        }
        factorList = JobUtils.repeatStr(numFactor,"factor_%s",",");

        String comma = "";
        String query = "";
        for (int j =0 ;j<groupKeys.size() ;j++) {
            query += " "+comma+" SELECT "+projectId+","+paramKey+","+numFactor+",'"+rotation+"',"+groupKeys.get(j);
            for (int i = 0;i<numFactor;i++) {
                query += "," +m[j][i];
            }
            comma = " UNION ";
        }
        params.put("analysisKey", paramKey);
        params.put("factorList", factorList);
        params.put("query", query);
        themeMapper.saveClusterFactor(params);
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveFactorName(Integer projectId, Integer numFactor, Integer analysisKey, String name, String rotation, Integer factorInd) {
        //检测该主题名是否已经存在
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId", projectId);
        params.put("numFactor", numFactor);
        params.put("analysisKey", analysisKey);
        params.put("name", name);
        params.put("username", "Qi");
        params.put("rotation", rotation);
        params.put("factorInd", factorInd);
        int count = themeMapper.checkFactorName(params);
        if(count>0) {
            throw new ParameterIllegalException("该主题名已经存在！");
        }

        if(themeMapper.updateFactorName(params)==0) {
            themeMapper.saveFactorName(params);
        }
    }
}
