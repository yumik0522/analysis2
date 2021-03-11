package net.mofancy.security.admin.mapper;

import java.util.List;
import java.util.Map;

public interface DataPoolMapper {

	List<Map<String,Object>> selectDataPoolList(Map<String, Object> params);

}
