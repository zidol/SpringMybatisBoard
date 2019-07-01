package com.springbook.biz.user.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springbook.biz.user.UserVO;

@Repository
public class UserDAOMybatis {
	@Autowired
	private SqlSessionTemplate mybatis;

	// crud 기능의 메소드 구현
	// 회원 가입
	public int insertUser(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 insertUser() 기능 처리");
		int check = 0;
		check = mybatis.insert("UserDAO.insertUser", vo);
		System.out.println(check);
		return check;
	}
	
	// 회원정보 조회
	public UserVO getUser(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 getUser() 기능 처리");
		return (UserVO) mybatis.selectOne("UserDAO.getUser", vo);
	}

	// 회원 정보 수정
	public void updateUser(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 updateUser() 기능 처리");
		System.out.println(vo);
		mybatis.update("UserDAO.updateUser", vo);
	}

	// 회원 탈퇴
	public void deleteUser(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 deleteUser() 기능 처리");
		mybatis.delete("UserDAO.deleteUser", vo);
	}

	// 글 목록조회
//	public List<UserVO> getUserList(UserVO vo) {
//		System.out.println("===> Mybatis2 JDBC로 getUserList() 기능 처리");
//		if(vo.getSearchCondition().equals("TITLE")) {
//			return mybatis.selectList("UserDAO.getUserList_T", vo);
//		} else if(vo.getSearchCondition().equals("CONTENT")) {
//			return mybatis.selectList("UserDAO.getUserList_C", vo);
//		} else if(vo.getSearchCondition().equals("WRITER")) {
//			return mybatis.selectList("UserDAO.getUserList_W", vo);
//		}
//		return null;
//	}

	// Dynamic SQL 사용 할때
	public List<UserVO> getUserList(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 getUserList() 기능 처리");
		return mybatis.selectList("UserDAO.getUserList", vo);
	}
	
	//sns 계정으로 가입한 유저 확인
	public UserVO getBySns(UserVO snsUser) {
		System.out.println("===> Mybatis JDBC로 getBySns() 기능 처리");
		if(StringUtils.isNotEmpty(snsUser.getNaverid())){
			return mybatis.selectOne("UserDAO.getUserByNaver", snsUser.getNaverid()); 
		} else if(StringUtils.isNotEmpty(snsUser.getGoogleid())){
			return mybatis.selectOne("UserDAO.getUserByGoogle", snsUser.getGoogleid());
		} else {
			return mybatis.selectOne("UserDAO.getUserByFacebook", snsUser.getFacebookid());
		}
	}
	//sns계정 로그인 하기
	public int insertSnsUser(UserVO vo) {
		System.out.println("===> Mybatis JDBC로 insertUser() 기능 처리");
		int check = 0;
		check = mybatis.insert("UserDAO.insertUser", vo);
		System.out.println(check);
		return check;
	}
}
