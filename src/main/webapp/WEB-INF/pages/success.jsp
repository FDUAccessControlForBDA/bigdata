<%--
  Created by IntelliJ IDEA.
  User: symsimmy
  Date: 2017/10/12
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>success</title>
</head>
<body>
<h1>if you see this,you did it finally!</h1>
<input type="text" id="uid" value="1"/>
<input type="submit" id="submit" value="提交"/>
<p id="text">dispear</p>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function () {
    $("#submit").click(function () {
        //$("#text").hide();
        uid=$("#uid").val();
        $.ajax({
            type:"post",
            url:"api/user/getUserById",
            data:{
                uid:uid
            },
            success:function (data) {
                if(data.code=200){
                    var user=JSON.parse(''+data.user);
                    console.log(user.nickname);
                    $("#text").text(user.nickname);
                    //WAbysYPAJu
                }
            }
        });
    })
});
</script>
</body>
</html>
