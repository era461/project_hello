package com.project.hello.test.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.project.hello.test.dao.impl.FisrtDAOImpl;
import com.project.hello.test.vo.TestVO;

@Service("fisrtService")
public class FisrtServiceImpl{

	@Resource(name="fisrtDAO")
	private FisrtDAOImpl fisrtDAO;

	/**
	 * 데이터 생성
	 * @return
	 */
	public void insertData() {
		fisrtDAO.insertData();
	}

	/**
	 * 데이터 단일 조회
	 * @return
	 */
	public TestVO selectOneData() {
		return fisrtDAO.selectOneData();
	}

	/**
	 * 데이터 리스트 조회
	 * @return
	 */
	public List<TestVO> selectListData() {
		return fisrtDAO.selectListData();
	}

	/**
	 * 데이터 수정
	 * @return
	 */
	public void updateData() {
		fisrtDAO.updateData();
	}

	/**
	 * 데이터 삭제
	 * @return
	 */
	public void deleteData() {
		fisrtDAO.deleteData();
	}

	/**
	 * 트랜잭션 테스트
	 * @return
	 */
	public void transaction(){
		fisrtDAO.insertData();
		fisrtDAO.insertData();
		fisrtDAO.insertData();
		fisrtDAO.insertData();
	}
}
