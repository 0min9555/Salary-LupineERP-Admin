package com.yangjae.lupine;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    void test() {
        String url = "jdbc:log4jdbc:mysql://110.8.133.196:3306/tcast?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "3091";
        String smsAccessKey = "B73A6E5C20E9AC06A8B0";
        String smsSecretKey = "9F036A6E5D3C37AE644710CC09F5C2E976E7106B";
        String serviceId = "ncp:sms:kr:304854711957:tcast";
        String senderPhone = "07081453146";

        System.out.println("URL : "+jasyptEncoding(url));
        System.out.println("ID : "+jasyptEncoding(username));
        System.out.println("PW : "+jasyptEncoding(password));
        System.out.println("Access Key : "+jasyptEncoding(smsAccessKey));
        System.out.println("Secret Key : "+jasyptEncoding(smsSecretKey));
        System.out.println("serviceId : "+jasyptEncoding(serviceId));
        System.out.println("senderPhone : "+jasyptEncoding(senderPhone));
    }

    public String jasyptEncoding(String value) {

        String key = "tcast";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }

}
