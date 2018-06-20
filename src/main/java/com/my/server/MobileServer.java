package com.my.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.bean.LoginBean;
import com.my.common.BaseReturn;
import com.my.dao.MobileInfoDao;
import com.my.mapper.TMobileInfoMapper;
import com.my.mapper.TMobileLoginLogMapper;
import com.my.pojo.TMobileInfo;
import com.my.pojo.TMobileLoginLog;
import com.my.util.AESCodec;
import com.my.util.GUID;
import com.my.util.JYMD5;

/**
 * @ClassName: MobileServer 
 * @Description: �ֻ��û�server
 *
 */
@Service
public class MobileServer {
	
	@Autowired
	private MobileInfoDao mobileInfoDao;

	/**
	 * �жϵ�¼�û��Ƿ�ע������ֻ���,�ж�����Բ���,��ȡ�ֻ�����-->����һ��baseResult
	 * @param loginMap
	 * @param br
	 * @return
	 */
	
	@Autowired
	private TMobileInfoMapper mobileInfoMapper;
	
	public BaseReturn loginMobile(LoginBean loginBean, BaseReturn br){
		//1,��ѯMOBILE ���� ���� �� busable
		TMobileInfo mobileInfo = new TMobileInfo();
		mobileInfo.setMobile(loginBean.getMobile());
		TMobileInfo dbMobileIfo = this.mobileInfoMapper.selectOne(mobileInfo);
		
		//2,û�в鵽���ֻ���һ����ע����Ϣ
		if(dbMobileIfo==null){
			br.setSuccess(false);
			br.setInfo("δ�����뱾�ֻ��������������");
			return br;
		}else{
			if(dbMobileIfo.getBusable()!= null && dbMobileIfo.getBusable().toString().equals("0")){
				br.setSuccess(false);
				br.setInfo("�뱾�ֻ����������������ͣ��");
				return br;
			}

			if( dbMobileIfo.getSecretKey() != null ){
				//TYPEΪ1��ʾiosϵͳ��¼
				if(loginBean.getType() != null && loginBean.getType().toString().equals("1")){
					String secret_key = AESCodec.aesDecrypt(dbMobileIfo.getSecretKey().toString());
					if( !JYMD5.MD5Encoder(secret_key,"UTF-8").equalsIgnoreCase(loginBean.getSecret_key().toString())){
						br.setSuccess(false);
						br.setInfo("������֤��ͨ��");
						return br;
					}
				}else{
					//type����1����Ϊnull�ж�Ϊ��׿�ֻ���¼
					if(!dbMobileIfo.getSecretKey().toString().equals(loginBean.getSecret_key())){
						br.setSuccess(false);
						br.setInfo("������֤��ͨ��");
						return br;
					}
				}
			}
		}
		
		br.setSuccess(true);
		br.setInfo("��¼�ɹ�");
		return br;
	}
	
	public BaseReturn loginMobile(Map<String,String> loginMap, BaseReturn br){
		//1,��ѯMOBILE ���� ���� �� busable
		List<Map<String, Object>> list = mobileInfoDao.loginMobile(loginMap);
		//2,û�в鵽���ֻ���һ����ע����Ϣ
		if(list == null || list.size() <= 0){
			br.setSuccess(false);
			br.setInfo("δ�����뱾�ֻ��������������");
			return br;
		}else{
			Map<String, Object> map = list.get(0);
			if(map.get("BUSABLE") != null && map.get("BUSABLE").toString().equals("0")){
				br.setSuccess(false);
				br.setInfo("�뱾�ֻ����������������ͣ��");
				return br;
			}

			if( map.get("SECRET_KEY") != null ){
				//TYPEΪ1��ʾiosϵͳ��¼
				if(loginMap.get("type") != null && loginMap.get("type").toString().equals("1")){
					String secret_key = AESCodec.aesDecrypt(map.get("SECRET_KEY").toString());
					if( !JYMD5.MD5Encoder(secret_key,"UTF-8").equalsIgnoreCase(loginMap.get("secret_key").toString())){
						br.setSuccess(false);
						br.setInfo("������֤��ͨ��");
						return br;
					}
				}else{
					//type����1����Ϊnull�ж�Ϊ��׿�ֻ���¼
					if(!map.get("SECRET_KEY").toString().equals(loginMap.get("secret_key"))){
						br.setSuccess(false);
						br.setInfo("������֤��ͨ��");
						return br;
					}
				}
			}
		}
		
		br.setSuccess(true);
		br.setInfo("��¼�ɹ�");
		return br;
	}
	
	/**
	 * �����¼
	 * @param loginMap
	 * @param br
	 * @return
	 */
	@Autowired
	private TMobileLoginLogMapper mobileLoginLogMapper;
	
	public boolean saveLoginMobileLog(LoginBean loginBean, BaseReturn br){
		TMobileLoginLog mobileLoginLog = new TMobileLoginLog();
		mobileLoginLog.setGuid(new GUID().toString());
		mobileLoginLog.setMobile(loginBean.getMobile());
		mobileLoginLog.setSecretKey(loginBean.getSecret_key());
		mobileLoginLog.setSuccess(br.isSuccess() ? "1" : "0");
		mobileLoginLog.setCreateTime(new Date());
		 return  this.mobileLoginLogMapper.insert(mobileLoginLog)==1?true:false;
	}
	
	/**
	 * ��������
	 * @param passwordMap
	 * @param br
	 * @return
	 */
	public BaseReturn passwordModify(Map<String,String> passwordMap, BaseReturn br){
		loginMobile(passwordMap, br);
		if( br.isSuccess() ){
			br.setInfo("�޸�����ɹ�");
			if( !mobileInfoDao.passwordModify(passwordMap, br) ){
				br.setSuccess(false);
				br.setInfo("�޸������쳣");
			}
		}
		return br;
	}
}
