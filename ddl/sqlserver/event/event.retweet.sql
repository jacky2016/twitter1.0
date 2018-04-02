-- 原创转发统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Retweet_Statis')
create table Event_Retweet_Statis(
	--事件id
	eventid int not null,
	--原创数
	tweetCnt int not null,
	--转发数
	retweetCnt int not null,
	--评论数
	commentCnt int not null,
	--组合关键字
	primary key(eventid,retweetCnt,tweetCnt,commentCnt)
);

if exists (select * from sys.objects where name ='sys_put_Event_Retweet_Statis')
drop procedure sys_put_Event_Retweet_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Retweet_Statis
	@eventid int,
	@tweetCnt   int,
	@retweetCnt int,
	@commentCnt  int
AS
BEGIN
	if not exists (select eventid from Event_Retweet_Statis where eventid=@eventid)
	begin
		insert into Event_Retweet_Statis (eventid,tweetCnt,retweetCnt,commentCnt) values (@eventid,@tweetCnt,@retweetCnt,@commentCnt)
	end
	else
	begin
		if(@retweetCnt = 1)
		begin
			set @retweetCnt = @retweetCnt+1
		end
		else if(@tweetCnt = 1)
		begin
			set @tweetCnt = @tweetCnt+1
		end
		else
		begin
			set @commentCnt = @commentCnt+1
		end
		update Event_Retweet_Statis set retweetCnt=@retweetCnt,tweetCnt=@tweetCnt,commentCnt=@commentCnt where eventid=@eventid
	end	
END;
