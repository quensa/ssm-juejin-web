<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 这是侧边栏 -->

<!-- 未登录 -->
<c:if test="${empty USER}">

    <div class="panel panel-default" id="sidebar2" style="width: 20%;margin:1% 2% 1% 0%;float: right">
        <div class="panel-heading" style="background-color: white;text-align: center">
            <blockquote>
                逍遥论坛
                <small>中华万里腾飞，逍遥古今中外</small>
            </blockquote>
        </div>
        <ul class="list-group" style="width: 100%">
            <li class="list-group-item">
                <a href="toLoginPage.do" class="btn btn-primary btn-block">登录</a>
                <a href="toSignUpPage.do" class="btn btn-default btn-block">注册</a>
            </li>
        </ul>
    </div>
</c:if>

<!-- 已登录 -->
<c:if test="${!empty USER}">

    <div class="panel panel-default" id="sidebar2" style="width: 20%;margin:1% 2% 1% 0%;float: right">
        <div class="panel-heading" style="background-color: white;text-align: center">欢迎您！
                <%--展示用户昵称--%>
            <a href="getUserInfo.do?userId=${USER.user_id}">
                <c:choose>
                    <c:when test="${empty USER.user_nick}">${USER.user_name}</c:when>
                    <c:otherwise>${USER.user_nick}</c:otherwise>
                </c:choose>
            </a>

                <%--    <form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">--%>
                <%--        <input type="file" name="file"/>--%>
                <%--        <input type="submit" value="upload">--%>
            <img src="D:\下载\浏览器下载路径\新建文件夹\xyqas-master\target\xyqas\upload/5e38abe9-8e36-4332-9e2d-336b6faa4079.jpg"
                 width="25px" height="25px">
                <%--        用户:  ${user}&nbsp;--%>

                <%--    </form>--%>
                <%--展示用户权限--%>
            <c:choose>
                <c:when test="${USER.user_type == 0}"> <span class="label label-success">超级管理员</span></c:when>
                <c:when test="${USER.user_type == 1}"> <span class="label label-warning">管理员</span></c:when>
                <c:otherwise><span class="label label-default">普通用户</span></c:otherwise>
            </c:choose>

        </div>
        <ul class="list-group" style="width: 100%">
                <%--被锁定的用户不能发贴--%>
            <c:if test="${USER.user_status != 2}">
                <li class="list-group-item">
                    <a href="toPublishTipPage.do">发表新贴</a>
                </li>
            </c:if>
                <%--超管特有--%>
            <c:if test="${USER.user_type == 0}">
                <li class="list-group-item">
                    <a href="toSignUpPage.do">注册新的管理员</a>
                </li>
            </c:if>
                <%--管理员功能 2020-03-14 22:33--%>
            <c:if test="${USER.user_type == 1 || USER.user_type == 0}">
                <li class="list-group-item"><a href="toUserManagePage.do">用户管理</a></li>
                <li class="list-group-item"><a href="toTipManagePage.do">贴子管理</a></li>
                <li class="list-group-item"><a href="toForumManagePage.do">版块管理</a></li>
                <li class="list-group-item"><a href="toTabManagePage.do">分类管理</a></li>
                <li class="list-group-item"><a href="tophoto.do">图像管理</a></li>
            </c:if>
            <li class="list-group-item"><a href="#">...待添加...</a></li>

        </ul>
    </div>
</c:if>

</div>
