package net.mofancy.security.admin.entity;

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
public class GateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单
     */
    private String menu;

    /**
     * 操作
     */
    private String opt;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 操作时间
     */
    private Date crtTime;

    /**
     * 操作人ID
     */
    private String crtUser;

    /**
     * 操作人
     */
    private String crtName;

    /**
     * 操作主机
     */
    private String crtHost;

    private String body;


}
