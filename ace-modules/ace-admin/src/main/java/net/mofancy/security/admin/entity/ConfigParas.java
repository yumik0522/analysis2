package net.mofancy.security.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 常量配置表
 * </p>
 *
 * @author zwq
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wg_config_paras")
public class ConfigParas implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模块编码
     */
    private String moudleId;

    /**
     * 模块名称
     */
    private String moudleName;

    /**
     * 配置组编码
     */
    private String groupId;

    /**
     * 配置组名称
     */
    private String groupName;

    /**
     * 常量编码
     */
    private String paraId;

    /**
     * 常量名称
     */
    private String paraName;

    /**
     * 附加内容
     */
    private String attach;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 1=可用，0=不可用
     */
    private Boolean isValid;

    /**
     * 创建人
     */
    private Integer crtUser;

    /**
     * 创建时间
     */
    private Date crtTime;

    /**
     * 修改人
     */
    private Integer updUser;

    /**
     * 修改时间
     */
    private Date updTime;


}
