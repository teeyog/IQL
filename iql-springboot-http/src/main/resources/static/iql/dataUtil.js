function addParamsToUrl(url, params) {
	return [
		url, 
		$.map(params, function(v, k) { 
			return [k, v].join("=") 
		}).join("&") 
	].join("?")
}
