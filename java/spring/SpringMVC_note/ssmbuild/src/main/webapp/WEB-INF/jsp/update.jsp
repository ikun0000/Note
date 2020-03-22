<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title></title>
</head>
<body>
    <h3>修改书籍页面</h3>
    <form action="/ssmbuild/book/updateBook" method="post">
        <input type="hidden" name="bookId" value="${book.bookId}">
        书名：<input type="text" value="${book.bookName}" name="bookName"><br>
        数量：<input type="text" value="${book.bookCounts}" name="bookCounts"><br>
        描述：<input type="text" value="${book.detail}" name="detail"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>        