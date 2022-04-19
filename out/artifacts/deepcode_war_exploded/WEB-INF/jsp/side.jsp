<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 这是侧边栏 -->

<!-- 未登录 -->
<c:if test="${empty USER}">

    <div class="panel panel-default" id="sidebar2" style="width: 20%;margin:1% 2% 1% 0%;float: right">
        <div class="panel-heading" style="background-color: white;text-align: center">
            <blockquote>
                吸土掘京
                <small>代码不止，掘京不停</small>
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
            <img src="${USER.user_img}" style="border-radius:100%" width="40px" height="40px" >
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
                    <a href="toPublishTipPage.do">发表文章</a>
                </li>
                <li class="list-group-item">
                    <a href="MyCollect">我的收藏</a>
                </li>
                <li class="list-group-item">
                    <a href="getPush.do">我的关注</a>
                </li>
                <li class="list-group-item">
                    <a href="toAsk.do">提问</a>
                </li>
                <li class="list-group-item">
                    <a href="selask.do">回答</a>
                </li>
            </c:if>
            <li class="list-group-item"><a href="#">...待添加...</a></li>

        </ul>
    </div>
</c:if>

</div>
