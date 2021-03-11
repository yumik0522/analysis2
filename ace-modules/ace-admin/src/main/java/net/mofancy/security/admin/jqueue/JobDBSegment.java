package net.mofancy.security.admin.jqueue;

import QueueManager.Job;
import QueueManager.RunMultiSqlJob;
import net.mofancy.security.admin.util.JobUtils;
import org.springframework.util.StringUtils;

public class JobDBSegment {
	
	private JqueueSegment jqueue;
	
	public JobDBSegment (JqueueSegment jqueue) {
		this.jqueue = jqueue;
	}

	public boolean createNewProjectBase(int projectId, String prrojType) {
		String query = "/*CreateNewProjCustomer*/SELECT create_new_proj_customer(" + projectId + ",'" + prrojType + "',#jobid#);";
		String jobName = "CreateNewProjCustomer";
		String jobDesc = "New Project " + projectId + " customer";
		runJQueueJobMultiSql(jobName, jobDesc,query, false);
		query = "/*CreateNewProjProduct*/SELECT create_new_proj_product(" + projectId + ",#jobid#);";
		jobName = "CreateNewProjProduct";
		jobDesc = "New Project " + projectId + " product";
		return runJQueueJobMultiSql(jobName, jobDesc,query, false);
	}
	
	public boolean createNewProjectLFSFromTran(int projectId, String projName, String projDesc, int multiProcess) {
		String query = "/*createNewProjectLFSFromTran*/SELECT create_new_proj_LFS_tran(" + projectId + ",'" + projName + "','" + projDesc + "'," + multiProcess + ",#jobid#);";
		String jobName = "createNewProjectLFSFromTran";
		String jobDesc = "New Lifestyle segmentation " + projectId + " from pool";
		return runJQueueJobMultiSql(jobName, jobDesc,query, false);
	}
	
	public boolean createNewProjectBasketTran(int projectId,String projName,String projDesc,int multiProcess) {
		String query = "/*createNewProjectBasketTran*/SELECT create_new_proj_Basket_tran(" + projectId + ",'" + projName+ "','" + projDesc + "'," + multiProcess + ",#jobid#);";
		return runJQueueJobMultiSql( "createNewProjectBasketTran", "New Basket Mission " + projectId + " from pool",query,false);
	}
	
	public boolean createNewProject(int projectId, int multiProcess) {
		String query = "/*createNewProject*/SELECT create_new_proj(" + projectId + "," + multiProcess + ",#jobid#);";
		runJQueueJobMultiSql("createNewProject", "", query,false);
		query = "/*createNewProjectCustomerSummary*/SELECT customer_summary(" + projectId + "," + multiProcess + ",#jobid#);";
		return runJQueueJobMultiSql("createNewProjectCustomerSummary", "createNewProjectCustomerSummary",query, false);
	}
	
	public boolean createProdSummary(int projectId, int multiProcess) {
		String query = "/*createNewProjectProdSummary*/SELECT product_summary(" + projectId + "," + multiProcess + ",#jobid#);";
		if (jqueue.getJob() instanceof RunMultiSqlJob) {
			return runJQueueJobMultiSql("createNewProjectProdSummary", "Product summary",query,true);
		} else {
			return runJQueueJob("createNewProjectProdSummary", "Product summary " + projectId,query);
		}
	}
	
	public boolean deleteProject(int projectId,String []proj_dt, int multiProcess) {
		
		String[] tables = { "cluster_factor", "correlation_vector", "customer_segment_summary", "prod_cluster", "prod_clustered_group", "segment_cluster_mean", "prod_associated_group" };
		
		String query = "do  $$/*deleteProject*/" +
				"\n DECLARE" +
				"\n   v_proj int:=" + projectId + ";" +
				"\n   v_rowcount int;" +
				"\n BEGIN";
		for (int i = 0; i < proj_dt.length; i++) {
			if(proj_dt[i]!=null) {
				query += "\n DROP TABLE IF EXISTS " + proj_dt[i] + " CASCADE;" + 
						"\n PERFORM logMsg('Dropped project table:'||'" + proj_dt[i] + "','deleteProject',-1);";
			}
			
		}
		for (String t : tables) {
			query += "\n DELETE FROM " + t + " WHERE proj=v_proj;";
		}
		query += "\n DELETE FROM project_param WHERE project_id=v_proj;" +
				"\n DELETE FROM project WHERE project_id=v_proj;" +
				"\n DELETE FROM product_cat WHERE prod_type='C_P'||v_proj;" +
				"\n GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n IF v_rowcount>0 THEN" +
				"\n   DELETE FROM product_sku WHERE prod_type='PP_P'||v_proj;" +
				"\n   DROP TABLE IF EXISTS customer_proj" + projectId + " CASCADE;" +
				"\n   DROP TABLE IF EXISTS tran_proj" + projectId + " CASCADE;" +
				"\n   DROP TABLE IF EXISTS tran_rest_proj" + projectId + " CASCADE;" +
				"\n   DROP TABLE IF EXISTS prod_sku_proj" + projectId + " CASCADE;" +
				"\n   DROP TABLE IF EXISTS transaction_lookup_proj" + projectId + " CASCADE;" +
				"\n END IF;" +
				"\n DELETE FROM dimensions WHERE dim_cd='VAL_SEG'||v_proj;" +
				"\n DELETE FROM attributes WHERE dim_cd='VAL_SEG'||v_proj;" +
				"\n DROP TABLE IF EXISTS customer_attributes_VAL_SEG" + projectId + " CASCADE;" +
				"\n PERFORM logMsg('Deleted project data, proj:'||v_proj,'deleteProject',-1);" +
				"\n END;$$;";
		if (jqueue.getJob() instanceof RunMultiSqlJob) {
			return runJQueueJobMultiSql("deleteProject", "deleteProject",query,true);
		} else {
			return runJQueueJob("deleteProject", "deleteProject " + projectId,query);
		}
	}
	
