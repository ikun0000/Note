<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>书籍展示页面</title>
</head>
<body>
    <h1>书籍展示页面</h1>
    <div>
        <table>
            <tr>
                <th style="width: 300px">书名</th>
                <th style="width: 300px">数量</th>
                <th style="width: 300px">描述</th>
                <th style="width: 300px">操作</th>
            </tr>

           <c:forEach var="book" items="${books}">
            <tr>
                <td><c:out value="${book.bookName}"/></td>
                <td><c:out value="${book.bookCounts}"/></td>
                <td><c:out value="${book.detail}"/></td>
                <td>
                    <a href='<c:url value="/book/deleteBook"><c:param name="bookId" value="${book.bookId}" ></c:param></c:url>'>删除</a>
                    <a href='<c:url value="/book/updateBook"><c:param name="bookId" value="${book.bookId}" ></c:param></c:url>'>更新</a>
                </td>
            </tr>
           </c:forEach>
        </table>
    </div>
</body>
</html>        