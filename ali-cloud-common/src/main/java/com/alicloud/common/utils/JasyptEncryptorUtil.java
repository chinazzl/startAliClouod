package com.alicloud.common.utils;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * @author: zhaolin
 * @Date: 2025/11/11
 * @Description: 加解密工具
 **/
public class JasyptEncryptorUtil {
    /**
     * 创建加密器
     * @param password 加密密钥
     * @return 加密器实例
     */
    public static StringEncryptor createEncryptor(String password) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(password);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * 加密
     * @param plainText 明文
     * @param password 加密密钥
     * @return 密文
     */
    public static String encrypt(String plainText, String password) {
        StringEncryptor encryptor = createEncryptor(password);
        return encryptor.encrypt(plainText);
    }

    /**
     * 解密
     * @param encryptedText 密文
     * @param password 加密密钥
     * @return 明文
     */
    public static String decrypt(String encryptedText, String password) {
        StringEncryptor encryptor = createEncryptor(password);
        return encryptor.decrypt(encryptedText);
    }

    /**
     * 测试加密解密
     */
    public static void main(String[] args) {
//        args = new String[]{"encrypt","H31ThYM0n1t0r","1qaz!QAZ"};
        args = new String[]{"encrypt","H31ThYM0n1t0r","redis_zzl"};
        if (args.length < 2) {
            System.out.println("使用方法:");
            System.out.println("加密: java JasyptEncryptorUtil encrypt <密钥> <要加密的文本>");
            System.out.println("解密: java JasyptEncryptorUtil decrypt <密钥> <要解密的文本>");
            System.out.println("\n示例:");
            System.out.println("java JasyptEncryptorUtil encrypt mySecretKey123 root123456");
            return;
        }

        String operation = args[0];
        String password = args[1];
        String text = args[2];

        try {
            if ("encrypt".equalsIgnoreCase(operation)) {
                String encrypted = encrypt(text, password);
                System.out.println("原文: " + text);
                System.out.println("密文: " + encrypted);
                System.out.println("\n在配置文件中使用: ENC(" + encrypted + ")");
            } else if ("decrypt".equalsIgnoreCase(operation)) {
                String decrypted = decrypt(text, password);
                System.out.println("密文: " + text);
                System.out.println("原文: " + decrypted);
            } else {
                System.out.println("未知操作: " + operation);
                System.out.println("请使用 encrypt 或 decrypt");
            }
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 批量加密示例
     */
    public static void batchEncryptExample(String encryptKey) {

        System.out.println("=== 开发环境配置加密 ===");
        System.out.println("数据库密码: ENC(" + encrypt("dev_password", encryptKey) + ")");
        System.out.println("Redis密码: ENC(" + encrypt("dev_redis_pwd", encryptKey) + ")");

        System.out.println("\n=== 生产环境配置加密 ===");
        System.out.println("数据库密码: ENC(" + encrypt("prod_password", encryptKey) + ")");
        System.out.println("Redis密码: ENC(" + encrypt("prod_redis_pwd", encryptKey) + ")");
    }
}
