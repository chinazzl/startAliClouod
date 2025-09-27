package com.alicloud.base;

import org.junit.Test;

/**
 * @author: zhaolin
 * @Date: 2024/12/26
 * @Description:
 **/
public class BaseTest {

    // 打印等边三角形
    @Test
    public void print() {
        int row = 6;
        for (int i = 0; i < row; i++) {
            for (int j = 1; j <= row -i; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= 2 * i -1; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

}
