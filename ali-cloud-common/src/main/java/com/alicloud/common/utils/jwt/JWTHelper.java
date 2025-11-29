package com.alicloud.common.utils.jwt;

import org.joda.time.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JWTHelper {
	private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

	/**
	 * 密钥加密token
	 *
	 * @param jwtInfo
	 * @param priKeyPath
	 * @param expire
	 * @return
	 * @throws Exception
	 */
	public static String generateToken(JWTInfo jwtInfo, String priKeyPath, int expire) throws Exception {
		PrivateKey privateKey = rsaKeyHelper.getPrivateKey(priKeyPath);
		String compactJws = Jwts.builder()
				.subject(jwtInfo.getId())
				.claims(jwtInfo.toJsonObj())
				.expiration(DateTime.now().plusMinutes(expire).toDate())
				.signWith(privateKey, Jwts.SIG.RS256)
				.compact();
		return compactJws;
	}

	/**
	 * 密钥加密token
	 *
	 * @param jwtInfo
	 * @param priKey
	 * @param expire
	 * @return
	 * @throws Exception
	 */
	public static String generateToken(JWTInfo jwtInfo, byte priKey[], int expire) throws Exception {
        jwtInfo.setType("token");
		PrivateKey privateKey = rsaKeyHelper.getPrivateKey(priKey);
		String compactJws = Jwts.builder()
				.subject(jwtInfo.getId())
				.claims(jwtInfo.toJsonObj())
				.expiration(DateTime.now().plusMinutes(expire).toDate())
				.signWith(privateKey, Jwts.SIG.RS256)
				.compact();
		return compactJws;
	}

    public static String generateRefreshToken(JWTInfo jwtInfo, byte priKey[], int expire) throws Exception {
        jwtInfo.setType("refresh_token");
        PrivateKey privateKey = rsaKeyHelper.getPrivateKey(priKey);
        String compactJws = Jwts.builder()
                .subject(jwtInfo.getId())
                .claims(jwtInfo.toJsonObj())
                .expiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
        return compactJws;
    }


	/**
	 * 公钥解析token
	 *
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
		PublicKey publicKey = rsaKeyHelper.getPublicKey(pubKeyPath);
		Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(publicKey)
                .build()
				.parseSignedClaims(token);
		return claimsJws;
	}

	/**
	 * 公钥解析token
	 *
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
		PublicKey publicKey = rsaKeyHelper.getPublicKey(pubKey);
		Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(publicKey)
				.build()
				.parseSignedClaims(token);
		return claimsJws;
	}

	/**
	 * 获取token中的用户信息
	 *
	 * @param token
	 * @param pubKeyPath
	 * @return
	 * @throws Exception
	 */
	public static JWTInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
		Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
		Claims body = claimsJws.getPayload();
		JWTInfo info = JWTInfo.of(body);
		return info;
	}

	/**
	 * 获取token中的用户信息
	 *
	 * @param token
	 * @param pubKey
	 * @return
	 * @throws Exception
	 */
	public static JWTInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
		Jws<Claims> claimsJws = parserToken(token, pubKey);
		Claims body = claimsJws.getPayload();
		JWTInfo info = JWTInfo.of(body);
		return info;
	}
}
