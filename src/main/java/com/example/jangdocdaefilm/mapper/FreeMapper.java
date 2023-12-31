package com.example.jangdocdaefilm.mapper;

import com.example.jangdocdaefilm.dto.FreeDto;
import com.example.jangdocdaefilm.dto.FreeFileDto;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FreeMapper {
  //  게시물 전체 목록 출력
//  List<FreeDto> selectFreeList() throws Exception;
  Page<FreeDto> selectFreeListNewest() throws Exception;
  Page<FreeDto> selectFreeListViewed() throws Exception;

  //  게시물 상세 내용 출력
  FreeDto selectFreeDetail(int idx) throws Exception;

  //  게시물 등록
  void writeFree(FreeDto free) throws Exception;

  //  게시물 내용 수정페이지로 이동
  FreeDto updateFreeView(int idx) throws Exception;

  //  게시물 수정
  void updateFree(FreeDto free) throws Exception;

  //  게시물 삭제
  void deleteFree(int idx) throws Exception;

  //  게시물 조회수 카운트
  void updateFreeHitCount(int idx) throws Exception;

  // 파일 업로드 수정
  void insertFreeFileList(List<FreeFileDto> fileList) throws Exception;

  void deleteFreeFileList(int freeIdx) throws Exception;

  List<FreeFileDto> selectFreeFile(int idx) throws Exception;
}
