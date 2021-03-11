package net.mofancy.security.admin.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.mofancy.security.admin.biz.FamilyBiz;
import net.mofancy.security.admin.biz.ThemeBiz;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.vo.PageData;
import net.mofancy.security.common.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {
	
	
	@Autowired
	private ThemeBiz themeBiz;

	/**
	 * 主题预算列表
	 * @author zwq
	 * @date 2020/3/14 0014
	 * @param [datasetKey, projectId]
	 * @return net.mofancy.security.common.web.ApiResponse
	 */
	@RequestMapping(value = "/getOperationList", method = RequestMethod.GET)
	public ApiResponse getOperationList(Integer datasetKey,Integer projectId) {

		Map<String,Object> resultMap = themeBiz.getOperationList(projectId);

		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value = "/runFactorAnalysis", method = RequestMethod.POST)
	public ApiResponse runFactorAnalysis(Integer datasetKey,Integer projectId,String analysisName,Integer minSpend,Integer maxSpend,String params) {
		if(StringUtils.isEmpty(analysisName)) {
			throw new ParameterIllegalException("主题分析名不能为空！");
		}
		if(StringUtils.isEmpty(params)) {
			throw new ParameterIllegalException("请至少选择一个商品组合！");
		}
		themeBiz.runFactorAnalysis(datasetKey,projectId,analysisName,minSpend,maxSpend,params);
		return ApiResponse.buildSuccess("正在运算主题分析，请稍等...!");
	}

	@RequestMapping(value ="/getNumFactorList",method = RequestMethod.GET)
	public ApiResponse getNumFactorList(Integer datasetKey, Integer projectId, Integer analysisKey) {
		Map<String,Object> resultMap  = themeBiz.getNumFactorList(projectId,analysisKey);
		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value ="/getDistributionList",method = RequestMethod.GET)
	public ApiResponse getDistributionList(Integer datasetKey, Integer projectId, Integer analysisKey) {
		Map<String,Object> resultMap  = themeBiz.getDistributionList(projectId,analysisKey);
		return ApiResponse.buildSuccess(resultMap);
	}

	/**
	 * 已保存的主题方案
	 * @author zwq
	 * @date 2020/3/19 0019
	 * @param [datasetKey, projectId]
	 * @return net.mofancy.security.common.web.ApiResponse
	 */
	@RequestMapping(value ="/getSaveSolutions",method = RequestMethod.GET)
	public ApiResponse getSaveSolutions(Integer datasetKey, Integer projectId) {
		Map<String,Object> resultMap  = themeBiz.getSaveSolutions(projectId);
		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value ="/purchaseThemes",method = RequestMethod.GET)
	public ApiResponse purchaseThemes(Integer datasetKey,Integer projectId) {
		Map<String,Object> resultMap = themeBiz.purchaseThemes(projectId);
		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value ="/getLoadFactorResult",method = RequestMethod.GET)
	public ApiResponse getLoadFactorResult(Integer datasetKey,Integer projectId,Integer numFactor,Integer analysisKey,String rotation,String includeGroups) {
		Map<String,Object> resultMap = themeBiz.getLoadFactorResult(projectId,numFactor,analysisKey,rotation,includeGroups);
		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value ="/getFactorResult",method = RequestMethod.GET)
	public ApiResponse getFactorResult(Integer datasetKey, Integer projectId, Integer numFactor, Integer analysisKey, String rotation, String includeGroups, @RequestParam(value = "page", defaultValue = "0")Integer pageNum, @RequestParam(value = "limit", defaultValue = "10") Integer pageSize) {

		PageData pageData = themeBiz.getFactorResult(projectId,numFactor,analysisKey,rotation,includeGroups,pageNum,pageSize);
		return ApiResponse.buildSuccess(pageData);
	}

	@RequestMapping(value ="/getFamilyList",method = RequestMethod.GET)
	public ApiResponse getFamilyList(Integer datasetKey,Integer projectId,Integer numFactor,Integer analysisKey,String rotation,String includeGroups,Integer factorInd) {
		Map<String,Object> resultMap =themeBiz.getFamilyList(projectId,numFactor,analysisKey,rotation,includeGroups,factorInd);
		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value ="/deleteFactorSolution",method = RequestMethod.DELETE)
	public ApiResponse deleteFactorSolution(Integer datasetKey,Integer projectId,Integer analysisKey) {

		themeBiz.deleteFactorSolution(projectId, analysisKey);

		return ApiResponse.buildSuccess("删除成功!");
	}

	@RequestMapping(value ="/deleteFactorAnalysis",method = RequestMethod.DELETE)
	public ApiResponse deleteFactorAnalysis(Integer datasetKey,Integer projectId,Integer analysisKey) {

		themeBiz.deleteFactorAnalysis(projectId, analysisKey);

		return ApiResponse.buildSuccess("删除成功!");
	}

	@RequestMapping("/saveFactorName")
	public ApiResponse saveFactorName(Integer datasetKey,Integer projectId,Integer numFactor,Integer analysisKey,String name,String rotation,Integer factorInd,String includeGroups) {
		if(StringUtils.isEmpty(name)) {
			throw new ParameterIllegalException("主题名不能为空!");
		}
		if(StringUtils.isEmpty(factorInd)||factorInd==0) {
			throw new ParameterIllegalException("请选择主题列!");
		}
		themeBiz.saveFactorSolution(projectId, numFactor, analysisKey, rotation,includeGroups);
		themeBiz.saveFactorName(projectId,numFactor,analysisKey,name,rotation,factorInd);


		return ApiResponse.buildSuccess("更新成功!");
	}

	@RequestMapping(value="/getSavedSolutions",method = RequestMethod.GET)
	public ApiResponse getSavedSolutions(Integer datasetKey,Integer projectId) {

		List<Map<String,Object>> list =themeBiz.getSavedSolutions(projectId);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("list",list);
		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value="/getClusterDistribution",method = RequestMethod.GET)
	public ApiResponse getClusterDistribution(Integer datasetKey,Integer projectId,Integer analysisKey) {
		if(StringUtils.isEmpty(analysisKey)||analysisKey==0) {
			throw new ParameterIllegalException("请选择主题方案！");
		}
		List<Map<String,Object>> list = themeBiz.getClusterDistribution(projectId,analysisKey);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("list",list);
		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value="/getDeptDistribution",method = RequestMethod.GET)
	public ApiResponse getDeptDistribution(Integer datasetKey,Integer projectId,Integer analysisKey) {
		if(StringUtils.isEmpty(analysisKey)||analysisKey==0) {
			throw new ParameterIllegalException("请选择主题方案！");
		}
		List<Map<String,Object>> list = themeBiz.getDeptDistribution(projectId,analysisKey);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("list",list);
		return ApiResponse.buildSuccess(resultMap);
	}


	@RequestMapping(value="/getPurchaseThemes",method = RequestMethod.GET)
	public ApiResponse getPurchaseThemes(Integer datasetKey,Integer projectId,Integer parentKey,Integer numFactor) {
		List<Map<String,Object>> list = themeBiz.getPurchaseThemes(projectId,parentKey,numFactor);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("list",list);
		return ApiResponse.buildSuccess(resultMap);
	}
}
