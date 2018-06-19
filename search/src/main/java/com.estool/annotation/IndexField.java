package com.estool.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IndexField {

	/**
	 * Defines name of the special es index field.
	 */
	String value() default "";

}
