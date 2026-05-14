package cn.com.springCai.service.entity.testUser.vo;


import lombok.Data;
import java.io.Serializable;
import lombok.experimental.Accessors;


/**
 * @author LK
 * @version V1.0
 * @date 2020
 */
@Data
@Accessors(chain = true)
public class TestUserVo implements Serializable {

    private static final long serialVersionUID = -761279406455191420L;
    /**
     * 主键id
     */
    private Integer id;

    private String userName;

    private String classNum;
}