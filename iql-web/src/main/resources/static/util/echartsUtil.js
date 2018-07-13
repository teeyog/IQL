initLineChart = function(t, data, showLabel) {
	if(showLabel) {
		var label = {
			normal: {
				show: true,
				position: 'top',
				formatter: '{c}'
			}
		};
		$.each(data.series, function(i, v) {
			v.label = label
		})
	}
	var option = {
		title : {
	    	show: true,
	        text: data.title,
	        textStyle: {
	        	color: '#337ab7',
	        	fontSize: 16
	        }
	    },
	    tooltip: {
	        trigger: "axis"
	    },
	    legend: {
	        data: data.legend
	    },
	    calculable: !0,
	    toolbox: {
	        feature: {
	            saveAsImage: {},
	            dataView: {readOnly: false}
	        },
	        right: 20
	    },
	    xAxis: [{
	        type: "category",
	        "axisLabel": {  
	            interval: 0,
	            rotate: 30
	         },
	         data: data.xdata
	    }],
	    yAxis: [{
	        type: "value",
	        scale: 1
	    }],
	    series: data.series
	}
	t.setOption(option, {notMerge: true});
	$(window).resize(t.resize);
}

function initPieChart(t, data) {
	var option = {
		tooltip : {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
		legend: {
			data: data.legend,
			bottom: 0,
			//orient: 'vertical',
			formatter: function (name) {
		        return echarts.format.truncateText(name, 100, '14px Microsoft Yahei', '…');
		    },
		    tooltip: {
		        show: true
		    }
		},
		series : [
			{
		    	name: data.name,
		        type: 'pie',
		        radius : '60%',
		        data: data.series,
		        itemStyle: {
		            emphasis: {
		               shadowBlur: 10,
		               shadowOffsetX: 0,
		               shadowColor: 'rgba(0, 0, 0, 0.5)'
		            }
		        },
		        label: {
		        	normal: {
		        		formatter: '{b} {d}%'
		        	}
		        }
		   }
		]
	};
    t.setOption(option);
    $(window).resize(t.resize);
}

function initBarAndPieChart(t, data) {
	var option = {
		tooltip: {
			trigger: 'item',
			formatter: "{b} <br/>{a} : {c}"
		},
		toolbox: {
            feature: {
                saveAsImage: {},
                dataView: {readOnly: false}
            },
            right: 20
        },
		grid: [
			{x: '7%', y: '7%', width: '70%', containLabel: true},
	        {x2: '7%', y: '7%', width: '30%'}
		],
		xAxis : [
		    {
		        type : 'category',
		        name: data.xname,
		        data : data.bar.xdata,
		        axisTick: {
		            alignWithLabel: true
		        },
		        axisLabel: {  
	                interval: 0,
	                rotate: 30
	            }
		    }
		],
		yAxis : [
			{
		        type : 'value',
		        name: data.yname
		    }
		],
		series : [
			{
		    	name: data.title,
		        type: 'bar',
		        barWidth: '60%',
		        barMaxWidth: 60,
		        data: data.bar.series,
		        label: {
		        	normal: {
		        		show: true,
		        		position: 'top'
		        	}
		        },
		        itemStyle: {
	                normal: {
	                	color: ['#3398DB']
	                }
	            }
		    },
		    {
	            name: data.title,
	            type: 'pie',
	            radius : '60%',
	            center: ['90%', '50%'],
	            data: data.pie.series,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            },
	            tooltip: {
	            	formatter: "{b} <br/>{a} : {c} ({d}%)"
	            },
	            label: {
	            	normal: {
	            		show: true,
		            	position: 'outside',
		            	formatter: '{b} {d}%'
	            	}
	            }
		    }
		]
	};
    t.setOption(option);
    $(window).resize(t.resize);
}

function initPieAndBarChart(t, data) {
	var idx = $('input:radio[name="operatorType"]:checked').val()
	$.each(data.bar.xdata, function(i, v) {
		if(operators[idx][v.toLowerCase()])
			data.bar.xdata[i] = operators[idx][v.toLowerCase()]
		else
			data.bar.xdata[i] = "未知"
	})
	$.each(data.pie.series, function(i, v) {
		if(operators[idx][v.name.toLowerCase()])
			v.name = operators[idx][v.name.toLowerCase()]
		else
			v.name = "未知"
	})
	var option = {
		tooltip: {
			trigger: 'item',
			formatter: "{b} <br/>{a} : {c}"
		},
		toolbox: {
            feature: {
                saveAsImage: {},
                dataView: {readOnly: false}
            },
            right: 20
        },
		grid: [
			{x: '7%', y: '7%', width: '70%', containLabel: true},
	        {x2: '7%', y: '7%', width: '30%'}
		],
		xAxis : [
		    {
		    	type : 'value'
		    }
		],
		yAxis : [
			{
				type : 'category',
		        data : data.bar.xdata
		    }
		],
		series : [
			{
		    	name: data.title,
		        type: 'bar',
		        barWidth: '50%',
		        data: data.bar.series,
		        label: {
		        	normal: {
		        		show: true,
		        		position: 'right'
		        	}
		        },
		        itemStyle: {
	                normal: {
	                	color: ['#3398DB']
	                }
	            }
		    },
		    {
	            name: data.title,
	            type: 'pie',
	            radius : '60%',
	            center: ['90%', '50%'],
	            data: data.pie.series,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            },
	            tooltip: {
	            	formatter: "{b} <br/>{a} : {c} ({d}%)"
	            },
	            label: {
	            	normal: {
	            		show: true,
		            	position: 'outside',
		            	formatter: '{b} {d}%'
	            	}
	            }
		    }
		]
	};
    t.setOption(option);
    $(window).resize(t.resize);
}

function initBarChart(t, data) {
	var option = {
		title: {
			text: data.title,
			left: 'center'
		},
		color: ['#3398DB'],
		tooltip : {
		    trigger: 'axis',
		    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		    	type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		    }
		},
		toolbox: {
            feature: {
                saveAsImage: {},
                dataView: {readOnly: false}
            },
            right: 20
        },
		grid: {
			/* left: '3%',
		    right: '4%',
		    bottom: '3%', */
		    containLabel: true
		},
		xAxis : [
		    {
		        type : 'category',
		        data : data.xdata,
		        axisTick: {
		            alignWithLabel: true
		        }
		    }
		],
		yAxis : [
			{
		        type : 'value'
		    }
		],
		series : [
			{
		    	name: data.title,
		        type: 'bar',
		        barWidth: '60%',
		        barMaxWidth: '60',
		        data: data.series,
		        label: {
		        	normal: {
		        		show: true,
		        		position: 'top'
		        	}
		        }
		    }
		]
	};
    t.setOption(option);
    $(window).resize(t.resize);
}
