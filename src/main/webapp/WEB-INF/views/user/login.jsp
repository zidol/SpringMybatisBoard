<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<%@ include file="../include/head.jsp" %>
<style>
.btn-naver{
	background-color: #1EC800;
	color: #ffffff;
}
.btn-naver:hover{
	background-color: #41AF39;
	color: #ffffff;
}
.i-naver {
	background-image: url("${path}/dist/img/naver_login_icon.png");
	background-size: 32px 32px;
}
</style>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="${path}/home.do">
            <b>ZidolS</b>-BOARD
        </a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg"><spring:message code="message.user.login.title"/></p>
		<p class="login-box-msg">
			<a href="login.do?lang=en"><spring:message code="message.user.login.language.en"/></a>&nbsp;&nbsp;
			<a href="login.do?lang=ko"><spring:message code="message.user.login.language.ko"/></a>
       </p>
        <form action="login.do" method="post">
            <div class="form-group has-feedback">
                <input type="text" name="id" class="form-control" placeholder="<spring:message code="message.user.login.id"/>">
                <span class="glyphicon glyphicon-exclamation-sign form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" class="form-control" placeholder="<spring:message code="message.user.login.password"/>">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">
                        <i class="fa fa-sign-in"></i> <spring:message code="message.user.login.loginBtn"/>
                    </button>
                </div>
            </div>
        </form>

        <div class="social-auth-links text-center">
            <p>- 또는 -</p>
            <a href="${ naver_url }" class="btn btn-block btn-social btn-flat btn-naver" >
                <i class="i-naver"></i> 네이버 계정으로 로그인
            </a>
            <a href="${ google_url }" class="btn btn-block btn-social btn-google btn-flat">
                <i class="fa fa-google-plus"></i> 구글 계정으로 로그인
            </a>
            <a href="${ facebook_url }" class="btn btn-block btn-social btn-facebook btn-flat">
                <i class="fa fa-facebook"></i> 페이스북 계정으로 로그인
            </a>
        </div>
        <!-- /.social-auth-links -->

        <a href="joinPage.do" class="text-center"><spring:message code="message.board.list.join"/></a>

    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<%@ include file="../include/plugin_js.jsp" %>
<script>

    var msg = "${msg}";
    if (msg === "REGISTERED") {
        alert("회원가입이 완료되었습니다. 로그인해주세요~");
    } else if (msg == "FAILURE") {
        alert("아이디와 비밀번호를 확인해주세요.");
    }

    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
</script>
</body>
</html>
