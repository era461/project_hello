package com.project.hello.test.dao.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.hello.test.vo.TestVO;

@Repository(value="fisrtDAO")
public class FisrtDAOImpl{

	@Autowired
	private SqlSession sql;

	/**
	 * 데이터 생성
	 * @return
	 */
	public void insertData() {
		sql.insert("Fisrt_SQL.insertData");
	}

	/**
	 * 데이터 단일 조회
	 * @return
	 */
	public TestVO selectOneData() {
		return sql.selectOne("Fisrt_SQL.selectOneData");
	}

	/**
	 * 데이터 리스트 조회
	 * @return
	 */
	public List<TestVO> selectListData() {
		return sql.selectList("Fisrt_SQL.selectListData");
	}

	/**
	 * 데이터 수정
	 * @return
	 */
	public void updateData() {
		sql.update("Fisrt_SQL.updateData");
	}

	/**
	 * 데이터 삭제
	 * @return
	 */
	public void deleteData() {
		sql.delete("Fisrt_SQL.deleteData");
	}
}
