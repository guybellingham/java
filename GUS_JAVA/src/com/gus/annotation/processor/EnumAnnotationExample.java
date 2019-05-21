package com.gus.annotation.processor;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public class EnumAnnotationExample {

  public static <E extends Enum, A extends Annotation> Map<E, A> getEnumsAnnotatedWith(
          Class<E> enumClass,
          Class<A> annotationType) {

      Map<E, A> map = new LinkedHashMap<>();
      if (enumClass == null || annotationType == null) {
          return map;
      }
      for (E enumConstant : enumClass.getEnumConstants()) {
          Field declaredField = null;
          try {
              declaredField = enumClass.getDeclaredField(enumConstant.name());
          } catch (NoSuchFieldException e) {
              //this exception will never be thrown
              e.printStackTrace();
          }
          if (declaredField != null) {//should never be null
              A annotation = declaredField.getAnnotation(annotationType);
              if (annotation != null) {
                  map.put(enumConstant, annotation);
              }
          }
      }
      return map;
  }

  public static void main(String[] args) {
      Map<Day, Weekend> map = getEnumsAnnotatedWith(Day.class, Weekend.class);
      map.forEach((k, v) -> System.out.printf("enum= %s, annotation= %s%n", k, v));
  }

  public enum Day {
      Monday,
      Tuesday,
      Wednesday,
      Thursday,
      Friday,
      @Weekend
      Saturday,
      @Weekend
      Sunday
  }

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Weekend {}
}
