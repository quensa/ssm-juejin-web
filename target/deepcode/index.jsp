<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
//    request.getSchema()可以返回当前页面使用的协议，http 或是 https;
//    request.getServerName()可以返回当前页面所在的服务器的名字，这里是localhost;
//    request.getServerPort()可以返回当前页面所在的服务器使用的端口,就是8080;
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>
<!DOCTYPE html>
<html>
<body>
<%
    response.sendRedirect(basePath + "toMainPage.do");

%>
</body>
</html>
