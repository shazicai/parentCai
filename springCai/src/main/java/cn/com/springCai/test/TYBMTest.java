
package cn.com.springCai.test;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @Author caiJH
 * @Date 2026/3/25 11:10 AM
 * @Version 1.0
 */
public class TYBMTest {
    /**
     * 主函数，用于测试冒泡排序算法
     *
     * @param args 命令行参数（注：无实际作用）
     */
    public static void main(String[] args) {
        int[] arr = {3, 1, 5, 2, 4};
        int[] sortedArr = bubbleSort(arr);
        System.out.println(Arrays.toString(sortedArr));

        Student source = new Student();
        source.setName("张三");
        source.setAge(20);
        source.setScore(95.5);

        Student target = new Student();
        copyProperties(source, target);

        System.out.println("目标对象：" + target);
    }

    /**
     * 使用冒泡排序算法对整型数组进行升序排序
     *
     * @param arr 待排序的整型数组
     * @return 排序后的数组
     */
    public static int[] bubbleSort(int[] arr) {
        int temp = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 通用属性复制方法：将 source 对象的属性值复制到 target 对象
     * 支持不同类但具有相同属性名和兼容类型的对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("源对象和目标对象不能为空");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            sourceField.setAccessible(true);

            try {
                Object value = sourceField.get(source);

                if (value != null) {
                    try {
                        Field targetField = targetClass.getDeclaredField(fieldName);
                        targetField.setAccessible(true);

                        if (isAssignable(sourceField.getType(), targetField.getType())) {
                            targetField.set(target, value);
                        }
                    } catch (NoSuchFieldException e) {
                        System.out.println("目标对象不存在字段：" + fieldName);
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("无法访问字段：" + fieldName);
            }
        }
    }

    /**
     * 判断源类型是否可以赋值给目标类型
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 是否可赋值
     */
    private static boolean isAssignable(Class<?> sourceType, Class<?> targetType) {
        if (sourceType.equals(targetType)) {
            return true;
        }

        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }

        if (sourceType.isPrimitive() && targetType.isAssignableFrom(getWrapperType(sourceType))) {
            return true;
        }

        if (targetType.isPrimitive() && sourceType.isAssignableFrom(getWrapperType(targetType))) {
            return true;
        }

        return false;
    }

    /**
     * 获取基本类型对应的包装类型
     *
     * @param primitiveType 基本类型
     * @return 包装类型
     */
    private static Class<?> getWrapperType(Class<?> primitiveType) {
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == double.class) return Double.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == void.class) return Void.class;
        return primitiveType;
    }
}

class Student {
    private String name;
    private int age;
    private double score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", score=" + score + "}";
    }
}