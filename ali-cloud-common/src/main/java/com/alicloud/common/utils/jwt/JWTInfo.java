package com.alicloud.common.utils.jwt;

import java.io.Serializable;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alicloud.common.constant.JSONFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTInfo implements Serializable {
	private String username;
	private String id;
	private String password;
    private String type;
	private Long sessionVersion = 0L;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JWTInfo jwtInfo = (JWTInfo) o;

		if (username != null ? !username.equals(jwtInfo.username) : jwtInfo.username != null) {
			return false;
		}
		return id != null ? id.equals(jwtInfo.id) : jwtInfo.id == null;

	}

	public JSONObject toJsonObj() {
		return JSON.parseObject(JSON.toJSONString(this, JSONFeature.FASTJSON2_WRITE_FEATURES));
	}

	public static JWTInfo of(Object body) {
		return JSON.parseObject(JSON.toJSONString(body, JSONFeature.FASTJSON2_WRITE_FEATURES)
				,JWTInfo.class, JSONFeature.FASTJSON2_READER_FEATURES);
	}
}
