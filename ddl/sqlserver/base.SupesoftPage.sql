
if exists (select * from sys.objects where name ='SupesoftPage')
drop procedure SupesoftPage;
-- 创建分页存储过程
create procedure SupesoftPage
	-- 表名
 	@TableName  nvarchar(200),     
 	-- 需要返回的列  
 	@ReturnFields nvarchar(200) = '*',
 	-- 每页记录数      
 	@PageSize  int = 10,
	-- 当前页码
 	@PageIndex  int = 1,
 	-- 查询条件        
 	@Where   nvarchar(max) = '',
 	-- 排序字段名 最好为唯一主键      
 	@Orderfld  nvarchar(200),
 	-- 排序类型 1:降序 其它为升序       
 	@OrderType  int = 1         
AS
 	declare @TotalRecord int    
 	declare @TotalPage int    
 	declare @CurrentPageSize int    
    declare @TotalRecordForPageIndex int    
    declare @OrderBy nvarchar(255)    
        
    if @OrderType = 1    
  	BEGIN    
   		set @OrderBy = ' Order by ' + REPLACE(@Orderfld,',',' desc,') + ' desc '    
  	END    
 	else    
  	BEGIN    
   		set @OrderBy = ' Order by ' + REPLACE(@Orderfld,',',' asc,') + ' asc '      
  	END    
     
 	-- 记录总数    
 	declare @countSql nvarchar(4000)      
     
 	set @countSql='SELECT @TotalRecord=Count(*) From '+@TableName+' '+@Where    
 	
 	execute sp_executesql @countSql,N'@TotalRecord int out',@TotalRecord out    
     
	SET @TotalPage=(@TotalRecord-1)/@PageSize+1    
     
 	SET @CurrentPageSize=(@PageIndex-1)*@PageSize    
	
	-- 返回记录    
 	set @TotalRecordForPageIndex=@PageIndex*@PageSize    
     
 	exec ('SELECT *    
   	FROM (SELECT TOP '+@TotalRecordForPageIndex+' '+@ReturnFields+', ROW_NUMBER() OVER ('+@OrderBy+') AS Supesoft_RowNo    
   	FROM '+@TableName+ ' ' + @Where +' ) AS TempSupesoftPageTable    
   	WHERE TempSupesoftPageTable.Supesoft_RowNo >     
   	'+@CurrentPageSize)    
    
    
 	-- 返回总页数和总记录数    
 	SELECT @TotalPage as PageCount,@TotalRecord as RecordCount    
