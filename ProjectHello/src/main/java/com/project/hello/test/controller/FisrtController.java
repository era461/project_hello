package com.project.hello.test.controller;

import static com.project.hello.common.Config.*;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartRequest;

import com.project.hello.common.DefaultVO;
import com.project.hello.test.service.impl.FisrtServiceImpl;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 포뎁스
 * 2017. 06 .14
 * 최초 생성
 * @author YOONHWAN
 * Service단 이후의 인터페이스는 cglib를 사용하여 강제 프록시객체 생성
 * interface필요시 impl 상위 폴더에 파일명Impl을 제외하여 생성 ex) service 폴더에 FisrtServiceImpl -> FisrtService
 * 구조      ->   controller -> serviceImpl -> daoImpl  -> sql mapper
 * 디렉토리  ->   controller -> service.impl-> dao.impl -> sqlmap     
 * naming    ->   입맛에 바꾸시면될듯합니다..
 */
@Controller
public class FisrtController {
	
	private static final Logger Logger = LoggerFactory.getLogger(FisrtController.class);
	
	@Resource(name="fisrtService")
	private FisrtServiceImpl fisrtServiceImpl;
	
	/**	
	 * 데이터 생성
	 * @return
	 */
	@RequestMapping("/insert.do")
	public String insertData(){
		fisrtServiceImpl.insertData();
		return "home";
	}
	
	/**
	 * 단일 데이터 조회
	 * @return
	 */
	@RequestMapping("/selectOneData.do")
	public String selectOneData(Model model){
		model.addAttribute("result", fisrtServiceImpl.selectOneData());
		return "selectOne";
	}
	
	/**
	 * 다중 데이터 조회
	 * @return
	 */
	@RequestMapping("/selectListData.do")
	public String selectListData(Model model){
		model.addAttribute("result", fisrtServiceImpl.selectListData());
		return "selectList";
	}
	
	/**
	 * 데이터 수정
	 * @return
	 */
	@RequestMapping("/updateData.do")
	public String updateData(){
		fisrtServiceImpl.updateData();
		return "home";
	}
	
	/**
	 * 데이터 삭제
	 * @return
	 */
	@RequestMapping("/deleteData.do")
	public String deleteData(){
		fisrtServiceImpl.deleteData();
		return "home";
	}
	
	/**
	 * 트랜잭션 테스트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/transaction.do")
	public String transaction(){
		fisrtServiceImpl.transaction();
		return "home";
	}
	
	/**
	 * 파일 업로드 뷰
	 * @return
	 */
	@RequestMapping("/file.do")
	public String fileView(){
		return "file";
	}
	
	/**
	 * 파일 업로드 처리
	 * @return
	 */
	@RequestMapping("/fileProc.do")
	public String fileProc(@RequestParam("file") MultipartRequest files){
		return "redirect:/file.do";
	}
	
	/**
	 * 페이징처리
	 * @param model
	 * @return
	 */
	@RequestMapping("/paging.do")
	public String paging(@ModelAttribute("searchVO") DefaultVO searchVO, Model model){
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setPageSize(searchVO.getPageSize());
		paginationInfo.setRecordCountPerPage(searchVO.getRecordCountPerPage());
		paginationInfo.setTotalRecordCount(120);
		model.addAttribute("paginationInfo", paginationInfo);
		return "paging";
	}
	
	/**
	 * 설정 필드
	 * @return
	 */
	@RequestMapping("/config.do")
	public String config(){
		System.out.println("항목1[" + GLOBALS_FILESTOREPATH + "]");
		System.out.println("항목2[" + GLOBALS_SERVERCONFPATH + "]");
		System.out.println("항목2[" + GLOBALS_SYNCHRNSERVERPATH + "]");
		return "";
	}
}