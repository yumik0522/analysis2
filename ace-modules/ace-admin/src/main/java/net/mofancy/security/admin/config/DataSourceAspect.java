package net.mofancy.security.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Before("execution(* net.mofancy.security.admin.rest.*.*(..))")
    public void beforeSwitchDS(JoinPoint point){

        //是否拦截
        boolean flag = false;
        //获得访问的方法名
        List<String> list = getIgnorePathPatterns();
        for (String declaringTypeName : list) {
            if(declaringTypeName.equals(point.getSignature().getDeclaringTypeName())) {
                flag = true;
            }
        }

        if(flag) {
            return;
        }

        try {
            int datasetKey = getMethodInfo(point);
            if(datasetKey!=0){
                String datasetName = DataSourceConfig.dbMap.get(datasetKey+"");
                if(!datasetName.equals(DataSourceContextHolder.getDataSource())) {
                    DataSourceContextHolder.setDataSource(DataSourceConfig.dbMap.get(datasetKey+""));
                }

            } else {
                DataSourceContextHolder.setDataSource(DataSourceContextHolder.DEFAULT_DS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @After("execution(* net.mofancy.security.admin.rest.*.*(..))")
    public void afterSwitchDS(){
        DataSourceContextHolder.clearDataSource();
    }

    private int getMethodInfo(JoinPoint point) {
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        int datasetKey = 0;
        if (Objects.nonNull(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                String value = point.getArgs()[i] != null ? point.getArgs()[i].toString() : "null";
                if("datasetKey".equals(parameterNames[i])&&!"null".equals(value)) {
                    datasetKey = Integer.parseInt(value);
                    break;
                }
            }

        }
        return datasetKey;
    }

    /**
     * 需要用户和服务认证判断的路径
     * @return
     */
    private ArrayList<String> getIgnorePathPatterns() {

        String prefix = "net.mofancy.security.admin.rest.";

        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                prefix+"MainController",
                prefix+"LoginController",
                prefix+"DataPoolController"
        };
        Collections.addAll(list, urls);
        return list;
    }
}
