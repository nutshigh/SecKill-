package com.example.seckill.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author kenly
 * @since 2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TUser implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nickname;

    /**
     * 使用MD5加密，MD5(MD5(pwd明文+固定slat)+slat)
     */
    private String password;

    private String slat;

    /**
     * 头像
     */
    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;


}
