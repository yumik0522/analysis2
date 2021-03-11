package net.mofancy.security.admin.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.mofancy.security.admin.config.DataSourceConfig;
import net.mofancy.security.admin.config.DataSourceContextHolder;
import net.mofancy.security.admin.mapper.DataPoolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class DataPoolBiz   {

    @Autowired
    private DataPoolMapper dataPoolMapper;


    public IPage<Map<String,Object>> getDataPoolList(String dbname, Integer pageNo, Integer pageSize) {

        List<Map<String,Object>> list = new ArrayList<>();
        Set<String> set= DataSourceConfig.dbMap.keySet();
        int index = 0,start = (pageNo-1)*pageSize+1,end = pageNo*pageSize ;
        for (String s : set) {
            if(!StringUtils.isEmpty(dbname)) {
                if(DataSourceConfig.dbMap.get(s).toLowerCase().indexOf(dbname.toLowerCase())<0) {
                    continue;
                }
            }
            index++;
            if(index<start) {
                continue;
            }
            if(index>end) {
                break;
            }
            DataSourceContextHolder.setDataSource(DataSourceConfig.dbMap.get(s));
            Map<String,Object> params = new HashMap<>();
            params.put("datasetKey", s);
            params.put("datasetName", DataSourceConfig.dbMap.get(s).toString());
            list.addAll(dataPoolMapper.selectDataPoolList(params));

        }
        long totalCount = DataSourceConfig.dbMap.size();

        Page< Map<String,Object>> page = new Page<>(pageNo, pageSize);
        page.setTotal(totalCount);
        page.setRecords(list);

        return page;
    }
}
