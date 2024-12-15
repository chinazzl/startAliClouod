package com.alicloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public static <T> Builder<T> builder(){
        return new Builder<T>();
    }

    public static class Builder<T> {
        private int code;
        private String msg;
        private T data;

        public Builder success() {
            this.code = 0;
            this.msg = "success";
            return this;
        }

        public Builder<T> success(T data) {
            this.code = 0;
            this.msg = "success";
            this.data = data;
            return this;
        }

        public Builder<T> failed(int code, String msg) {
            this.code = code;
            this.msg = msg;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public CommonResponse<T> build() {
            return new CommonResponse<>(this.code,this.msg,this.data);
        }
    }
}
