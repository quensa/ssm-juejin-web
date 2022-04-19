<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>评论管理 - 掘京</title>
    <link href="<%=path%>/static/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=path%>/static/js/jquery-3.2.1.js"></script>
    <script src="<%=path%>/static/js/bootstrap.min.js"></script>
    <style>
        li {list-style-type:none;}

        a{
            color: #8A8A8A;
            cursor: pointer;
        }

        th {
            text-align:center; /*设置水平居中*/
            /* vertical-align:middle; */ /*设置垂直居中*/
        }
        td {
            text-align:center; /*设置水平居中*/
            /* vertical-align:middle; */ /*设置垂直居中*/
            border: 1px solid gray;
        }
    </style>
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

<%--引入header文件--%>
<%@ include file="header.jsp"%>

<div class="panel panel-default"
     style="width: 90%;margin-left: 5%; margin-right: 5%; margin-bottom: 5%">
    <div class="panel-heading" style="background-color: #fff">
        <h3 class="panel-title">评论管理</h3>
    </div>
    <div class="panel-body">

        <%-- 文章管理界面添加搜索功能  --%>
        <form role="form" method="reply" action="searchTipsFuzzy.do">
            <div class="input-group col-md-3">
                <input type="text" class="form-control" name="tipKeyword" placeholder="文章ID/标题/正文内容"/>
                <span class="input-group-btn"><button type="submit" class="btn btn-info btn-search">查询</button></span>
            </div>
        </form>

        <%-- 这里显示所有文章信息 --%>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>用户</th>
                <th>评论内容</th>
                <th>点赞数</th>
                <th>点踩数</th>
                <th>发表时间</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <%--这里是表格内容，需要遍历数组--%>
            <c:forEach items="${replies}" var="reply">
                <tr>
                    <td>${reply.reply_id}</td>
                        <%--发贴用户昵称或用户名--%>
                    <td>
                        <c:choose>
                            <c:when test="${not empty reply.user.user_nick}">${reply.user.user_nick}</c:when>
                            <c:otherwise>${reply.user.user_name}</c:otherwise>
                        </c:choose>
                    </td>
                        <%--内容--%>
                    <td style="overflow: hidden; text-overflow: ellipsis; max-width: 120px; white-space: nowrap">${reply.reply_content}</td>
                        <%--点赞数--%>
                    <td>${reply.reply_good_count}</td>
                        <%--点踩数--%>
                    <td>${reply.r_bad_count}</td>
                        <%--发贴时间 修改时间--%>
                    <td>
                        <fmt:formatDate value="${reply.reply_publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                        <%--状态--%>
                    <td>
                        <c:if test="${reply.reply_isDeleted == 1}"><span class="label label-danger">已删除</span></c:if>
                    </td>
                    <td><!-- 这里显示操作按钮 -->
                        <input type="button" class="btn btn-warning" value="查看"
                               onclick="window.location.href='<%=basePath%>toReplyContent.do?reply_id=${reply.reply_id}'"/>
                        <c:choose>
                            <c:when test="${reply.reply_isDeleted == 1}">
                                <input type="button" class="btn btn-success" value="取消删除"
                                       onclick="window.location.href='<%=basePath%>ChangeReplyStatus.do?reply_id=${reply.reply_id}&opr=0'"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="btn btn-danger" value="删除"
                                       onclick="window.location.href='<%=basePath%>ChangeReplyStatus.do?reply_id=${reply.reply_id}&opr=1'"/>
                            </c:otherwise>
                        </c:choose>
                            <%--置顶 2020-02-27 10:00--%>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <input type="button" class="btn btn-default" value="返回"
               style="margin-left: 17%" onclick="window.location.href='<%=basePath%>toMainPage.do'" />

    </div>
</div>

<!-- 引入footer文件 -->
<%@ include file="footer.jsp"%>

</body>
</html>