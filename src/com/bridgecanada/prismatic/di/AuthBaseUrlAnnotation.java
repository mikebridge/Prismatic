package com.bridgecanada.prismatic.di;


  import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;
/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface AuthBaseUrlAnnotation {}
