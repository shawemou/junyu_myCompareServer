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
 * @Description: 手机用户server
 *
 */
@Service
public class MobileServer {
	
	@Autowired
	private MobileInfoDao mobileInfoDao;

	/**
	 * 判断登录用户是否注册过此手机号,判定密码对不对,获取手机配置-->返回一个baseResult
	 * @param loginMap
	 * @param br
	 * @return
	 */
	
	@Autowired
	private TMobileInfoMapper mobileInfoMapper;
	
	public BaseReturn loginMobile(LoginBean loginBean, BaseReturn br){
		//1,查询MOBILE 返回 密码 和 busable
		TMobileInfo mobileInfo = new TMobileInfo();
		mobileInfo.setMobile(loginBean.getMobile());
		TMobileInfo dbMobileIfo = this.mobileInfoMapper.selectOne(mobileInfo);
		
		//2,没有查到与手机号一样的注册信息
		if(dbMobileIfo==null){
			br.setSuccess(false);
			br.setInfo("未发现与本手机号相关联的配置");
			return br;
		}else{
			if(dbMobileIfo.getBusable()!= null && dbMobileIfo.getBusable().toString().equals("0")){
				br.setSuccess(false);
				br.setInfo("与本手机号相关联的配置已停用");
				return br;
			}

			if( dbMobileIfo.getSecretKey() != null ){
				//TYPE为1表示ios系统登录
				if(loginBean.getType() != null && loginBean.getType().toString().equals("1")){
					String secret_key = AESCodec.aesDecrypt(dbMobileIfo.getSecretKey().toString());
					if( !JYMD5.MD5Encoder(secret_key,"UTF-8").equalsIgnoreCase(loginBean.getSecret_key().toString())){
						br.setSuccess(false);
						br.setInfo("密码验证不通过");
						return br;
					}
				}else{
					//type不是1或者为null判定为安卓手机登录
					if(!dbMobileIfo.getSecretKey().toString().equals(loginBean.getSecret_key())){
						br.setSuccess(false);
						br.setInfo("密码验证不通过");
						return br;
					}
				}
			}
		}
		
		br.setSuccess(true);
		br.setInfo("登录成功");
		return br;
	}
	
	public BaseReturn loginMobile(Map<String,String> loginMap, BaseReturn br){
		//1,查询MOBILE 返回 密码 和 busable
		List<Map<String, Object>> list = mobileInfoDao.loginMobile(loginMap);
		//2,没有查到与手机号一样的注册信息
		if(list == null || list.size() <= 0){
			br.setSuccess(false);
			br.setInfo("未发现与本手机号相关联的配置");
			return br;
		}else{
			Map<String, Object> map = list.get(0);
			if(map.get("BUSABLE") != null && map.get("BUSABLE").toString().equals("0")){
				br.setSuccess(false);
				br.setInfo("与本手机号相关联的配置已停用");
				return br;
			}

			if( map.get("SECRET_KEY") != null ){
				//TYPE为1表示ios系统登录
				if(loginMap.get("type") != null && loginMap.get("type").toString().equals("1")){
					String secret_key = AESCodec.aesDecrypt(map.get("SECRET_KEY").toString());
					if( !JYMD5.MD5Encoder(secret_key,"UTF-8").equalsIgnoreCase(loginMap.get("secret_key").toString())){
						br.setSuccess(false);
						br.setInfo("密码验证不通过");
						return br;
					}
				}else{
					//type不是1或者为null判定为安卓手机登录
					if(!map.get("SECRET_KEY").toString().equals(loginMap.get("secret_key"))){
						br.setSuccess(false);
						br.setInfo("密码验证不通过");
						return br;
					}
				}
			}
		}
		
		br.setSuccess(true);
		br.setInfo("登录成功");
		return br;
	}
	
	/**
	 * 保存记录
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
	 * 重置密码
	 * @param passwordMap
	 * @param br
	 * @return
	 */
	public BaseReturn passwordModify(Map<String,String> passwordMap, BaseReturn br){
		loginMobile(passwordMap, br);
		if( br.isSuccess() ){
			br.setInfo("修改密码成功");
			if( !mobileInfoDao.passwordModify(passwordMap, br) ){
				br.setSuccess(false);
				br.setInfo("修改密码异常");
			}
		}
		return br;
	}
}
