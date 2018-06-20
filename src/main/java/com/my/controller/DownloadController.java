package com.my.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.util.Log4jUtil;

@Controller
public class DownloadController {

	private static final long serialVersionUID = 5097542735180737884L;

	@RequestMapping(value = "download", method = { RequestMethod.POST,RequestMethod.GET })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Log4jUtil.log.warn("--œ¬‘ÿ“≥√Ê");
		request.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");
		// String header = request.getHeader("user-agent");
		// if(header.toLowerCase().indexOf("mobile") != -1){//PC
		request.getRequestDispatcher("download/wap.html").forward(request, response);
		// response.sendRedirect("download/wap.html");
		// }else{
		// request.getRequestDispatcher("download/web.html").forward(request,
		// response);
		// out.print("µÁƒ‘");
		// response.sendRedirect("download/web.html");
		// }
	}

}
