package com.example.jangdocdaefilm.service;

import com.example.jangdocdaefilm.common.FreeFileUtils;
import com.example.jangdocdaefilm.dto.FreeDto;
import com.example.jangdocdaefilm.dto.FreeFileDto;
import com.example.jangdocdaefilm.mapper.FreeMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public class FreeServiceImpl implements FreeService {

  @Autowired
  private FreeMapper freeMapper;
  // common패키지의 FreeFileUtils 내려받기
  @Autowired
  private FreeFileUtils freeFileUtils;

  @Override
  public Page<FreeDto> selectFreeListNewest(int pageNum) throws Exception {
    PageHelper.startPage(pageNum, 10);
    return freeMapper.selectFreeListNewest();
  }
  @Override
  public Page<FreeDto> selectFreeListViewed(int pageNum) throws Exception {
    PageHelper.startPage(pageNum, 10);
    return freeMapper.selectFreeListViewed();
  }

  @Override
  public FreeDto selectFreeDetail(int idx) throws Exception {
    freeMapper.updateFreeHitCount(idx);
    return freeMapper.selectFreeDetail(idx);
  }

  @Override
  public void writeFree(FreeDto free, MultipartHttpServletRequest uploadFiles) throws Exception {
    freeMapper.writeFree(free);
    // 파일 업로드 구현
    List<FreeFileDto> fileList = freeFileUtils.parseFreeFileInfo(free.getIdx(), free.getId(), uploadFiles);
    if (!CollectionUtils.isEmpty(fileList)) {
      freeMapper.insertFreeFileList(fileList);
    }
  }

  // 파일 출력
  @Override
  public List<FreeFileDto> selectFreeFile(int idx) throws Exception {
    return freeMapper.selectFreeFile(idx);
  }

  @Override
  public FreeDto updateFreeView(int idx) throws Exception {
    return freeMapper.updateFreeView(idx);
  }

  @Override
  public void updateFree(FreeDto free, MultipartHttpServletRequest updateFiles) throws Exception {
    freeMapper.updateFree(free);

    List<FreeFileDto> fileList = freeFileUtils.parseFreeFileInfo(free.getIdx(), free.getId(), updateFiles);
    int freeIdx = free.getIdx();
    if (!CollectionUtils.isEmpty(fileList)) {
      freeMapper.deleteFreeFileList(freeIdx);
      freeMapper.insertFreeFileList(fileList);
    }
  }

  @Override
  public void deleteFree(int idx) throws Exception {
    freeMapper.deleteFree(idx);
  }

  @Override
  public void freeMultiDelete(Integer[] idx) throws Exception {
    for (int i=0; i < idx.length; i++) {
      freeMapper.deleteFree(idx[i]);
    }
  }

  @Override
  public void updateFreeHtiCount(int idx) throws Exception {

  }
}
