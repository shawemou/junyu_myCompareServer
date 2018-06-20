$(function() {
	var cok = $.cookie('pageSize_mobile');
    if( cok != null ){
    	pageSize = cok;
    	$("#pageCount").val(cok);
    }
    $("#pageCount").on('change', function() {
		pageSize = $("#pageCount").val();
		$.cookie('pageSize_mobile', pageSize, { expires: 30 });
		query_stat_AJAX();
	});
    
	var shadowCSS = [];
	shadowCSS["width"] = $("#show_div").width();
	shadowCSS["height"] = $("#show_div").height();
	$("#loading_Shadow").css(shadowCSS);
	$("#loading_Shadow").hide();

	$("#statQueryBtn").on('click', function() {
		$("#page_no").val("1");
		query_stat_AJAX();
	});
	
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

function query_stat_AJAX() {
	var shadowCSS = [];
	shadowCSS["width"] = $("#show_div").width();
	shadowCSS["height"] = $("#show_div").height();
	$("#loading_Shadow").css(shadowCSS);
	
	$("#err_msg").html("");	
	$("#loading_Shadow").show();
	$("#statQueryBtn").attr("disabled", true);
	var param = {};
	param["beginDate"] = encodeURI($("#begin_date").val());
	param["endDate"] = encodeURI($("#end_date").val());
	param["bankName"] = encodeURI($("#bank_name").val());
	param["page_no"] = encodeURI($("#page_no").val());
	param["page_count"] = encodeURI($("#pageCount").val());
	param["random"] = newGuid();
	var url = "webStat?method=list";
	$.post(url, param, function(data, textStatus) {
		$("#statQueryBtn").attr("disabled", false);
		queryStatCallBack(data, textStatus);
	});
	
}

function queryStatCallBack(data, textStatus) {
	$("#loading_Shadow").hide();
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
	$("#page_list").empty();
	$("#page_no_html").empty();
	var pageNo = myobj["page_no"];
	$("#page_no").val(pageNo);
	var itemCount = myobj["itemCount"];
	if (itemCount) {
		$("#page_list").html(printPage(itemCount,pageNo));
		$("#page_no_html").html(printPageNum(itemCount,pageNo));
		onChilkPage();
	}else{
		$("#page_no_html").html("共计0条")
	}
	$("#maobile_query tbody").empty();
	for ( var i in myobj["data"]) {
		var item = myobj["data"][i];
		var tdhtml = '<tr>'
			+'<td>'+item["BANK_NAME"]+'</td>'
			+'<td>'+item["NAME"]+'</td>'
			+'<td>'+item["MOBILE"]+'</td>'
			+'<td>'+item["CC"]+'</td>'
		+'</tr>';
		$("#maobile_query tbody").append(tdhtml);
	}
	$("#maobile_query tbody tr:even").addClass("alt");	
}

function onChilkPage(){
	$("#fristPage").on('click', function() {
		$("#page_no").val("1");
		query_stat_AJAX();
	});
	$("#endPage").on('click', function() {
		$("#page_no").val(endPage);
		query_stat_AJAX();
	});
	$("#lastPage").on('click', function() {
		var page_no = parseInt($("#page_no").val());
		page_no = (page_no - 1) <= 0 ? 1 : (page_no - 1);
		$("#page_no").val(page_no);
		query_stat_AJAX();
	});
	$("#nextPage").on('click', function() {
		var page_no = parseInt($("#page_no").val());
		page_no = (page_no + 1) > endPage ? endPage : (page_no + 1);
		$("#page_no").val(page_no);
		query_stat_AJAX();
	});
	$("#page_next").keydown(function(e) {
		if(e.keyCode==13){
			var page_no = $("#page_next").val();
			if(!isNaN(page_no)){
				page_no = page_no > endPage ? endPage : page_no ;
				page_no = page_no <= 0 ? 1 : page_no ;
				$("#page_no").val(page_no);
				query_stat_AJAX();
			}else{
				alert("页码请输入数字");
			}
		}
	});
	
}
