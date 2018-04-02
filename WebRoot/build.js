//node r.js -o build.js
//node r.js -o cssIn=themes/modules/main.css out=themes/modules/main.min.css optimizeCss=standard
({
    dir: './packCompress',
    name: 'modules/program',
    optimize: 'uglify',
    fileExclusionRegExp: /^(r|build)\.js$/,
    optimizeCss: 'standard',
    removeCombined: true,
    paths: {
        jquery: 'scripts/lib/jquery-1.9.1.min',
        highcharts: 'scripts/lib/highcharts-3.0.1.min',
        //插件
        bgIframe: 'scripts/plugin/jquery.bgiframe',
        corner: 'scripts/plugin/jquery.corner',
        loading: 'scripts/plugin/jquery.loading',
        tool: 'scripts/plugin/jquery.tool',
        dateTool: 'scripts/plugin/jquery.tool.date',
        pngFix: 'scripts/plugin/jquery.pngFix',
        pager: 'scripts/plugin/jquery.pager.simple',
        regExp: 'scripts/plugin/jquery.regExp',
        keyupTo: 'scripts/plugin/jquery.keyupTo',
        patch: 'scripts/plugin/jquery.patch',
        datePicker: 'scripts/plugin/jquery.datePicker',
        timePicker: 'scripts/plugin/jquery.timePicker',
        taskPicker: 'scripts/plugin/jquery.taskPicker',
        expressionFormat: 'scripts/plugin/jquery.expression.format',
        expressionExtend: 'scripts/plugin/jquery.expression.extend',
        expression: 'scripts/plugin/jquery.expression',
        list: 'scripts/plugin/jquery.load.list',
        table: 'scripts/plugin/jquery.load.table',
        windows: 'scripts/plugin/jquery.windows',
        uploadify: 'scripts/plugin/jquery.uploadify',
        messageBox: 'scripts/plugin/jquery.windows.messageBox',
        easing: 'scripts/plugin/jquery.easing',
        circulate: 'scripts/plugin/jquery.circulate',
        cloud: 'scripts/plugin/jquery.cloud',
        returnTop: 'scripts/plugin/jquery.returnTop',
        input: 'scripts/plugin/jquery.input',
        quake: 'scripts/plugin/jquery.quake',
        cookie: 'scripts/plugin/jquery.cookie',
        
        //模块
        navigationBars: 'modules/portal/portal.navigationBars',
        base: 'modules/portal/portal.base',
        currentUser: 'modules/portal/portal.currentUser',
        twitterEnum: 'modules/portal/portal.enum',
        mediator: 'modules/portal/portal.mediator',
        repost: 'modules/portal/portal.view.repost',
        comment: 'modules/portal/portal.view.comment',
        collect: 'modules/portal/portal.function.collect',
        portal: 'modules/portal/portal',

        homePage: 'modules/homePage/homePage',

        dataAnalysis: 'modules/myTwitter/myTwitter.dataAnalysis',
        myHomePage: 'modules/myTwitter/myTwitter.myHomePage',
        myReview: 'modules/myTwitter/myTwitter.myReview',
        publishManage: 'modules/myTwitter/myTwitter.publishManage',
        talkMe: 'modules/myTwitter/myTwitter.talkMe',
        myTwitter: 'modules/myTwitter/myTwitter',

        deal: 'modules/twitterPubSentiment/twitterPubSentiment.deal',
        view: 'modules/twitterPubSentiment/twitterPubSentiment.view',
        twitterPubSentiment: 'modules/twitterPubSentiment/twitterPubSentiment',

        eventMonitor: 'modules/eventMonitor/eventMonitor',

        accountMonitor: 'modules/accountMonitor/accountMonitor',

        spreadAnalysis: 'modules/spreadAnalysis/spreadAnalysis',

        departmentManager: 'modules/coordination/coordination.departmentManager',
        examineManager: 'modules/coordination/coordination.examineManager',
        taskNotify: 'modules/coordination/coordination.taskNotify',
        coordination: 'modules/coordination/coordination',

        twitterPush: 'modules/alertService/alertService.twitterPush',
        alertInfo: 'modules/alertService/alertService.alertInfo',
        alertList: 'modules/alertService/alertService.alertList',
        alertService: 'modules/alertService/alertService',
        
        //系统管理
        accountBinding: 'modules/sysManager/sysManager.accountBinding',
        authorityManager: 'modules/sysManager/sysManager.authorityManager',
        taskManager: 'modules/sysManager/sysManager.taskManager',
        userManager: 'modules/sysManager/sysManager.userManager',
        sysManager: 'modules/sysManager/sysManager'
    },
    
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
})