package com.drore.util.captcah;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 创建验证码的servlet <br/>
 * 项目名称: guided-assistant-client <br/>
 * 创建日期: 2016年8月29日 上午10:59:27 <br/>
 * 作者: wdz
 */
public class CaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = -124247581620199710L;

	public static final String KEY_CAPTCHA = "SE_KEY_MM_CODE";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置相应类型,告诉浏览器输出的内容为图片
		resp.setContentType("image/jpeg");
		// 不缓存此内容
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expire", 0);
		try {

			HttpSession session = req.getSession();

			CaptchaUtil tool = new CaptchaUtil();
			StringBuffer code = new StringBuffer();
			BufferedImage image = tool.genRandomCodeImage(code);
			session.removeAttribute(KEY_CAPTCHA);
			session.setAttribute(KEY_CAPTCHA, code.toString());

			// 将内存中的图片通过流动形式输出到客户端
			ImageIO.write(image, "JPEG", resp.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
