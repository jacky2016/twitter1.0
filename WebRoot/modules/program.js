//常量
var _p1 = '../plugin/jquery.',
	   _p2 = '../../modules/portal/portal',
	   _p3 = '../../modules/myTwitter/myTwitter',
	   _p4 = '../../modules/coordination/coordination',
	   _p5 = '../../modules/',
	   _p6 = '../../modules/sysManager/sysManager',
	   _p7 = '../../modules/alertService/alertService';

require.config({
    //定义根路径
    baseUrl: 'scripts/lib',
    paths: {
        //加载框架
        jquery: 'jquery-1.9.1.min',
        highcharts: 'highcharts-3.0.1.min',
        //加载插件
        bgIframe: _p1 + 'bgiframe',
        corner: _p1 + 'corner',
        loading: _p1 + 'loading',
        tool: _p1 + 'tool',
        dateTool: _p1 +  'tool.date',
        pngFix: _p1 + 'pngFix',
        pager: _p1 + 'pager.simple',
        regExp: _p1 + 'regExp',
        keyupTo: _p1 + 'keyupTo',
        patch: _p1 + 'patch',
        datePicker: _p1 + 'datePicker',
        timePicker: _p1 + 'timePicker',
        taskPicker: _p1 + 'taskPicker',
        expressionFormat: _p1 + 'expression.format',
        expressionExtend: _p1 + 'expression.extend',
        expression: _p1 + 'expression',
        list: _p1 + 'load.list',
        table: _p1 + 'load.table',
        windows: _p1 + 'windows',
        uploadify:  _p1 + 'uploadify',
        messageBox: _p1 + 'windows.messageBox',
        easing: _p1 + 'easing',
        circulate: _p1 + 'circulate',
        cloud: _p1 + 'cloud',
        returnTop: _p1 + 'returnTop',
        input: _p1 + 'input',
        quake: _p1 + 'quake',
        cookie: _p1 + 'cookie',
        
        //加载项目主模块(入口模块)
        navigationBars: _p2 + '.navigationBars',
        base: _p2 + '.base',
        currentUser: _p2 + '.currentUser',
        twitterEnum: _p2 + '.enum',
        mediator: _p2 + '.mediator',
        repost: _p2 + '.view.repost',
        comment: _p2 + '.view.comment',
        collect: _p2 + '.function.collect',
        portal: _p2,
        //加载子模块
        //首页
        homePage: _p5 + 'homePage/homePage',
        //我的微博
        dataAnalysis: _p3 + '.dataAnalysis',
        myHomePage: _p3 + '.myHomePage',
        myReview: _p3 + '.myReview',
        publishManage: _p3 + '.publishManage',
        talkMe: _p3 + '.talkMe',
        myTwitter: _p3,
        //微博舆情
        deal: _p5 +'twitterPubSentiment/twitterPubSentiment.deal',
        view: _p5 +'twitterPubSentiment/twitterPubSentiment.view',
        twitterPubSentiment: _p5 + 'twitterPubSentiment/twitterPubSentiment',
        //事件监控
        eventMonitor: _p5 +'eventMonitor/eventMonitor',
        //账号监测
        accountMonitor: _p5 +'accountMonitor/accountMonitor',
        //传播分析
        spreadAnalysis: _p5 +'spreadAnalysis/spreadAnalysis',
        //协同办公
        departmentManager: _p4 + '.departmentManager',
        examineManager: _p4 + '.examineManager',
        taskNotify: _p4 + '.taskNotify',
        coordination: _p4,
        //预警服务 
        twitterPush: _p7 +'.twitterPush',
        alertInfo: _p7 +'.alertInfo',
        alertList: _p7 + '.alertList',
        alertService: _p7,
        
        //系统管理
        accountBinding: _p6 + '.accountBinding',
        authorityManager: _p6 + '.authorityManager',
        taskManager: _p6 + '.taskManager',
        userManager: _p6 + '.userManager',
        sysManager: _p6
    },

    //非AMD标准依赖关系
    shim: {  
    	jquery: {
    		exports: 'jQuery'
    	},
        highcharts: {  
            deps: ['jquery']
        },
        uploadify: {
        	deps: ['highcharts']
        },
        tool: {
        	deps: ['jquery']
        },
        dateTool: {
        	deps: ['tool']
        },
        bgIframe: {
        	deps: ['tool']
        },
        corner: {
        	deps: ['tool']
        },
        loading: {
        	deps: ['tool']
        },
        
        pngFix: {
        	deps: ['tool']
        },
        pager: {
        	deps: ['tool']
        },
        regExp: {
        	deps: ['tool']
        },
        keyupTo: {
        	deps: ['tool']
        },
        patch: {
        	deps: ['tool']
        },
        datePicker: {
        	deps: ['tool']
        },
        timePicker: {
        	deps: ['tool']
        },
        taskPicker: {
        	deps: ['tool']
        },
        expressionFormat: {
        	deps: ['tool']
        },
        expressionExtend: {
        	deps: ['tool', 'expression']
        },
        expression: {
        	deps: ['tool']
        },
        list: {
        	deps: ['tool']
        },
        table: {
        	deps: ['tool']
        },
        windows: {
        	deps: ['tool']
        },
        messageBox: {
        	deps: ['tool']
        },
        easing: {
        	deps: ['tool']
        },
        circulate: {
        	deps: ['tool']
        },
        cloud: {
        	deps: ['tool']
        },
        returnTop: {
        	deps: ['tool']
        },
        input: {
        	deps: ['tool']
        },
        quake: {
        	deps: ['tool']
        },
        cookie: {
        	deps: ['tool']
        },
        portal: {
        	 deps: ['bgIframe', 'corner', 'loading', 'tool', 'dateTool', 'pngFix', 
        	 			 'pager', 'regExp', 'keyupTo', 'patch', 'datePicker', 'timePicker', 
        	 			 'taskPicker', 'expressionFormat', 'expressionExtend', 'expression', 
        	 			 'list', 'table', 'windows', 'uploadify', 'messageBox', 'easing', 'circulate', 'cloud', 'returnTop', 'input', 'quake', 'cookie']
        }
    }  

});

