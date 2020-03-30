jQuery(document).ready(function($){
    //登录
	$("#loginButton").click(function(){	
	var user = document.getElementById("user");
	var pass = document.getElementById("pass");
	var tran ={user:user.value,
			   password:pass.value};
    if(user.value == ""|| pass.value == "")
     {
		alert("账户和密码不能为空");
	 }else{
		 $.ajax({
			type:"POST",
			url:"/user/adminLogin.do",
			data:tran,
			dataType:"text",
			success:function(res){
				console.log(res);
				var json=JSON.parse(res);
				var judge=json.state;
				if(judge=="true"){
				window.open("page/manage.html");
			}else{
				alert("密码或账户输入错误");
			}
		 }
		 }
		 );
	 }
	});
});