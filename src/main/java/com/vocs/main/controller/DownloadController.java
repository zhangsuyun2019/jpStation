package com.vocs.main.controller;

import com.github.pagehelper.util.StringUtil;
import com.vocs.main.bean.FilesDto;
import com.vocs.main.bean.UserIntegralDto;
import com.vocs.main.pojo.Files;
import com.vocs.main.pojo.UserIntegral;
import com.vocs.main.service.FileService;
import com.vocs.main.service.UserService;
import com.vocs.main.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * * 下载文件controller
 *
 * @author
 * @date 2019-10-13 13:00
 */
@Slf4j
@RequestMapping("download")
@Controller
@CrossOrigin
public class DownloadController {

  @Autowired private UserService userService;

  @Autowired private FileService fileService;

  @Value("${uploadFile.filePath}")
  private String filePath;

  @RequestMapping("/files")
  @ResponseBody
  public String downLoad(HttpServletResponse response, @RequestBody FileVo fileVo)
      throws UnsupportedEncodingException {
    if (null == fileVo || StringUtil.isEmpty(fileVo.getFileName())) {
      return "文件名不能为空";
    }
    File file = new File(this.filePath + "/" + fileVo.getFileName());
    // 判断文件父目录是否存在
    if (file.exists()) {
      // 根据文件名查找文件对象
      FilesDto filesDto = new FilesDto();
      filesDto.setFileName(fileVo.getFileName());
      List<Files> list = fileService.searchFileList(filesDto);
      if (CollectionUtils.isEmpty(list)) {
        return fileVo.getFileName() + "文件不存在";
      }

      Files files = list.get(0);
      // 根据用户id获取用户余额
      UserIntegralDto userIntegralDto = new UserIntegralDto();
      userIntegralDto.setUserId(fileVo.getUserId());
      List<UserIntegralDto> userIntegralDtoList = userService.searchIntegral(userIntegralDto);
      if (CollectionUtils.isEmpty(userIntegralDtoList)) {
        return fileVo.getUserId() + "对应的余额不存在";
      }
      UserIntegralDto userIntegralDtoFinal = userIntegralDtoList.get(0);
      if (userIntegralDtoFinal.getBalance().compareTo(files.getAmount()) < 0) {
        return fileVo.getUserId() + "余额不足，不能下载";
      }
      // 扣除下载金额
      BigDecimal balence = userIntegralDtoFinal.getBalance().subtract(files.getAmount());
      userIntegralDtoFinal.setBalance(
          balence.compareTo(BigDecimal.ZERO) > 0 ? balence : new BigDecimal("0.00"));

      response.setContentType("application/force-download"); // 设置强制下载不打开
      String excelFileName = URLEncoder.encode(fileVo.getFileName(), "UTF-8");
      response.addHeader("Content-Disposition", "attachment;fileName=" + excelFileName); // 设置文件名
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

        // 扣除用户余额
        userIntegralDtoFinal.setUpdateTime(new Date());
        UserIntegral userIntegral = new UserIntegral();
        BeanUtils.copyProperties(userIntegralDtoFinal, userIntegral);
        UserIntegralDto userIntegralDtoUpdate = userService.updateIntegral(userIntegral);
        if (null != userIntegralDtoUpdate) {
          log.info("更新用户积分成功");
        }
        return null;
      } catch (Exception e) {
        log.warn("下载文件异常:{}", e);
      } finally {
        log.info("----------file download---" + fileVo.getFileName());
        try {
          if (bis != null) {
            bis.close();
          }
          if (fis != null) {
            fis.close();
          }
          if (os != null) {
            os.close();
          }
        } catch (IOException e) {
          log.warn("关闭流文件异常:{}", e);
        }
      }
    }
    return null;
  }
}
