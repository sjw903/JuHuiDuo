var api_host = typeof(juduohui) != "undefined" ? juduohui.getAppIP() : "https://online.juduohui.cn";
var yx_host =  getYXDomain() + "/H5_API/";
var api_list = {
	"getTjgwConfig":"/api/UserHuisign/getTjgwConfig",
	"getSurveyTaskType": "/api/TaskMakeMoneyType/getType", // 获取调查问卷分类开关列表(完成)
	"setType": "/api/TaskMakeMoneyType/setType",
	"getSurveyTask": "/api/TaskMakeMoney/getSurveyTask", // 获取任务
	"getSurveyTaskDetail": "/api/TaskMakeMoney/getSurveyTaskDetail", // 获取调查问卷详情
	"submitSurveyTask": "/api/TaskMakeMoney/completeSurveyTask", // 提交
	"cancelSurveyTask": "/api/TaskMakeMoney/cancelSurveyTask", // 取消
	"informSurveyTask": "/api/TaskMakeMoney/informSurveyTask", // 举报
	"earningQuantity": "/api//TaskMakeMoneyRecord/earningQuantity",
	"getSurveyTaskRecord": "/api/TaskMakeMoneyRecord/surveyTaskRecord",
	"getUploadSign": "/api/UploaderUser/userUploadSign",
	"getArticleMsg": "/api/Article/getArticleMsg",
	"surveyTaskAppeal": "/api/TaskMakeMoneyRecord/surveyTaskAppeal",
	"getAutoReadUrl":"/api/TaskMakeMoney/getAutoReadUrl",
	// 检测用户是否关注公众号
	"getJudge": "/api/OfficialAccount/getJudge",
	// 签到
	"getAllHPoint": "/api/UserHuisign/getAllhpoint",
	"getContinuousDay": "/api/UserHuisign/getContinuousDay",
	"isSignToday": "/api/UserHuisign/isSignToday",
	"signIn": "/api/UserHuisign/singin",
	"getSignRecord": "/api/UserHuisign/getSignRecord",
	"isCanWatch": "/api/UserHuisign/isCanWatch",
	// 广告位
	"getAdbyChannel":"/api/advertisementplace/getAdbyChannel",
	"getAdvPlacelist":"/api/UserHuisign/getAdvertisementList",
	"getAdvertisement": "/api/UserHuisign/getAdvertisement",
	"setAdvertisementOrder":"/api/UserHuisign/setAdvertisementOrder",
	"getHuiPoint": "/api/UserHuisign/getHuiPoint",
	// 文章相关
	"getGoldsDaytime": "/api/UserGold/getDaytime",
	"getReadConfig": "/api/UserGold/getconfig",
	"getXhmediaLibtype": "/api/XhmediaLib/getXhmediaLibtype",
	"getXhmediaLiblist": "/api/XhmediaLib/getXhmediaLiblist",
	"getXhmediaLib": "/api/XhmediaLib/getXhmediaLib",
	"getXhmediaLibComment": "/api/XhmediaLib/getXhmediaLibcomment",
	"goldExchangeToHB": "/api/UserGold/goldtohpoint",
	"addGolds": "/api/UserGold/getnumgold",
	"getAllGolds": "/api/UserGold/getAllgold",
	"getGoldrecord": "/api/UserGold/getGoldrecord",
	// 评论相关
	"XhmediaLibCommentlike": "/api/XhmediaLib/XhmediaLibCommentlike", // 本地评论点赞

	// 提现相关
	"withdrawalSet": "/api/UserDrawApply/withdrawalSet",
	"changeAlipay": "/api/UserDrawApply/changeAlipay",
	"draw": "/api/UserDrawApply/draw",
	"getHpointlist": "/api/UserHuisign/getHpointlist",
	"drawStatistics":"/api/UserBalanceRecord/drawStatistics",
	"withdrawalRecord":"/api/UserDrawApply/withdrawalRecord",
	//工具箱
	"toolBoxGetList": "api/HoldAll/getlist",
	"articleMonitorrList": "/api/ArticleMonitor/articleList",
	"addArticleMonitor": "/api/ArticleMonitor/addArticle",
	// 分站
	"addSubStation": "/api/Branch/addBranch",
	"getSubStationList": "/api/Branch/getBranchList",
	"getAddModuleList": "/api/Branch/addModuleList",
	// 云享
	"addYxTaskRecord":"/api/ButtJoinYun/addTaskRecord",
	"updateYxTaskRecord":"/api/ButtJoinYun/updateTaskRecord",
	//我的
	'getUserMsg':"/api/User/getUserMsg",
	//绑定账号
	'getAccountInfo':"api/UserBind/getAccountInfo",
	"getInfobyAuth":"api/UserBind/getInfobyAuth",
	"setBindbyCode":"api/UserBind/setBindbyCode",
	"returnInvitation":"api/UserBind/returnInvitation",
	"userGetAllhpoint":"api/UserBind/userGetAllhpoint",
	"userGetDraw":"api/UserBind/userGetDraw",
	"qxBindUser":"api/UserBind/qxBindUser",
	"setAgentbyCode":"api/UserBind/setAgentbyCode",
	//领工资
	"getpayAll":"api/PayrollPage/getpayAll",
	"getPaidHPoint":"api/PayrollPage/getPaidHpoint",
	"getPayInfo":"api/PayrollPage/getpayinfo",
};

