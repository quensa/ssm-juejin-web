<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link href="<%=path%>/static/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=path%>/static/js/jquery-3.2.1.js"></script>
    <script src="<%=path%>/static/js/bootstrap.min.js"></script>
</head>
<body>

<!-- 弹出结果 -->
<c:if test="${not empty myInfo}">
    <script type="text/javascript" language="javascript">
        {
            alert("<%=request.getAttribute("myInfo")%>");
        }
    </script>
</c:if>

<!-- 引入header文件 -->
<%@ include file="header.jsp"%>

<div class="panel panel-default" id="login" style="width: 80%;margin-left: 10%;margin-top: 5%;margin-bottom: 5%">
    <div class="panel-heading" style="background-color: #fff">
        <h3 class="panel-title">登录</h3>
    </div>
    <div class="panel-body">
        <form action="userLogin.do" method="POST" id="mySignUpForm" class="form-horizontal" role="form"
              style="margin-left: 5%" onsubmit="submit_confirm()">
            <div class="form-group">
                <label class="col-sm-2 control-label">用户名</label>
                <div class="col-sm-6" style="width: 40%;">
                    <input type="text" class="form-control" id="user_name" name="user_name"
                           value="${cookie.username.value}"required />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">密码</label>
                <div class="col-sm-6" style="width: 40%;">
                    <input type="password" class="form-control" id="user_password" name="user_password"
                           value="${cookie.password.value}"  required />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <input type="checkbox" id="status" name="rememberMe" value="true" checked>记住我
                </label>
            </div>

            <input type="hidden" name="postId" value="<%=request.getParameter("postId")%>"/>
            <input type="button" class="btn btn-default" value="返回"
                   style="margin-left: 5%" onclick="window.location.href='<%=basePath%>toMainPage.do'"/>
            <input type="submit" class="btn btn-success" value="登录"
                   style="margin-left: 25%" onclick="window.location.href='<%=basePath%>userLogin.do'"/>
            <input type="button" class="btn btn-primary" value="忘记密码？"
                   style="margin-left: 5%" onclick="window.location.href='<%=basePath%>forget'"/>
        </form>

    </div>
</div>

<script type="text/javascript" language="javascript">
    function login_check()
    {
        var form = document.getElementById("myLoginForm"); // 由id获取表单
        var uname = form.user_name.value; // 获取输入的用户名
        var upwd = form.user_password.value; // 获取输入的密码
        if(uname == '' || upwd == '') {
            alert("请将登录信息填写完整！");
        }else{
            form.submit(); // 提交表单
        }
    }

    function changeVerifyCode(img){
        img.src="../Kaptcha?" + Math.floor(Math.random()*100);
    }
</script>

</body>
</html>