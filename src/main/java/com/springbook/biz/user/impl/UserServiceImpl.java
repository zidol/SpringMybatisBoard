package com.springbook.biz.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbook.biz.user.UserService;
import com.springbook.biz.user.UserVO;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDAOMybatis userDAO;

	@Override
	public int insertUserVO(UserVO vo) {
		int joinCheck = 0;
		joinCheck =  userDAO.insertUser(vo);
		return joinCheck;
	}

	@Override
	public UserVO getUserVO(UserVO vo) {
		return userDAO.getUser(vo);
	}

	@Override
	public void updateUser(UserVO vo) {
		userDAO.updateUser(vo);
		
	}

	@Override
	public void deleteUser(UserVO vo) {
		userDAO.deleteUser(vo);		
	}

	@Override
	public UserVO getBySns(UserVO snsUser) {
		return userDAO.getBySns(snsUser);
	}

	@Override
	public int insertSnsUserVO(UserVO vo) {
		int joinCheck = 0;
		joinCheck =  userDAO.insertSnsUser(vo);
		return joinCheck;
	}

	


}
