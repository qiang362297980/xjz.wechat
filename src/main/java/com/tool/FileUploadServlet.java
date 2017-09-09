package com.tool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class FileUploadServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private static Logger log = Logger.getLogger(FileUploadServlet.class);

  public static void doPost(MultipartHttpServletRequest request, HttpServletResponse response)
  {
    JsonResult<String> jsonResult = new JsonResult<String>();

    String savePath = TemplateConfig.getValue("imageSavaPath");

    if (StringUtils.isEmpty(savePath)) {
      savePath = "";
    }

    log.error("imageSavePath:" + savePath);

    File saveFile = new File(savePath);
    if (!saveFile.exists()) {
      saveFile.mkdirs();
    }

    List<MultipartFile> files = request.getFiles("fileData");

    String url = "";
    String pathname = null;
    String currentTime = null;
    String sixRandomNum = null;
    String fileSuffix = "*.jpg";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    try
    {
      for (MultipartFile file : files)
      {
        if ((!StringUtils.isEmpty(file.getOriginalFilename())) && (file.getOriginalFilename().lastIndexOf(".") >= 0)) {
          fileSuffix = file.getOriginalFilename()
            .substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
        }

        currentTime = dateFormat.format(new Date());
        sixRandomNum = String.valueOf(new Double((Math.random() * 9.0D + 1.0D) * 100000.0D).intValue());
        pathname = savePath + currentTime + sixRandomNum + fileSuffix;
        file.transferTo(new File(pathname));
        url = url + TemplateConfig.getValue("imageUrl") + currentTime + sixRandomNum + fileSuffix + ",";
      }

      jsonResult.setCode(0);
      jsonResult.setData(url);
      jsonResult.setMessage("success");
      response.getWriter().print(JSONObject.toJSONString(jsonResult));
      return;
    } catch (IOException e) {
      log.error(e.getMessage());
      log.error(e.getStackTrace());
      jsonResult.setCode(1);
      jsonResult.setData(e.getMessage());
      jsonResult.setMessage("err");
      try
      {
        response.getWriter().print(JSONObject.toJSONString(jsonResult));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  public static void doGet(MultipartHttpServletRequest request, HttpServletResponse response)
  {
    doPost(request, response);
  }
}