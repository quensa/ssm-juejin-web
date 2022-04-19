package com.lf.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 作用：用于发送短信验证码
 * 使用场景：找回密码
 */
public class DxyzmService {

    // 字符串
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 获取长度为 4 的随机数字
     *
     * @return 随机数字
     */
    public static String getNonce_str() {

        // 如果需要4位，那 new char[4] 即可，其他位数同理可得
        char[] nonceChars = new char[4];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }


}