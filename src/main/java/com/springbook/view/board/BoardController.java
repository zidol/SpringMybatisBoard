package com.springbook.view.board;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbook.biz.board.BoardService;
import com.springbook.biz.board.BoardVO;
import com.springbook.biz.board.Criteria;
import com.springbook.biz.board.PageMaker;

@Controller
@SessionAttributes("board")
public class BoardController {
	@Autowired
	private BoardService boardService;

//	XML방식으로 처리
//	@RequestMapping("/dataTransform.do")
//	@ResponseBody
//	public BoardListVO dataTransform(BoardVO vo) {
//		vo.setSearchCondition("TITLE");
//		vo.setSearchKeyword("");
//		List<BoardVO> boardList = boardService.getBoardList(vo);
//		BoardListVO boardListVO = new BoardListVO();
//		boardListVO.setBoardList(boardList);
//		return boardListVO;
//	}
	
//	JSON 방법으로 처리
	@RequestMapping("/dataTransform.do")
	@ResponseBody
	public List<BoardVO> dataTransform(BoardVO vo) {
		vo.setSearchCondition("TITLE");
		vo.setSearchKeyword("");
		List<BoardVO> boardList = boardService.getBoardList(vo);
		return boardList;
	}

	// 검색 조건 목록 설정
	@ModelAttribute("conditionMap")
	public Map<String, String> searchCondtionMap() {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("제목", "TITLE");
		conditionMap.put("내용", "CONTENT");
		conditionMap.put("작성자", "WRITER");
		return conditionMap;
	}
	
	@RequestMapping(value="/insertBoardForm.do", method=RequestMethod.GET)
	public String insertBoardForm(HttpSession session) {
		String userId  = (String)session.getAttribute("userId");
		System.out.println(userId);
		return "/article/insertBoard";
	}
	
	@RequestMapping(value="/insertBoard.do")	//value=생략 가능
	public String insertBoard(BoardVO vo, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {
		System.out.println("글 등록 처리");
		
		Map<String, String> fileName = null;
		if(vo.getUploadFile() != null) {
	        fileName= boardService.fileUpload(vo, request);
	    }
		
		String temp = vo.getContent();
		temp = temp.replaceAll("\r\n", "");
		vo.setContent(temp);
		vo.setOriginalFileName(fileName.get("originalFileName"));
		vo.setSaveFileName(fileName.get("saveFileName"));
		boardService.insertBoard(vo);
		redirectAttributes.addFlashAttribute("msg", "regSuccess");
		return "redirect:getBoardList.do";
		
	}
	@RequestMapping(value="/modifyForm.do", method=RequestMethod.GET)
	public String modifyForm(@RequestParam("seq") int seq,
            @ModelAttribute("criteria") Criteria criteria,BoardVO vo, Model model,HttpSession session) {
		session.getAttribute("userId");
		model.addAttribute("board", boardService.getBoard(seq));
		return "/article/modify";
	}
	@RequestMapping(value="/modify.do", method=RequestMethod.POST)
	public String updateBoard(@ModelAttribute("board") BoardVO vo, Criteria criteria, RedirectAttributes redirectAttributes) {
		String temp = vo.getContent();
		temp = temp.replaceAll("\r\n", "");
		vo.setContent(temp);
		
		boardService.updateBoard(vo);
		redirectAttributes.addAttribute("page", criteria.getPage());
	    redirectAttributes.addAttribute("perPageNum", criteria.getPerPageNum());
		redirectAttributes.addFlashAttribute("msg", "modSuccess");
		return "redirect:getBoardList.do";
	}
	
	@RequestMapping("/deleteBoard.do")
	public String deletBoard(@RequestParam("seq") int seq,
            				 Criteria criteria,
            				 BoardVO vo,
            				 RedirectAttributes redirectAttributes) {
		System.out.println("글 삭제 처리");
		
		redirectAttributes.addAttribute("page", criteria.getPage());
	    redirectAttributes.addAttribute("perPageNum", criteria.getPerPageNum());
	    redirectAttributes.addFlashAttribute("msg", "delSuccess");
		boardService.deleteBoard(seq);
		return "redirect:getBoardList.do";
	}
	
	@RequestMapping(value="/getBoard.do", method = RequestMethod.GET)
	public String getBoard(@RequestParam("seq") int seq,
            @ModelAttribute("criteria") Criteria criteria,BoardVO vo, Model model) {
		System.out.println("글 상세 조회 처리");
		model.addAttribute("board", boardService.getBoard(seq));
		return "article/getBoard";
	}
	
//	원본
//	@RequestMapping("/getBoardList.do")
//	public String getBoardList(BoardVO vo, Model model, HttpSession session) {
//		System.out.println("글 목록 검색 처리");
//		System.out.println(session.getAttribute("userName"));
//		String auth  = (String)session.getAttribute("userName");
//		
//		// Null Check
//		if(vo.getSearchCondition() == null) vo.setSearchCondition("TITLE");
//		if(vo.getSearchKeyword() == null) vo.setSearchKeyword("");
//		if(auth == null && auth.equals("")) {
//			return "redirect:login.do";
//		} else {
//			model.addAttribute("boardList", boardService.getBoardList(vo));
//			return "getBoardList.jsp";
//		}
//		
//	}
	
	
	@RequestMapping(value="/getBoardList.do", method = RequestMethod.GET)
	public String getBoardList(BoardVO vo, Model model, HttpSession session, Criteria criteria) throws Exception {
		System.out.println("글 목록 검색 처리");
		String auth  = (String)session.getAttribute("userName");
		
		// Null Check
		if(vo.getSearchCondition() == null) vo.setSearchCondition("TITLE");
		if(vo.getSearchKeyword() == null) vo.setSearchKeyword("");
		if(auth == null && auth.equals("")) {
			return "redirect:login.do";
		} else {
			PageMaker pageMaker = new PageMaker();
		    pageMaker.setCriteria(criteria);
		    pageMaker.setTotalCount(boardService.countArticles(criteria));
		    
			model.addAttribute("boardList", boardService.listCriteria(criteria));
			model.addAttribute("pageMaker", pageMaker);
			System.out.println(pageMaker.makeQuery(criteria.getPage()));
			return "/article/getBoardList";
		}
		
	}
	
	
	@RequestMapping("/download.do")
    @ResponseBody
    public byte[] downProcess(HttpServletResponse response, HttpServletRequest request,
            @RequestParam String originalFileName,@RequestParam int seq) throws Exception{
//		String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
//        File file = new File(uploadPath + filename);
//        byte[] bytes = FileCopyUtils.copyToByteArray(file);
//        
//        String fn = new String(file.getName().getBytes("utf-8"), "iso_8859_1");
//        System.out.println(fn);
//        response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
//        response.setContentLength(bytes.length);
		BoardVO vo = new BoardVO();
		vo.setOriginalFileName(originalFileName);
		vo.setSeq(seq);
		byte[] bytes = boardService.fileDownload(vo, response, request);
        return bytes;
    }
}
