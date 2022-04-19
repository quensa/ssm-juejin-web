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
    <title>文章管理 - 掘京</title>
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
        <h3 class="panel-title">文章管理</h3>
    </div>
    <div class="panel-body">


        <%-- 这里显示所有文章信息 --%>
        <table class="table">
            <thead>
            <tr>
                <th>发信人ID</th>
                <th>内容</th>
                <th>发送时间</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <%--这里是表格内容，需要遍历数组--%>
            <c:forEach items="${messages}" var="message">
                <tr>
                    <td>${message.user_id}</td>
                        <%--内容--%>
                    <td style="overflow: hidden; text-overflow: ellipsis; max-width: 120px; white-space: nowrap">${message.message_content}</td>

                        <%--发贴时间--%>
                    <td>
                        <fmt:formatDate value="${message.user_regTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                        <%--状态--%>
                    <td>

                        <c:if test="${message.message_type == 0}"><span class="label label-danger">未读</span></c:if>
                        <c:if test="${message.message_type == 1}"><span class="label label-warning">已读</span></c:if>
                         <c:if test="${message.message_type == 2}"><span class="label label-warning">已删除/span></c:if>

                    </td>
                    <td><!-- 这里显示操作按钮 -->
                        <input type="button" class="btn btn-warning" value="回复"
                               onclick="window.location.href='<%=basePath%>toAsk.do?user_id=${message.user_id}'"/>
                        <c:choose>
                            <c:when test="${message.message_type == 0}">
                                <input type="button" class="btn btn-success" value="已读"
                                       onclick="window.location.href='<%=basePath%>ChangeMessageStatus.do?message_type=1&message_id=${message.message_id}'"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="btn btn-danger" value="删除"
                                       onclick="window.location.href='<%=basePath%>ChangeMessageStatus.do?message_type=2&message_id=${message.message_id}'"/>
                            </c:otherwise>
                        </c:choose>
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