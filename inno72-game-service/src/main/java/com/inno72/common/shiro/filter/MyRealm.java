package com.inno72.common.shiro.filter;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.vo.UserSessionVo;

@Service
public class MyRealm extends AuthorizingRealm {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	/**
	 * 大坑！，必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
	 */
	@SuppressWarnings("unused")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String session = JWTUtil.getSession(principals.toString());
		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(session);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		return simpleAuthorizationInfo;
	}

	/**
	 * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
		// 解密获得username，用于和数据库进行对比
		String session = JWTUtil.getSession(token);
		if (StringUtil.isEmpty(session)) {
			throw new AuthenticationException("token invalid");
		}
		UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(session);
		if (sessionKey == null) {
			throw new AuthenticationException("User didn't existed!");
		}

		if (!JWTUtil.verify(token, session, sessionKey.getMachineCode())) {
			throw new AuthenticationException("Username or password error");
		}

		return new SimpleAuthenticationInfo(token, token, "my_realm");
	}
}