var yx_api = {
	"getCookie":"Auth.aspx?g="+ getYXAppID() +"&userkey=" + getUid(),
	"cancelTask":"H5_Handler.ashx?M=0.7389357883107354&cmd=cancelmembertask",
	"taskRecord":"H5_Handler.ashx?M="+ Math.random() +"&cmd=getrecords_bytaskids"
}

window.TencentGDT = window.TencentGDT || [];
// 阻塞快速多次点击
var first_click_time = 0;
document.addEventListener("click",function(e){
	if (e.srcElement.className == 'layui-layer-shade'){
		e.stopPropagation();
		return 
	}
	var now_click_date = new Date().getTime();
	if (now_click_date - first_click_time > 500) {
		first_click_time = now_click_date
		return true;
	}
	else{
		e.stopPropagation();
		return false;
	}
	
},true);

function readNewsStorage() {
	this.constructor();
}

// readNewsStorage = new JdhStorage();
readNewsStorage.prototype = {
	key: "jdh_news_readed_lib",
	adv_display_status:true,
	init_struts: {
		"id": 0,
		"last_read_lenght": 0,
		"t": 0
	},
	time_list: [], // 保存所有键值的时间
	lib_list: [],
	constructor: function() {
		if (localStorage.getItem(this.key) !== null) {
			this.lib_list = JSON.parse(localStorage.getItem(this.key))
		}

		if (localStorage.getItem("time_list") !== null) {
			this.time_list = JSON.parse(localStorage.getItem("time_list"))
		}
	},
	/**
	 * 保存key的创建时间和过期时长（秒）
	 * @param {Object} keys
	 * @param {Object} expires
	 */
	saveUpdateTime(keys, expires) {

		var need_update = false;
		var current_time = new Date().getTime()

		this.time_list.filter(function(item, index, obj) {
			if (item.keys === keys) {
				obj[index].times = current_time
				obj[index].expires = expires
				need_update = true
			}
			return false
		});

		if (!need_update) {
			this.time_list.push({
				"keys": keys,
				"times": current_time,
				"expires": expires === undefined ? -1 : expires
			})

		}
		localStorage.setItem("time_list", JSON.stringify(this.time_list))
	},
	loadUpdateTime(keys) {
		var find_item = null
		var need_update = false
		var current_time = new Date().getTime()
		var filter_ed = this.time_list.filter(function(ret, ind, obj) {
			if (keys === ret.keys) find_item = ret
			if (ret.expires === -1) return true
			if (current_time - ret.times > ret.expires * 1000) {
				need_update = true
				find_item = null
				localStorage.removeItem(ret.keys)
				return false
			} else {
				return true
			}
		});

		this.time_list = filter_ed
		if (need_update) localStorage.setItem("time_list", JSON.stringify(this.time_list))
		return find_item
	},

	/**
	 * 长期存储，可设置过期时间
	 * 
	 * @param {Object} items 键
	 * @param {Object} values 值
	 * @param {Object} expires 过期时间，单位：秒
	 */
	save(items, values, expires) {
		localStorage.setItem(items, values)
		if (expires !== undefined) this.saveUpdateTime(items, expires);
	},
	/**
	 * 读取存储值
	 * @param {Object} items
	 */
	load(items) {
		this.loadUpdateTime(items)
		return localStorage.getItem(items)
	},
	/** 
	 * 自动存储JSON数据
	 * @param {Object} items 键
	 * @param {Object} values 值
	 * @param {Object} expires 过期时间，单位：秒,如果不设置则不会过期
	 */
	saveJSON(items, values, expires) {
		localStorage.setItem(items, JSON.stringify(values))
		if (expires !== undefined) this.saveUpdateTime(items, expires);
	},
	/**
	 * 读取存储的JSON数据
	 * @param {Object} items 键
	 * @param {Object} default_return 为空时默认返回的值
	 */
	loadJSON(items, default_return) {
		this.loadUpdateTime(items)
		var tmp = localStorage.getItem(items)
		try {
			tmp = JSON.parse(tmp);
		} catch (e) {
			tmp = null;
		}

		if (tmp === null) {
			tmp = default_return !== undefined ? default_return : null
		}

		return tmp
	},
	del(items) {
		return localStorage.removeItem(items)
	},
	// 取KEY存储的时间
	getSaveTime(items) {
		return localStorage.getItem(items + "_t");
	},
	// 临时存，关闭窗口即消失
	tempSave(items, values) {
		return sessionStorage.setItem(items, values)
	},
	// 临时取，关闭窗口即消失
	tempLoad(items) {
		return sessionStorage.getItem(items)
	},
	// 临时删除，关闭窗口即消失
	tempDel(items) {
		return sessionStorage.removeItem(items)
	},
	// 复制信息到剪切板
	copyToClip(msg) {
		$("body").append(
			"<div id='copyTempNodeDiv' style='width:1px;height:1px;overflow:hidden;'><input type='text' id='copyTempNode' value='" +
			msg + "'></div>");
		$("#copyTempNode").select();
		document.execCommand("copy");
		$("#copyTempNodeDiv").detach();
	},
	setCircleTimer(obj) {
		try {
			juduohui.setCircleTimer(JSON.stringify(obj))
		} catch (err) {
			// console.log(err);
		}
	},
	/**
	 * 返回 start和end 之间的整数
	 * @param {Object} start_number
	 * @param {Object} end_number
	 */
	rndInt(start_number, end_number) {
		return Math.floor(Math.random() * (end_number - start_number) + start_number)
	},
	// 32位随机字符串
	rndString() {

		var e = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (var o = "", n = 32; n > 0; --n) {
			o += e[Math.floor(Math.random() * e.length)]
		}
		return o;
	},
	// 获取签名版本
	getVersion() {
		return "V1"
	},
	// 获取渠道码
	getSourceCode(){
		
	},
	encrypt(message){
			return juduohui.e(message);
	},
	decrypt(message){
			return juduohui.e(message);
	},
	SignData(json_data) {

		console.log(json_data)
		let timestamp = new Date().getTime()
		let version = this.getVersion()
		let token = getToken()
		let dataType = "JSON"
		let jdh_secret = this.getJDHSecret()

		// 填充公共请求参数
		json_data.token = token
		json_data.timestamp = timestamp
		json_data.version = version
		json_data.dataType = "JSON"

		// 请求参数自然排序
		let tmp_array = [];
		for (var it in json_data) {
			tmp_array.push(it)
		}

		tmp_array.sort()

		var new_data = {};

		for (var it in tmp_array) {
			for (var itm in json_data) {
				if (itm === tmp_array[it]) {

					new_data[itm] = json_data[itm]
				}
			}
		}

		// 使用SECRET+[KEY+VALUE]+SECRET组成签名串
		let str = jdh_secret

		for (var item in new_data) {
			if (new_data[item] === "") continue
			str += item + new_data[item]
		}

		str += jdh_secret
		// MD5().toUpperCase()
		str = this.md5(str).toUpperCase()
		new_data.timestamp = timestamp
		new_data.version = version
		new_data.dataType = "JSON"
		new_data.sign = str
		return new_data
	},
	md5(r) {
		return juduohui.md532(r);
	},
	getJDHSecret() {
		//return juduohui.getJDHSecret()
		return 'juDuoHuiVeryBangBang6'
	},
	formatDate(date, fmt) {
	  var currentDate = new Date(date);
	  var o = {
	    "M+": currentDate.getMonth() + 1, //月份
	    "d+": currentDate.getDate(), //日
	    "h+": currentDate.getHours(), //小时
	    "m+": currentDate.getMinutes(), //分
	    "s+": currentDate.getSeconds(), //秒
	    "q+": Math.floor((currentDate.getMonth() + 3) / 3), //季度
	    "S": currentDate.getMilliseconds() //毫秒
	  };
	  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (currentDate.getFullYear() + "").substr(4 - RegExp.$1.length));
	  for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	  return fmt;
	},
	unWaterHost() {
		return "https://v.xie2.net/"
	},
	unWaterPostUrl() {
		console.log("这里")
		console.log(this.unWaterHost());
		return this.unWaterHost() + "v1/parse"
	},
	getVideoUrlPostParameter(video_share_url) {
		var rnd = this.rndString()
		var m = this.md5(this.unWaterPostUrl + "&^@&^" + rnd)

		var return_obj = {
			sourceURL: video_share_url,
			sPath: this.unWaterPostUrl(),
			r: rnd,
			e: m
		}
		return return_obj
	},
	setAdvStation(adv_space_clazz,display_status){
		// console.log("setAdvStation(" + adv_space_clazz + "," +display_status +")")
		// console.log("setAdvStation(" + typeof(adv_space_clazz) + "," +typeof(display_status) +")")
		try{
			if (typeof(adv_space_clazz) == "boolean"){
				 console.log("设置广告展现状态 - 1" + "---" + adv_space_clazz)
				this.adv_display_status = adv_space_clazz
				juduohui.setAdvStation(adv_space_clazz);
			}
			else{
				 console.log("设置广告展现状态 - 2")
				this.adv_display_status = display_status
				juduohui.setAdvStation(adv_space_clazz,display_status);
			}
		}catch(e){
			 console.log("设置广告展现状态 - 错误分支")
			 console.log(JSON.stringify(e))
		}
	},
	async getAdvCfg(identifys){
		var adv_cfg = {};
		let imei = typeof(juduohui) != "undefined" ? juduohui.getIMEI() : "";
		await axios.post(api_list.getAdvPlacelist,{"token":getToken(),"identifys":identifys,"item":imei}).then(res=>{
			adv_cfg = res.data.data
		}).catch(err=>{
			console.log(JSON.stringify(err));
		})
		return adv_cfg;
	},
	// 控制广告展现状态
	getAdvStation(){
		return this.adv_display_status
	},
	/**
	 * 渲染广告
	 * @param {Object} json_config 
		{
			type:0, // 0:信息流,1:激励视频,2:banner,3:视频
			channel:"",
			position_id:"0",
			ads_class:"",
			order_id:"",
			ondisplay:function(), // 显示时调用
			onerror:function(), // 广告位出错时调用
			onclick:function(),  // 点击时调用
			ondownload:function(), // 下载时调用【暂时无用】
			onclose:function(), // 广告关闭时调用
		}
	 */
	renderAdvert(json_config){

		if (localReadLib.load("is_ad_open") == "false"){
			return;
		}
		
		var s = JSON.stringify(json_config, function(key, val) {
			if (typeof val === 'function') {
				return val + '';
			}
			return val;
		});
		
		// console.log(s)
		
		try{
			if (this.getAdvStation()){
				juduohui.renderAdvert(s);
			}
			else{
				setTimeout(()=>{
					this.renderAdvert(json_config);
				},200);
			}
		}
		catch(e){
			// console.log(e)
		}
		
	},
	scanQrCode(json_config){		
		var s = JSON.stringify(json_config, function(key, val) {
			if (typeof val === 'function') {
				return val + '';
			}
			return val;
		});
		
		// console.log(s)
		
		try{
			juduohui.scanQrCode(s);
		}
		catch(e){
			// console.log(e)
		}
		
	},
	/**
	 * 调用安卓原生的alert方法
	 * ---------------------------
	 * json_config结构第一种
	 * {
		 "title":"标题",
		 "message":"文本显示的内容",
		 "submit":{"text":"按钮内容","active":"是否反色显示","click":function(){ console.log('点击提交按钮的回调') }} //提交按钮配置
		 "cancel":{}, //取消按钮配置
		 "cancel_outside":false, // 外部点击是否允许关闭 false不允许
		 "close":function(){}//关闭后的回调
	 * }
	 * json_config结构第二种
	 * {
		 "url":"./alert_messag.html", // 中间以网页形式展示，url与上一种互斥，url优先
		 "cancel_outside":false // 外部点击是否允许关闭 false不允许
		 "close":function(){}//关闭后的回调
	 * }
	 * @param {Object} json_config
	 */
	alert(json_config){
		var s = JSON.stringify(json_config, function(key, val) {
			if (typeof val === 'function') {
				return val + '';
			}
			return val;
		});
		
		// console.log(s)
		
		try{
			juduohui.alert(s);
		}
		catch(e){
			// console.log(e)
		}
		
	},
	// 已弃用
	getAdv(channel,position_id,clazz,order_id,display_function,click_function,download_function,error_function){
		try{
			if (this.getAdvStation()){
				juduohui.getAdv(channel+"",position_id+"",clazz+"",order_id,display_function.toString(),click_function.toString(),download_function.toString(),error_function.toString());
			}
			else{
				setTimeout(()=>{
					this.getAdv(channel,position_id,clazz,order_id,display_function,click_function,download_function,error_function)
				},200);
			}
		}
		catch(e){
			// console.log(e)
		}
	},
	// 移除当前页面所有广告位
	removeAllAdv(){
		try{
			juduohui.removeAllAdv();
		}catch(e){
			
		}
	},
	require(module_name){
		var CryptoElm = document.createElement("script");
		CryptoElm.src = "model/"+module_name
		CryptoElm.defer = "defer"
		CryptoElm.async = "async"
		document.head.appendChild(CryptoElm);
	},
	/*
	下边方法的示例
	localReadLib.ajax({
		url: api_host + api_list.getAllHPoint,
		type:"post",
		data:{token:getToken()},
		success:function(res,pageThis){
			console.log("这里！")
			console.log(JSON.stringify(pageThis))
			console.log("这里结束！")
			this.vue.allHPoint = res.balance_hui_b
			pageThis.allHPoint = res.data.balance_hui_b
		},
		error:function(err,pageThis){}, 出错的时候调用
		complete:function(){} 请求完毕的时候调用，不管正确还是错误 
	});
	*/
	ajax(json_config) {
		var s = JSON.stringify(json_config, function(key, val) {
			if (typeof val === 'function') {
				return val + '';
			}
			return val;
		});
		juduohui.zjax(s);
		// try{
		// 	console.log("try")
		// 	console.log(JSON.stringify(json_config))
		// 	juduohui.zjax(JSON.stringify(json_config))
		// }
		// catch(er){
		// 	console.log("catch")
		// 	console.log(JSON.stringify(json_config))
		// 	juduohui.zjax(JSON.stringify(json_config))
		// }
	},
}

