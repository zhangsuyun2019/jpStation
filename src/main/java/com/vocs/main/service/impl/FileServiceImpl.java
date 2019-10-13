package com.vocs.main.service.impl;

import com.vocs.main.bean.FilesDto;
import com.vocs.main.mapper.FilesMapper;
import com.vocs.main.pojo.Files;
import com.vocs.main.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

  @Resource
  private FilesMapper filesMapper;

  @Override
  public int addFile(Files files) {
    return filesMapper.insertSelective(files);
  }

  @Override
  public int updateFile(Files files) {
    return filesMapper.updateByPrimaryKeySelective(files);
  }

  @Override
  public int searchCountFileList(FilesDto filesDto) {
    return filesMapper.selectCountByCondition(filesDto);
  }

  @Override
  public List<Files> searchFileList(FilesDto filesDto) {
    return filesMapper.selectByCondition(filesDto);
  }
}
