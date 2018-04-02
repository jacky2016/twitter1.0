define(['portal'], function (context) {
    
    return {
    	//微博类型
        TwitterType: {
        	Sina: 1, //新浪
        	Tencent: 2, //腾讯
        	People: 5, //人民
        	All: -1 //全部
        },
        //图表类型
        CharType: {
        	Column: 0, //柱状图
        	Line: 1, //线图
        	Pie: 2, //饼图
        	Bar: 3, //横条图
        	ColumnPercent: 4 // 柱状图百分比模式
        },
        PublishManageType:{
        	Dsh:0,//待审核
        	Dfb:1,//待审核
        	Yfb:2,//已发布
        	Yfq:3 //已废弃
        },
        //展现列表的类
        LoadListType: {
        	All: 0, //全部
        	Original: 1, //原创
        	Pic: 2 //图片
        },
        //推送服务列表中类型
        PushServicesType: {
          EveryDayCheck: 0,//日常检测
          EventCheck: 1//事件检测
        },
        //协同办公中的任务通知的优先级
        TaskNotifyRankType: {
           RankHigh: 2,//优先级 高
           RankMiddle: 1,//中
           RankLow: 0//低
        },
        TaskNotifyStates: {
		 UnRead: 0,//未读 
		  HaveRead: 1//已读
        },
        //模块类型
        ModuleType: {
        	MyHomePage: 0, //我的首页模块
        	View: 1, //舆情展示模块
        	talkMe: 2, //@到我的模块
        	myReview: 3, //我的评论
        	EventMonitor: 4, //事件监控
        	Deal: 5 ,//舆情处理
        	ExamineManager:6,//考核管理
        	WarningService:7,//预警服务
        	SpreadAnalysis: 8 //传播分析
        }
    };
});