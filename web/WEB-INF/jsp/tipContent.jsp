<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<%=path%>/static/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="<%=path%>/static/js/jquery-3.2.1.js"></script>
    <script src="<%=path%>/static/js/bootstrap.min.js"></script>
    <title>${post.post_title} - 掘京 </title>
</head>
<body>
<%--这是进入文章查看内容的页面--%>

<%--弹出结果--%>
<c:if test="${not empty myInfo}">
    <script type="text/javascript" language="javascript">
        {
            alert("<%=request.getAttribute("myInfo")%>");
        }
    </script>
</c:if>

<!-- 引入header文件 -->
<%@ include file="header.jsp" %>

<div style="width: 70%;margin:1% 2% 1% 5%;float: left;">
    <div class="panel panel-default" id="main" style="">
        <div class="panel-heading" style="background-color: white">
            <div>
                <div class="panel-heading" style="background-color: white">
                    <a href="<%=basePath%>">掘京</a> › <a href="showpost.do?tipId=${post.post_id}">${post.post_title}</a>
                </div>
                <%--文章标题--%>
                <h4>${post.post_title}
                </h4>

                <span class="label label-warning" title="点击量">阅读${post.post_click}</span>
                <div>
                    <%--显示发表人昵称--%>
                    <a href="getUserInfo.do?userId=${post.user.user_id}">
                        <span>
                        <strong>
                            <c:choose>
                                <c:when test="${post.post_isHide==0}">
                                    ${post.user.user_name}
                                </c:when>
                                <c:otherwise>
                                    <span>匿名</span>
                                </c:otherwise>
                            </c:choose>
                        </strong>
                            <%--展示用户权限--%>
                        <c:choose>
                            <c:when test="${post.user.user_type == 0}"> <span
                                    class="label label-success">超级管理员</span></c:when>
                            <c:when test="${post.user.user_type == 1}"> <span
                                    class="label label-warning">管理员</span></c:when>
                            <c:otherwise><span class="label label-default">普通用户</span></c:otherwise>
                        </c:choose>
                        </span>
                    </a>
                    <c:if test="${USER.user_id != post.user.user_id}">
                        <a class="label label-info" onclick="addFocus()">+关注</a>
                    </c:if>
                </div>
                <div>
                    <span>
                    <small class="text-muted">发表于：<fmt:formatDate value="${post.post_publishTime}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></small>
                    <br>
                    <small class="text-muted">更新于：<fmt:formatDate value="${post.post_modifyTime}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></small>
                </span>

                </div>
            </div>
        </div>
        <div style="height: 80px; overflow:auto; word-wrap:break-word;">
            <%--这里显示回复的正文--%>
            <c:out value="${post.post_content}"></c:out>
            <div class="btn-group" style="float:right">
                <button onclick="a2();" class="btn btn-primary btn-small" type="button">点赞</button>
                <button style="float: right" onclick="a1();" type="button" class="btn btn-default" id="btn_collect">
                    <span class="glyphicon glyphicon-star-empty" id="btn_collect_icon" aria-hidden="true"></span>收藏
                </button>
            </div>
        </div>
    </div>


    <%--这里显示贴子的回复--%>
    <ul class="list-group" style="width: 100%">
        <%--遍历并显示回复--%>
        <c:forEach items="${replies}" var="reply">
            <input type="text" id="text" value="${reply.reply_id}" hidden>
            <li class="list-group-item">
                <div style="height: auto; ">
                    <div>
                        <a href="getUserInfo.do?userId=${reply.user.user_id}">
                            <strong>
                                    <%--显示发表回复的用户昵称--%>
                                <c:choose>
                                    <c:when test="${reply.reply_isHide==0}">
                                        <img src="${reply.user.user_img}" style="border-radius:100%" width="30px"
                                             height="30px">
                                        ${reply.user.user_name}
                                    </c:when>
                                    <c:otherwise>
                                        <span>匿名</span>
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                                <%--展示用户权限--%>
                            <c:choose>
                                <c:when test="${reply.user.user_type == 0}"> <span
                                        class="label label-success">超级管理员</span></c:when>
                                <c:when test="${reply.user.user_type == 1}"> <span
                                        class="label label-warning">管理员</span></c:when>
                                <c:otherwise><span class="label label-default">普通用户</span></c:otherwise>
                            </c:choose>
                        </a>
                            <%--发表回复的用户如果是作者则显示作者标签 2020-03-14 23:36--%>
                        <c:if test="${reply.user.user_id == post.user_id}"><span
                                class="label label-info">作者</span></c:if>
                        &nbsp;
                        <small class="text-muted">发表于：
                                <%--显示回复发表的时间--%>
                            <fmt:formatDate value="${reply.reply_publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </small>
                    </div>
                    <div style="height: 80px; overflow:auto; word-wrap:break-word;">
                            <%--这里显示回复的正文--%>
                        <c:out value="${reply.reply_content}"></c:out>
                        <div class="btn-group" style="float:right">
                            <button onclick="a3();" class="btn btn-primary btn-small"
                                    type="button">${reply.reply_good_count}赞
                            </button>

                            <button onclick="a4();" class="btn btn-primary btn-small" type="button">${reply.r_bad_count}踩</button>
                            <button onclick="a5()" class="btn btn-primary btn-small" type="button">查看评论</button>

                        </div>
                        <ul>
                            <li>
                                <span id="replyshow"></span>
                            </li>
                        </ul>
                    </div>
                </div>
            </li>
        </c:forEach>
    </ul>


    <div class="panel panel-default">
        <div class="panel-heading" style="background-color: white">评论</div>
        <div class="panel-body">
            <div class="form-group">
                <%--非删除或结贴的贴子才能回复--%>
                <c:choose>
                    <c:when test="${not empty USER}">
                        <c:choose>
                            <c:when test="${USER.user_status == 2}">
                                <%--被锁定的用户不能评论--%>
                                <p style="color: red">您的账号已被锁定，不能评论，请联系管理员解锁。</p>
                            </c:when>
                            <c:otherwise>
                                <%--这里是发表回复的表单--%>
                                <form action="publishReply.do" method="post" id="myReplyForm">
                                    <input type="hidden" name="post_id" value="${post.post_id}">
                                        <%--这里显示输入回复内容的文本框--%>
                                    <textarea class="form-control" rows="3" name="reply_content"
                                              id="reply_content" required></textarea>
                                    <br/>
                                    <input type="button" class="btn btn-success btn-sm"
                                           value="发表" onclick="publishReply_confirm()"/>
                                    <input type="checkbox" id="status" name="IsHide" value="true" checked>匿名
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <%--如果用户没有登录则取消文本框--%>
                        <input type="button" class="btn btn-warning btn-sm"
                               value="请先登录"
                               onclick="window.location.href='<%=basePath%>toLoginPage.do?postId=${post.post_id}'"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<!-- 引入侧边栏文件 -->
