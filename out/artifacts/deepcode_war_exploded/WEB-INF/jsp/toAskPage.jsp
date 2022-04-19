<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<%=path%>/static/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=path%>/static/js/jquery-3.2.1.js"></script>
    <script src="<%=path%>/static/js/bootstrap.min.js"></script>
    <title>掘京 ›提问</title>
</head>
<body>
<%-- 引入header文件 --%>
<%@ include file="header.jsp" %>

<div style="width: 70%;margin:1% 2% 1% 5%;float: left;">
    <div class="panel panel-default" id="main" style="">
        <div class="panel-heading" style="background-color: white">
            <a href="<%=basePath%>">掘京</a>&nbsp;›››&nbsp;提问

        </div>

        <div class="panel-body" >
            <%-- 表单 id与js关联 --%>
            <form action="gotoAsk.do" id="myUpdateForm" method="POST" class="form-horizontal" role="form">
                <label>用户ID</label>
                <input onkeyup="checkUserID()" type="text" class="form-control" id="buser_id" name="buser_id" placeholder="请输入被提问用户的ID" value="${userID}"/>
                <label>提问内容</label>
                    <textarea class="form-control" rows="8" id="tip_content" name="message_content"></textarea>
               <span id="askID"></span>
                <%-- 按钮 --%>
                <div>
                    <input class="btn btn-default" type="reset" value="重填" style="margin-left: 5%">
                    <span>
                        <input type="submit" class="btn btn-primary" value="提问" style="float: right"/>
                        </span>
                </div>
            </form>
        </div>
    </div>
</div>

<%-- 引入侧边栏文件 --%>
<%@ include file="side.jsp" %>

<%-- 引入footer文件 --%>
<%@ include file="footer.jsp" %>

<script>
    // 2020-09-25
    function update_confirm() {
        if (checkUserID() == false) {
            alert("数据错误！请返回再试。")
        }
        else {
            var r = confirm("确定提交?");
            if (r == true) {
                var form = document.getElementById("myUpdateForm"); // 由id获取表单
                form.submit();
            }
        }
    }

    /**
     * 检查昵称
     */
    function checkUserID() {
        var buser_id = document.getElementById('buser_id').value; // 获取输入的新密码
        // 比较
        if (buser_id == ${userID}) {
            document.getElementById('askID').innerText = "不能和自己ID相同！";
            return false;
        }else {
            document.getElementById('askID').innerText = "OK";
            return true;
        }
    }
</script>

</body>
</html>