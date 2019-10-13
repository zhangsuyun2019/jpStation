package com.vocs.main.controller;

import com.github.pagehelper.PageHelper;
import com.vocs.main.bean.FilesDto;
import com.vocs.main.pojo.Files;
import com.vocs.main.response.BaseResponse;
import com.vocs.main.response.PagedResponse;
import com.vocs.main.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * * 文件controller
 *
 * @author
 * @date 2019-10-13 13:00
 */
@Slf4j
@RequestMapping("file")
@Controller
@CrossOrigin
public class FileController {

  @Autowired private FileService fileService;

  @Value("${uploadFile.filePath}")
  private String filePath;

  @PostMapping("/upload")
  @ResponseBody
  public BaseResponse<String> upload(@RequestParam("file") MultipartFile file, Files files) {
    log.info("**************上传文件开始****************");
    try {
      if (ObjectUtils.isEmpty(file)) {
        return BaseResponse.fail("900", "文件不能为空");
      }
      String fileName = file.getOriginalFilename();
      FilesDto filesDto = new FilesDto();
      filesDto.setFileName(fileName);
      List<Files> fileList = fileService.searchFileList(filesDto);
      if (!CollectionUtils.isEmpty(fileList)) {
        Files filesUpdate = fileList.get(0);
        filesUpdate.setAmount(files.getAmount());
        filesUpdate.setUploadDate(new Date());
        filesUpdate.setUpdateTime(new Date());
        int updateCount = fileService.updateFile(filesUpdate);
        if (updateCount > 0) {
          // 保存文件
          file.transferTo(new File(filePath + fileName));
          return BaseResponse.success("");
        } else {
          return BaseResponse.fail("900", "上传失败");
        }
      } else {
        files.setFileName(fileName);
        files.setCreateTime(new Date());
        files.setUploadDate(new Date());
        files.setUpdateTime(new Date());
        files.setFileType(fileName.substring(fileName.lastIndexOf('.') + 1));
        int insertCount = fileService.addFile(files);
        if (insertCount > 0) {
          // 保存文件
          file.transferTo(new File(filePath + fileName));
          return BaseResponse.success("");
        } else {
          return BaseResponse.fail("900", "上传失败");
        }
      }
    } catch (Exception e) {
      log.error("upload exception:{}", e);
      return BaseResponse.fail("900", "上传异常");
    }
  }

  @RequestMapping(value = "getFilesByPage")
  @ResponseBody
  public BaseResponse<PagedResponse<Files>> getFilesByPage(@RequestBody FilesDto filesDto) {
    PagedResponse<Files> pageResponse = new PagedResponse<>();
    try {
      Integer pageNum = filesDto.getPageNum();
      Integer pageSize = filesDto.getPageSize();
      PageHelper.startPage(pageNum, pageSize);

      List<Files> list = fileService.searchFileList(filesDto);
      filesDto.setPageNum(null);
      filesDto.setPageSize(null);
      long total = fileService.searchCountFileList(filesDto);
      pageResponse.setPageNum(pageNum);
      pageResponse.setPageSize(pageSize);
      pageResponse.setTotalCount(total);
      pageResponse.setResult(list);
      return BaseResponse.success(pageResponse);
    } catch (Exception e) {
      log.error("getFilesByPage exception:{}", e);
      return BaseResponse.fail("900", "分页查询失败");
    }
  }
}
