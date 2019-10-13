package com.vocs.main.service;

import com.vocs.main.bean.FilesDto;
import com.vocs.main.pojo.Files;

import java.util.List;

public interface FileService {

  /**
   * * 增加文件
   *
   * @param files 文件对象
   * @return
   */
  int addFile(Files files);

  /**
   * * 更新文件
   *
   * @param files 文件对象
   * @return
   */
  int updateFile(Files files);

  /**
   * * 查询文件个数
   *
   * @param filesDto 查询对象
   * @return
   */
  int searchCountFileList(FilesDto filesDto);

  /**
   * * 查询文件列表
   *
   * @param filesDto 查询对象
   * @return
   */
  List<Files> searchFileList(FilesDto filesDto);
}
