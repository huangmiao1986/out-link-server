package com.out.link.server.http.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.out.link.server.http.log.LoggerFactory;

@Controller
public class UploadFileController {
	public Logger loggerInfo = LoggerFactory.getServerInfoLogger(UploadFileController.class);
	public Logger logger = LoggerFactory.getServerErrorLogger(UploadFileController.class);
	@RequestMapping(value = "action/photo/uploadAvatar", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String uploadAvatarPhoto(HttpServletRequest request,
			@RequestParam(value="userId",required = true) String userId)  throws ServletException, IOException{
			final long imgMaxSize = 1024*1024; //1M
		 	final String[] allowedImg = new String[]{"jpg", "jpeg", "gif"};
		 	try {
		 		request.setCharacterEncoding("utf-8");  
				uploadFile(request,allowedImg,imgMaxSize,userId);
			} catch(FileUploadException e){
				logger.error("上传头像异常", e);
				return  "{ \"ret\" : 1, \"err\" : \"upload file format is not allowed!\"}";
			}catch (Exception e) {
				logger.error("上传头像异常", e);
				return  "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
			}
		 	return  "{ \"ret\" :0}";
	}
	
	@SuppressWarnings("unchecked")
	private void uploadFile(HttpServletRequest request,String[] allowedType,long maxSize,String userId) throws Exception,FileUploadException {
		 //获得磁盘文件条目工厂。  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件上传需要保存的路径，upload文件夹需存在。  
        String path = request.getSession().getServletContext().getRealPath("/upload/avatar_photo");  
        //设置暂时存放文件的存储室，这个存储室可以和最终存储文件的文件夹不同。因为当文件很大的话会占用过多内存所以设置存储室。  
        factory.setRepository(new File(path));  
        //设置缓存的大小，当上传文件的容量超过缓存时，就放到暂时存储室。  
        factory.setSizeThreshold(1024*1024);  
        //上传处理工具类（高水平API上传处理？）  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        upload.setSizeMax(maxSize);
        //调用 parseRequest（request）方法  获得上传文件 FileItem 的集合list 可实现多文件上传。  
        List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
        for(FileItem item:list){  
            //获取表单属性名字。  
            String name = item.getFieldName();  
            //如果获取的表单信息是普通的文本信息。即通过页面表单形式传递来的字符串。  
            if(item.isFormField()){  
                //获取用户具体输入的字符串，  
                String value = item.getString();  
                request.setAttribute(name, value);  
            }  
            //如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。  
            else{   
                //获取路径名  
                String value = item.getName();  
                //取到最后一个反斜杠。  
                int start = value.lastIndexOf("\\");  
                //截取上传文件的 字符串名字。+1是去掉反斜杠。  
                String filename = value.substring(start+1);  
                boolean allowedFlag = false;
                String fileFormat = filename.substring(filename.lastIndexOf(".") + 1);
                for (String allowed : allowedType)
                {
                    if (allowed.equals(fileFormat)) {
                    	allowedFlag = true;
                        break;
                    }
                }
                request.setAttribute(name, filename);  
                /*写到文件中*/  
                if(allowedFlag) {
                	loggerInfo.info("获取文件总量的容量:"+ item.getSize());
                	item.write(new File(path,userId));
	            }  
	        }  
        }
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	 public String handleIOException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	 }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public String handleMissParaException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	}
}
