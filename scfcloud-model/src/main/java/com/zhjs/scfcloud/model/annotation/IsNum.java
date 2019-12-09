package com.zhjs.scfcloud.model.annotation;

import cn.hutool.core.util.NumberUtil;
import com.zhjs.scfcloud.util.util.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IsNum.LongChecker.class })
public @interface IsNum {
    String message() default "必须是整数";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};

    class LongChecker implements ConstraintValidator<IsNum, Object> {

        @Override
        public void initialize(IsNum constraintAnnotation) {

        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (StringUtil.isEmpty(value)){ //为空不做校验
                return true;
            }
            if (StringUtil.isNum(value)) { //为数字
               return true;
            }
            return false;
        }

    }
}