/**
 * 获取去水印的使用用例,ajax需要使用安卓去获取
var video_share_url = "https://www.toutiao.com/a6989854339212771879/?app=news_article_lite&amp;is_hit_share_recommend=0";

axios.post(localReadLib.unWaterPostUrl(),localReadLib.getVideoUrlPostParameter(video_share_url),{
	headers:{
		"App": "M_DingDing",
		"Auth-Token": "wxm_d8cfb83400633440a442181aabd08d0b",
		"User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat",
		"content-type": "application/json",
		// "Referer": "https://servicewechat.com/wx0bfd6b96e32eea31/12/page-frame.html"
	}
}).then(res=>{
	console.log(res)
}).catch(err=>{
	console.log(err)
})
 */

var localReadLib = new readNewsStorage();

function commonAppSetting() {
	axios.defaults.withCredentials = true; //开启后服务器才能拿到cookie
	axios.defaults.crossDomain = true; //配置axios跨域
	axios.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8' //配置默认请求头
	axios.defaults.baseURL = api_host // 基础网址
	axios.defaults.responseType = 'json'
	// axios.defaults.httpsAgent = new https.Agent({ keepAlive: true,rejectUnauthorized:false })
}

function getToken() {
	if (typeof(juduohui) != "undefined") {
		return juduohui.getToken()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return "f90d863687d07598e5a51f2fd54390d3";
	}
}

