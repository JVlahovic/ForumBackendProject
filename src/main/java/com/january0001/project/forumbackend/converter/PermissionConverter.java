package com.january0001.project.forumbackend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.january0001.project.forumbackend.security.util.Permissions;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;


@Converter
@Slf4j
public class PermissionConverter implements AttributeConverter<Permissions, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Permissions permissions) {
        if(permissions == null) {
            return null; //perhaps this might be worth revisiting? Maybe returning something else?
        }
        else try {
            return objectMapper.writeValueAsString(permissions);
        } catch (JacksonException e) {
            log.error("Failed to serialize permissions to JSON.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Permissions convertToEntityAttribute(String dataBaseData) {
        if (dataBaseData == null || dataBaseData.isEmpty()) {
            return null;
        } else try {
            return objectMapper.readValue(dataBaseData, Permissions.class);
        } catch (JacksonException e) {
            throw new RuntimeException("Error parsing permissions to JSON" +  dataBaseData, e);
        }
    }
}
