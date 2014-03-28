package com.estool.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsIndex {
	String indexName() default "";

	String[] indexTypes() default "";

}