function getResource(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getResource()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return "yyb";
	}
}

function getUid(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getUid()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return "89064";
	}
}

function getAdIsOpen(){
	if (typeof(juduohui) != "undefined") {
		// console.log("juduohui.getAdIsOpen() = "+juduohui.getAdIsOpen())
		return juduohui.getAdIsOpen()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return true;
	}
}

function getKefuUrl(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getKefuUrl()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return "https://tb.53kf.com/code/app/76e0b6a7e99d942080ad154280b9d6679/1";
	}
}

function getAppCode(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getAppCode()
	} else {
		// alertMessage("请从聚多汇APP打开")
		return "1071";
	}
}
	
function getMYXQAppID() {
	// return "6fab3143f92a739d"
	if (typeof(juduohui) != "undefined") {
		return juduohui.getMYXQAppID();
	} else {
		alertMessage("请从聚多惠APP打开")
		return ""
	}
}

function getYXDomain(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getYXDomain();
	} else {
		return api_host;
	}
}
function getYXAppID(){
	if (typeof(juduohui) != "undefined") {
		return juduohui.getYXAppID();
	} else {
		return "2c7d89c8-6461-4e87-bddb-6580025293b9"
	}
}

function checkPermission() {
	juduohui.checkPermission();
}

/**
 * 获取URL传参
 * @param {Object} variable 参数KEY
 */
