define(["text!/vue/components/segment_detail.html"], function(template) {
    return {
        template:template,
        data() {
	        return {
	        	activeName:'first',
	        	tableData:[],
	        	initVisible:false,
                initVisible2:false,
	        	segmentions:[],
	        	deptOptions:[],
	        	levelOptions:[],
	        	statsOptions:[],
	        	segmentationName:'',
	        	segmentationKey:'',
                segmentationPName:'',
                segmentationPKey:'',
                deptKey:0,
	        	levelKey:'G',
	        	statsKey:'SPECIFICITY',
	        	analysisKey:0,
	        	segmentationSummaryData:[],
                segmentStoreProfileData:[],
	        	numFactor:0,
	        	segmentNames:[],
	        	chartTable:'table',
	        	xOptions:[],
	        	xKey:'avg_freq',
	        	yOptions:[],
	        	yKey:'avg_spend',
	        	sizeOptions:[],
	        	sizeKey:'customer_count',
                segmentationPData:[],
                segmentationPList:[],
	        	segmentProfileData:[],
                numSegment:0,
                numFactorP:0,
                factorKey:0,
                segmentNames2:[],
				tmout:null,
                buildCount:undefined,
                segmentNameKey:0
	        }
        },
        mounted:function(){
        	let target = this;
        	// this.limitOptions = [{label:'Top 100',value:100},{label:'Top 500',value:500},{label:'Top 1000',value:1000}];
        	this.levelOptions = [{label:'按商品家族',value:'G'},{label:'按SKU',value:'P'}];
        	this.statsOptions = [{label:'特征指数(Specificity)',value:'SPECIFICITY'},{label:'消费金额(Spend)',value:'SPEND'},{label:'市场细分的消费占比(% Seg Spend)',value:'SEG_SPEND'},{label:'顾客数量（Customers）',value:'CUSTOMERS'},{label:'顾客渗透率(Customer Penetration)',value:'CUSTOMERS'}];
        	this.xOptions = [{label:'Avg Spend',value:'avg_spend'},{label:'Avg Level 1',value:'avg_dept'},{label:'Avg # Product Families',value:'avg_prod_group'},{label:'Avg Frequency',value:'avg_freq'},{label:'Avg Units',value:'avg_quantity'}];
        	this.yOptions = [{label:'Avg Spend',value:'avg_spend'},{label:'Avg Level 1',value:'avg_dept'},{label:'Avg # Product Families',value:'avg_prod_group'},{label:'Avg Frequency',value:'avg_freq'},{label:'Avg Units',value:'avg_quantity'}];
        	this.sizeOptions = [{label:'Customers',value:'customer_count'},{label:'Spend',value:'spend'}];
        	if(this.$route.query) {
        		this.projectId = this.$route.query.projectId;
        		this.datasetKey = this.$route.query.datasetKey;
        		if(this.$route.query.layid==44) {
        			this.init();
            	}
        	}
        	
        },
        methods:{
        	init(){
        		if(!this.initVisible) {
        			this.list();
                	this.initVisible = true;
        		}
        	},init2(){
                this.segmentDropDownList();
                this.checkIfProfileBuild();
			}, tabClick(){
        		if(this.activeName==='second'&&!this.initVisible2) {
					this.initVisible2= true;
					this.init2();
				}
			},
        	list(){
        		let target = this;
        		let params = {datasetKey:this.datasetKey,projectId:this.projectId};
        		post('/segment/list',params,res=>{
        			target.tableData=res.data.list;
        			
        			target.segmentions = [];
        			for(let i = 0;i<res.data.list.length;i++) {
        				let obj = {};
        				obj.value=res.data.list[i].segmentation_key;
        				obj.label=res.data.list[i].segmentation;
        				target.segmentions.push(obj);
        			}
        		});
        	},getSegmentSummary(sortKey,order){
        		let target = this;
        		let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentationKey:this.segmentationKey,numFactor:this.numFactor};
        		if(sortKey) {
                    params.sortKey = sortKey;
                    params.order = order;
				}
        		post('/segment/getSegmentSummary',params,res=>{
        			if(res.data) {
//        				res.data.splice(res.data.length-1,1);
        				target.segmentationSummaryData=res.data;
        				target.bubbleChart();
        			}
        			
        		});
        	},editable:function(e,row,index){
        		let target = this;
        		target.$editable(e,function(value){

                	target.segmentationSummaryData[index].segment_name = value;
                    target.saveSegmentName(target.segmentationSummaryData[index].segment_key,value);
                });
            },changeSel(val){
        		let target = this;
        		let obj = {};
                obj = this.tableData.find((item)=>{
                    return item.segmentation_key === val;
                });
                if(!obj) {
                	obj = {};
                	obj.num_factor = 0;
                    obj.segmentation = '';
                    obj.segmentation_key = '';
                    obj.factor_key = 0;
				}
                this.numFactor = obj.num_factor;
                this.segmentationName = obj.segmentation;
                this.segmentationKey = obj.segmentation_key;
                this.analysisKey = obj.factor_key;
                this.segmentationSummaryData = [];
                this.numSegment = obj.num_segment;
                this.getSegmentPurchaseThemes();
                this.getSegmentSummary();
                this.getSegmentProfile();
                this.init2();
                // this.getSegmentStoreProfile();
        	},xChangeSel(val){
        		this.xKey = val;
        		this.bubbleChart();
        	},yChangeSel(val){
        		this.yKey = val;
        		this.bubbleChart();
        	},sizeChangeSel(val){
        		this.sizeKey = val;
        		this.bubbleChart();
        	},getSegmentPurchaseThemes(){
        		let target = this;
        		let params = {datasetKey:this.datasetKey,projectId:this.projectId,analysisKey:this.analysisKey,numFactor:this.numFactor};
        		post('/segment/getSegmentPurchaseThemes',params,res=>{
        			target.segmentNames=res.data;
        		});
        	},cellStyle(item) {
        		if(item.column.label!=='SN'&&item.column.label!=='群体名'&&item.column.label!=='人数数量占比'&&item.column.label!=='平均消费额') {
        			let val = item.row[item.column.columnKey];
        			if(val<=50) {
        				return 'background:#e48186;border-bottom: 1px solid #aaaaaa;border-right: 1px solid #aaaaaa;';
        			} else if(val<=80) {
        				return 'background:#ddaab2;border-bottom: 1px solid #aaaaaa;border-right: 1px solid #aaaaaa;';
        			} else if(val<120) {
        				return 'background:#a8e8da;border-bottom: 1px solid #aaaaaa;border-right: 1px solid #aaaaaa;';
        			} else if(val<200) {
        				return 'background:#c1de71;border-bottom: 1px solid #aaaaaa;border-right: 1px solid #aaaaaa;';
        			} else if (val>=200) {
        				return 'background:#81ce69;border-bottom: 1px solid #aaaaaa;border-right: 1px solid #aaaaaa;';
        			}
        		}
        		
        	},getSummaries(param){
        		const { columns, data } = param;
    	        const sums = [];
    	        columns.forEach((column, index) => {
    	          if (index === 0) {
    	            sums[index] = '合计';
    	            return;
    	          }
    	          const values = data.map(item =>{
    	        	  const key = column.property?column.property:column.columnKey;
    	        	  return Number(item[key]);
    	          });
    	          if (!values.every(value => isNaN(value))) {
    	            sums[index] = values.reduce((prev, curr) => {
    	              const value = Number(curr);
    	              if (!isNaN(value)) {
    	                return prev + curr;
    	              } else {
    	                return prev;
    	              }
    	            }, 0);
    	            if(index<=3) {
    	            	sums[index] = Math.round(sums[index]);
    	            } else {
    	            	sums[index] = Math.round(sums[index]/this.numFactor*100)/100+"%";
    	            }
    	            
    	          } else {
    	        	
    	            sums[index] = '';
    	          }
    	        });

    	        return sums;
        	},bubbleChart(){
        		let series = [];
        		let acv = [];
        		console.log(this.segmentationSummaryData);
        		for(let i =0;i<this.segmentationSummaryData.length;i++) {
        			let o = this.segmentationSummaryData[i];
        			let obj = {};
        			obj.name = (i+1)+"."+(o.segment_name.length>13?o.segment_name.substr(0,10)+"...":o.segment_name);
        			obj.data = [];
        			acv.push("£ "+Math.round(o.avg_spend));
        			obj.data.push([o[this.xKey]?o[this.xKey]:0,o[this.yKey]?o[this.yKey]:0,o[this.sizeKey]?o[this.sizeKey]:0]);
        			series.push(obj);
        		}
	    		Highcharts.chart('bubbleChart', {  series: series
		  		  ,chart: {
		  		    height: 400
		  		    ,plotBorderWidth: 1
		  		    ,type: 'bubble'
		  		    }
		  		  ,credits: {enabled: false}
		  		  ,legend: {
		  		    align: 'right'
		  		    ,layout: 'vertical'
		  		    ,verticalAlign: 'bottom'
		  		  },plotOptions: {
		  		    bubble: {
		  		      maxSize: '10%'
		  		      ,minSize: '3%'}
		  		    ,series: {
		  		      dataLabels: {
		  		        color: 'black'
		  		        ,enabled: true
		  		        ,formatter: function() {return acv[this.series.index];}
		  		        ,useHTML: true}}}
		  		  ,title: {text: null}
		  		  ,xAxis: {
		  		    gridLineWidth: 1
		  		    ,title: {text: 'Avg Frequency'}}
		
		  		  ,yAxis: {
		  		    title: {text: 'Avg Spend'}}
		  		  });
        		
        	},saveSegmentName(segmentKey,name) {
        		let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentKey:segmentKey,name:name};
        		post('/segment/saveSegmentName',params,res=>{
        			if(res.code==2000000) {
               		   this.$message({
       	                type: 'success',
       	                message:res.data
       	               });
               	   } else {
               		   this.$message({
       	                type: 'error',
       	                message:res.msg
       	               });
               	   }
        		});
        	},getSegmentProfile(){
        		let target = this;
        		let params = {datasetKey:this.datasetKey,projectId:this.projectId,solutionKey:this.segmentNameKey,numSegment:this.numSegment,numFactor:this.numFactor,analysisKey:this.segmentationKey,level:this.levelKey,deptCode:this.deptKey};
        		post('/segment/getSegmentProfile',params,res=>{
        			if(res.data) {
        				target.segmentProfileData=res.data;
        			}
        			
        		});
        	},sortChange(item){
				if(item.order) {
                    this.getSegmentSummary(item.column.columnKey,item.order.replace("ending",""));
				}

			},updateSegmentationName(segmentKey,name) {//更新市场细分名称

                let target = this;

                let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentationKey:this.segmentationKey,name:this.segmentationName};
                post('/segment/updateSegmentationName',params,res=>{
                    if(res.code==2000000) {
                        this.$message({
                            type: 'success',
                            message:res.data
                        });
                        let obj = target.segmentions.find((item)=>{
                        	console.log(item);
                            if(item.value === target.segmentationKey) {
								item.label = target.segmentationName;
                                return item;
                            }
                        });
                    } else {
                        this.$message({
                            type: 'error',
                            message:res.msg
                        });
                    }
                });
			},updateColumnName (column,e) { //修改商品影响力列表列名
				let target = this;
				let dbclick = false;
				if(target.tmout != null) {
                    dbclick = true;
                    clearTimeout(target.tmout);
				} else {
                    target.tmout = null;
				}

                target.tmout = setTimeout(()=>{
					if(dbclick) {
                        let index = (column.columnKey.replace("fi",""))|0;
                        target.$editable(e,function(value){
                            target.segmentNames[index-1].factor_name = value;
                            target.saveFactorName(value,target.segmentNames[index-1].factor_ind,target.segmentNames[index-1].factor_key);
                        });
                        
					}
                    target.tmout = null;
				},300)

			},saveFactorName (factorName,factorInd,factorKey) { //更新主题名
                let params = {
                    datasetKey: this.datasetKey,
                    projectId: this.projectId,
                    analysisKey: this.factorCorrKey,
                    factorKey:factorKey,
                    name: factorName,
                    factorInd:factorInd
                };
                post("/segment/saveFactorName",params,res=>{
                    if(res.code==2000000) {
                        this.$message({
                            type: 'success',
                            message:res.data
                        });
                    } else {
                        this.$message({
                            type: 'error',
                            message:res.msg
                        });
                    }
                });
			},deleteSegmentation () {//删除市场细分
                let target = this;

                let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentationKey:this.segmentationKey};
                post('/segment/deleteSegmentation',params,res=>{
                    if(res.code==2000000) {
                        this.$message({
                            type: 'success',
                            message:res.data
                        });
						target.changeSel('');
						target.list();
                    } else {
                        this.$message({
                            type: 'error',
                            message:res.msg
                        });
                    }
                });
			},segmentDropDownList(){
                let target = this;
                let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentKey:this.segmentationKey};
                post('/segment/segmentDropDownList',params,res=>{
                    target.segmentationPData=res.data;

                    target.segmentationPList = [];
                    for(let i = 0;i<res.data.length;i++) {
                        let obj = {};
                        obj.value=res.data[i].segment_name_key;
                        obj.label=res.data[i].segment_name;
                        target.segmentationPList.push(obj);
                    }
                });
            },changeSel2(val){
                let target = this;
                let obj = {};
                obj = this.segmentationPData.find((item)=>{
                    return item.segment_name_key === val;
                });
				this.segmentationPName=obj.segment_name;
				this.segmentationPKey=obj.segment_name_key;
				this.segmentNameKey = obj.segment_name_key;

                this.getPoolDepartments(obj.dept_codes);
                this.getSegmentProfile();
                this.getSegmentPurchaseThemes2();

            },saveSegmentName2() {
				let target = this;
                let params = {datasetKey:this.datasetKey,projectId:this.projectId,segmentKey:this.segmentationPKey,name:this.segmentationPName};
                post('/segment/saveSegmentName',params,res=>{
                    if(res.code==2000000) {
                        this.$message({
                            type: 'success',
                            message:res.data
                        });
                        let obj = target.segmentationPList.find((item)=>{
                            if(item.value === target.segmentationPKey) {
                                item.label = target.segmentationPName;
                                return item;
                            }
                        });
                        console.log(obj);
                        this.getSegmentSummary();
                        this.getSegmentProfile();
                    } else {
                        this.$message({
                            type: 'error',
                            message:res.msg
                        });
                    }
                });
            },getSegmentPurchaseThemes2(){
                let target = this;
                let params = {datasetKey:this.datasetKey,projectId:this.projectId,analysisKey:this.factorKey,numFactor:this.numFactorP};
                post('/segment/getSegmentPurchaseThemes',params,res=>{
                    target.segmentNames2=res.data;
                });
            },getPoolDepartments(deptCodes) {
                let target = this;

                let params = {datasetKey:this.datasetKey,projectId:this.projectId,deptCodes:deptCodes};
                post('/segment/getPoolDepartments',params,res=>{
                    target.deptOptions = [];
                    for(let i = 0;i<res.data.length;i++) {
                        let obj = {};
                        obj.value=res.data[i].prod_sys_key;
                        obj.label=res.data[i].dept_desc;
                        target.deptOptions.push(obj);
                    }
                });
			},runSegmentProdSummary() {
                let target = this;
                let params = {datasetKey:this.datasetKey,projectId:this.projectId,analysisKey:this.segmentationKey,numSegment:this.numSegment};
                post('/segment/runSegmentProdSummary',params,res=>{
                    if(res.code==2000000) {
                        this.$message({
                            type: 'success',
                            message:res.data
                        });
                    } else {
                        this.$message({
                            type: 'error',
                            message:res.msg
                        });
                    }
                });
			},checkIfProfileBuild () {
                let target = this;
                let params = {datasetKey:this.datasetKey,projectId:this.projectId,analysisKey:this.segmentationKey};
                post('/segment/checkIfProfileBuild',params,res=>{
					target.buildCount = res.data.count;
                });
			}
        	
        }
    }

});



