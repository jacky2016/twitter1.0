if exists (select * from sys.objects where name ='SupesoftPageBySql')
drop procedure SupesoftPageBySql;


create procedure SupesoftPageBySql
(   
	-- 需要返回的列
	@ReturnFields nvarchar(500) = '*',
	 -- 每页记录数          
	@PageSize  int = 10,  
	   -- 当前页码 
	@PageIndex  int = 1,  
	-- From ....
	@From nvarchar(1000), 
	-- 查询条件
	@Where   nvarchar(1000) = '',
	-- 排序字段名 最好为唯一主键      
	@Orderfld  nvarchar(200),
	-- 排序类型 1:降序 其它为升序       
	@OrderType  int = 1        
) 
AS  
begin	 
DECLARE @SearchSql varchar(4000) 
SET @SearchSql='SELECT * FROM 
	(SELECT ROW_NUMBER() OVER (ORDER BY ID ) Row_ID,' + @ReturnFields + ' ' +
	@From + ' ' + @Where + ') as O 
	WHERE Row_ID BETWEEN '+cast(@PageIndex as varchar) +' and ' + 
	cast(@PageIndex + @PageSize-1 as varchar)
execute(@SearchSql) 

DECLARE @SqlCount AS Nvarchar(4000) 
SET @SqlCount= 'SELECT Count(*) AS Total ' + @From + ' ' + @Where
exec(@SqlCount)
end;