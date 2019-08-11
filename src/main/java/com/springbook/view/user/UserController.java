package com.springbook.view.user;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbook.biz.auth.SNSLogin;
import com.springbook.biz.auth.SnsValue;
import com.springbook.biz.user.UserService;
import com.springbook.biz.user.UserVO;
import com.springbook.biz.user.impl.UserDAO;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private SnsValue naverSns;
	
	@Autowired
	private SnsValue googleSns;
	
	@Autowired
	private SnsValue facebookSns;
	
	@Autowired
	private GoogleConnectionFactory googleConnectionFactory;
	
	@Autowired
	private OAuth2Parameters googleOAuth2Parameters;
	
	@Autowired
	private FacebookConnectionFactory facebookConnectionFactory;
	
	@Autowired
	private OAuth2Parameters facebookOAuth2Parameters;

	// 1. JoinController
	@RequestMapping(value="/join.do", method=RequestMethod.POST)
	public String jogin(UserVO vo) {
		System.out.println(vo); 
		int join = 0;
		join = userService.insertUserVO(vo);
		if(join == 1) {						//회원가입 성공
			return "redirect:home.do";
		} else {		
			return "/user/join";			//회원 가입 실패
		}
	}
	//다국어 처리 설정 때문에 Controller를 거쳐야 함.
	@RequestMapping(value="/joinPage.do", method=RequestMethod.GET)
	public String joinPage(Model model) {
		SNSLogin snsLogin = new SNSLogin(naverSns);
		model.addAttribute("naver_url", snsLogin.getNaverAuthURL());
		
//		SNSLogin googleLogin = new SNSLogin(googleSns);
//		model.addAttribute("google_url", googleLogin.getNaverAuthURL());
		
		/* 구글code 발행을 위한 URL 생성 */
		OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
		String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
		model.addAttribute("google_url", url);
		
		/* 페이스북code 발행을 위한 URL 생성 */
		OAuth2Operations oauthOperations2 = facebookConnectionFactory.getOAuthOperations();
		String url2 = oauthOperations2.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, facebookOAuth2Parameters);
		System.out.println("google profile_url : " + url);
		System.out.println("facebook profile_url : " + url2);
		model.addAttribute("facebook_url", url2);
		return "/user/join";
	}
	
	// 회원 개인 정보 조회
	@RequestMapping("/getUser.do")
	public String getUser(UserVO vo, Model model, HttpSession session) {
		String auth = (String)session.getAttribute("userName");
		if(auth == null && auth.equals("")) {	//유저 정보 세션에 없으면 로그인 창으로 이동
			return "redirect:login.do";
		} else {
			model.addAttribute("user", userService.getUserVO(vo));	//유저 정보 갖고 페이지 이동
			return "/user/joinInfo";
		}
	}
	
	@RequestMapping(value="/updateUser.do", method=RequestMethod.POST)
	public String updateUser(@ModelAttribute("user") UserVO vo, Model model
			, RedirectAttributes redirectAttributes, HttpSession session) {
		System.out.println("개인정보 수정");
		userService.updateUser(vo);
		redirectAttributes.addAttribute("id", session.getAttribute("userId") );
		return "redirect:getUser.do";
	}
	
	@RequestMapping("/deleteUser.do")
	public String deletUser(UserVO vo) {
		System.out.println("회원 탈퇴 처리");
		userService.deleteUser(vo);
		return "redirect:login.do";
	}
	
	@RequestMapping(value = "/auth/{service}/callback", method = { RequestMethod.GET, RequestMethod.POST})
	public String snsLoginCallback(@PathVariable String service, Model model, @RequestParam String code, HttpSession session) throws Exception {
		//1. code를이용해서accesstoken 받기
		//2. accesstoken 을 이용해 사용자 profile 정보 가져오기
		//3. DB 해당 유저가 존재하는지 체크(googleId, naverId컬럼 추가)
		//4. 존재시강제로그인, 미존재가입 페이지
		SnsValue sns = null;
		if(StringUtils.equals("naver" , service)) {
			sns = naverSns;
		} else if (StringUtils.equals("google" , service)) {
			sns = googleSns;
		} else if (StringUtils.equals("facebook", service)) {
			sns = facebookSns;
		}
		SNSLogin snsLogin = new SNSLogin(sns);
		UserVO snsUser = snsLogin.getUserProfile(code);
		System.out.println(snsUser.toString());
		UserVO user = userService.getBySns(snsUser);
		if (user == null) {
			userService.insertSnsUserVO(snsUser);
		} else {
			// 4. 존재시 강제로그인 
			session.setAttribute("userName", user.getName());
			session.setAttribute("userId", user.getId());
		}
		return "redirect:/home.do";
	}
	
	// 2.loginController
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginView(@ModelAttribute("user") UserVO vo, Model model) {
		System.out.println("로그인 화면으로 이동");
		
		SNSLogin snsLogin = new SNSLogin(naverSns);
		model.addAttribute("naver_url", snsLogin.getNaverAuthURL());
		
		/* 구글code 발행을 위한 URL 생성 */
		OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
		String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
		model.addAttribute("google_url", url);
		
		/* 페이스북code 발행을 위한 URL 생성 */
		OAuth2Operations oauthOperations2 = facebookConnectionFactory.getOAuthOperations();
		String url2 = oauthOperations2.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, facebookOAuth2Parameters);
		System.out.println("google profile_url : " + url);
		System.out.println("facebook profile_url : " + url2);
		model.addAttribute("facebook_url", url2);
		return "/user/login";
	}

	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public String login(UserVO vo, UserDAO userDAO, HttpSession session) {
		System.out.println("로그인 인증 처리");
		
		if(vo.getId() == null || vo.getId().equals("")) {
			throw new IllegalArgumentException("아이디는 반드시 입력해야 합니다.");
		}
		if(vo.getPassword() == null || vo.getPassword().equals("")) {
			throw new IllegalArgumentException("패스워드는 반드시 입력해야 합니다.");
		}
		
		UserVO user = userDAO.getUser(vo);
		
		if(user != null) {
			session.setAttribute("userName", user.getName());
			session.setAttribute("userId", user.getId());
			return "redirect:home.do";
		} else {
			return "redirect:login.do";
		}
	}
	
	// 3. LogoutController
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		System.out.println("로그아웃 처리");

		session.invalidate();
		return "redirect:home.do";
	}
}