function getQueryVariable(variable, search_url) {
	var query = search_url === undefined ? window.location.search.substring(1) : search_url;
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
	return (false);
}
/**
 * 在新窗口中打开urls
 * @param {Object} urls
 */
function openNewActivite(urls, config_json) {
	console.log(config_json);
	if (window.juduohui != undefined) {

		if (config_json !== undefined) {
			juduohui.openUrl(urls, JSON.stringify(config_json))
		} else {
			juduohui.openUrl(urls)
		}
	} else {
		location.href = urls
	}
}

function reloadWindow() {
	if (window.juduohui != undefined) {
		juduohui.reloadWindow();
	} else {
		window.location.reload(true);
	}
}

function closeWindow() {
	if (window.juduohui != undefined) {
		juduohui.closeWindow();
	} else {
		window.close();
	}
}

/**
 * 信息显示
 * @param {Object} message 需要显示的信息
 * @param {Object} callback 点击确认后的回调
 * @p
 */

/**
 * @param {Object} message 需要显示的信息
 * @param {Object} callback 点击确认后的回调
 * @param {Object} icon_type 图标类型 0惊叹号,1,成功，2失败，3问号，4锁头，5哭脸，6 绿笑脸信息
 */

function alertMessage(message, callback, icon_type) {

	/**
	 * 对于只传2个参数的兼容
	 */
	let function_callback = null;
	if (typeof(callback) == "number") {
		icon_type = callback
	} else if (typeof(callback) == "function") {
		function_callback = callback
	}

	icon_type = undefined === icon_type ? 0 : icon_type
	layer.closeAll();

	if (typeof(juduohui) === "undefined") {
        layer.msg(message, {
            time: 1500
        }, function_callback);
	}
	else{
	    showToast(message);
	    setTimeout(function(){
			console.log("进超时执行")
			if (function_callback!=null ) function_callback();
	    },1500)
	}

	// layer.alert(message, {
	// 	title: false,
	// 	icon: icon_type,
	// 	shade: [0.7, "#000000"],
	// 	skin: 'layer-ext-demo' //见：扩展说明
	// }, function_callback)
}

