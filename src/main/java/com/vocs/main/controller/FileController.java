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
import org.springframework.web.bind.annotation.*;

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
  public String upload() {
    return null;
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