//require(['jquery'], function($) {
	//加载jquery公用库
	require(['navigationBars', 'base', 'currentUser', 'twitterEnum', 'mediator', 'collect', 'portal',
	             'homePage',
	             'dataAnalysis', 'myHomePage', 'myReview', 'publishManage', 'talkMe', 'myTwitter',
	             'deal', 'view', 'twitterPubSentiment',
	             'eventMonitor',
	             'accountMonitor',
	             'spreadAnalysis',
	             'departmentManager', 'examineManager', 'taskNotify', 'coordination',
	             'twitterPush', 'alertInfo', 'alertList', 'alertService',
	             'accountBinding', 'authorityManager', 'taskManager', 'userManager', 'sysManager'],
	function (navigationBars, base, currentUser, twitterEnum, mediator, collect, portal,
	              homePage,
	              dataAnalysis, myHomePage, myReview, publishManage, talkMe, myTwitter,
	              deal, view, twitterPubSentiment,
	              eventMonitor,
	              accountMonitor,
	              spreadAnalysis,
	              departmentManager, examineManager, taskNotify, coordination,
	              twitterPush, alertInfo, alertList, alertService,
	              accountBinding, authorityManager, taskManager, userManager, sysManager) {
	
	    //中介者模块初始化
	    mediator.Init(homePage, dataAnalysis, myHomePage, myReview, publishManage, talkMe,
	         myTwitter, deal, view, twitterPubSentiment, eventMonitor, accountMonitor, spreadAnalysis,
	        departmentManager, examineManager, taskNotify, coordination, twitterPush, alertInfo, alertList, alertService,
	        accountBinding, authorityManager, taskManager, userManager, sysManager);
	
	    //初始化主模块
	  	portal.Init(navigationBars, base, currentUser, twitterEnum, mediator, collect);
	});

//});

	   
	   

