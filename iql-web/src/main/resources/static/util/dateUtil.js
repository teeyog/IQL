/**
 * Created by dd on 2017/3/30.
 */

/**
 * date往前time天
 *
 * @param num
 * @return
 */
function operDay(date, time){
    var today = new Date(date);
    today.setTime(today.getTime()+(24*time)*60*60*1000);
    var y = today.getFullYear();
    var m = (today.getMonth() + 1) < 10 ? "0" + (today.getMonth() + 1) : (today.getMonth() + 1);// 获取当前月份的日期
    var d = today.getDate() < 10 ? "0" + today.getDate() : today.getDate();
    var pre = y + "-" + m + "-" + d;
    return pre;
}

/**
 * 获取前几天的数据
 *
 * @param num
 * @return
 */
function getPointTime(date) {
    var today = new Date(date);
//	today.setDate(today.getTime() - 2);
    var y = today.getFullYear();
    var m = (today.getMonth() + 1) < 10 ? "0" + (today.getMonth() + 1) : (today.getMonth() + 1);// 获取当前月份的日期
    var d = today.getDate() < 10 ? "0" + today.getDate() : today.getDate();
    var h = today.getHours() < 10 ? "0" + today.getHours() : today.getHours();
    var pre = y + "-" + m + "-" + d+" "+h;
    return pre;
}

/**
 * 获得日期年月日时
 * 如果分钟20之前返回当前时间前4个小时否则当前时间前3个小时
 * @return
 */
function getDateTimeByHour(){
    var date = new Date();
    var minutes = date.getMinutes();
    if(minutes < 20){
        return getPointTime(new Date(new Date().getTime()-(1000*60*60*4)));
    }else{
        return getPointTime(new Date(new Date().getTime()-(1000*60*60*3)));
    }
}

/**
 * 获取前几天的数据
 *
 * @param num
 * @return
 */
function getPointDate(num) {
    var today = new Date();
    today.setDate(today.getDate() - num);
    var y = today.getFullYear();
    var m = (today.getMonth() + 1) < 10 ? "0" + (today.getMonth() + 1) : (today.getMonth() + 1);// 获取当前月份的日期
    var d = today.getDate() < 10 ? "0" + today.getDate() : today.getDate();
    var pre = y + "-" + m + "-" + d;
    return pre;
}

function dateDiff(interval, date1, date2) {
	var long = new Date(date2).getTime() - new Date(date1).getTime(); //相差毫秒
	switch(interval.toLowerCase()) {
		case "y": return parseInt(date2.getFullYear() - date1.getFullYear());
		case "m": return parseInt((date2.getFullYear() - date1.getFullYear())*12 + (date2.getMonth()-date1.getMonth()));
		case "d": return parseInt(long/1000/60/60/24);
		case "w": return parseInt(long/1000/60/60/24/7);
		case "h": return parseInt(long/1000/60/60);
		case "n": return parseInt(long/1000/60);
		case "s": return parseInt(long/1000);
		case "l": return parseInt(long);
	}
}

function getFormJSON(e, params) {
	var formArray = e.serializeArray();
	$.each(formArray, function(i, item) {
		if(item.name=="date") {
			var date = item.value.split(" - ");
			if(date.length > 1) {
				params.startDate = date[0];
				params.endDate = date[1];
			} else {
				params.startDate = date[0];
				params.endDate = date[0];
			}
		} else {
			if(item.value.trim()!="") {
				params[item.name] = item.value;
			}
		}
	})
	return params;
}

$.fn.getFormJSON = function(params) {
	var formArray = $(this).serializeArray();
	$.each(formArray, function(i, item) {
		if(item.name=="date") {
			var date = item.value.split(" - ");
			if(date.length > 1) {
				params.startDate = date[0];
				params.endDate = date[1];
			} else {
				params.startDate = date[0];
				params.endDate = date[0];
			}
		} else {
			if(item.value.trim()!="") {
				params[item.name] = item.value;
			}
		}
	})
	return params;
}