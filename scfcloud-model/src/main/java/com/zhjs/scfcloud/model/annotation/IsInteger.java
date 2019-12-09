package com.zhjs.scfcloud.model.annotation;

import com.zhjs.scfcloud.util.util.StringUtil;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IsInteger.IntegerChecker.class })
public @interface IsInteger {
    String message() default "必须是整型数字";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};

    public static class IntegerChecker implements ConstraintValidator<IsInteger, Object> {

        @Override
        public void initialize(IsInteger constraintAnnotation) {

        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (StringUtil.isEmpty(value)){ //为空不做校验
                return true;
            }
            try {
               java.lang.Integer.parseInt(value.toString());//转为整型
            }catch (Exception e) {
               return false;    //转型发生异常 则不为整型
            }

            return true;
        }

    }
}

