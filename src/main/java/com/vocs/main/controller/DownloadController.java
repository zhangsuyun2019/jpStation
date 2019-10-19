package com.vocs.main.controller;

import com.github.pagehelper.util.StringUtil;
import com.vocs.main.bean.FilesDto;
import com.vocs.main.bean.UserIntegralDto;
import com.vocs.main.pojo.Files;
import com.vocs.main.pojo.UserIntegral;
import com.vocs.main.response.BaseResponse;
import com.vocs.main.service.FileService;
import com.vocs.main.service.UserService;
import com.vocs.main.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
  public BaseResponse<String> files(@RequestBody FileVo fileVo)
      throws UnsupportedEncodingException {
    if (null == fileVo || StringUtil.isEmpty(fileVo.getFileName())) {
      return BaseResponse.fail("900", "文件名不能为空");
    }
    File file = new File(this.filePath + "/" + fileVo.getFileName());
    if (!file.exists()) {
      return BaseResponse.fail("900", "文件不存在");
    }
    // 根据文件名查找文件对象
    FilesDto filesDto = new FilesDto();
    filesDto.setFileName(fileVo.getFileName());
    List<Files> list = fileService.searchFileList(filesDto);
    if (CollectionUtils.isEmpty(list)) {
      return BaseResponse.fail("900",fileVo.getFileName() + "文件不存在");
    }

    Files files = list.get(0);
    // 根据用户id获取用户余额
    UserIntegralDto userIntegralDto = new UserIntegralDto();
    userIntegralDto.setUserId(fileVo.getUserId());
    List<UserIntegralDto> userIntegralDtoList = userService.searchIntegral(userIntegralDto);
    if (CollectionUtils.isEmpty(userIntegralDtoList)) {
      return BaseResponse.fail("900",fileVo.getUserId() + "对应的余额不存在");
    }
    UserIntegralDto userIntegralDtoFinal = userIntegralDtoList.get(0);
    if (userIntegralDtoFinal.getBalance().compareTo(files.getAmount()) < 0) {
      return BaseResponse.fail("900",fileVo.getUserId() + "余额不足，不能下载");
    }
    // 扣除下载金额
    BigDecimal balence = userIntegralDtoFinal.getBalance().subtract(files.getAmount());
    userIntegralDtoFinal.setBalance(
            balence.compareTo(BigDecimal.ZERO) > 0 ? balence : new BigDecimal("0.00"));
    try {
      // 扣除用户余额
      userIntegralDtoFinal.setUpdateTime(new Date());
      UserIntegral userIntegral = new UserIntegral();
      BeanUtils.copyProperties(userIntegralDtoFinal, userIntegral);
      UserIntegralDto userIntegralDtoUpdate = userService.updateIntegral(userIntegral);
      if (null != userIntegralDtoUpdate) {
        log.info("更新用户积分成功");
        return BaseResponse.success(fileVo.getFileName());
      } else {
        log.info("更新用户积分失败");
        return BaseResponse.fail("900", "更新用户积分失败");
      }
    } catch (BeansException e) {
      log.warn("下载文件异常:{}", e);
      return BaseResponse.fail("900", "更新用户积分异常");
    }
  }

  //文件下载相关代码
  @RequestMapping("/downloadFile")
  public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
    String fileName = request.getParameter("fileName");
    if (StringUtil.isEmpty(fileName)) {
      return "文件名不能为空";
    }
    File file = new File(this.filePath + "/" + fileName);
    if (file.exists()) {
      byte[] buffer = new byte[2048];
      // 文件输入流
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      OutputStream os = null;
      try {
        response.setContentType("application/force-download");// 设置强制下载不打开
        String excelFileName = URLEncoder.encode(fileName,"UTF-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + excelFileName);// 设置文件名

        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        os = response.getOutputStream();
        int i = bis.read(buffer);
        while (i != -1) {
          os.write(buffer, 0, i);
          i = bis.read(buffer);
        }

      } catch (Exception e) {
        log.error("Download file throw exception:{}", e);
      } finally {
        if (bis != null) {
          try {
            bis.close();
          } catch (IOException e) {
            log.error("Download File close bis throw exception:{}", e);
          }
        }
        if (os != null) {
          try {
            os.close();
          } catch (IOException e) {
            log.error("Download File close os throw exception:{}", e);
          }
        }
      }
    }
    return null;
  }
}
