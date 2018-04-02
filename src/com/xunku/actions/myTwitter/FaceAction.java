package com.xunku.actions.myTwitter;

import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.SmileieDao;
import com.xunku.daoImpl.base.SmileieDaoImpl;
import com.xunku.dto.IDTO;
import com.xunku.dto.myTwitter.SmileieDTO;
import com.xunku.dto.myTwitter.myHomePageListDTO;
import com.xunku.pojo.base.Smileie;

/*
 * 获取标题图片信息
 * @author sunao
 */
public class FaceAction extends ActionBase {
	
	public List<Smileie> doAction() {
		// TODO Auto-generated method stub
		SmileieDao smileieDao = new SmileieDaoImpl();
		SmileieDTO smileieDTO =new SmileieDTO();
		List<Smileie> lst = smileieDao.queryByAll();
		return lst;
	}

}