// function log(message) {
// 	console.log(message)
// }

function checkImageExt(img_url) {

	var ext = [".jpg", ".bmp", ".png", ".jpeg", ".webp"];

	var is_img = false;
	for (var i = 0; i < ext.length; i++) {
		if (img_url.toLowerCase().indexOf(ext[i]) != -1) {
			is_img = true;
			break;
		}
	}
	if (!is_img) {
		$("body").append("<div id='tmp_qrcode_div'></div>")
		$("#tmp_qrcode_div").qrcode({
			width: 400,
			height: 400,
			text: img_url
		});
		var canvas = $("#tmp_qrcode_div").find("canvas")[0];
		var tmp_base64 = canvas.toDataURL('image/jpg');
		$("#tmp_qrcode_div").remove();
		return tmp_base64;

	} else {
		return img_url;
	}
}

function showLoading(msg) {
	var contents = typeof(msg) != undefined ? msg : "";
	
	if (typeof(juduohui) != "undefined"){
		juduohui.androidShowLoading(msg);
	}
	else{
		layer.load(1, {
			shade: [0.8, '#000'], //0.1透明度的白色背景,
			content: contents,
			skin: 'jdh_loading',
		});
	}
	
	// try{
	// 	juduohui.setAdvStation(false)
	// }catch(e){
	// 	console.log(JSON.stringify(e))
	// }
	
	
}

