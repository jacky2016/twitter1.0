package com.xunku.daoImpl.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.SmileieDao;
import com.xunku.pojo.base.Smileie;
import com.xunku.utils.DatabaseUtils;

public class SmileieDaoImpl implements SmileieDao{
    
    @Override
    public List<Smileie> queryByAll() {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Smileie> list = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    pstmt = conn.prepareStatement("SELECT ID,Name,FileName,Sina,QQ,Renmin FROM Base_Smileies");
	    rs = pstmt.executeQuery();
	    list = new ArrayList<Smileie>();
	    while(rs.next()){
		Smileie s = new Smileie();
		s.setId(rs.getInt("ID"));
		s.setName(rs.getString("Name"));
		s.setFileName(rs.getString("FileName"));
		s.setSina(rs.getString("Sina"));
		s.setQq(rs.getString("QQ"));
		s.setRenmin(rs.getString("Renmin"));
		list.add(s);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally{
	    try {
		if(rs != null){
		    rs.close();
		}
		if(pstmt != null){
		    pstmt.close();
		}
		if(conn != null){
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return list;
    }
    
    private static final String conStr = "jdbc:sqlserver://192.168.2.13;databaseName=xk_weibo;user=sa;password=kx*0000";
    private static void inserts(Smileie s) {
	SQLServerDataSource ds = new SQLServerDataSource();
	ds.setURL(conStr);
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    //conn = DatabaseUtils.cpdsMap.get("base").getConnection();
	    conn = ds.getConnection();
	    String sql = "INSERT INTO Base_Smileies (Name,FileName,Sina,QQ,Renmin) VALUES (?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, s.getName());
	    pstmt.setString(2, s.getFileName());
	    pstmt.setString(3, s.getSina());
	    pstmt.setString(4, s.getQq());
	    pstmt.setString(5, s.getRenmin());
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}  finally{
	    try {
		if(pstmt != null){
		    pstmt.close();
		}
		if(conn != null){
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }
    
    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        List<String> list = new ArrayList<String>();
	try {
            
                File file = new File(filepath);
                if (!file.isDirectory()) {
                        System.out.println("文件");
                        System.out.println("path=" + file.getPath());
                        System.out.println("absolutepath=" + file.getAbsolutePath());
                        System.out.println("name=" + file.getName());

                } else if (file.isDirectory()) {
                        //System.out.println("文件夹");
                        String[] filelist = file.list();
                        for (int i = 0; i < filelist.length; i++) {
                                File readfile = new File(filepath + "\\" + filelist[i]);
                                if (!readfile.isDirectory()) {
                                        //System.out.println("path=" + readfile.getPath());
                                        //System.out.println("absolutepath="
                                        //                + readfile.getAbsolutePath());
                                    list.add(readfile.getName());

                                } else if (readfile.isDirectory()) {
                                        readfile(filepath + "\\" + filelist[i]);
                                }
                        }

                }

        } catch (FileNotFoundException e) {
                System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return list;
}
    public static List<String> readTxtfile(String filepath) throws FileNotFoundException, IOException {
       File file = new File(filepath);
       List<String> list = new ArrayList<String>();
       if (file.isFile()) {
	   BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
	   String line = null;
	   while((line = reader.readLine())!=null){
	       if(line != null){
		   list.add(line);
	       }
	   }
	   reader.close();
       }
       return list;
    }
    public static void main(String[] args) {
	try {
	    List<String> list = readfile("C:/Users/Administrator/Desktop/teitter1.0Theme/themes/modules/sina/");
	    List<String> names = readTxtfile("C:/Users/Administrator/Desktop/teitter1.0Theme/themes/modules/txt/readme.txt");
	    for (int i=0;i<list.size();i++) {
		String fileName = list.get(i);
		String name = names.get(i);
		if(name != null){
		    String rexName = name.substring(0, 2);
		    if(fileName.contains(rexName)){
			
			Smileie s = new Smileie();
			s.setName(name.substring(2).trim());
			s.setFileName("$/smile/"+fileName.trim());
			inserts(s);
		    }
		}
	    }
	    
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
