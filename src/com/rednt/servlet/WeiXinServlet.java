package com.rednt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rednt.model.TextMessage;
import com.rednt.util.CheckUtil;
import com.rednt.util.MessageUtil;

//@Controller
//@RequestMapping("/weiXin")
public class WeiXinServlet extends HttpServlet {
//	@RequestMapping("/doGet")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature =req.getParameter("signature");
		String timestamp =req.getParameter("timestamp");
		String nonce =req.getParameter("nonce");
		String echostr =req.getParameter("echostr");
		PrintWriter out =resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
//	@RequestMapping("/doPost")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String,String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = "";
			if("text".equals(msgType)){
				TextMessage text = new TextMessage();
				text.setFromUserName(toUserName);
				text.setToUserName(fromUserName);
				text.setMsgType(msgType);
				text.setCreateTime(new Date().getTime());
				text.setContent("您发送的消息是："+content);
				message = MessageUtil.textMessageToXml(text);
			}
			out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}
