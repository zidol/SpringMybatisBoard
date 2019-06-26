package com.springbook.biz.user;

public interface UserService {
	
	//CRUD 기능의 메소드 구현
	//회원 등록
	public int insertUserVO(UserVO vo);
	
	//회원 조회
	UserVO getUserVO(UserVO vo);
	
	//회원 수정
	void updateUser(UserVO vo);
	
	//회원탈퇴
	void deleteUser(UserVO vo);
	
	UserVO getBySns(UserVO snsUser);
	
	int insertSnsUserVO(UserVO vo);
	
}
