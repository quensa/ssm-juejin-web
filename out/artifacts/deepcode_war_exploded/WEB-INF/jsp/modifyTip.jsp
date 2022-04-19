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
    <link href="<%=path%>/static/css/bootstrap.min.css" rel="stylesheet">
    <script src="<%=path%>/static/js/jquery-3.2.1.js"></script>
    <script src="<%=path%>/static/js/bootstrap.min.js"></script>
    <title>掘京 ›修改文章信息</title>
</head>
<body>
<!-- 引入header文件 -->
<%@ include file="header.jsp"%>

<div style="width: 70%;margin:1% 2% 1% 5%;float: left;">
    <div class="panel panel-default" id="main" style="">
        <div class="panel-heading" style="background-color: white">
            <a href="<%=basePath%>">掘京</a> › 修改文章信息 ›
            <a href="showpost.do?postId=${post.post_id}">${post.post_title}</a>
        </div>

        <div class="panel-body">
            <form  action="modifypost.do" id="mypostUpdateForm" method="POST" class="form-horizontal" role="form">
                <table class="table">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>修改前</th>
                        <th>修改后</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>ID</td>
                            <td>${post.post_id}</td>
                            <td><input class="form-control" type="hidden"
                                        name="post_id" value="${post.post_id}"/></td>
                        </tr>
                        <tr>
                            <td>版块</td>
                            <td>${post.tab.forum.forum_name}</td>
                            <td>
                                <select class="form-control" id="selectForum" name="selectedForumId" onchange="selectForumFunc()">
                                    <c:forEach items="${forums}" var="forum">
                                        <c:choose>
                                            <c:when test="${post.tab.forum.forum_id} == ${forum.forum_id}">
                                                <option value="${forum.forum_id}" selected>${forum.forum_name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${forum.forum_id}">${forum.forum_name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>分类</td>
                            <td>${post.tab.tab_name}</td>
                            <td>
                                <select class="form-control" id="selectTab" name="selectedTabId">
                                    <c:forEach items="${tabs}" var="tab">
                                        <c:choose>
                                            <c:when test="${post.tab.tab_id} == ${tab.tab_id}">
                                                <option value="${tab.tab_id}" selected>${tab.tab_name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${tab.tab_id}">${tab.tab_name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>作者</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty post.user.user_nick}">${post.user.user_nick}</c:when>
                                    <c:otherwise>${post.user.user_name}</c:otherwise>
                                </c:choose>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>标题</td>
                            <td>${post.post_title}</td>
                            <td><input class="form-control" type="text"
                                       name="post_title" value="${post.post_title}" required/></td>
                        </tr>
                        <tr>
                            <td>内容</td>
                            <td>${post.post_content}</td>
                            <td>
                                <textarea class="form-control" rows="2"
                                          name="post_content" required >${post.post_content}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>回复数</td>
                            <td>${post.post_replies}</td>
                        </tr>
                        <tr>
                            <td>发表时间<br>更新时间</td>
                            <td>
                                <fmt:formatDate value="${post.post_publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                <br>
                                <fmt:formatDate value="${post.post_modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                        </tr>
                        <tr>
                            <td>点击量</td>
                            <td>${post.post_click}</td>
                        </tr>
                        <tr>
                            <td>状态</td>
                            <td>
                                <c:choose>
                                    <c:when test="${post.post_isDeleted == 1}">删除</c:when>
                                    <c:otherwise>正常</c:otherwise>
                                </c:choose>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>操作</td>
                            <td>
                                <input class="btn btn-warning" type="button" value="修改" onclick="update_confirm()"/>
                                <input class="btn btn-default" type="reset" value="重填"/>
                                <input type="button" class="btn btn-default" value="返回"
                                       style="margin-left: 10%" onclick="window.location.href='<%=basePath%>toPostManagePage.do'" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>

<!-- 引入侧边栏文件 -->
<%@ include file="side.jsp"%>

<!-- 引入footer文件 -->
<%@ include file="footer.jsp"%>

<script>
    function update_confirm()
    {
        var r=confirm("确定修改?")
        if (r==true)
        {
            var form = document.getElementById("mypostUpdateForm"); // 由id获取表单
            form.submit();
        } else { }
    }

    function selectForumFunc() {
        // 获取选择的项目 jquery
        var selectedForum = $('select option:selected').val();
        // 获取分类下拉栏id
        var selectTab = document.getElementById("selectTab");
        // alert("您选择了：" + selectedForum);
        if (selectedForum != null){
            $.ajax(
                {
                    url:"<%=basePath%>getTabBySelectedForum.do",
                    type:"post",
                    data:{
                        selectedForum : selectedForum
                    },
                    dataType:"json",
                    success:function (data) {
                        var tabList = data;
                        if (tabList){
                            // 清除选项
                            selectTab.options.length = 0;
                            var optionStr = "";
                            // 先加一个无用的选项
                            optionStr += "<option value=\"\" selected>请选择分类</option>";
                            for (var i = 0; i < tabList.length; i++){
                                optionStr += "<option value=\"" + tabList[i].tab_id + "\">" +
                                    tabList[i].tab_name + "</option>";
                            }
                            //alert("将要添加的optionStr = " + optionStr);
                            // 添加到select标签（刷新选项）
                            $("select[id=selectTab]").append(optionStr);
                        }
                    }
                }
            )
        }else{
            alert("selectedForum == null");
            selectTab.options.length = 0;
        }
    }
</script>

</body>
</html>