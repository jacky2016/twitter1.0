--官微里面提到的相关数据存储对象

------------------------------------------------------------------------------------------------------
-- 提到某用户的存储表
-- 该表记录了某个帖子提到了某个人
if not exists (select * from sys.objects where name = 'My_Mentions')
create table My_Mentions(
	id bigint identity(1,1) not null primary key,
	--提到人的帖子
	tid varchar(36) not null,
	--帖子提到了哪个人
	uid varchar(36) not null,
	--是哪个平台的
	platform int not null,
	--这个作者是不是vip
	vip bit not null default(0),
	--这个作者是不是关注了uid
	friend bit not null default(0),
	--这个作者是不是系统里面定义的水军
	navies bit not null default(0),
	--这个帖子是什么时间创建的，该字段是冗余字段用来查询使用
	created bigint not null,
	--这个帖子是谁发的，冗余字段用来查询
	author varchar(36) null,
	--发帖子的内容，冗余字段用来查询
	text nvarchar(300) null,
	-- 这个帖子里面有没有图片，冗余字段用来查询
	hasImg bit default(0),
	-- 这个帖子是转发的还是评论的，冗余字段用来查询
	[type] int
);

-- 该存储过程用来插入My_Mentions数据
if exists (select * from sys.objects where name ='sys_put_My_Mention')
drop procedure sys_put_My_Mention;

create procedure sys_put_My_Mention
		@tid varchar(36),
		@uid varchar(36),
		@platform int,
		@vip bit,
		@friend bit,
		@navies bit,
		@created bigint,
		@author varchar(36),
		@text nvarchar(300),
		@type int,
		@hasImg bit
as
	begin
		if(not exists (select tid from My_Mentions where tid = @tid and @uid=uid))
		begin
			insert into My_Mentions (tid,uid,platform,vip,friend,navies,created,author,text,[type],hasImg)
			values(@tid,@uid,@platform,@vip,@friend,@navies,@created,@author,@text,@type,@hasImg) 
		end
	end;
------------------------------------------------------------------------------------------------------