function closeLoading(){
	
	if (typeof(juduohui) != "undefined"){
		// setTimeout(function () {
			juduohui.androidCloseLoading();
		// },1000)
	}
	else{
		setTimeout(function () {
			layer.closeAll();
		},1000)
	}
	
	
	// layer.closeAll();
	// try{
	// 	juduohui.setAdvStation(true)
	// }catch(e){
	// 	console.log(JSON.stringify(e))
	// }
	
}

function backPress() {
	console.log("公用方法中点击了这里")
	if (typeof(juduohui) === "undefined") {
		history.back();
	} else {
		juduohui.backPress();
	}
}

function showToast(msg) {
	console.log()
	if (typeof(juduohui) === "undefined") {
		layer.msg(msg);
	} else {
		juduohui.showToast(msg);
	}
}

// (function () {
//     var doc = document,
//         h = doc.getElementsByTagName('head')[0],
//         s = doc.createElement('script');
//     s.async = true;
//     s.src = 'https://qzs.qq.com/qzone/biz/res/i.js';
//     h && h.insertBefore(s, h.firstChild)
// })();



//常用的对象对比
this.isEqual = function() {
	var arg = getArg(arguments);
	var o1 = arg["object"][0] || [];
	var o2 = arg["object"][1] || [];
	if (!arg["object"].length) {
		return (o1 == o2);
	}
	var isEqualForInner = function(obj1, obj2) {
		var o1 = obj1 instanceof Object;
		var o2 = obj2 instanceof Object;
		if (!o1 || !o2) {
			return obj1 === obj2;
		}
		if (Object.keys(obj1).length !== Object.keys(obj2).length) {
			return false;
		}
		for (var attr in obj1) {
			var t1 = obj1[attr] instanceof Object;
			var t2 = obj2[attr] instanceof Object;
			if (t1 && t2) {
				return isEqualForInner(obj1[attr], obj2[attr]);
			} else if (obj1[attr] !== obj2[attr]) {
				return false;
			}
		}
		return true;
	}
	return isEqualForInner(o1, o2);
}

var getArg = function(arg) {
	var ret = {
		'number': [],
		'string': [],
		'boolean': [],
		'object': [],
		'Object': [],
		'Array': [],
		'function': [],
		'Date': [],
		'null': []
	}
	for (var i = 0, len = arg.length; i < len; i++) {
		var ar = arg[i];
		if (typeof(ar) == 'number') {
			ret['number'].push(ar);
		} else if (typeof(ar) == 'string') {
			ret['string'].push(ar);
		} else if (typeof(ar) == 'boolean') {
			ret['boolean'].push(ar);
		} else if (typeof(ar) == 'function') {
			ret['function'].push(ar);
		} else if (typeof(ar) == 'object') {
			if (ar) {
				if (ar.constructor == Object) {
					ret['object'].push(ar);
					ret['Object'].push(ar);
				} else if (ar.constructor == Array) {
					ret['object'].push(ar);
					ret['Array'].push(ar);
				} else if (ar.constructor == Date) {
					ret['Date'].push(ar);
				}
			} else {
				ret['null'].push(ar);
			}
		}
	}
	return ret;
}
// window.TencentGDT = window.TencentGDT || [];

// (function () {
// 	var doc = document,
// 		h = doc.getElementsByTagName('head')[0],
// 		s = doc.createElement('script');
// 	s.async = true;
// 	s.src = 'http://qzs.qq.com/qzone/biz/res/i.js';
// 	h && h.insertBefore(s, h.firstChild)
// })();

/**
 * 加货币弹窗
 * @param {string} symbol 符号加减
 * @param {string} add_num 货币数量
 * @param {string} unit 货币单位
 * @param {string} center_text 中间文案
 * @param {string} button_text 按钮文案
 * @param {Object} close_fun 按钮回调 function(result){} // 方法内的 this对象为当前页面的vue对象,result为本次观看视频的结果，成功true,失败false
 */
