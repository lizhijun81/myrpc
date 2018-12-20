package com.myrpc.config.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Service {

	Class<?> interfaceClass() default void.class;

	String interfaceName() default "";

	String application() default "";

	String registry() default "";

	String protocol() default "";

	String version() default "";

	String group() default "";

	int timeout() default 0;
}
