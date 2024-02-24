package com.example.beprojectsem4.helper;

import java.lang.reflect.Field;

public class TransferValuesIfNull {
    public static <T> T transferValuesIfNull(T obj1, T obj2) {
        Field[] fieldsObj1 = obj1.getClass().getDeclaredFields();
        Field[] fieldsObj2 = obj2.getClass().getDeclaredFields();

        for (Field fieldObj1 : fieldsObj1) {
            for (Field fieldObj2 : fieldsObj2) {
                if (fieldObj1.getName().equals(fieldObj2.getName())&& fieldObj1.getType().equals(fieldObj2.getType())) {
                    fieldObj1.setAccessible(true);
                    fieldObj2.setAccessible(true);
                    try {
                        if (fieldObj2.get(obj2) == null) {
                            fieldObj2.set(obj2, fieldObj1.get(obj1));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj2;
    }
}
