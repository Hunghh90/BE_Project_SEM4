package com.example.beprojectsem4.helper;

import com.example.beprojectsem4.entities.UserEntity;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;

public class EntityDtoConverter {
    public static <T, U> U convertToDto(T entity, Class<U> dtoClass) {
        U dto = null;
        try {
            dto = dtoClass.getDeclaredConstructor().newInstance();
            Field[] entityFields = entity.getClass().getDeclaredFields();
            Field[] dtoFields = dtoClass.getDeclaredFields();
            for (Field entityField : entityFields) {
                for (Field dtoField : dtoFields) {
                    if (entityField.getName().equals(dtoField.getName()) && entityField.getType().equals(dtoField.getType())) {
                        entityField.setAccessible(true);
                        dtoField.setAccessible(true);
                        dtoField.set(dto, entityField.get(entity));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public static <T, U> T convertToEntity(U dto, Class<T> entityClass) {
        T entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
            Field[] entityFields = entityClass.getDeclaredFields();
            Field[] dtoFields = dto.getClass().getDeclaredFields();
            for (Field dtoField : dtoFields) {
                for (Field entityField : entityFields) {
                    if (dtoField.getName().equals(entityField.getName()) && dtoField.getType().equals(entityField.getType())) {
                        entityField.setAccessible(true);
                        dtoField.setAccessible(true);
                        entityField.set(entity, dtoField.get(dto));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}
