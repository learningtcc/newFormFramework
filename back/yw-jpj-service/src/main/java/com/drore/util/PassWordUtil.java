package com.drore.util;

import com.drore.domain.sys.SysUser;
import org.apache.commons.lang.StringUtils;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:密码工具类 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月26日 上午9:42:26 <br/>
 * 作者: wdz
 */
public class PassWordUtil {
	 
	public static void main(String[] args) {
		getSysUser("123456","");
	}

	/**
	 * 传入未加密密码，如果为空，随机生成, 传入 如果盐值为空，随机生成 返回密码，盐值，和加密后的密码
	 * 
	 * @param password
	 * @return
	 */
	public static String[] getSysUser(String password, String pwdSalt) {
		String[] str = new String[3];
		if (StringUtils.isEmpty(password))
			password = RadomUtil.getRandomString(SysUser.PWD_LENGTH);
		// 密码盐值
		byte[] salt = null;
		// 密码盐值 如果是为空，那么随机获取
		if (StringUtils.isEmpty(pwdSalt)) {
			salt = Digests.generateSalt(SysUser.SALT_SIZE);
		} else
			salt = EncodeUtils.hexDecode(pwdSalt);
		// 密码加密
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt,
				SysUser.HASH_INTERATIONS);
		str[0] = password;
		str[1] = EncodeUtils.hexEncode(salt);
		str[2] = EncodeUtils.hexEncode(hashPassword);
		return str;

	}

	/**
	 * 传入未加密密码，如果为空，随机生成, 传入 如果盐值为空，随机生成 返回密码，盐值，和加密后的密码
	 *
	 * @param password
	 * @return
	 */
	public static String[] getStoreUser(String password, String pwdSalt) {
		String[] str = new String[3];
		if (StringUtils.isEmpty(password))
			password = RadomUtil.getRandomString(SysUser.PWD_LENGTH);
		// 密码盐值
		byte[] salt = null;
		// 密码盐值 如果是为空，那么随机获取
		if (StringUtils.isEmpty(pwdSalt)) {
			salt = Digests.generateSalt(SysUser.SALT_SIZE);
		} else
			salt = EncodeUtils.hexDecode(pwdSalt);
		// 密码加密
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt,
				SysUser.HASH_INTERATIONS);
		str[0] = password;
		str[1] = EncodeUtils.hexEncode(salt);
		str[2] = EncodeUtils.hexEncode(hashPassword);
		return str;

	}


	/**
	 * 传入未加密密码，如果为空，随机生成, 传入 如果盐值为空，随机生成 返回密码，盐值，和加密后的密码
	 *
	 * @param password
	 * @return
	 */
	public static String[] getTenant(String password, String pwdSalt) {
		String[] str = new String[3];
		if (StringUtils.isEmpty(password))
			password = RadomUtil.getRandomString(8);
		// 密码盐值
		byte[] salt = null;
		// 密码盐值 如果是为空，那么随机获取
		if (StringUtils.isEmpty(pwdSalt)) {
			salt = Digests.generateSalt(10);
		} else
			salt = EncodeUtils.hexDecode(pwdSalt);
		// 密码加密
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt,
				1024);
		str[0] = password;
		str[1] = EncodeUtils.hexEncode(salt);
		str[2] = EncodeUtils.hexEncode(hashPassword);
		return str;

	}
}
