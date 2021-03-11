package net.mofancy.security.api.vo.tbk;

/**
 * @author zwq
 * @version 1.0
 * @date 2019/11/19 0019 下午 6:13
 */
public class MaterialOptionalInfo {

    private String startDsr; //商品筛选(特定媒体支持)-店铺dsr评分。筛选大于等于当前设置的店铺dsr评分的商品0-50000之间
    private String pageSize; //页大小，默认20，1~100
    private String pageNo; //第几页，默认：１
    private String platform; //链接形式：1：PC，2：无线，默认：１
    private String endTkRate; //商品筛选-淘客佣金比率上限。如：1234表示12.34%
    private String startTkRate; //商品筛选-淘客佣金比率下限。如：1234表示12.34%
    private String endPrice; //商品筛选-折扣价范围上限。单位：元
    private String startPrice; //商品筛选-折扣价范围下限。单位：元
    private String isOverseas; //商品筛选-是否海外商品。true表示属于海外商品，false或不设置表示不限
    private String isTmall; //商品筛选-是否天猫商品。true表示属于天猫商品，false或不设置表示不限
    private String sort; //排序_des（降序），排序_asc（升序），销量（total_sales），淘客佣金比率（tk_rate）， 累计推广量（tk_total_sales），总支出佣金（tk_total_commi），价格（price）
    private String itemloc; //商品筛选-所在地
    private String cat; //商品筛选-后台类目ID。用,分割，最大10个，该ID可以通过taobao.itemcats.get接口获取到
    private String q; //商品筛选-查询词
    private String materialId; //不传时默认物料id=2836；如果直接对消费者投放，可使用官方个性化算法优化的搜索物料id=17004
    private String hasCoupon; //优惠券筛选-是否有优惠券。true表示该商品有优惠券，false或不设置表示不限
    private String ip; //ip参数影响邮费获取，如果不传或者传入不准确，邮费无法精准提供
    private String adzoneId; //mm_xxx_xxx_12345678三段式的最后一段数字
    private String needFreeShipment; //商品筛选-是否包邮。true表示包邮，false或不设置表示不限
    private String needPrepay; //商品筛选-是否加入消费者保障。true表示加入，false或不设置表示不限
    private String includePayRate30; //商品筛选(特定媒体支持)-成交转化是否高于行业均值。True表示大于等于，false或不设置表示不限
    private String includeGoodRate; //商品筛选-好评率是否高于行业均值。True表示大于等于，false或不设置表示不限
    private String includeRfdRate; //商品筛选(特定媒体支持)-退款率是否低于行业均值。True表示大于等于，false或不设置表示不限
    private String npxLevel; //商品筛选-牛皮癣程度。取值：1不限，2无，3轻微
    private String endKaTkRate; //商品筛选-KA媒体淘客佣金比率上限。如：1234表示12.34%
    private String startKaTkRate; //商品筛选-KA媒体淘客佣金比率下限。如：1234表示12.34%
    private String deviceEncrypt; //智能匹配-设备号加密类型：MD5
    private String deviceValue; //智能匹配-设备号加密后的值（MD5加密需32位小写）
    private String deviceType; //智能匹配-设备号类型：IMEI，或者IDFA，或者UTDID（UTDID不支持MD5加密），或者OAID
    private String lockRateEndTime; //锁佣结束时间
    private String lockRateStartTime; //锁佣开始时间

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getEndTkRate() {
        return endTkRate;
    }

    public void setEndTkRate(String endTkRate) {
        this.endTkRate = endTkRate;
    }

    public String getStartTkRate() {
        return startTkRate;
    }

    public void setStartTkRate(String startTkRate) {
        this.startTkRate = startTkRate;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getIsOverseas() {
        return isOverseas;
    }

    public void setIsOverseas(String isOverseas) {
        this.isOverseas = isOverseas;
    }

    public String getIsTmall() {
        return isTmall;
    }

    public void setIsTmall(String isTmall) {
        this.isTmall = isTmall;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getItemloc() {
        return itemloc;
    }

    public void setItemloc(String itemloc) {
        this.itemloc = itemloc;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(String hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(String adzoneId) {
        this.adzoneId = adzoneId;
    }

    public String getNeedFreeShipment() {
        return needFreeShipment;
    }

    public void setNeedFreeShipment(String needFreeShipment) {
        this.needFreeShipment = needFreeShipment;
    }

    public String getNeedPrepay() {
        return needPrepay;
    }

    public void setNeedPrepay(String needPrepay) {
        this.needPrepay = needPrepay;
    }

    public String getIncludePayRate30() {
        return includePayRate30;
    }

    public void setIncludePayRate30(String includePayRate30) {
        this.includePayRate30 = includePayRate30;
    }

    public String getIncludeGoodRate() {
        return includeGoodRate;
    }

    public void setIncludeGoodRate(String includeGoodRate) {
        this.includeGoodRate = includeGoodRate;
    }

    public String getIncludeRfdRate() {
        return includeRfdRate;
    }

    public void setIncludeRfdRate(String includeRfdRate) {
        this.includeRfdRate = includeRfdRate;
    }

    public String getNpxLevel() {
        return npxLevel;
    }

    public void setNpxLevel(String npxLevel) {
        this.npxLevel = npxLevel;
    }

    public String getEndKaTkRate() {
        return endKaTkRate;
    }

    public void setEndKaTkRate(String endKaTkRate) {
        this.endKaTkRate = endKaTkRate;
    }

    public String getStartKaTkRate() {
        return startKaTkRate;
    }

    public void setStartKaTkRate(String startKaTkRate) {
        this.startKaTkRate = startKaTkRate;
    }

    public String getDeviceEncrypt() {
        return deviceEncrypt;
    }

    public void setDeviceEncrypt(String deviceEncrypt) {
        this.deviceEncrypt = deviceEncrypt;
    }

    public String getDeviceValue() {
        return deviceValue;
    }

    public void setDeviceValue(String deviceValue) {
        this.deviceValue = deviceValue;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLockRateEndTime() {
        return lockRateEndTime;
    }

    public void setLockRateEndTime(String lockRateEndTime) {
        this.lockRateEndTime = lockRateEndTime;
    }

    public String getLockRateStartTime() {
        return lockRateStartTime;
    }

    public void setLockRateStartTime(String lockRateStartTime) {
        this.lockRateStartTime = lockRateStartTime;
    }

    public String getStartDsr() {
        return startDsr;
    }

    public void setStartDsr(String startDsr) {
        this.startDsr = startDsr;
    }

}
