package net.mofancy.security.admin.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.mofancy.security.admin.biz.DataPoolBiz;
import net.mofancy.security.common.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/datapool")
public class DataPoolController {
	
	
	@Autowired
	private DataPoolBiz dataPoolBiz;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ApiResponse getDataPoolList(@RequestParam(value = "page", defaultValue = "1")Integer pageNo, @RequestParam(value = "limit", defaultValue = "10")Integer pageSize, String dbname) {

		IPage<Map<String,Object>> page = dataPoolBiz.getDataPoolList(dbname,pageNo,pageSize);

		return ApiResponse.buildSuccess(page);
	}
	
}
