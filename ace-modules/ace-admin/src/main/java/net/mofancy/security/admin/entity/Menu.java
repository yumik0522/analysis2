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
@TableName("base_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 路径编码
     */
    private String code;

    /**
     * 标题
     */
    private String title;

    /**
     * 父级节点
     */
    private Integer parentId;

    /**
     * 资源路径
     */
    private String href;

    /**
     * 图标
     */
    private String icon;

    private String type;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 描述
     */
    private String description;

    /**
     * 菜单上下级关系
     */
    private String path;

    /**
     * 启用禁用
     */
    private String enabled;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

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
