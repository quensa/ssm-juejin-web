<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
<%@ include file="header.jsp" %>

<div class="panel panel-default" id="login" style="width: 80%;margin-left: 10%;margin-top: 5%;margin-bottom: 5%">
    <div class="panel-heading" style="background-color: #fff">
        <h3 class="panel-title">找回密码</h3>
    </div>
    <div class="panel-body">
        <form action="check" method="POST" id="mySignUpForm" class="form-horizontal" role="form"
              style="margin-left: 5%">
            <div class="form-group">
                <label class="col-sm-2 control-label">用户名</label>
                <div class="col-sm-6" style="width: 40%;">
                    <input type="text" class="form-control" id="user_name" name="user_name"
                           required/>
                    <button href="#" onclick="a1()">发送验证码</button>
                    <span id="userInfo"></span>
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">验证码</label>
                <div class="col-sm-6" style="width: 40%;">
                    <input type="text" class="form-control" name="user_password" required/>
                </div>
            </div>

            <input type="button" class="btn btn-default" value="返回"
                   style="margin-left: 5%" onclick="window.location.href='<%=basePath%>toMainPage.do'"/>
            <input type="submit" class="btn btn-success" value="验证"
                   style="margin-left: 25%" onclick="window.location.href='<%=basePath%>check'"/>
        </form>

    </div>
</div>

<script type="text/javascript" language="javascript">
    function login_check() {
        var form = document.getElementById("myLoginForm"); // 由id获取表单
        var uname = form.user_name.value; // 获取输入的用户名
        var upwd = form.user_password.value; // 获取输入的密码
        if (uname == '' || upwd == '') {
            alert("请将登录信息填写完整！");
        } else {
            form.submit(); // 提交表单
        }
    }
</script>
<script>
    function a1() {
        $.post({
            url: "${pageContext.request.contextPath}/send",
            data: {
                'user_name': $("#user_name").val()
            },
            success: function (data) {
                $("#userInfo").html(data);
            }
        });
    }
</script>

</body>
</html>