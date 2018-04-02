/*
*   中介者模式，提供一个中介平台，让所有模块之间的交互都业务都写在这里
*   这样更换子模块的时候，不用修改其他与这个子模块交互的模块
*/
define(['portal'], function (context) {
    var homePage, dataAnalysis, myHomePage, myReview, publishManage, talkMe,
           myTwitter, deal, view, twitterPubSentiment, eventMonitor, accountMonitor, spreadAnalysis,
           departmentManager, examineManager, taskNotify, coordination, twitterPush, alertInfo, alertList, alertService,
           authorityManager, taskManager, userManager, sysManager,
           modulesMapper = {},
           contentDiv;
    
	//私有方法
	function ModulesHide() {
		for (var key in modulesMapper)
			$('#' + key, contentDiv).hide();
	}
           
    return {
        Init: function (_homePage, _dataAnalysis, _myHomePage, _myReview, _publishManage, _talkMe,
         _myTwitter, _deal, _view, _twitterPubSentiment, _eventMonitor, _accountMonitor, _spreadAnalysis,
         _departmentManager, _examineManager, _taskNotify, _coordination, _twitterPush, _alertInfo, _alertList, _alertService,
         _accountBinding, _authorityManager, _taskManager, _userManager, _sysManager) {

			modulesMapper.homePage = homePage = _homePage;
            modulesMapper.dataAnalysis = dataAnalysis = _dataAnalysis;
            modulesMapper.myHomePage = myHomePage = _myHomePage;
            modulesMapper.myReview = myReview = _myReview;
            modulesMapper.publishManage = publishManage = _publishManage;
            modulesMapper.talkMe = talkMe = _talkMe;
            modulesMapper.myTwitter = myTwitter = _myTwitter;
            modulesMapper.pubSentimentDeal =  deal = _deal;
            modulesMapper.pubSentimentView = view = _view;
            modulesMapper.twitterPubSentiment = twitterPubSentiment = _twitterPubSentiment;
            modulesMapper.eventMonitor = eventMonitor = _eventMonitor;
            modulesMapper.accountMonitor = accountMonitor = _accountMonitor;
            modulesMapper.spreadAnalysis = spreadAnalysis = _spreadAnalysis;
            modulesMapper.departmentManager = departmentManager = _departmentManager;
            modulesMapper.examineManager = examineManager = _examineManager;
            modulesMapper.taskNotify = taskNotify = _taskNotify;
            modulesMapper.coordination = coordination = _coordination;
            modulesMapper.twitterPush = twitterPush =  _twitterPush;
            modulesMapper.alertInfo = alertInfo =  _alertInfo;
            modulesMapper.alertList = alertList = _alertList;
            modulesMapper.alertService = alertService =  _alertService;
            modulesMapper.accountBinding = accountBinding = _accountBinding;
            modulesMapper.authorityManager = authorityManager = _authorityManager;
            modulesMapper.taskManager = taskManager = _taskManager;
            modulesMapper.userManager = userManager = _userManager;
            modulesMapper.sysManager = sysManager = _sysManager;
        },
        SetContentDiv: function (div) {
            contentDiv = div;
            return this;
        },
        
        //反射方法(菜单触发点击事件专用)
        MenuReflectionMethod: function (moudleName) {
        	ModulesHide();
        	var moudle = modulesMapper[moudleName];
        	moudle.IsLoad ? moudle.Show() : modulesMapper[moudleName][oL](contentDiv);
        },
        //反射方法(导航条触发点击事件专用)
     	NavReflectionMethod: function (moudleName, methodName) {
     		modulesMapper[moudleName][methodName]();
     	},
     	//从门户进入任务通知模块
     	PortalToTaskNotify: function () {
     		var notify = modulesMapper.taskNotify;
     		ModulesHide();
     		notify.IsLoad ? notify.UnRead():
     			modulesMapper.taskNotify[oL](contentDiv, 0);
     	},
     	//从门户进入预警
     	PortalToAlertInfo: function () {
 			var alertInfo = modulesMapper.alertInfo;
     		ModulesHide();
     		alertInfo.IsLoad ? alertInfo.SmallBell():
     			modulesMapper.alertInfo[oL](contentDiv, 0);
     		// alert("还未开放此模块，当前在中介者方法里");
     	},
     	//递减消息通知数
     	DecreaseMessageCount: function () {
     		context.DecreaseMessageCount();
     	},
     	//加1消息通知数
     	AddMessageCount: function () {
     		context.AddMessageCount();
     	},
     	DecreaseWarningDecreaseCount: function(count) {
     		context.WarningDecreaseCount(count);
     	},
     	PortalToAccountBinding: function (isOpen) {
     		ModulesHide();
 			modulesMapper.accountBinding.OnLoad2(contentDiv, isOpen);
     	}
    };
});