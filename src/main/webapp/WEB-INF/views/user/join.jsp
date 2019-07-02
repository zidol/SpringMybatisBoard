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
<body class="hold-transition register-page">
<div class="register-box">
    <div class="register-logo">
        <a href="${path}/home.do">
            <b>ZidolS</b>-BOARD
        </a>
    </div>

    <div class="register-box-body">
        <p class="login-box-msg"><spring:message code="message.board.list.join"/></p>

        <form action="join.do" method="post">
            <div class="form-group has-feedback">
                <input type="text" name="id" class="form-control" placeholder="<spring:message code="message.board.join.id"/>">
                <span class="glyphicon glyphicon-exclamation-sign form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="name" class="form-control" placeholder="<spring:message code="message.board.join.name"/>">
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" class="form-control" placeholder="<spring:message code="message.board.join.password"/>">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="hp" class="form-control" placeholder="<spring:message code="message.board.join.hp"/>">
                <span class="glyphicon glyphicon-phone form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="address" class="form-control" placeholder="<spring:message code="message.board.join.address"/>">
                <span class="glyphicon glyphicon glyphicon-home form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
            	<label><spring:message code="message.board.join.sex"/></label>&nbsp;
               <input type="radio" name="sex" value="male">&nbsp;<spring:message code="message.board.join.male"/>&nbsp;
			   <input type="radio" name="sex" value="female">&nbsp;<spring:message code="message.board.join.female"/>&nbsp;
            </div>
            <div class="form-group has-feedback">
           	<label><spring:message code="message.board.join.birthday"/></label>
                <input type="date" name="birthday">
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            
                        </label>
                    </div>
                </div>
                <div class="col-xs-4">
                    <input type="submit" class="btn btn-primary btn-block btn-flat" value="<spring:message code="message.board.join.signupBtn"/>">
                </div>
            </div>
        </form>

        <div class="social-auth-links text-center">
            <p>- 또는 -</p>
            <a href="${ naver_url }" class="btn btn-block btn-social btn-flat btn-naver" >
                <i class="i-naver"></i> 네이버으로 가입
            </a>
            <a href="${ facebook_url }" class="btn btn-block btn-social btn-facebook btn-flat">
                <i class="fa fa-facebook"></i> 페이스북으로 가입
            </a>
            <a href="${ google_url }" class="btn btn-block btn-social btn-google btn-flat">
                <i class="fa fa-google-plus"></i> 구글 계정으로 가입
            </a>
        </div>

        <a href="login.do" class="text-center"><spring:message code="message.board.join.loginBtn"/></a>
    </div>
    <!-- /.form-box -->
</div>
<!-- /.register-box -->

<%@ include file="../include/plugin_js.jsp" %>
<script>
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
