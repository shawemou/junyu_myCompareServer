$(function() {
	var shadowCSS = [];
	shadowCSS["width"] = $("#show_div").width();
	shadowCSS["height"] = $("#show_div").height();
	$("#loading_Shadow").css(shadowCSS);
	$("#loading_Shadow").hide();

	$("#mobileSaveBtn").on('click', function() {
		mobile_Save_AJAX();
	});
	$("#clearBtn").on('click', function() {
		loadMobile();
	});
	$("#guid").val(getUrlParam('id'));
	loadMobile();
	
	 $( "#bank_name" ).autocomplete({
      source: function( request, response ) {
                $.ajax({
                    url: "dicBank",
                    dataType: "json",
                    data:{
                        bank_name: encodeURI($("#bank_name").val()),
                        random : newGuid()
                    },
                    success: function( data ) {
                        response( $.map( data, function( item ) {
                            return {
                                value:item.value
                            }
                        }));
                    }
                });
            },
      minLength: 0
    });
});

function loadMobile(){
	$("#mobileSaveBtn").attr("disabled", true);
	$("#loading_Shadow").show();
	var param = {};
	param["guid"] = $("#guid").val();
	param["random"] = newGuid();
	var url = "webMobile?method=loadMobile";
	$.post(url, param, function(data, textStatus) {
		$("#mobileSaveBtn").attr("disabled", false);
		loadMobileCallBack(data, textStatus);
	});
}

function loadMobileCallBack(data, textStatus) {
	$("#loading_Shadow").hide();
	$("#mobileSaveBtn").attr("disabled", false);
	if (textStatus != "success") {
		alert("请求错误！");
		return;
	}
	if (data === "ERROR") {
		alert("后台发生错误！");
		return;
	}
	try {
		var myobj = eval('(' + data + ')');
	} catch (Error) {
		alert("数据转换失败,格式不正确！");
		return;
	}
	if (myobj == undefined) {
		alert("未获取到数据！");
		return;
	}
	if(myobj["sessionState"] == 0){
		window.location.href = "login.html?data=" + newGuid();
		return;
	}
	if(myobj){
		$("#mobile").val(myobj["MOBILE"]);
		$("#name").val(myobj["NAME"]);
		$("#gender").val(myobj["GENDER"]);
		$("#id_number").val(myobj["ID_NUMBER"]);
		$("#department").val(myobj["DEPARTMENT"]);
		$("#duties").val(myobj["DUTIES"]);
		$("#detail_des").val(myobj["DETAIL_DES"]);
		$("#bank_name").val(myobj["BANK_NAME"]);
	}
}

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
    
function mobile_Save_AJAX() {
	$("#err_msg").html("");	
	if($("#mobile").val() == ""){
		$("#err_msg").html("手机号码为空");
		return;	
	}
	if($("#mobile").val().length > 11){
		$("#err_msg").html("手机号码长度大于11位");
		return;	
	}
	$("#loading_Shadow").show();
	$("#mobileSaveBtn").attr("disabled", true);
	var param = {};
	param["guid"] = $("#guid").val();
	param["mobile"] = encodeURI($("#mobile").val());
	param["name"] = encodeURI($("#name").val());
	param["gender"] = encodeURI($("#gender").val());
	param["id_number"] = encodeURI($("#id_number").val());
	param["department"] = encodeURI($("#department").val());
	param["duties"] = encodeURI($("#duties").val());
	param["detail_des"] = encodeURI($("#detail_des").val());
	param["bank_name"] = encodeURI($("#bank_name").val());
	param["random"] = newGuid();
	var url = "webMobile?method=updateMobile";
	$.post(url, param, function(data, textStatus) {
		$("#mobileQueryBtn").attr("disabled", false);
		queryMobileCallBack(data, textStatus);
	});
	
}

function queryMobileCallBack(data, textStatus) {
	$("#loading_Shadow").hide();
	$("#mobileSaveBtn").attr("disabled", false);
	if (textStatus != "success") {
		alert("请求错误！");
		return;
	}
	if (data === "ERROR") {
		alert("后台发生错误！");
		return;
	}
	try {
		var myobj = eval('(' + data + ')');
	} catch (Error) {
		alert("数据转换失败,格式不正确！");
		return;
	}
	if (myobj == undefined) {
		alert("未获取到数据！");
		return;
	}
	
	if(myobj["sessionState"] == 0){
		window.location.href = "login.html?data=" + newGuid();
		return;
	}
	
	if (myobj["success"]) {
		window.location.href = "mobile.html?data=" + newGuid();
	} else {
		$("#err_msg").html(myobj["info"]);
	}
}