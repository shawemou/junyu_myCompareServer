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
		alert("�������");
		return;
	}
	if (data === "ERROR") {
		alert("��̨��������");
		return;
	}
	try {
		var myobj = eval('(' + data + ')');
	} catch (Error) {
		alert("����ת��ʧ��,��ʽ����ȷ��");
		return;
	}
	if (myobj == undefined) {
		alert("δ��ȡ�����ݣ�");
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

//��ȡurl�еĲ���
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //����һ������Ŀ�������������ʽ����
    var r = window.location.search.substr(1).match(reg);  //ƥ��Ŀ�����
    if (r != null) return unescape(r[2]); return null; //���ز���ֵ
}
    
function mobile_Save_AJAX() {
	$("#err_msg").html("");	
	if($("#mobile").val() == ""){
		$("#err_msg").html("�ֻ�����Ϊ��");
		return;	
	}
	if($("#mobile").val().length > 11){
		$("#err_msg").html("�ֻ����볤�ȴ���11λ");
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
		alert("�������");
		return;
	}
	if (data === "ERROR") {
		alert("��̨��������");
		return;
	}
	try {
		var myobj = eval('(' + data + ')');
	} catch (Error) {
		alert("����ת��ʧ��,��ʽ����ȷ��");
		return;
	}
	if (myobj == undefined) {
		alert("δ��ȡ�����ݣ�");
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