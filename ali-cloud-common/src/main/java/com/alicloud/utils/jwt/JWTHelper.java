package com.alicloud.utils.jwt;

import org.joda.time.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
		String compactJws = Jwts.builder().setSubject(jwtInfo.getId())
				.addClaims(jwtInfo.toJsonObj())
				.setExpiration(DateTime.now().plusMinutes(expire).toDate())
				.signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath)).compact();
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
		String compactJws = Jwts.builder().setSubject(jwtInfo.getId())
				.addClaims(jwtInfo.toJsonObj())
				.setExpiration(DateTime.now().plusMinutes(expire).toDate())
				.signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey)).compact();
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
		Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath))
				.parseClaimsJws(token);
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
		Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
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
		Claims body = claimsJws.getBody();
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
		Claims body = claimsJws.getBody();
		JWTInfo info = JWTInfo.of(body);
		return info;
	}
}
