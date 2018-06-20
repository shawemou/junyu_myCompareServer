//�����ڵ�¼��js
$(function() {
	var shadowCSS = [];
	shadowCSS["width"] = $("#show_div").width();
	shadowCSS["height"] = $("#show_div").height();
	$("#loading_Shadow").css(shadowCSS);
	$("#loading_Shadow").hide();
	
	//�����¼
	$("#loginButton").on('click', function() {
		login_AJAX();
	});
	$("#login_name,#login_psd").keydown(function(e) {
		if(e.keyCode==13){
			login_AJAX();
		}
	});
});

	//����webLogin
function login_AJAX() {
	$("#err_msg").html("");
	if($("#login_name").val() == ""){
		$("#err_msg").html("��¼��Ϊ��");
		return;
	}else if($("#login_psd").val() == ""){
		$("#err_msg").html("��¼����Ϊ��");
		return;
	}
	
	$("#loading_Shadow").show();
	$("#loginButton").attr("disabled", true);
	var param = {};
	param["login_name"] = $("#login_name").val();
	param["login_psd"] = $("#login_psd").val();
	param["random"] = newGuid();
	var url = "webLogin";
	$.post(url, param, function(data, textStatus) {
		$("#loginButton").attr("disabled", false);
		queryCallBack(data, textStatus);
	});
}
	
	//��¼�ɹ�����ת��mobile.html
function queryCallBack(data, textStatus) {
	if (textStatus != "success") {
		alert("�������");
		$("#loading_Shadow").hide();
		return;
	}
	if (data === "ERROR") {
		alert("��̨��������");
		$("#loading_Shadow").hide();
		return;
	}
	try {
		var myobj = eval('(' + data + ')');
		//$("#loading_Shadow").hide();
	} catch (Error) {
		alert("����ת��ʧ��,��ʽ����ȷ��");
		$("#loading_Shadow").hide();
		return;
	}
	if (myobj == undefined) {
		alert("δ��ȡ�����ݣ�");
		$("#loading_Shadow").hide();
		return;
	}

	if (myobj["success"]) {
		window.location.href = "mobile.html?date=" + newGuid();
	} else {
		$("#err_msg").html(myobj["info"]);
		$("#loading_Shadow").hide();
	}
}