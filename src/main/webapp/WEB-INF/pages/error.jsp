<%@ page isErrorPage="true" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<h1>An error has occurred.</h1>

<div style="color: #F00;">
    Error message: <%= exception.toString() %>
</div>
</body>
</html>
