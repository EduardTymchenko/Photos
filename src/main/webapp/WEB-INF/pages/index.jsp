<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
<div align="center">
    <form action="/view" method="POST">
        Photo id: <input type="text" name="photo_id">
        <input type="submit"/>
    </form>

    <form action="/add_photo" enctype="multipart/form-data" method="POST">
        Photo: <input type="file" name="photo">
        <input type="submit"/>
    </form>
</div>
<hr align="center" color="grey">
<div align="center">
    <form action="/show" method="post">
        <button type="submit" name="show" >Show pic</button>
        <button type="submit" name="del">Delete</button>
        <button type="submit" name="zip">Get in ZIP</button>

        <br>
        ${no_rez}
        <table>
            <c:forEach items="${photos}" var="photo">
                <tr>
                    <td>
                        <input type="checkbox" name="id_del" value="${photo}">
                    </td>
                    <td>
                        <img src="photo/${photo}" width="50" height="50">
                    </td>
                    <td>
                            ${photo}
                    </td>
                </tr>

            </c:forEach>
        </table>
    </form>

</div>
</body>
</html>
