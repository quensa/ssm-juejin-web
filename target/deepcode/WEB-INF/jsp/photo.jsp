<%--
  Created by IntelliJ IDEA.
  User: lf
  Date: 2022/2/21
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图像管理</title>
</head>
<body>
<form action="upload.do" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="upload">
</form>
</body>
</html>
