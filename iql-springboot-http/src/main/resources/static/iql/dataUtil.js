function addParamsToUrl(url, params) {
	return [
		url, 
		$.map(params, function(v, k) { 
			return [k, v].join("=") 
		}).join("&") 
	].join("?")
}

function autocomplete(cm, event) {
    //所有的字母和'$','{','.'在键按下之后都将触发自动完成
    if (!cm.state.completionActive &&
        ((event.keyCode >= 65 && event.keyCode <= 90 ) || event.keyCode == 52 || event.keyCode == 219 || event.keyCode == 190)) {
        CodeMirror.commands.autocomplete(cm, null, {completeSingle: false});
    }
}
