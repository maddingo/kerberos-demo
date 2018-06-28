<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Kerberos Demo page</title>
        <spring:url value="/css/main.css" var="springCss" />
        <link href="${springCss}" rel="stylesheet" />
    </head>
    <body>
        <h1>Kerberos Demo page</h1>
        <h2>Error ${status}</h2>
        <p>
        Message:
        <pre>
        ${message}
        </pre>
        </p>
        <p><a href="/">Back</a></p>
    </body>
</html>