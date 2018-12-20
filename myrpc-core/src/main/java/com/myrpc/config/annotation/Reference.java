package com.myrpc.config.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Reference {

	Class<?> interfaceClass() default void.class;

	String interfaceName() default "";

	String application() default "";

	String registry() default "";

	String protocol() default "";

	String version() default "";

	String group() default "";

	int timeout() default 0;

	int retries() default 0;

	int connecttimeout() default 0;

	String proxy() default "";

	String cluster() default "";

	String loadbalance() default "";
}
