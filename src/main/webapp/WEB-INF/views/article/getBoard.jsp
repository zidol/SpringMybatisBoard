<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>

<%@ include file="../include/head.jsp"%>

<body class="hold-transition skin-blue sidebar-mini layout-boxed">

<div class="wrapper">

    <!-- Main Header -->
    <%@ include file="../include/main_header.jsp"%>

    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="../include/left_column.jsp"%>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <spring:message code="message.board.detail.mainTitle" />
            </h1>
            <ol class="breadcrumb">
                <li><i class="fa fa-edit"></i> article</li>
                <li class="active"><a href="${path}/getBoardList.do">getBoard</a></li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content container-fluid">
            <div class="col-lg-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title"><spring:message code="message.board.detail.title" />: ${board.title}</h3>
                    </div>
                    <div class="box-body" >
                       <div id="content">
                       </div>
                    </div>
                    <div class="box-footer">
                        <div class="user-block">
                            <img class="img-circle img-bordered-sm" src="${path }/dist/img/user1-128x128.jpg" alt="user image">
                            <span class="username">
                                <a href="#">${board.writer }</a>
                            </span>
                            <span class="description"><fmt:formatDate pattern="yyyy-MM-dd a HH:mm" value="${board.regDate}"/></span>
                            <span><spring:message code="message.board.detail.attachments" /> : 
                            <a href="download.do?originalFileName=${board.originalFileName }&seq=${board.seq}">${board.originalFileName }</a></span>
                        </div>
                    </div>
                    <div class="box-footer">
                        <form role="form" method="post">
                            <input type="hidden" name="seq" value="${board.seq }">
                            <input type="hidden" name="page" value="${criteria.page}">
					        <input type="hidden" name="perPageNum" value="${criteria.perPageNum}">
					        <input type="hidden" name="searchCondition" value="${criteria.searchCondition}">
				        	<input type="hidden" name="searchKeyword" value="${criteria.searchKeyword}">
                        </form>
                        <button type="button" class="btn btn-info" onclick="location.href='getBoardList.do?page=${criteria.page}&perPageNum=${criteria.perPageNum}&searchCondition=${criteria.searchCondition}&searchKeyword=${criteria.searchKeyword}'">
                        <i class="fa fa-list"></i> <spring:message code="message.board.detail.link.boardList" /></button>
                    <div class="pull-right">
                    <c:if test="${userId eq board.writer}">
                    	<button type="button" onclick="location.href='modifyForm.do?page=${criteria.page}&perPageNum=${criteria.perPageNum}&seq=${board.seq }&searchCondition=${criteria.searchCondition}&searchKeyword=${criteria.searchKeyword}'" 
                    	class="btn btn-warning"><i class="fa fa-edit"></i> <spring:message code="message.board.detail.updateBtn"/></button>
                    	<button type="button" onclick="location.href='deleteBoard.do?page=${criteria.page}&perPageNum=${criteria.perPageNum}&seq=${board.seq }&searchCondition=${criteria.searchCondition}&searchKeyword=${criteria.searchKeyword}'" 
                    	class="btn btn-danger"><i class="fa fa-trash"></i> <spring:message code="message.board.detail.link.deleteBoard" /></button>
                    </c:if>
                    </div>
                    </div>
                </div>
                
                <div class="box box-warning">
                    <div class="box-header with-border">
                        <a class="link-black text-lg"><i class="fa fa-pencil margin-r-5"></i> 댓글 쓰기</a>
                    </div>
                    <div class="box-body">
                        <c:if test="${not empty userId}">
                            <form>
                                <div class="form-group">
                                    <textarea class="form-control" id="newReplyText" rows="3" placeholder="댓글내용..."style="resize: none"></textarea>
                                </div>
                                <div class="col-sm-2" hidden>
                                    <input class="form-control" id="newReplyWriter" type="text" value="${userId}" readonly>
                                </div>
                                <button type="button" class="btn btn-default btn-block replyAddBtn">
                                    <i class="fa fa-save"></i> 댓글 저장
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${empty userId}">
                            <a href="${path}/user/login" class="btn btn-default btn-block" role="button">
                                <i class="fa fa-edit"></i> 로그인 한 사용자만 댓글 등록이 가능합니다.
                            </a>
                        </c:if>
                    </div>
                </div>

                <div class="box box-success collapsed-box">
                    <%--댓글 유무 / 댓글 갯수 / 댓글 펼치기, 접기--%>
                    <div class="box-header with-border">
                        <a class="link-black text-lg"><i class="fa fa-comments-o margin-r-5 replyCount"></i> </a>
                        <div class="box-tools">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                <i class="fa fa-plus"></i>
                            </button>
                        </div>
                    </div>
                    <%--댓글 목록--%>
                    <div class="box-body repliesDiv">

                    </div>
                    <%--댓글 페이징--%>
                    <div class="box-footer">
                        <div class="text-center">
                            <ul class="pagination pagination-sm no-margin">

                            </ul>
                        </div>
                    </div>
                </div>

                <%--댓글 수정 modal 영역--%>
                <div class="modal fade" id="modModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">댓글수정</h4>
                            </div>
                            <div class="modal-body" data-rno>
                                <input type="hidden" class="replyNo"/>
                                <%--<input type="text" id="replytext" class="form-control"/>--%>
                                <textarea class="form-control" id="replyText" rows="3" style="resize: none"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">닫기</button>
                                <button type="button" class="btn btn-primary modalModBtn">수정</button>
                            </div>
                        </div>
                    </div>
                </div>

                <%--댓글 삭제 modal 영역--%>
                <div class="modal fade" id="delModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">댓글 삭제</h4>
                                <input type="hidden" class="rno"/>
                            </div>
                            <div class="modal-body" data-rno>
                                <p>댓글을 삭제하시겠습니까?</p>
                                <input type="hidden" class="rno"/>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">아니요.
                                </button>
                                <button type="button" class="btn btn-primary modalDelBtn">네. 삭제합니다.</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
                
            
            
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- Main Footer -->
    <%@ include file="../include/main_footer.jsp"%>

