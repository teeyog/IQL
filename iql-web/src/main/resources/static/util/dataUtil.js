jQuery.fn.setMultiselect = function(nonSelectedText, url) {
	$(this).multiselect({
		nonSelectedText: nonSelectedText,
		selectAllText: '全部',
		nSelectedText: '已选择',
		allSelectedText: '全部选择',
		enableClickableOptGroups: true,
        enableCollapsibleOptGroups: true,
        includeSelectAllOption : true,
        disableIfEmpty: true,
        maxHeight: 320
	});
	var $this = $(this)
	$.getJSON(url, function(response) {
		if(response.options==undefined) 
			$this.multiselect('dataprovider', response);
		else
			$this.multiselect('dataprovider', response.options);
	})
}

jQuery.fn.getMultiselect = function(type) {
	var data = $.map($(this).find("option:selected"), function(row) {
		return type=="value"? row.value:row.label;
	})
	if(data.length > 0)
		return data.join(",")
	else
		return undefined
}

function addParamsToUrl(url, params) {
	return [
		url, 
		$.map(params, function(v, k) { 
			return [k, v].join("=") 
		}).join("&") 
	].join("?")
}
