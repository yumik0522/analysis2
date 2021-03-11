package net.mofancy.security.admin.vo;

import lombok.Data;

/**
 * @author zwq
 * @version 1.0
 * @date 2020/3/11 0011 下午 5:59
 */
@Data
public class Goods {

    private String prodCode;

    private String prodDesc;

    private String parentCode;

    private String parentDesc;

    private Integer projectId;

    private String sort;
}
