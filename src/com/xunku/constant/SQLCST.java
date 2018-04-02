package com.xunku.constant;

/**
 * SQL 相关的常量
 * 
 * @author wujian
 * @created on Jun 27, 2014 2:01:23 PM
 */
public interface SQLCST {

	/**
	 * 数据表站位副模板 #tableName#
	 */
	public static final String TEMPLATE_TABLE_NAME = "#tableName#";

	/**
	 * 所有put存储过程的前缀 sys_put_*
	 */
	public static final String PROC_PUT_PREFIX = "sys_put_";

	/**
	 * 创建分页存储过程
	 */
	public static final String PROC_CREATE_PAGER = "CREATE PROCEDURE SupesoftPage "
			+ "@TableName  nvarchar(200),"
			+ "@ReturnFields nvarchar(200) = '*',"
			+ "@PageSize  int = 10,"
			+ "@PageIndex  int = 1,"
			+ "@Where  nvarchar(max) = '',"
			+ "@Orderfld  nvarchar(200),"
			+ "@OrderType  int = 1"
			+ "AS DECLARE @TotalRecord int,@TotalPage int,@CurrentPageSize int,@TotalRecordForPageIndex int,@OrderBy nvarchar(255) "
			+ "if @OrderType = 1 BEGIN set @OrderBy = ' Order by ' + REPLACE(@Orderfld,',',' desc,') + ' desc ' END else BEGIN set @OrderBy = ' Order by ' + REPLACE(@Orderfld,',',' asc,') + ' asc ' END "
			+ "declare @countSql nvarchar(4000) set @countSql='SELECT @TotalRecord=Count(*) From '+@TableName+' '+@Where  execute sp_executesql @countSql,N'@TotalRecord int out',@TotalRecord out "
			+ "SET @TotalPage=(@TotalRecord-1)/@PageSize+1 SET @CurrentPageSize=(@PageIndex-1)*@PageSize set @TotalRecordForPageIndex=@PageIndex*@PageSize "
			+ "exec ('SELECT * FROM (SELECT TOP '+@TotalRecordForPageIndex+' '+@ReturnFields+', ROW_NUMBER() OVER ('+@OrderBy+') AS Supesoft_RowNo FROM '+@TableName+ ' ' + @Where +' ) AS TempSupesoftPageTable WHERE TempSupesoftPageTable.Supesoft_RowNo >'+@CurrentPageSize) "
			+ "SELECT @TotalPage as PageCount,@TotalRecord as RecordCount ";

	/**
	 * 创建put tweet的存储过程，#tableName#为占位符
	 */
	public static final String PROC_CREATE_PUT = "create PROCEDURE ["
			+ PROC_PUT_PREFIX
			+ "#tableName#]@id bigint,@uid varchar(36),@platform int,"
			+ "@tid varchar(36),@type int,@text nvarchar(300),@created bigint,"
			+ "@xfrom int,@ucode varchar(36),@reposts int,@comments int,"
			+ "@source varchar(36),@images varchar(500),@url varchar(150),"
			+ "@layer int,@out int output AS BEGIN set @out = 0 "
			+ "if not exists (select id from #tableName# where tid = @tid) "
			+ "begin insert into #tableName#(id,[uid],[platform],tid,[type],"
			+ "[text],created,xfrom,ucode,reposts,comments,[source],images,url,layer)"
			+ "Values(@id,@uid,@platform,@tid,@type,@text,@created,@xfrom,@ucode,@reposts,@comments,"
			+ "@source,@images,@url,@layer) set @out = 1 end "
			+ "else begin update #tableName# set [text]=@text,xfrom=@xfrom,reposts="
			+ "@reposts,comments=@comments,images=@images where tid = @tid "
			+ "set @out = 2 end end";

	/**
	 * 创建tweet表，#tableName#为占位符
	 */
	public static final String TABLE_CREATE_TWEET = "CREATE TABLE #tableName#("
			+ "[id] [bigint] not null primary key,[uid] [varchar](36) NULL,[ucode] [varchar](36) NOT NULL,"
			+ "[platform] [int] NOT NULL,	[tid] [varchar](36) NOT NULL,[type] [int] NOT NULL,"
			+ "[text] [nvarchar](300) NOT NULL,[created] [bigint] NOT NULL,	[xfrom] [int] NOT NULL,"
			+ "[reposts] [int] NOT NULL default 0,	[comments] [int] NOT NULL default 0,	[source] [varchar](36) NULL,"
			+ "[sourceid] [bigint] NULL,	[contentid] [varchar](50) NULL,	[CT] [datetime] NOT NULL default getdate(),"
			+ "[images] [nvarchar](500) NULL,	[url] [varchar](150) NULL,[layer] [int] NOT NULL)";

}
