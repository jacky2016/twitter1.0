/****** Object:  StoredProcedure [dbo].[SupesoftPage]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SupesoftPage]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'
/*********************************************************************************    
*      Copyright (C) 2006 supesoft.com,All Rights Reserved       *    
*      Function:  SupesoftPage              *    
*      Description:                                                              *    
*             Sql2005分页存储过程             *    
*      Author:                                                                   *    
*             By Michael Li               *    
*      Finish DateTime:                                                          *    
*             2006/7/15                *    
*    Example:                  *    
*           SupesoftPage @Tablename = ''Table1'', @Returnfields = ''*'',           *    
*     @PageSize = 2, @PageIndex = 1, @Where = '''', @Orderfld = ''id'',      *    
*     @Ordertype = 0              *               
*********************************************************************************/    
CREATE PROCEDURE [dbo].[SupesoftPage]    
 @TableName  nvarchar(200),   -- 表名    
 @ReturnFields nvarchar(200) = ''*'', -- 需要返回的列     
 @PageSize  int = 10,    -- 每页记录数    
 @PageIndex  int = 1,    -- 当前页码    
 @Where   nvarchar(max) = '''',  -- 查询条件    
 @Orderfld  nvarchar(200),   -- 排序字段名 最好为唯一主键    
 @OrderType  int = 1     -- 排序类型 1:降序 其它为升序    
AS    
 DECLARE @TotalRecord int    
 DECLARE @TotalPage int    
 DECLARE @CurrentPageSize int    
    DECLARE @TotalRecordForPageIndex int    
    DECLARE @OrderBy nvarchar(255)    
        
    if @OrderType = 1    
  BEGIN    
   set @OrderBy = '' Order by '' + REPLACE(@Orderfld,'','','' desc,'') + '' desc ''    
  END    
 else    
  BEGIN    
   set @OrderBy = '' Order by '' + REPLACE(@Orderfld,'','','' asc,'') + '' asc ''      
  END    
     
 -- 记录总数    
 declare @countSql nvarchar(4000)      
     
 set @countSql=''SELECT @TotalRecord=Count(*) From ''+@TableName+'' ''+@Where    
 execute sp_executesql @countSql,N''@TotalRecord int out'',@TotalRecord out    
     
     
     
 SET @TotalPage=(@TotalRecord-1)/@PageSize+1    
     
 SET @CurrentPageSize=(@PageIndex-1)*@PageSize    
    
      
 -- 返回记录    
 set @TotalRecordForPageIndex=@PageIndex*@PageSize    
     
 exec (''SELECT *    
   FROM (SELECT TOP ''+@TotalRecordForPageIndex+'' ''+@ReturnFields+'', ROW_NUMBER() OVER (''+@OrderBy+'') AS Supesoft_RowNo    
   FROM ''+@TableName+ '' '' + @Where +'' ) AS TempSupesoftPageTable    
   WHERE TempSupesoftPageTable.Supesoft_RowNo >     
   ''+@CurrentPageSize)    
    
    
 -- 返回总页数和总记录数    
 SELECT @TotalPage as PageCount,@TotalRecord as RecordCount    
       
' 
END
GO
/****** Object:  Table [dbo].[Event_WordCloud]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_WordCloud]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_WordCloud](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[keyword] [nvarchar](50) NOT NULL,
	[count] [int] NOT NULL,
	[lastupdated] [datetime] NOT NULL,
 CONSTRAINT [PK_Event_WordCloud] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Event_Source_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_Source_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_Source_An](
	[eventid] [int] NOT NULL,
	[sourceId] [bigint] NOT NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_Event_Source_An_1] PRIMARY KEY CLUSTERED 
(
	[eventid] ASC,
	[sourceId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Event_Reg_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_Reg_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_Reg_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[v1] [int] NOT NULL,
	[v2] [int] NOT NULL,
	[v3] [int] NOT NULL,
	[v4] [int] NOT NULL,
 CONSTRAINT [PK_Event_Reg_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Event_Local_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_Local_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_Local_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[city] [int] NOT NULL,
	[location] [nvarchar](50) NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_Event_Local_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Event_KeyUser_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_KeyUser_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_KeyUser_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[ucode] [varchar](36) NOT NULL,
	[followers] [int] NOT NULL,
	[weibos] [int] NOT NULL,
	[friends] [int] NOT NULL,
 CONSTRAINT [PK_Event_KeyUser_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Event_KeyPoint_Top100]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_KeyPoint_Top100]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_KeyPoint_Top100](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[tid] [varchar](36) NOT NULL,
	[reposts] [int] NOT NULL,
 CONSTRAINT [PK_Event_KeyPoint_Top100] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Event_Count_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_Count_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_Count_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventId] [int] NOT NULL,
	[posts] [int] NOT NULL,
	[reposts] [int] NOT NULL,
	[females] [int] NOT NULL,
	[males] [int] NOT NULL,
	[unsex] [int] NOT NULL,
	[vip] [int] NOT NULL,
 CONSTRAINT [PK_Event_Count_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Event_Big_User]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Event_Big_User]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Event_Big_User](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ucode] [varchar](36) NOT NULL,
	[eventId] [int] NOT NULL,
	[followers] [int] NOT NULL,
	[tid] [varchar](36) NOT NULL,
 CONSTRAINT [PK_Event_Big_User] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Weibo_Repost_Source_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Source_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Source_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[type] [int] NOT NULL,
	[source] [int] NOT NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_Weibo_Repost_Source_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Weibo_Repost_Opinion_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Opinion_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Opinion_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[opinion] [int] NOT NULL,
	[unopinion] [int] NOT NULL,
 CONSTRAINT [PK_Weibo_Repost_Opinion_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Weibo_Repost_Local_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Local_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Local_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[type] [int] NOT NULL,
	[city] [int] NOT NULL,
	[location] [nvarchar](150) NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_Weibo_Repost_Local_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Weibo_Repost_Hot_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Hot_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Hot_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[type] [int] NOT NULL,
	[keyword] [nvarchar](150) NOT NULL,
	[times] [int] NOT NULL,
	[updated] [datetime] NOT NULL,
 CONSTRAINT [PK_Weibo_Repost_Hot_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Weibo_Repost_Fans_Pop_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Fans_Pop_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[type] [int] NOT NULL,
	[v1] [int] NOT NULL,
	[v2] [int] NOT NULL,
	[v3] [int] NOT NULL,
	[v4] [int] NOT NULL,
	[v5] [int] NOT NULL,
	[v6] [int] NOT NULL,
 CONSTRAINT [PK_Weibo_Repost_Fans_Pop_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Weibo_Repost_Count_An]    Script Date: 07/01/2014 11:47:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Count_An]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Weibo_Repost_Count_An](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[weiboid] [int] NOT NULL,
	[type] [int] NOT NULL,
	[vip] [int] NOT NULL,
	[novip] [int] NOT NULL,
	[female] [int] NOT NULL,
	[male] [int] NOT NULL,
	[unsex] [int] NOT NULL,
 CONSTRAINT [PK_Weio_Repost_Count_An] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Weibo_Repost_Source_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Weibo_Repost_Source_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[sys_put_Weibo_Repost_Source_An] 
	@weiboid int,
	@type int,
	@source int
AS
BEGIN
	update weibo_repost_source_an set value=value+1 
	where weiboid = @weiboid and [type] = @type and [source] = @source
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Weibo_Repost_Opinion_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Weibo_Repost_Opinion_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[sys_put_Weibo_Repost_Opinion_An] 
	@weiboid int,
	@op int,
	@unop int
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Opinion_An where @weiboid=weiboid))
	begin
		insert into Weibo_Repost_Opinion_An(weiboid,opinion,unopinion)
		values(@weiboid,@op,@unop)
	end
	else
	begin
		update Weibo_Repost_Opinion_An set opinion=opinion+@op,
		unopinion=unopinion+@unop where @weiboid=weiboid 
	end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Weibo_Repost_Local_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Weibo_Repost_Local_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Weibo_Repost_Local_An] 
	@weiboid int,
	@type int,
	@city int,
	@location nvarchar(50)
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Local_An where @weiboid=weiboid and @type=[type]
	and city=@city and location=@location))
	begin
		insert into Weibo_Repost_Local_An(weiboid,[type],city,location,value)
		values(@weiboid,@type,@city,@location,1)
	end
	else
	begin
		update Weibo_Repost_Local_An set value = value+1 where @weiboid=weiboid and @type=[type]
	and city=@city and location=@location
	end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Weibo_Repost_Fans_Pop_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Weibo_Repost_Fans_Pop_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[sys_put_Weibo_Repost_Fans_Pop_An] 
	@weiboid int,
	@type int,
	@v1 int,
	@v2 int,
	@v3 int,
	@v4 int,
	@v5 int,
	@v6 int
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Fans_Pop_An where @weiboid=weiboid and [type] =@type))
	begin
		insert into Weibo_Repost_Fans_Pop_An(weiboid,[type],v1,v2,v3,v4,v5,v6)
		values(@weiboid,@type,@v1,@v2,@v3,@v4,@v5,@v6)
	end
	else
	begin
		update Weibo_Repost_Fans_Pop_An set v1=v1+@v1,
		v2=v2+@v2,v3=v3+@v3,v4=v4+@v4,v5=v5+@v5
		where @weiboid=weiboid and [type] = @type
	end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Weibo_Repost_Count_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Weibo_Repost_Count_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[sys_put_Weibo_Repost_Count_An] 
	@weiboid int,
	@type int,
	@vip int,
	@novip int,
	@female int,
	@male int,
	@unsex int
AS
BEGIN
	update Weibo_Repost_Count_An set 
	vip=vip+@vip,novip=novip+@novip,
	female=female+@female,male=@male+male,
	unsex = unsex+@unsex
	where weiboid = @weiboid and [type] = @type
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_My_Posts]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_My_Posts]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'create PROCEDURE [dbo].[sys_put_My_Posts]@uid varchar(36),@platform int,@tid varchar(36),@type int,@text nvarchar(150),@created datetime,@xfrom int,@ucode varchar(36),@reposts int,@comments int,@source varchar(36),@images varchar(500),@url varchar(150),@layer int,@out int output AS BEGIN set @out = 0	if not exists (select id from My_Posts where tid = @tid) begin  insert into My_Posts([uid],[platform],tid,[type],[text],created,xfrom,ucode, reposts,comments,[source],images,url,layer)Values(@uid,@platform,@tid,@type,@text,@created,@xfrom,@ucode,@reposts,@comments,@source,@images,@url,@layer) set @out = 1 end	else begin update My_Posts set [text]=@text,xfrom=@xfrom,reposts=@reposts,comments=@comments,images=@images where tid = @tid set @out = 2 end end' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_My_Mention]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_My_Mention]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_My_Mention]
	@tid varchar(36),
	@uid varchar(36),
	@platform int,
	@vip bit,
	@friend bit,
	@navies bit
	
AS
BEGIN
	if(not exists (select tid from My_Mentions where tid = @tid and @uid=[uid]))
	begin
		insert into My_Mentions (tid,uid,platform,vip,friend,navies)
		values(@tid,@uid,@platform,@vip,@friend,@navies) 
	end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_Source_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_Source_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_Source_An] 
	@eventId int,
	@sourceId int
AS
BEGIN
if(not exists (select * from Event_Source_An where eventid = @eventId and @sourceId = sourceId))
begin
	insert into event_source_an (eventid,sourceid,value)
	values(@eventid,@sourceId,1)
end
else
begin
	update event_source_an set value=value+1 where eventid = @eventId and @sourceId = sourceId
end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_Reg_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_Reg_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: 2014.6.17
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_Reg_An] 
	@eventId int,
	@v1 int,
	@v2 int,
	@v3 int,
	@v4 int
AS
BEGIN
if(not exists (select * from Event_Reg_An where eventid = @eventId))
begin
	insert into Event_Reg_An (eventid,v1,v2,v3,v4)
	values(@eventid,@v1,@v2,@v3,@v4)
end
else
begin
	update Event_Reg_An set 
	v1=v1+@v1,v2=v2+@v2,
	v3=v3+@v3,v4=v4+@v4
	where eventid = @eventId
end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_Local_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_Local_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: 2014.6.17
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_Local_An] 
	@eventId int,
	@city int,
	@location int
AS
BEGIN
if(not exists (select * from Event_Local_An where eventid = @eventId and @city=city and @location=location))
begin
	insert into Event_Local_An (eventid,city,location,value)
	values(@eventId,@city,@location,1)
end
else
begin
	update Event_Local_An set 
	value=value+1
	where eventid = @eventId and @city=city and @location=location
end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_KeyUser_An1]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_KeyUser_An1]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'
-- =============================================
-- Author:		wujian
-- Create date: 2014.6.17
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_KeyUser_An1] 
	@eventId int,
	@ucode varchar(36),
	@followers int,
	@weibos int,
	@friends int
AS
BEGIN

declare @count int

select @count = count(*) from event_keyuser_an where eventId = @eventId

if(@count<50)
	begin
		insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
	end
	else
	begin
		declare @min int
		declare @id int

		select top 1 @min=followers,@id=id from event_keyuser_an where @eventId=eventId order by followers desc

		if(@followers>@min)
		begin
			delete from event_keyuser_an where id=@id
			
			insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
		end
				
	end

END

' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_KeyUser_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_KeyUser_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: 2014.6.17
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_KeyUser_An] 
	@eventId int,
	@ucode varchar(36),
	@followers int,
	@weibos int,
	@friends int
AS
BEGIN

declare @count int

select @count = count(*) from event_keyuser_an where eventId = @eventId

if(@count<50)
	begin
		insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
	end
	else
	begin
		declare @min int
		declare @id int

		select top 1 @min=followers,@id=id from event_keyuser_an where @eventId=eventId order by followers desc

		if(@followers>@min)
		begin
			delete from event_keyuser_an where id=@id
			
			insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
		end
				
	end

END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_KeyPoint_Top100]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_KeyPoint_Top100]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wujian
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_KeyPoint_Top100]
	@eventid int,
	@tid varchar(36),
	@reposts int
AS
BEGIN
	declare @count int
	select @count=COUNT(id) from Event_KeyPoint_Top100 where eventId =@eventid
	
	if(@count<50)
	begin
		insert into Event_KeyPoint_Top100 (eventId,tid,reposts)
		values(@eventid,@tid,@reposts)
	end
	else
	begin
		-- 找到最小的repost
		declare @min int
		declare @id int
		select top 1 @min = reposts,@id=id from Event_KeyPoint_Top100 where @eventid =eventId order by reposts desc
		if(@reposts>@min)
			begin
				delete from Event_KeyPoint_Top100 where id =@id
				insert into Event_KeyPoint_Top100 (eventId,tid,reposts)
				values(@eventid,@tid,@reposts)
			end
	end
END
' 
END
GO
/****** Object:  StoredProcedure [dbo].[sys_put_Event_Count_An]    Script Date: 07/01/2014 11:47:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_put_Event_Count_An]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[sys_put_Event_Count_An] 
	@eventid bigint,
	@ispost int,
	@isrepost int,
	@isfemale int,
	@ismale int,
	@isunsex int,
	@isvip int
AS
BEGIN
	if not exists (select eventid from Event_Count_An where eventId = @eventid)
		begin
			insert into Event_Count_An(eventid,posts,reposts,females,males,unsex,vip)
			values (@eventid,0,0,0,0,0,0)
		end
	
	if(@ispost >= 1)
		begin
			update Event_Count_An set posts=posts+@ispost where eventId = @eventid
		end
	if(@isrepost >=1)
		begin
			update Event_Count_An set reposts=reposts+@isrepost where eventId = @eventid
		end
	if(@isfemale >=1)
		begin
			update Event_Count_An set females=females+@isfemale where eventId = @eventid
		end
	if(@ismale >=1)
		begin
			update Event_Count_An set males=males+@ismale where eventId = @eventid
		end
	if(@isunsex >=1)
		begin
			update Event_Count_An set unsex=unsex+@isunsex where eventId = @eventid
		end
	if(@isvip >=1)
		begin
			update Event_Count_An set vip=vip+@isvip where eventId = @eventid
		end
	
END
' 
END
GO
/****** Object:  Default [DF_Event_WordCloud_lastupdated]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Event_WordCloud_lastupdated]') AND parent_object_id = OBJECT_ID(N'[dbo].[Event_WordCloud]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Event_WordCloud_lastupdated]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Event_WordCloud] ADD  CONSTRAINT [DF_Event_WordCloud_lastupdated]  DEFAULT (getdate()) FOR [lastupdated]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v1]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v1]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v1]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v1]  DEFAULT ((0)) FOR [v1]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v2]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v2]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v2]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v2]  DEFAULT ((0)) FOR [v2]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v3]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v3]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v3]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v3]  DEFAULT ((0)) FOR [v3]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v4]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v4]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v4]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v4]  DEFAULT ((0)) FOR [v4]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v5]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v5]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v5]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v5]  DEFAULT ((0)) FOR [v5]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Fans_Pop_An_v6]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Fans_Pop_An_v6]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Fans_Pop_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Fans_Pop_An_v6]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Fans_Pop_An] ADD  CONSTRAINT [DF_Weibo_Repost_Fans_Pop_An_v6]  DEFAULT ((0)) FOR [v6]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Hot_An_times]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Hot_An_times]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Hot_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Hot_An_times]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Hot_An] ADD  CONSTRAINT [DF_Weibo_Repost_Hot_An_times]  DEFAULT ((0)) FOR [times]
END


End
GO
/****** Object:  Default [DF_Weibo_Repost_Hot_An_updated]    Script Date: 07/01/2014 11:47:05 ******/
IF Not EXISTS (SELECT * FROM sys.default_constraints WHERE object_id = OBJECT_ID(N'[dbo].[DF_Weibo_Repost_Hot_An_updated]') AND parent_object_id = OBJECT_ID(N'[dbo].[Weibo_Repost_Hot_An]'))
Begin
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_Weibo_Repost_Hot_An_updated]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Weibo_Repost_Hot_An] ADD  CONSTRAINT [DF_Weibo_Repost_Hot_An_updated]  DEFAULT (getdate()) FOR [updated]
END


End
GO
