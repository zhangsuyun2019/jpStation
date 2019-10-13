package com.vocs.main.controller;

import com.github.pagehelper.util.StringUtil;
import com.vocs.main.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/***
 * 下载文件controller
 * @author
 * @date 2019-10-13 13:00
 */
@Slf4j
@RequestMapping("download")
@Controller
@CrossOrigin
public class DownloadController {

  @Value("${uploadFile.filePath}")
  private String filePath;

  @PostMapping("/files")
  @ResponseBody
  public String downLoad(HttpServletResponse response, @RequestBody FileVo FileVo) throws UnsupportedEncodingException {
    if (null == FileVo || StringUtil.isEmpty(FileVo.getFileName())) {
      return "文件名不能为空";
    }
    File file = new File(this.filePath + "/" + FileVo.getFileName());
    // 判断文件父目录是否存在
    if (file.exists()) {
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/force-download");
      response.setHeader(
          "Content-Disposition",
          "attachment;fileName=" + java.net.URLEncoder.encode(FileVo.getFileName(), "UTF-8"));
      byte[] buffer = new byte[1024];
      // 文件输入流
      FileInputStream fis = null;
      BufferedInputStream bis = null;

      // 输出流
      OutputStream os = null;
      try {
        os = response.getOutputStream();
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        int i = bis.read(buffer);
        while (i != -1) {
          os.write(buffer);
          i = bis.read(buffer);
        }

      } catch (Exception e) {
        log.warn("下载文件异常:{}",e);
      } finally{
        log.info("----------file download---" + FileVo.getFileName());
        try {
          if (bis != null) {
            bis.close();
          }
          if (fis != null) {
            fis.close();
          }
        } catch (IOException e) {
          log.warn("关闭流文件异常:{}",e);
        }
      }
    }
    return null;
  }
}
