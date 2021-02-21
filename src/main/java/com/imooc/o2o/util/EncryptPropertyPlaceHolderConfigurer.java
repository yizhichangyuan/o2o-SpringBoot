package com.imooc.o2o.util;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class EncryptPropertyPlaceHolderConfigurer extends PropertySourcesPlaceholderConfigurer {
    private String[] encryptPropNames = {"jdbc.master.username", "jdbc.master.password",
            "jdbc.slave.username", "jdbc.slave.password"}; // 要加密的参数名

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProperty(propertyName)) {
            String decryptValue = DESUtils.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProperty(String propertyName) {
        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
