package com.out.link.server.websocket.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
@WebServlet(name="websocket",urlPatterns="/message")
public class WebSocketMessageServlet extends org.apache.catalina.websocket.WebSocketServlet{

	private static final long serialVersionUID = -5266831343193318242L;
	
	public static int ONLINE_USER_COUNT = 1;  
    
    public String getUser(HttpServletRequest request){  
        return (String) request.getParameter("user");
    }  
    
	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,HttpServletRequest request) {
		 return new WebSocketMessageInbound(this.getUser(request));  
	}
	
}