</div>
<!-- ./wrapper -->
<%@ include file="../include/plugin_js.jsp"%>

<script type="text/javascript" src="${path}/dist/js/article_file_upload.js"></script>
<script type="text/javascript" src="${path}/dist/js/reply.js"></script>
<script>
    var result = "${msg}";
    if (result == "regSuccess") {
        alert("게시글 등록이 완료되었습니다.");
    } else if (result == "modSuccess") {
        alert("게시글 수정이 완료되었습니다.");
    } else if (result == "delSuccess") {
        alert("게시글 삭제가 완료되었습니다.");
    }
    document.getElementById('content').innerHTML = "${board.content}"
</script>
<script id="replyTemplate" type="text/x-handlebars-template">
    
    {{#each.}}
    <div class="post replyDiv" data-replyNo={{replyNo}}>
        <div class="user-block">
            <%--댓글 작성자 프로필사진--%>
            <img class="img-circle img-bordered-sm" src="${path }/dist/img/user1-128x128.jpg" alt="user image">
            <%--댓글 작성자--%>
            <span class="username">
                <%--작성자 이름--%>
                <a href="#">{{replyWriter}}</a>
                {{#eqReplyWriter replyWriter}}
                <%--댓글 삭제 버튼--%>
                <a href="#" class="pull-right btn-box-tool replyDelBtn" data-toggle="modal" data-target="#delModal">
                    <i class="fa fa-times"> 삭제</i>
                </a>
                <%--댓글 수정 버튼--%>
                <a href="#" class="pull-right btn-box-tool replyModBtn" data-toggle="modal" data-target="#modModal">
                    <i class="fa fa-edit"> 수정</i>
                </a>
                {{/eqReplyWriter}}
            </span>
            <%--댓글 작성일자--%>
            <span class="description">{{prettifyDate regDate}}</span>
        </div>
        <%--댓글 내용--%>
        <div class="oldReplyText">{{{escape replyText}}}</div>
        <br/>
    </div>
    {{/each}}
    
</script>

<script id="fileTemplate" type="text/x-handlebars-template">
    <li data-src="{{fullName}}">
        <span class="mailbox-attachment-icon has-img">
            <img src="{{imgSrc}}" alt="Attachment">
        </span>
        <div class="mailbox-attachment-info">
            <a href="{{originalFileUrl}}" class="mailbox-attachment-name">
                <i class="fa fa-paperclip"></i> {{originalFileName}}
            </a>
        </div>
    </li>
</script>
<script>
    $(document).ready(function () {

        Handlebars.registerHelper("eqReplyWriter", function (replyWriter, block) {
            var accum = "";
            if (replyWriter === "${userId}") {
                accum += block.fn();
            }
            return accum;
        });

        var articleNo = "${board.seq}";  // 현재 게시글 번호
        var replyPageNum = 1; // 댓글 페이지 번호 초기화
        // 첨부파일 목록
        getFiles(articleNo);

        // 댓글 목록 함수 호출
        getReplies("${path}/replies/" + articleNo + "/" + replyPageNum);

        // 댓글 페이지 번호 클릭 이벤트
        $(".pagination").on("click", "li a", function (event) {
            event.preventDefault();
            replyPageNum = $(this).attr("href");
            getReplies("${path}/replies/" + articleNo + "/" + replyPageNum);
        });

        // 댓글 저장 버튼 클릭 이벤트
        $(".replyAddBtn").on("click", function () {

            // 입력 form 선택자
            var replyWriterObj = $("#newReplyWriter");
            var replyTextObj = $("#newReplyText");
            var replyWriter = replyWriterObj.val();
            var replyText = replyTextObj.val();

            // 댓글 입력처리 수행
            $.ajax({
                type: "post",
                url: "${path}/replies/",
                headers: {
                    "Content-Type": "application/json",
                    "X-HTTP-Method-Override": "POST"
                },
                dataType: "text",
                data: JSON.stringify({
                    seq: articleNo,
                    replyWriter: replyWriter,
                    replyText: replyText
                }),
                success: function (result) {
                    console.log("result : " + result);
                    if (result === "regSuccess") {
                        alert("댓글이 등록되었습니다.");
                        replyPageNum = 1;  // 페이지 1로 초기화
                        getReplies("${path}/replies/" + articleNo + "/" + replyPageNum); // 댓글 목록 호출
                        replyTextObj.val("");   // 댓글 입력창 공백처리
                    }
                }
            });
        });

        // 댓글 수정을 위해 modal창에 선택한 댓글의 값들을 세팅
        $(".repliesDiv").on("click", ".replyDiv", function (event) {
            var reply = $(this);
            $(".replyNo").val(reply.attr("data-replyNo"));
            $("#replyText").val(reply.find(".oldReplyText").text());
        });

        // modal 창의 댓글 수정버튼 클릭 이벤트
        $(".modalModBtn").on("click", function () {
            var replyNo = $(".replyNo").val();
            var replyText = $("#replyText").val();
            $.ajax({
                type: "put",
                url: "${path}/replies/" + replyNo,
                headers: {
                    "Content-Type": "application/json",
                    "X-HTTP-Method-Override": "PUT"
                },
                dataType: "text",
                data: JSON.stringify({
                    replyText: replyText
                }),
                success: function (result) {
                    console.log("result : " + result);
                    if (result === "modSuccess") {
                        alert("댓글이 수정되었습니다.");
                        getReplies("${path}/replies/" + articleNo + "/" + replyPageNum); // 댓글 목록 호출
                        $("#modModal").modal("hide"); // modal 창 닫기
                    }
                }
            })
        });

        // modal 창의 댓글 삭제버튼 클릭 이벤트
        $(".modalDelBtn").on("click", function () {
            var replyNo = $(".replyNo").val();
            $.ajax({
                type: "delete",
                url: "${path}/replies/" + replyNo,
                headers: {
                    "Content-Type": "application/json",
                    "X-HTTP-Method-Override": "DELETE"
                },
                dataType: "text",
                success: function (result) {
                    console.log("result : " + result);
                    if (result === "delSuccess") {
                        alert("댓글이 삭제되었습니다.");
                        getReplies("${path}/replies/" + articleNo + "/" + replyPageNum); // 댓글 목록 호출
                        $("#delModal").modal("hide"); // modal 창 닫기
                    }
                }
            });
        });

        var formObj = $("form[role='form']");
        console.log(formObj);

        $(".modBtn").on("click", function () {
            formObj.attr("action", "/article/paging/search/modify");
            formObj.attr("method", "get");
            formObj.submit();
        });

        $(".delBtn").on("click", function () {

            var replyCnt = $(".replyDiv").length;
            if (replyCnt > 0) {
                alert("댓글이 달린 게시글은 삭제할수 없습니다.");
                return;
            }

            var arr = [];
            $(".uploadedFileList li").each(function () {
                arr.push($(this).attr("data-src"));
            });

            if (arr.length > 0) {
                $.post("/article/file/deleteAll", {files: arr}, function () {

                });
            }

            formObj.attr("action", "/article/paging/search/remove");
            formObj.submit();
        });

        $(".listBtn").on("click", function () {
            formObj.attr("action", "/article/paging/search/list");
            formObj.attr("method", "get");
            formObj.submit();
        });

    });
</script>
</body>
</html>