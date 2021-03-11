package net.mofancy.security.admin.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WebUtils {
    
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        // 参数Map  
        Map<String,String[]> properties = request.getParameterMap();  
        // 返回值Map  
        Map<String, String>  returnMap = new HashMap<>();
        Iterator<Entry<String, String[]>>  entries = properties.entrySet().iterator();  
        Entry<String, String[]>  entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = entries.next();
            name =  entry.getKey();
            Object valueObj = entry.getValue();  
            if(null == valueObj){  
                value = "";  
            }else if(valueObj instanceof String[]){  
                String[] values = (String[])valueObj;  
                for(int i=0;i<values.length;i++){  
                    value = values[i] + ",";  
                }  
                value = value.substring(0, value.length()-1);  
            }else{  
                value = valueObj.toString();  
            }  
            returnMap.put(name, value);  
        }  
        return returnMap;  
    }  
    
}
