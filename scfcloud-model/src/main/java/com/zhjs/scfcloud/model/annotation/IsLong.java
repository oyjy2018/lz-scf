package com.zhjs.scfcloud.model.annotation;

import com.zhjs.scfcloud.util.util.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IsLong.LongChecker.class })
public @interface IsLong {
    String message() default "必须是长整型数字";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};

    public static class LongChecker implements ConstraintValidator<IsLong, Object> {

        @Override
        public void initialize(IsLong constraintAnnotation) {

        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (StringUtil.isEmpty(value)){ //为空不做校验
                return true;
            }
            try {
               java.lang.Long.parseLong(value.toString());//转为整型
            }catch (Exception e) {
               return false;    //转型发生异常 则不为整型
            }

            return true;
        }

    }
}

