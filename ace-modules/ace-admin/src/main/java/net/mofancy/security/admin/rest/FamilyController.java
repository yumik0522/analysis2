package net.mofancy.security.admin.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.mofancy.security.admin.biz.FamilyBiz;
import net.mofancy.security.admin.biz.ProjectBiz;
import net.mofancy.security.admin.util.WebUtils;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/family")
public class FamilyController {
	
	
	@Autowired
	private FamilyBiz familyBiz;

	@RequestMapping(value = "/getHierList", method = RequestMethod.GET)
	public ApiResponse getHierList(Integer datasetKey,Integer projectId) {

		Map<String,Object> resultMap = familyBiz.getHierList(projectId);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/getDigitalList", method = RequestMethod.GET)
	public ApiResponse getDigitalList(Integer datasetKey,Integer projectId) {

		Map<String,Object> resultMap = familyBiz.getDigitalList(projectId);

		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value = "/getRecommList", method = RequestMethod.GET)
	public ApiResponse getRecommList(Integer datasetKey,Integer projectId,Integer parentKey) {

		Map<String,Object> resultMap = familyBiz.getRecommList(projectId,parentKey);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/getRecommSaleList", method = RequestMethod.GET)
	public ApiResponse getRecommSaleList(Integer datasetKey,Integer projectId,Integer parentKey,Integer g) {

		Map<String,Object> resultMap = familyBiz.getRecommSaleList(projectId,parentKey,g);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/getSkuList", method = RequestMethod.GET)
	public ApiResponse getSkuList(Integer datasetKey,Integer projectId,Integer solutionKey,Integer groupKey) {

		Map<String,Object> resultMap = familyBiz.getSkuList(projectId,solutionKey,groupKey);

		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value = "/runCluster", method = RequestMethod.POST)
	public ApiResponse runCluster(Integer datasetKey,Integer projectId,Integer deptKey,String deptName,Integer minSale) {
		familyBiz.runCluster(projectId,deptKey,deptName,datasetKey,minSale);
		return ApiResponse.buildSuccess("正在计算,请稍等...");
	}

	@RequestMapping(value = "/runClusterAll", method = RequestMethod.POST)
	public ApiResponse runClusterAll(Integer datasetKey,Integer projectId,Integer minSale) {
		familyBiz.runClusterAll(projectId,datasetKey,minSale);
		return ApiResponse.buildSuccess("正在计算,请稍等...");
	}


	@RequestMapping(value = "/getTop20List", method = RequestMethod.GET)
	public ApiResponse getTopGoodsList(Integer datasetKey,Integer projectId,Integer parentKey) {

		Map<String,Object> resultMap = familyBiz.getTopGoodsList(projectId,parentKey);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/getDistributionList", method = RequestMethod.GET)
	public ApiResponse getDistributionList(Integer datasetKey,Integer projectId,Integer parentKey) {

		Map<String,Object> resultMap = familyBiz.getDistributionList(projectId,parentKey);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/getSolutionsList", method = RequestMethod.GET)
	public ApiResponse getSolutionsList(Integer datasetKey,Integer projectId,Integer solutionKey) {

		Map<String,Object> resultMap = familyBiz.getSolutionsList(projectId,solutionKey);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/updateGroupName", method = RequestMethod.PUT)
	public ApiResponse updateGroupName(Integer datasetKey,Integer projectId,Integer groupKey,String name) {

		familyBiz.updateGroupName(projectId, groupKey, name);

		return ApiResponse.buildSuccess("修改成功");
	}

	@RequestMapping(value = "/deleteSolution", method = RequestMethod.DELETE)
	public ApiResponse deleteSolution(Integer datasetKey,Integer projectId,Integer solutionKey) {
		familyBiz.deleteSolution(projectId,solutionKey);
		return ApiResponse.buildSuccess("删除成功");
	}

	@RequestMapping(value = "/saveClusterSolution", method = RequestMethod.POST)
	public ApiResponse saveClusterSolution(Integer datasetKey,Integer projectId,Integer deptKey,String name,Integer numCluster) {

		if(StringUtils.isEmpty(name)) {
			throw new ParameterIllegalException("商品组合名字不能为空!");
		}

		if(StringUtils.isEmpty(projectId)) {
			throw new ParameterIllegalException("参数projectId不能为空!");
		}

		if(StringUtils.isEmpty(deptKey)) {
			throw new ParameterIllegalException("参数deptKey不能为空!");
		}

		if(StringUtils.isEmpty(numCluster)) {
			throw new ParameterIllegalException("参数numCluster不能为空!");
		}


		familyBiz.saveClusterSolution(projectId,deptKey,name,numCluster);
		return ApiResponse.buildSuccess("保存成功");
	}
}
