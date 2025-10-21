package org.example;

import java.lang.reflect.*;
import java.util.ArrayList;


public class DeepCopy {

    public Object clone(Object object) {
        Class<?> classType = object.getClass();
        Object result = null;
        try {
            boolean isAccessible = classType.getDeclaredConstructor().isAccessible();
            classType.getDeclaredConstructor().setAccessible(true);
            result = classType.getDeclaredConstructor().newInstance();
            classType.getDeclaredConstructor().setAccessible(isAccessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(classType != null) {
            Field[] fields = classType.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType.equals(ArrayList.class)) {
                    try {
                        ArrayList<?> originalList = (ArrayList<?>) field.get(object);
                        ArrayList<Object> clonedList = new ArrayList<>();
                        for (Object item : originalList) {
                            if (item instanceof Field) {
                                Object value = ((Field) item).get(item);
                                ((Field) item).set(result, value);
                            }

                        }
                        field.set(result, clonedList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(object);
                        field.set(result, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            classType = classType.getSuperclass();
        }
        return result;
    }
}