	public boolean runCustomerProfile(Integer projectId, Integer solutionKey, Integer profileKey, String profileName,String selectedFactors,int minDept, int minCluster, int sampleSize, int seedSize,String username) {
		String customer = "customer_proj"+projectId;
		String customer_normal_profile = "customer_normal_profile_"+projectId;
		String customer_cluster_summary = "customer_cluster_summary_"+projectId;
		String[] s = selectedFactors.split("\\,");
		String query = "do  $$/*runCustomerProfile*/" +
				"\n DECLARE" +
				"\n   v_jobID int:=#jobid#;" +
				"\n   v_proj int:=" + JobUtils.sqlNumNull(projectId) + ";" +
				"\n   v_num_factor int:=" + JobUtils.sqlNum(s.length) + ";" +
				"\n   v_analysis_key int;" +
				"\n   v_solution_key int:=" + JobUtils.sqlNum(solutionKey) + ";/*the saved solution*/" +
				"\n   v_subseedsize int:=" + JobUtils.sqlNum(String.format("%.0f", Math.ceil(seedSize / 10d))) + ";" +
				"\n   v_profile_key int:=" + JobUtils.sqlNum(profileKey) + ";" +
				"\n   v_max_ind int;" +
				"\n   v_total_cust int;" +
				"\n   v_rowcount int;" +
				"\n BEGIN" +
				"\n   PERFORM logMsg('--Start running Customer Profile, proj:'||v_proj||' solution:'||v_solution_key||' profile:'||v_profile_key||' selected_factors:'||" + JobUtils.sqlStr(selectedFactors) + "||' minDept:'||" + JobUtils.sqlNum(minDept) + "||' minCluster:'||" + JobUtils.sqlNum(minCluster) + "||' sampleSize:'||" + JobUtils.sqlNum(sampleSize) + "||' seedSize:'||" + JobUtils.sqlNum(seedSize) + ",'runCustomerProfile',v_jobID);" +
				"\n   SELECT INTO v_total_cust count(*) FROM (SELECT * FROM customer_summary WHERE proj=v_proj LIMIT 1) t;" +
				"\n   IF v_total_cust<1 THEN" +
				"\n     PERFORM customer_summary(v_proj,v_jobID);" +
				"\n   END IF;" +

				"\n   SELECT INTO v_analysis_key parent_key /*the orginal analysis that contains the parent */" +
				"\n   FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_solution_key AND param_type='FACTOR_SOLUTION';" +

				//check profile, and clear it if find any
				"\n   SELECT INTO v_rowcount count(*)" +
				"\n   FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_solution_key;" +
				"\n   IF v_rowcount>0 THEN" +
				"\n     PERFORM logMsg('Deleting existing profile '||v_solution_key,'runCustomerProfile',v_jobID);" +
				"\n   BEGIN" +
				"\n     DROP TABLE IF EXISTS " + customer_normal_profile + "_" + profileKey + ";" +
				(isDatabaseXL() ? "" : "\n   EXCEPTION" +
				"\n     WHEN others THEN" +
				"\n       DELETE FROM " + customer_normal_profile +
				"\n       WHERE proj=v_proj AND analysis_key=v_profile_key;") +
				"\n   END;" +
				(isDatabaseXL() ? "\n  TRUNCATE TABLE temp_project_param_xl;" +
				"\n   INSERT INTO temp_project_param_xl" +
				"\n   SELECT * FROM project_param"+ 
				"\n   WHERE project_id=v_proj AND param_type='FACTOR_NAME' AND parent_key NOT IN (" +
				"\n       SELECT param_key FROM project_param" +
				"\n       WHERE  project_id=v_proj AND parent_key=v_profile_key AND param_type='FACTOR_SOLUTION'" +
				"\n   );" +
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_type='FACTOR_NAME';" +
				"\n   INSERT INTO project_param SELECT * FROM temp_project_param_xl;" +
				"\n  TRUNCATE TABLE temp_project_param_xl;"
				:
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_type='FACTOR_NAME' AND parent_key IN (" +
				"\n       SELECT param_key FROM project_param" +
				"\n       WHERE  project_id=v_proj AND parent_key=v_profile_key AND param_type='FACTOR_SOLUTION'" +
				"\n   );") +
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND parent_key=v_profile_key " +
				"\n     AND param_type IN ('FACTOR_SLN_PARAM');" +
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_profile_key AND param_type='SEG_PROFILE';" +
				"\n   PERFORM logMsg('--Deleted Segment Profile, proj:'||v_proj||' profile_key:'||v_profile_key||' user:'||" + JobUtils.sqlStr(username) + ",'deleteFactorAnalysis');" +
				"\n   END IF;" +

				"\n   INSERT INTO project_param(project_id,param_type,param_key,parent_key,param_name,char_val,num_val,created_date,created_by)" +
				"\n   SELECT project_id,'SEG_PROFILE',v_profile_key,param_key," + JobUtils.sqlStr(profileName) + ",char_val,num_val,now()," + JobUtils.sqlStr(username) +
				"\n   FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_solution_key;" +
				"\n   CREATE TABLE IF NOT EXISTS " + customer_normal_profile + "_" + profileKey + " (CONSTRAINT customer_normal_profile_chk CHECK (proj=" + projectId + " AND analysis_key=" + profileKey + ")) INHERITS (" + customer_normal_profile +
				");" +

				//"\n   TRUNCATE temp_customer_cluster_summary;" +
				//"\n   INSERT INTO temp_customer_cluster_summary(dept,cust_sys_key,cluster_key,spend,select_flag)" +
				//"\n   SELECT dept,cust_sys_key,cluster_key,spend,select_flag" +
				//"\n   FROM customer_cluster_summary " +
				//"\n   WHERE proj=v_proj AND analysis_key=v_analysis_key;" +

				"\n   ANALYZE cluster_factor;" +
				"\n   ANALYZE " + customer_cluster_summary + ";" +
				"\n   TRUNCATE temp_customer_org_profile;" +
				"\n   TRUNCATE temp_customer_profile;" +
				//temp_customer_org_profile will be used in runSegmentSummary in the save step, but not the save transaction
				//temp_customer_profile will be used in allocateCustomer in a later step
				"\n   PERFORM logMsg('Creating temp_customer_org_profile','runCustomerProfile',v_jobID);" +
				"\n   INSERT INTO temp_customer_org_profile(proj,analysis_key,cust_sys_key," + JobUtils.repeatStr(s, "factor_%s", ",") + ")" +
				"\n   SELECT v_proj,v_profile_key,c.cust_sys_key," + JobUtils.repeatStr(s, "sum(c.spend*f.factor_%s)", ",") +
				"\n   FROM cluster_factor f" +
				"\n   INNER JOIN " + customer_cluster_summary + " c" +
				"\n   ON f.group_key=c.cluster_key AND c.proj=v_proj AND c.analysis_key=v_analysis_key" +
				"\n   WHERE f.proj=v_proj AND f.analysis_key=v_solution_key" +
				"\n   GROUP BY c.cust_sys_key;" +
				"\n   GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n   PERFORM logMsg('Created temp_customer_org_profile, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +

		"\n   ANALYZE temp_customer_org_profile;" +
				"\n   UPDATE temp_customer_org_profile" +
				"\n   SET min_val=least(" + JobUtils.repeatStr(s, "factor_%s", ",") + ")" +
				"\n      ,sum_val=" + JobUtils.repeatStr(s, "factor_%s", "+") + ";" +

				"\n   UPDATE temp_customer_org_profile" +
				"\n   SET sum_val=sum_val-min_val*v_num_factor," + JobUtils.repeatStr(s, "factor_%1$s=factor_%1$s-min_val", "\n,") + ";" +

				"\n   PERFORM logMsg('Creating temp_customer_profile','runCustomerProfile',v_jobID);" +
				"\n   INSERT INTO temp_customer_profile(cust_sys_key," + JobUtils.repeatStr(s, "factor_%s", ",") + ")" +
				"\n   SELECT cust_sys_key," + JobUtils.repeatStr(s, "factor_%s/sum_val", ",") +
				"\n   FROM temp_customer_org_profile t" +
				"\n   WHERE sum_val!=0;" +
				"\n   GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n   PERFORM logMsg('Created temp_customer_profile, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +
				"\n   ANALYZE temp_customer_profile;";

		String sample_ind = "c.select_flag";//1 selected customer, 10,11,12,13.. are the sample ind
		if (sampleSize > 0)
		{
			sample_ind = "CASE WHEN c.select_flag!=0 AND p.cust_ind<=v_max_ind THEN 1 ELSE 0 END";
			query += "\n  SELECT INTO v_max_ind max(cust_ind)" +
							"\n   FROM (" +
							"\n     SELECT p.cust_ind" +
							"\n     FROM temp_customer_org_profile t" +
							"\n     INNER JOIN " + customer + " p ON p.cust_sys_key=t.cust_sys_key" +
							"\n     WHERE p.cust_ind>0" +
							"\n     ORDER BY p.cust_ind LIMIT " + sampleSize +
							"\n   ) t;";
		}
		query += "\n  TRUNCATE TABLE temp_customer_purchase_criteria;" +
				"\n  INSERT INTO temp_customer_purchase_criteria(cust_sys_key,num_dept,num_cluster,spend,cust_ind,sample_ind)" +
				"\n  SELECT p.cust_sys_key,c.num_dept,c.num_cluster,c.spend,CASE WHEN c.select_flag=0 THEN 0 ELSE p.cust_ind END AS cust_ind," + sample_ind + " AS sample_ind" +
				"\n  FROM " + customer + " p" +
				"\n  INNER JOIN (" +
				"\n     SELECT count(*) AS num_cluster,count(DISTINCT dept) AS num_dept,sum(spend) AS spend,cust_sys_key,select_flag" +
				"\n     FROM " + customer_cluster_summary + " c" +
				"\n     WHERE c.proj=v_proj AND c.analysis_key=v_analysis_key" +
				"\n     GROUP BY cust_sys_key,select_flag" +
				"\n  ) c ON c.cust_sys_key=p.cust_sys_key;" +
				"\n  GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n  PERFORM logMsg('Created temp_customer_purchase_criteria, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +
				"\n  ANALYZE temp_customer_purchase_criteria;" +

				"\n  PERFORM logMsg('Start saving profile mean and stddev into segment_cluster_mean','runCustomerProfile',v_jobID);" +
				"\n  DELETE FROM segment_cluster_mean WHERE proj=v_proj AND analysis_key=v_profile_key AND cluster_type IN ('PF_MEAN','PF_STDDEV');" +
				"\n  INSERT INTO segment_cluster_mean(proj,analysis_key,cluster_type," + JobUtils.repeatStr(s, "mean_%s", ",") + ")" +
				"\n  SELECT v_proj,v_profile_key,'PF_MEAN'," + JobUtils.repeatStr(s, "AVG(factor_%s)", ",") +
				"\n  FROM temp_customer_profile t" +
				"\n  INNER JOIN (" +
				"\n     SELECT cust_sys_key,CASE WHEN sample_ind>0" +
				((minDept > 1) ? " AND num_dept>=" + JobUtils.sqlNum(minDept) : "") +
				((minCluster > 1) ? " AND num_cluster>=" + JobUtils.sqlNum(minCluster) : "") + " THEN 1 END AS select_flag" +
				"\n     FROM temp_customer_purchase_criteria t" +
				"\n  ) p ON p.cust_sys_key=t.cust_sys_key AND p.select_flag=1;" +
				"\n  GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n  PERFORM logMsg('Saved profile mean into segment_cluster_mean PF_MEAN, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +
				"\n  INSERT INTO segment_cluster_mean(proj,analysis_key,cluster_type," + JobUtils.repeatStr(s, "mean_%s", ",") + ")" +
				"\n  SELECT v_proj,v_profile_key,'PF_STDDEV'," + JobUtils.repeatStr(s, "stddev_samp(factor_%s)", ",") +
				"\n  FROM temp_customer_profile t" +
				"\n  INNER JOIN (" +
				"\n    SELECT cust_sys_key,CASE WHEN sample_ind>0" +
				((minDept > 1) ? " AND num_dept>=" + JobUtils.sqlNum(minDept) : "") +
				((minCluster > 1) ? " AND num_cluster>=" + JobUtils.sqlNum(minCluster) : "") + " THEN 1 END AS select_flag" +
				"\n    FROM temp_customer_purchase_criteria t" +
				"\n  ) p ON p.cust_sys_key=t.cust_sys_key AND p.select_flag=1;" +
				"\n  GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n  PERFORM logMsg('Saved profile standard deviation into segment_cluster_mean PF_STDDEV, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +

				(isDatabaseXL() ? "\n  TRUNCATE TABLE temp_customer_profile_xl;" +
				"\n   INSERT INTO temp_customer_profile_xl(cust_sys_key," + JobUtils.repeatStr(s, "factor_%s", ",") + ")" +
				"\n   SELECT p.cust_sys_key," + JobUtils.repeatStr(s, "(factor_%1$s-m%1$s)/sd%1$s", "\n,") +
				"\n   FROM temp_customer_profile p" +
				"\n   CROSS JOIN (SELECT " + JobUtils.repeatStr(s, "mean_%1$s m%1$s", ",") +
				"\n       FROM segment_cluster_mean WHERE proj=v_proj AND analysis_key=v_profile_key AND cluster_type='PF_MEAN') avg" +
				"\n   CROSS JOIN (SELECT " + JobUtils.repeatStr(s, "mean_%1$s sd%1$s", ",") +
				"\n       FROM segment_cluster_mean WHERE proj=v_proj AND analysis_key=v_profile_key AND cluster_type='PF_STDDEV') std;" +
				"\n   TRUNCATE TABLE temp_customer_profile;" +
				"\n   INSERT INTO temp_customer_profile SELECT * FROM temp_customer_profile_xl;" +
				"\n   TRUNCATE TABLE temp_customer_profile_xl;"
				:
				"\n   UPDATE temp_customer_profile" +
				"\n   SET " + JobUtils.repeatStr(s, "factor_%1$s=(factor_%1$s-m%1$s)/sd%1$s", "\n,") +
				"\n   FROM ( SELECT *" +
				"\n     FROM (SELECT " + JobUtils.repeatStr(s, "mean_%1$s m%1$s", ",") +
				"\n       FROM segment_cluster_mean WHERE proj=v_proj AND analysis_key=v_profile_key AND cluster_type='PF_MEAN') avg" +
				"\n     CROSS JOIN (SELECT " + JobUtils.repeatStr(s, "mean_%1$s sd%1$s", ",") +
				"\n       FROM segment_cluster_mean WHERE proj=v_proj AND analysis_key=v_profile_key AND cluster_type='PF_STDDEV') std" +
				"\n   ) t;") +

				"\n   PERFORM logMsg('Creating customer_normal_profile','runCustomerProfile',v_jobID);" +
				"\n   INSERT INTO " + customer_normal_profile + "_" + profileKey + "(proj,analysis_key,cust_sys_key,num_dept,num_cluster,cust_ind,sample_ind," + JobUtils.repeatStr(s, "factor_%s", ",") + ")" +
				"\n   SELECT v_proj,v_profile_key,t.cust_sys_key,num_dept,num_cluster,cust_ind" +
				//"\n      ,CASE WHEN select_flag=1 AND row_num<=" + JobUtils.sqlNum(seedSize) + " THEN 10+(row_num-1)/v_subSeedSize ELSE sample_ind END AS sample_ind" +
				"\n      ,CASE WHEN select_flag=1 AND row_num<=" + JobUtils.sqlNum(seedSize) + " THEN 10+(row_num-1)/v_subSeedSize ELSE select_flag END AS sample_ind" +
				//20140718, only allocate customers who qualified for both spend and variation restriction 
				"\n      ," + JobUtils.repeatStr(s, "factor_%s", ",") +
				"\n    FROM temp_customer_profile p" +
				"\n    INNER JOIN (" +
				"\n      SELECT t.*,row_number() OVER (PARTITION BY select_flag ORDER BY cust_ind) AS row_num" +
				"\n      FROM (" +
				"\n        SELECT t.*,CASE WHEN sample_ind>0" +
				((minDept > 1) ? " AND num_dept>=" + JobUtils.sqlNum(minDept) : "") +
				((minCluster > 1) ? " AND num_cluster>=" + JobUtils.sqlNum(minCluster) : "") + " THEN 1 END AS select_flag" +
				"\n        FROM temp_customer_purchase_criteria t" +
				"\n      ) t" +
				"\n    ) t ON p.cust_sys_key=t.cust_sys_key;" +
				"\n   GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n   PERFORM logMsg('Created customer_normal_profile, rows '||v_rowcount,'runCustomerProfile',v_jobID);" +

				"\n   SELECT INTO v_analysis_key parent_key /*the orginal analysis that contains the parent */" +
				"\n   FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_solution_key AND param_type='FACTOR_SOLUTION';" +
				//				"\n   SELECT INTO v_total_cust count(DISTINCT cust_sys_key) " +
				//"\n   FROM customer_cluster_summary " +
				//"\n   WHERE proj=v_proj AND analysis_key=v_analysis_key AND select_flag=1;" + //qualified customers
				"\n   SELECT INTO v_total_cust count(*) " +
				"\n   FROM " + customer_normal_profile + "_" + profileKey +
				"\n   WHERE cust_ind>0 AND sample_ind>0;" + //20140718

				"\n   DELETE FROM project_param WHERE parent_key=v_profile_key AND param_type='FACTOR_SLN_PARAM';" +
				"\n   INSERT INTO project_param(project_id,param_type,parent_key,param_name,char_val,num_val,created_date,created_by) VALUES" +
				"\n   (v_proj,'FACTOR_SLN_PARAM',v_profile_key,'SELECT_FACTOR'," + JobUtils.sqlStr(selectedFactors) + ",null,now()," + JobUtils.sqlStr(username) + ")" +
				"\n   ,(v_proj,'FACTOR_SLN_PARAM',v_profile_key,'TOTAL_CUST',null,v_total_cust,now()," + JobUtils.sqlStr(username) + ")" +
				"\n   ,(v_proj,'FACTOR_SLN_PARAM',v_profile_key,'MIN_DEPT',null," + JobUtils.sqlNum(minDept) + ",now()," + JobUtils.sqlStr(username) + ")" +
				"\n   ,(v_proj,'FACTOR_SLN_PARAM',v_profile_key,'MIN_CLUSTER',null," + JobUtils.sqlNum(minCluster) + ",now()," + JobUtils.sqlStr(username) + ")" +
				"\n   ,(v_proj,'FACTOR_SLN_PARAM',v_profile_key,'SAMPLE_SIZE',null," + JobUtils.sqlNum(sampleSize) + ",now()," + JobUtils.sqlStr(username) + ")" +
				"\n   ,(v_proj,'FACTOR_SLN_PARAM',v_profile_key,'SEED_SIZE',null," + JobUtils.sqlNum(seedSize) + ",now()," + JobUtils.sqlStr(username) + ");" +

				"\n   TRUNCATE TABLE temp_customer_purchase_criteria;" +
				"\n   PERFORM logMsg('--Finished running Customer Profile, proj:'||v_proj||' solution:'||v_solution_key||' selected_factors:'||" + JobUtils.sqlStr(selectedFactors) + ",'runCustomerProfile',v_jobID);" +
				"\n END;$$;";
		return runJQueueJobMultiSql("runCustomerProfile", "Spend Profiles " + projectId,query,true);
	}


	public boolean deleteSegmentation(String projectId, String segKey, String username)  {
		String product_segment_summary = "product_segment_summary_"+projectId;
		String query = "do  $$/*deleteSegmentation*/" +
				"\n DECLARE" +
				"\n   v_count int;" +
				"\n   v_proj int:=" + JobUtils.sqlNumNull(projectId) + ";" +
				"\n   v_seg_key int:=" + JobUtils.sqlNumNull(segKey) + ";" +
				"\n   v_solution_key int;" +
				"\n   v_num_seg int;" +
				"\n BEGIN" +
				"\n   PERFORM logMsg('--Deleting Segmentation, proj:'||v_proj||' seg:'||v_seg_key||' user:'||" + JobUtils.sqlStr(username) + ",'deleteSegmentation');" +
				"\n   SELECT INTO v_solution_key,v_num_seg parent_key,num_val" +
				"\n   FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_seg_key AND param_type='SEGMENTATION';" +
				"\n   DELETE FROM segment_cluster_mean" +
				"\n   WHERE proj=v_proj AND analysis_key=v_solution_key AND total_cluster=v_num_seg;" +
				"\n   DELETE FROM " + product_segment_summary +
				"\n   WHERE proj=v_proj AND analysis_key=v_seg_key;" +
				"\n   DELETE FROM customer_segment_summary" +
				"\n   WHERE proj=v_proj AND analysis_key=v_seg_key;" +
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND parent_key=v_seg_key AND param_type IN ('SEGMENT_NAME','SEG_PARAM');" +
				"\n   DELETE FROM project_param" +
				"\n   WHERE project_id=v_proj AND param_key=v_seg_key AND param_type='SEGMENTATION';" +
				"\n END;$$;";
		return runJQueueJobMultiSql("deleteSegmentation", "Spend Profiles " + projectId,query,true);
	}

	public boolean runSegmentProdSummary(Integer projectId, Integer analysisKey, Integer numSegment,Integer numProcess,String transactionTable,String deptType,String skuCol) {
		String product_segment_summary = "product_segment_summary_"+projectId;
//		String deptType = "SELECT char_val FROM project_param WHERE param_name = 'typeDept' AND project_id = "+projectId;
//		String skuCol = "SELECT char_val FROM project_param WHERE param_name = 'typeSKU' AND project_id = "+projectId;
		skuCol = "prod_sys_key";

		String cust_seg;

		cust_seg = "TRUNCATE TABLE temp_cust_segment_rep;" +
				"\n INSERT INTO temp_cust_segment_rep(cust_sys_key,seg_ind)" +
				"\n SELECT cust_sys_key,seg_" + numSegment + " AS seg_ind" +
				"\n FROM customer_segments_" + projectId + " s" +
				"\n WHERE s.proj=" + projectId + " AND s.analysis_key=v_solution_key AND s.seg_" + numSegment + " IS NOT NULL;" +
				"\n ANALYZE temp_cust_segment_rep;";

		String query = "do  $$/*runSegmentProdSummary*/" +
				"\n DECLARE" +
				"\n   v_jobID int:=#jobid#;" +
				"\n   v_proj int:=" + projectId + ";" +
				"\n   v_seg_solution int:=" + analysisKey + ";" +
				"\n   v_org_seg int:=" +  analysisKey  + ";" +
				"\n   v_solution_key int;" +
				"\n   v_num_segment int;" +
				"\n   v_profile_key int;" +
				"\n   v_corr_solution int;" +
				"\n   v_dept_codes varchar(200);" +
				"\n   v_dept_key bigint;" +
				"\n   v_rec RECORD;" +
				"\n   v_rowcount int;" +
				"\n   v_script TEXT;" +
				"\n   v_commands TEXT := '';" +
				"\n   v_description TEXT := '';" +
				"\n BEGIN" +
				"\n   PERFORM logMsg('--Start running Segment Product Summary, proj:'||v_proj||' seg_solution:'||v_seg_solution||' num_segment:'||" + numSegment + ",'runSegmentProdSummary',v_jobID);" +
				"\n   SELECT INTO v_solution_key,v_num_segment parent_key,num_val FROM project_param where param_key=v_org_seg;" +
				"\n   SELECT INTO v_profile_key parent_key FROM project_param where param_key=v_solution_key;" +
				"\n   SELECT INTO v_corr_solution parent_key FROM project_param where param_key=v_profile_key;" +
				"\n   SELECT INTO v_dept_codes param_att FROM project_param where param_key=v_corr_solution;" +

				(numProcess > 1 ?
						"\n   PERFORM run_parallel_jobs(1, '\"Delete summary\",\"Insert into temp cust segment\"', " +
								"      '\"/*schema*/DELETE FROM " + product_segment_summary + " WHERE proj='||v_proj||' AND analysis_key='||v_seg_solution||';\",\"/*schema*/" + cust_seg.replace("v_solution_key", "'||v_solution_key||'") +
								"\"',v_jobID);" :
						"\n   DELETE FROM " + product_segment_summary + " WHERE proj=v_proj AND analysis_key=v_seg_solution;" +
								"\n   " + cust_seg) +

				//"\n   TRUNCATE temp_customer_cluster_summary;" +
				//"\n   INSERT INTO temp_customer_cluster_summary(dept,cust_sys_key,cluster_key,spend,select_flag)" +
				//"\n   SELECT dept,cust_sys_key,cluster_key,spend,select_flag" +
				//"\n   FROM " + customer_cluster_summary +
				//"\n   WHERE proj=v_proj AND analysis_key=v_corr_solution;" +

				"\n   FOR v_rec IN SELECT prod_sys_key,prod_code,getTransTable(v_proj,prod_sys_key,prod_code) AS tran_table,row_number() OVER (ORDER BY prod_code) AS row_ind " +
				"\n                FROM product_cat WHERE prod_type='" + deptType + "' AND ','||v_dept_codes||',' LIKE '%,'||prod_code||',%' ORDER BY prod_code" +
				"\n   LOOP" +
				//"\n     PERFORM logMsg('Running Segment pseudo product summary for dept:'||v_rec.prod_code,'runSegmentProdSummary',v_jobID);" +
				/*(isDatabaseXL() ? "\n     TRUNCATE TABLE temp_transaction_xl;" +
				"\n     EXECUTE 'INSERT INTO temp_transaction_xl SELECT * FROM '||v_rec.tran_table||' t';" +
				"\n     GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n     PERFORM logMsg('Inserted into temp_transaction_xl for dept:'||v_rec.prod_code||', rows:'||v_rowcount,'runSegmentProdSummary',v_jobID);" +
				"\n     INSERT INTO " + product_segment_summary + "(proj,analysis_key,segment_ind,summary_level,dept,prod_sys_key,customer_count,spend)" +
				"\n     SELECT v_proj,v_seg_solution,s.seg_ind,'P',t.dept_sys_key,t." + skuCol + ",count(DISTINCT s.cust_sys_key) AS num_cust,sum(spend) AS spend" +
				"\n     FROM (" + cust_seg + ") s" +
				"\n     INNER JOIN temp_transaction_xl t" +
				"\n     ON s.cust_sys_key=t.cust_sys_key" +
				"\n     GROUP BY s.seg_ind,t.dept_sys_key,t." + skuCol + ";" +
				"\n     GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n     PERFORM logMsg('Finished Segment pseudo product summary for dept:'||v_rec.prod_code||', rows:'||v_rowcount,'runSegmentProdSummary',v_jobID);" +
				"\n     TRUNCATE TABLE temp_transaction_xl;"
				:*/
				"\n     v_script := 'DO $'||'$'||chr(13)" +
				"\n     ||' DECLARE'||chr(13)" +
				"\n     ||'   v_rowcount numeric;'||chr(13)" +
				"\n     ||' BEGIN'||chr(13)" +
				"\n     ||'   PERFORM logMsg(''Running Segment pseudo product summary for dept:'||v_rec.prod_code||''',''#whence#'', '||v_jobID||');'||chr(13)" +
				"\n     ||'   INSERT INTO " + product_segment_summary + "(proj,analysis_key,segment_ind,summary_level,dept,prod_sys_key,customer_count,spend)'" +
				"\n     ||'   SELECT '||v_proj||','||v_seg_solution||',s.seg_ind,''P'','||v_rec.prod_sys_key||',t." + skuCol + ",count(DISTINCT s.cust_sys_key) AS num_cust,sum(spend) AS spend'" +
				"\n     ||'   FROM temp_cust_segment_rep s'" +
				"\n     ||'   INNER JOIN '||v_rec.tran_table||' t'" +
				"\n     ||'   ON s.cust_sys_key=t.cust_sys_key'" +
				"\n     ||'   GROUP BY s.seg_ind,t." + skuCol + ";'||chr(13)" +
				"\n     ||'   GET DIAGNOSTICS v_rowcount = ROW_COUNT;'||chr(13)" +
				"\n     ||'   PERFORM logMsg(''Finished Segment pseudo product summary for dept:'||v_rec.prod_code||', rows:''||v_rowcount,''#whence#'','||v_jobID||');'||chr(13)" +
				"\n     ||' END;$'||'$;';" +

				(numProcess > 1 ?
						"\n     v_description := v_description||'\"Run Segment pseudo product summary dept: '||v_rec.prod_code||'\",';" +
								"\n     v_commands := v_commands||'\"/*runSegmentProdSummaryMulti'||v_rec.row_ind||'*//*schema*/'||REPLACE(v_script,'#whence#','runSegmentProdSummaryMulti'||v_rec.row_ind)||'\",';" :
						"\n    EXECUTE REPLACE(v_script,'#whence#','runSegmentProdSummary');") +
				"\n   END LOOP;" +
				(numProcess > 1 ? "\n   PERFORM run_parallel_jobs(" + numProcess +", v_description, v_commands, v_jobID);" : "") +

				"\n   PERFORM logMsg('Running Segment product family summary','runSegmentProdSummary',v_jobID);" +
				"\n   INSERT INTO " + product_segment_summary + "(proj,analysis_key,segment_ind,summary_level,dept,prod_sys_key,customer_count,spend)" +
				"\n   SELECT v_proj,v_seg_solution,s.seg_ind,'G',c.dept,c.cluster_key,count(*) AS num_cust,sum(spend) AS spend" +
				"\n   FROM temp_cust_segment_rep s" +
				"\n   INNER JOIN customer_cluster_summary c" +
				"\n   ON s.cust_sys_key=c.cust_sys_key AND c.proj=v_proj AND c.analysis_key=v_corr_solution" +
				"\n   GROUP BY s.seg_ind,c.dept,c.cluster_key;" +
				"\n   GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n   PERFORM logMsg('Finished Segment product family summary, rows:'||v_rowcount,'runSegmentProdSummary',v_jobID);" +

				"\n   PERFORM logMsg('Running Segment department summary','runSegmentProdSummary',v_jobID);" +
				"\n   INSERT INTO " + product_segment_summary + "(proj,analysis_key,segment_ind,summary_level,dept,prod_sys_key,customer_count,spend)" +
				"\n   SELECT v_proj,v_seg_solution,s.seg_ind,'D',c.dept,c.dept,count(DISTINCT s.cust_sys_key) AS num_cust,sum(spend) AS spend" +
				"\n   FROM temp_cust_segment_rep s" +
				"\n   INNER JOIN customer_cluster_summary c" +
				"\n   ON s.cust_sys_key=c.cust_sys_key AND c.proj=v_proj AND c.analysis_key=v_corr_solution" +
				"\n   GROUP BY s.seg_ind,c.dept;" +
				"\n   GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n   PERFORM logMsg('Finished Segment department summary, rows:'||v_rowcount,'runSegmentProdSummary',v_jobID);" +

				"\n   SELECT INTO v_rowcount count(*) FROM (SELECT * FROM " + transactionTable + " LIMIT 10000) t WHERE store_key IS NOT NULL;" +
				"\n   IF v_rowcount>0 THEN" +
				"\n     PERFORM logMsg('Running Segment store summary','runSegmentProdSummary',v_jobID);" +
				"\n     INSERT INTO " + product_segment_summary + "(proj,analysis_key,segment_ind,summary_level,dept,customer_count,spend)" +
				"\n     SELECT v_proj,v_seg_solution,s.seg_ind,t.store_key,-9999,count(DISTINCT s.cust_sys_key) AS num_cust,sum(spend) AS spend" +
				"\n     FROM temp_cust_segment_rep s" +
				"\n     INNER JOIN " + transactionTable + " t" +
				"\n     ON s.cust_sys_key=t.cust_sys_key" +
				"\n     GROUP BY s.seg_ind,t.store_key;" +
				"\n     GET DIAGNOSTICS v_rowcount = ROW_COUNT;" +
				"\n     PERFORM logMsg('Finished Segment store summary, rows:'||v_rowcount,'runSegmentProdSummary',v_jobID);" +
				"\n   END IF;" +

				"\n   TRUNCATE TABLE temp_cust_segment_rep;" +
				"\n   PERFORM logMsg('--Finished running Segment Product Summary, proj:'||v_proj||' seg_solution:'||v_seg_solution,'runSegmentProdSummary',v_jobID);" +
				"\n END;$$;";
		//Optimisation on DISTINCT?
		return runJQueueJobMultiSql("runSegmentProdSummary", "Profiles",query,true);
	}
	
	protected boolean runJQueueJobMultiSql(String jobName, String jobDesc,String query, boolean run) {
		RunMultiSqlJob job = (RunMultiSqlJob) jqueue.getJob();
		if (job == null) {
			return false;
		}
		if (!StringUtils.isEmpty(jobDesc)) {
			job.setDesc(jobDesc);
		}
		if (!StringUtils.isEmpty(jobName)) {
			job.setName(jobName);
		} 

		query = query.replace("#jobid#", "" + job.getJobId());
		job.addSql(query);

		job.setFinishAction(job.getFinishAction().replace("#jobid#", "" + job.getJobId()));
		jqueue.setJob(job);
		if (run) {
			if (jqueue.runJQueueJob()) {
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	protected boolean runJQueueJob(String jobName, String jobDesc,String query) {
		
		Job job = jqueue.getJob();
		
		if (job == null) {
			return false;
		}
		job.addDescToFront(jobDesc);
		job.setName(jobName);

		query = query.replace("#jobid#", "" + job.getJobId());
		job.setCommand(query);

		job.setFinishAction(job.getFinishAction().replace("#jobid#", "" + job.getJobId()));
		if (jqueue.runJQueueJob()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isDatabaseXL() {
	   return false;
	}


}
