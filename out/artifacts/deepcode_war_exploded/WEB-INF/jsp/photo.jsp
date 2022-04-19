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
    <title>掘京 ›修改昵称</title>
</head>
<body>
<%-- 引入header文件 --%>
<%@ include file="header.jsp" %>

<div style="width: 70%;margin:1% 2% 1% 5%;float: left;">
    <div class="panel panel-default" id="main" style="">
        <div class="panel-heading" style="background-color: white">
            <a href="<%=basePath%>">掘京</a>&nbsp;›&nbsp;
            <c:choose>
                <c:when test="${empty userObject.user_nick}">${userObject.user_name}</c:when>
                <c:otherwise>${userObject.user_nick}</c:otherwise>
            </c:choose>
            ›&nbsp;更换头像

        </div>

        <div class="panel-body" style="margin: 1% " >
            <%-- 表单 id与js关联 --%>
                <form action="/uploadImg" enctype="multipart/form-data" method="post">
                    <input  type="file" name="file"/>
                    <input class="btn btn-success" type="submit" value="上传头像">
                </form>
        </div>
    </div>
</div>

<%-- 引入侧边栏文件 --%>
<%@ include file="side.jsp" %>

<%-- 引入footer文件 --%>
<%@ include file="footer.jsp" %>

</body>
</html>