function alertAddGolds(symbol,add_num,unit,center_text,button_text,close_fun){
	if (typeof(juduohui)!="undefined"){
		juduohui.alertAddGolds(symbol,add_num.toString(),unit,center_text,button_text,close_fun.toString());
	}
}
/**
 * 获取激励视频广告
 * @param {Object} adv_config 广告配置JSONObject
 * @param {Object} close_fun 关闭回调 function(result){} // 方法内的 this对象为当前页面的vue对象,result为本次观看视频的结果，成功true,失败false
 */
function getRewardVideoAd(adv_config,close_fun) {
	if (localReadLib.load("is_ad_open") == "false"){
		return;
	}

	var s = JSON.stringify(adv_config, function(key, val) {
		if (typeof val === 'function') {
			return val + '';
		}
		return val;
	});
	// var f = JSON.stringify(close_fun, function(key, val) {
	// 	if (typeof val === 'function') {
	// 		return val + '';
	// 	}
	// 	return val;
	// });

	if (typeof(juduohui)!="undefined"){
		juduohui.getRewardVideoAd(s,close_fun.toString());
		}
	// if (channel === "2"){

	// }
	// else
	// {

	// 	TencentGDT.push({
	// 	    placement_id: ad_position_id, // String，广告位id
	// 	    app_id: '1111812401', // String，appid
	// 	    type: 'rewardVideo', // String，广告类型，请根据广告类型填写对应的type值
	// 	    // display_type: 'banner',
	// 	    onComplete: function(res) { // Function，广告位初始化回调方法，已激励视频接入为例
	// 	        if(res.code == 0) {
	// 	            // 激励视频实例化，注意：插屏广告、模板广告方位为：renderAd
	// 	            var video = new TencentGDT.NATIVE.rewardVideoAd( function(res) {
	// 	                // 激励视频回调参数
	// 	                console.log(res);
	// 	            });
	// 	            // 激励视频加载方法
	// 	            video.loadAd();
	// 	            // 激励视频播放方法
	// 	            video.showAd();
	// 	        }
	// 	    }
	// 	});
	// }
}

/**
 * 设置原生标题栏
 * @param {Object} jsonSetting { display:boolean,title:string,back_function:javascript function }
 */
function setNativeTitle(jsonSetting) {
	juduohui.setNativeTitle(JSON.stringify(jsonSetting))
}

function showBigImage(obj) {
	//juduohui.showBigImage(obj.href);
	localStorage.setItem("show_img_url", obj)
	openNewActivite('./show_big_image.html', {
		"setSupportZoom": true,
		"setBuiltInZoomControls": true,
	});
	return false;
	// 
}

function reloadAdv(adv_cls){
	var adv_index = localReadLib.load("tmp_" + advclss.replace(".") + "_index") // 广告最后的索引
	var adv_cfg = localReadLib.load("tmp_" + advclss.replace(".") + "_setting") // 广告配置
	if (adv_index+1 >= adv_cfg.length){
		adv_index = 0;
	}
	else{
		adv_index = adv_index +1;
	}
	var ad_channel_cfg = adv_cfg.list[adv_index];
	
	if ($(adv_cls).length <= 0) return;
	
	localReadLib.renderAdvert({
		"type": ad_type,
		"channel":ad_channel_cfg['channel'],
		"position_id":ad_channel_cfg['advertisement_id'],
		"ad_type": ad_channel_cfg['ad_type'],
		"ads_class": adv_cls,
		"order_id":"#order_id",
		"ondisplay":function(){},
		"onerror":function(advclss){
				console.log("重新加载广告位")
				// var type_id = localReadLib.load("news_read_type_id") // 对应9
				// var ad_index = localReadLib.load("tmp_" + advclss.replace(".") + "_index")
				// this.$refs['jdh_load_'+  type_id][0].$refs.container.__vue__.$refs['jdh_adv_tt'+ad_index][0].loadAdvs()
				reloadAdv(advclss);
				console.log("重新加载广告位")
		},
		"onclick":function(){},
		"ondownload":function(){},
		"onclose":function(){
			console.log(this.adv_clss)
			console.log(JSON.stringify(this.$refs))
			this.$refs.jdh_adv.style.display = "none"
		},
	});
	
}
