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
 * 
 * </p>
 *
 * @author zhangweiqi
 * @since 2019-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_group_type")
public class GroupType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date crtTime;

    /**
     * 创建人ID
     */
    private String crtUser;

    /**
     * 创建人
     */
    private String crtName;

    /**
     * 创建主机
     */
    private String crtHost;

    /**
     * 最后更新时间
     */
    private Date updTime;

    /**
     * 最后更新人ID
     */
    private String updUser;

    /**
     * 最后更新人
     */
    private String updName;

    /**
     * 最后更新主机
     */
    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;


}
