package com.yangjae.lupine.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ColumnConverter implements AttributeConverter<String, String> {

    private final AESUtil aesUtil = new AESUtil();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return aesUtil.encrypt(attribute);
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting attribute", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return aesUtil.decrypt(dbData);
        } catch (Exception e) {
            throw new IllegalStateException("Error decrypting attribute", e);
        }
    }
}
