package com.january0001.project.forumbackend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.january0001.project.forumbackend.model.Permissions;
import lombok.extern.log4j.Log4j;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;


@Converter
@Log4j
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
            throw new RuntimeException("Error converting permissions to JSON", e);
        }
    }

    @Override
    public Permissions convertToEntityAttribute(String dataBaseData) {
        if (dataBaseData == null) {
            return Permissions.noPerms();
        } else try {
            return objectMapper.readValue(dataBaseData, Permissions.class);
        } catch (JacksonException e) {
            throw new RuntimeException("Error parsing permissions to JSON" +  dataBaseData, e);
        }
    }
}
