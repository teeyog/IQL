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

// 时间戳格式化 x小时前 x天前...
function timeago(dateTimeStamp) {
	var minute = 1000 * 60;      //把分，时，天，周，半个月，一个月用毫秒表示
    var hour = minute * 60;
    var day = hour * 24;
    var week = day * 7;
    var halfamonth = day * 15;
    var month = day * 30;
    
    var now = new Date().getTime();   //获取当前时间毫秒
    var diffValue = now - dateTimeStamp; //时间差
    
    if (diffValue < 0) { return; }
    
    var minC = diffValue / minute;  //计算时间差的分，时，天，周，月
    var hourC = diffValue / hour;
    var dayC = diffValue / day;
    var weekC = diffValue / week;     
    var monthC = diffValue / month;

    if (monthC >= 1) {
    	result = " " + parseInt(monthC) + "月前"
    } else if (weekC >= 1) {
    	result = " " + parseInt(weekC) + "周前"
    } else if (dayC >= 1) {
    	result = " " + parseInt(dayC) + "天前"
    } else if (hourC >= 1) {
    	result = " " + parseInt(hourC) + "小时前"
    } else if (minC >= 1) {
    	result = " " + parseInt(minC) + "分钟前"
    } else {
    	result = "刚刚"
    }  
    return result;
}

//时间戳转日期
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '年';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
    D = date.getDate() + '日 ';
    h = date.getHours() + '点';
    m = date.getMinutes() + '分';
    s = date.getSeconds() + '秒';
    return Y+M+D+h+m;
}