<%@ include file="side.jsp" %>

<!-- 引入footer文件 -->
<%@ include file="footer.jsp" %>

<script>
    function publishReply_confirm() {
        var form = document.getElementById("myReplyForm"); // 由id获取表单
        var replycontent = form.reply_content.value; // 获取输入的回复内容
        if (replycontent == '') {
            alert("请填写回复内容！");
        } else {
            var r = confirm("确定发表该回复?")
            if (r == true) {
                form.submit(); // 提交表单
            } else {
            }
        }
    }


    function a3() {
        $.ajax({
            url: "<%=basePath%>addReplyFavor", //这个对应Controller的URL，和你们以前表单里面的action一样
            data: { //data就是你想要传什么数据到Controller层，这里的数据是json数据。
                replyid: document.getElementById("text").value
            },
            type: "POST", //类型，POST或者GET,就和表单与超链接一样
            dataType: 'text', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (data) { //成功，回调函数
                alert(data.toString());//可以用data调用Controller返回的值
            },
            error: function () { //失败，回调函数
                alert("请先登录");
            }
        });
    }

    function a4() {
        $.ajax({
            url: "<%=basePath%>addReplyBad",//这个对应Controller的URL，和你们以前表单里面的action一样
            data: { //data就是你想要传什么数据到Controller层，这里的数据是json数据。
                replyid: document.getElementById("text").value
            },
            type: "POST", //类型，POST或者GET,就和表单与超链接一样
            dataType: 'text', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (data) { //成功，回调函数
                alert(data.toString());//可以用data调用Controller返回的值
            },
            error: function () { //失败，回调函数
                alert("请先登录");
            }
        });
    }

    function a5() {
        $.ajax({
            url: "<%=basePath%>showReplyTwo.do", //这个对应Controller的URL，和你们以前表单里面的action一样
            data: { //data就是你想要传什么数据到Controller层，这里的数据是json数据。
                reply_id: document.getElementById("text").value
            },
            type: "POST", //类型，POST或者GET,就和表单与超链接一样
            dataType: 'json', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (s) { //成功，回调函数
                $.each(s, function (reply_content, value) {
                    $("#replyshow").html(value.reply_content);
                    console.log(value.user);
                });

            },

            error: function () { //失败，回调函数
                alert("请先登录");
            }
        });
    }

</script>


<script type="text/javascript" src="http://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script type="text/javascript">
    a1 = function () {
        $.ajax({
            url: "<%=basePath%>addCollect",
            type: "post",
            data: {
                postId: ${post.post_id},
            },
            dataType: 'text', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (data) {
                var classname = $("#btn_collect_icon").attr("class");
                $("#btn_collect_icon").removeClass("glyphicon-star-empty glyphicon-star");
                if (data.toString() == "收藏成功") {
                    $("#btn_collect_icon").addClass("glyphicon glyphicon-star");
                    alert("收藏成功");
                } else {
                    $("#btn_collect_icon").addClass("glyphicon glyphicon-star-empty");
                    alert("取消收藏");
                }
            },
            error: function () {
                alert("请先登录");
            }
        })
    }

    function a2() {
        $.ajax({
            url: "<%=basePath%>addFavor", //这个对应Controller的URL，和你们以前表单里面的action一样
            data: { //data就是你想要传什么数据到Controller层，这里的数据是json数据。
                postId: ${post.post_id},
            },
            type: "POST", //类型，POST或者GET,就和表单与超链接一样
            dataType: 'text', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (data) { //成功，回调函数
                alert(data.toString());//可以用data调用Controller返回的值

            },
            error: function () { //失败，回调函数
                alert("请先登录");
            }
        });
    }

    function addFocus() {
        $.ajax({
            url: "<%=basePath%>addUserFocus", //这个对应Controller的URL，和你们以前表单里面的action一样
            data: { //data就是你想要传什么数据到Controller层，这里的数据是json数据。
                buid: ${post.user.user_id}
            },
            type: "POST", //类型，POST或者GET,就和表单与超链接一样
            dataType: 'text', //Controller层返回类型，如果返回String，就用text,返回json,就用json
            success: function (data) { //成功，回调函数
                alert(data.toString());//可以用data调用Controller返回的值
            },
            error: function () { //失败，回调函数
                alert("请先登录");
            }
        });
    }
</script>


</body>
</html>