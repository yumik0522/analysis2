package net.mofancy.security.admin.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.mofancy.security.admin.biz.DataPoolBiz;
import net.mofancy.security.admin.biz.ProjectBiz;
import net.mofancy.security.admin.util.WebUtils;
import net.mofancy.security.common.exception.common.ParameterIllegalException;
import net.mofancy.security.common.msg.ObjectRestResponse;
import net.mofancy.security.common.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	
	@Autowired
	private ProjectBiz projectBiz;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ApiResponse getProjectList(Integer datasetKey,@RequestParam(value = "page", defaultValue = "1")Integer pageNo,@RequestParam(value = "limit", defaultValue = "10")Integer pageSize,String projectName) {

		IPage<Map<String,Object>> page = projectBiz.getProjectList(datasetKey,pageNo,pageSize,projectName);

		return ApiResponse.buildSuccess(page);
	}

	@RequestMapping(value = "",method = RequestMethod.POST)
	public ApiResponse saveProject(Integer datasetKey,HttpServletRequest request){

		Map<String,String> params = WebUtils.getParameterMap(request);

		if(StringUtils.isEmpty(params.get("projName"))) {
			throw new ParameterIllegalException("沙盘名称不能为空!");
		}

		if(StringUtils.isEmpty(params.get("projDesc"))) {
			throw new ParameterIllegalException("沙盘描述不能为空!");
		}

		if(StringUtils.isEmpty(params.get("period"))) {
			throw new ParameterIllegalException("时间区间不能为空!");
		}

		if(StringUtils.isEmpty(params.get("goodsLevel"))) {
			throw new ParameterIllegalException("商品层级不能为空!");
		}

		if (params.get("projDesc").toString().length() > 500) {
			throw new ParameterIllegalException("沙盘描述长度不能超过500个字符!");
		}

		projectBiz.saveProject(params,datasetKey);

		return ApiResponse.buildSuccess();
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ApiResponse getProjectById(Integer datasetKey,Integer projectId) {
		JSONObject resultMap = new JSONObject();
		List<Map<String,Object>> list = projectBiz.getProjectById(datasetKey,projectId);
		for (Map<String, Object> map : list) {
			resultMap.put(map.get("param_name").toString(),map.get("param_info"));
		}

		return ApiResponse.buildSuccess(resultMap);
	}

	@RequestMapping(value = "/getDeptList", method = RequestMethod.GET)
	public ApiResponse getDeptList(Integer datasetKey,@RequestParam(value = "page", defaultValue = "1")Integer pageNo,@RequestParam(value = "limit", defaultValue = "10") Integer pageSize,Integer projectId) {
		IPage<Map<String,Object>> page = projectBiz.getDeptList(pageNo,pageSize,projectId);
		return ApiResponse.buildSuccess(page);
	}

	@RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
	public ApiResponse getGoodsList(Integer datasetKey,Integer projectId,@RequestParam(value = "page", defaultValue = "1")Integer pageNo,@RequestParam(value = "limit", defaultValue = "20")Integer pageSize,String prodCode,String prodDesc,String parentCode,String parentDesc,String sort) {
		IPage<Map<String,Object>> page = projectBiz.getGoodsList(projectId,pageNo,pageSize,prodCode,prodDesc,parentCode,parentDesc,sort);
		return ApiResponse.buildSuccess(page);
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.DELETE)
	public ApiResponse deleteProject(HttpServletRequest request,Integer projectId,Integer datasetKey) {

		if(StringUtils.isEmpty(projectId)) {
			throw new ParameterIllegalException("参数projectId不能为空!");
		}

		projectBiz.deleteProject(projectId,datasetKey);
		return ApiResponse.buildSuccess("删除成功");
	}
	
}
