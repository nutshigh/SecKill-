package com.example.seckill.vo;

import com.example.seckill.utils.ValidatorUtill;
import com.example.seckill.validator.IsMobile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.thymeleaf.util.StringUtils;

//手机号码校验规则类
public class isMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtill.isMobile(s);
        }else{
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidatorUtill.isMobile(s);
            }
        }
    }


}
