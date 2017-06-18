<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>login</title>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
	
</script>
<script>
	function checkCardNum() {
		$.ajax({
			type : "POST",
			url : "${pageContext.request.contextPath }/card/checkCardNum.htm",
			data : //$("form").serialize(),	
			{cardNum:$("#cardNum").val()},
			dataType : "json",
			success : function(data) {
				if (data.code != 1) {
					$("#msg").html(data.msg);
				}
			}
		});
	}

	//GET方式
	/*$.ajax({
	 type : "get",
	 url : "${pageContext.request.contextPath }/LoginServlet?method=checkPwd",
	 data : "name=xxx",
	 dataType: "json",
	 success : function(data) {
	 if(data.code==0){
	 $("#msg").html(data.msg);
	 }
	 }
	 });*/
</script>
</head>
<body>
	<form action="${pageContext.request.contextPath }/card/check.htm"
		method="post">
		<table>
			<tr>
				<td>卡号：</td>
				<td><input type="text" name="cardNum" id="cardNum"
					onblur="checkCardNum();" onfocus="$('#msg').html('');" /></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="text" name="password" id="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><span id="msg"  style="color: red"></span></td>
			</tr>
			<tr>
				<td><input type="submit" value="提交" /></td>
			</tr>
		</table>
	</form>
</body>
</html>