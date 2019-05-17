package com.zd.secutiry;

import com.alibaba.fastjson.JSON;
import com.sf.pg.entity.R;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * JWT生成，和验签的类
 * Created by zDong on 2018/11/15 下午5:36
 */
public class TokenAuthenticationService {
	/**
	 * 5天
	 */
	private static final long EXPIRATIONTIME = 432_000_000;

	/**
	 * JWT密码
	 */
	private static final String SECRET = "P@ssw02d";

	/**
	 * Token前缀
	 */
	private static final String TOKEN_PREFIX = "Bearer";

	/**
	 * 存放 Token的Header Key
	 */
	private static final String HEADER_STRING = "Authorization";

	/**
	 * 生成JWT
	 * @param response httpResponse
	 * @param auth Authentication
	 */
	public static void addAuthentication(HttpServletResponse response, Authentication auth) {
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		if (iterator.hasNext()) {
			for (GrantedAuthority authority : authorities) {
				stringBuilder.append(authority.getAuthority());
				stringBuilder.append(",");
			}
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		String jwt = Jwts.builder()
				// 保存权限（角色）
				.claim("authorities", stringBuilder.toString())
				// 用户名写入标题
				.setSubject(auth.getName())
				// 有效期设置
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				// 签名设置（头部）
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.claim("uid", "100000")
				.compact();
		try {
			response.getOutputStream().println(JSON.toJSONString(R.success(jwt)));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * 校验请求头中的 jwt
	 * @param request httpRequest
	 * @return
	 */
	public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
		if (StringUtils.isNotBlank(token)) {
			try {
				// 解析 Token
				Claims claims = Jwts.parser()
						// 验签（盐值）
						.setSigningKey(SECRET)
						// 去掉 Bearer
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody();
				// 拿用户名
				String user = claims.getSubject();
				List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
				// 返回验证令牌
				return user != null ?
						new UsernamePasswordAuthenticationToken(user, null, authorities) :
						null;
			} catch (MalformedJwtException e) {
				throw new MalformedJwtException(e.getMessage());
			}
		}
		throw new MalformedJwtException("jwt格式不正确");
	}
}
