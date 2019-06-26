package com.springbook.biz.user;

import java.sql.Date;

import lombok.Data;

@Data
public class UserVO {
	private String id;
	private String password;
	private String name;
	private String sex;
	private Date birthday;
	private String hp;
	private String address;
	private Date reg_date;
	private String email;
	private String googleid;
	private String naverid;
	private String nickname;

}
