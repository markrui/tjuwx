package org.tju.wx.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.tju.wx.service.LetterService;
import org.tju.wx.util.MessageProcessor;
import org.tju.wx.util.MessageUtil;
import org.tju.wx.util.SignUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Legend on 2014/7/21.
 */
public class WXMsgProcessAction extends ActionSupport{
    private LetterService letterService;

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 接收参数微信加密签名、 时间戳、随机数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // System.out.println(signature+"111");
        PrintWriter out = response.getWriter();
        LoggerFactory.getLogger(WXMsgProcessAction.class).info("Request!!");
        if(signature != null) {
            String method = ServletActionContext.getRequest().getMethod();
            if (method.equals("POST")) {

                // 设置信的服务
                request.getSession().setAttribute("letterService", letterService);

                // 调用核心服务类接收处理请求
                String respXml = MessageProcessor.processRequest(request);
                Logger logger = LoggerFactory.getLogger(WXMsgProcessAction.class);
                logger.info(respXml);
                out.print(respXml);
            } else {
                // 请求校验
                LoggerFactory.getLogger(WXMsgProcessAction.class).info("check!!");
                if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                    out.print(echostr);
                }
            }
        }
        else{
            out.print("这是用于微信公众平台的服务器！");
        }
        out.close();
        out = null;
        return null;
    }

    public LetterService getLetterService() {
        return letterService;
    }

    public void setLetterService(LetterService letterService) {
        this.letterService = letterService;
    }
}
