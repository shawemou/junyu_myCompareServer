package com.my.server;

import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.custle.sdk.TestIDPhotoAuthService;
import com.my.common.CompareReturn;
import com.my.common.EReturnCompare;
import com.my.common.ReadSetting;
import com.my.common.EnumInstance.EReturnSource;
import com.my.common.EnumInstance.Return;
import com.my.dao.CompareDao;
import com.my.server.Client.JYWebserviceClient;
import com.my.util.Check;
import com.my.util.GUID;
import com.my.util.Log4jUtil;

/**
 * @ClassName: CompareServer 
 * @Description: 比对服务
 *
 */
@Service
public class CompareServer {
	
	@Autowired
	private CompareDao compareDao;
	
	private static int TOTAL_COUNT = 2;
	
	/**
	 * 调用本地比对服务
	 * @param compareMap
	 * @param cr
	 * @return
	 */
	public  CompareReturn compare(Map<String,String> compareMap, CompareReturn cr){
		Log4jUtil.log.warn("------------------");
		String[] arrPhoto = new String[]{compareMap.get("photo_user")};
		Log4jUtil.log.warn("photo_id长度:" + compareMap.get("photo_id").length());
		String headXml = JYWebserviceClient.createInHeadXml(new GUID().toString(),JYWebserviceClient.iType_V3,JYWebserviceClient.iSubType_Compare);
		String compareDataXml = JYWebserviceClient.createInCompareDataXml(new GUID().toString(), compareMap.get("name"), compareMap.get("id_number"), compareMap.get("photo_id"), arrPhoto);
		
		String compare_url = ReadSetting.getInstance().getCompare_url();
		String clientReturn = null;
		int iTryCount = 0;
		do {
			iTryCount++;
			clientReturn = JYWebserviceClient.executeClient(compare_url , headXml,compareDataXml);
			if (clientReturn != null) {
				break;
			}
		}while (iTryCount < TOTAL_COUNT);
		
		if(iTryCount > 1){
			Log4jUtil.log.warn("---ESB比对照片次数:" + iTryCount);
		}
		
		cr = JYWebserviceClient.parserCompareOutXml(JYWebserviceClient.parseXmlForData(clientReturn),cr);
		
		if( cr.isSuccess() ){
			int iSimilarity = cr.getSimilarity();
			int iSim = Integer.valueOf(ReadSetting.getInstance().getSimilarity());
			int otherISim = Integer.valueOf(ReadSetting.getInstance().getOther_similarity());
			if(iSimilarity >= 0 ){
				if(compareMap.get("id_type").equals("1")){
					if ( iSim >= 0 && iSim <= 1000) {
						if (iSimilarity >= iSim) {
							cr.setSuccess(true);
							cr.setCode(Return.RT_Success);
							cr.setInfo("比对通过");
							Log4jUtil.log.warn("身份证，判断相似度通过");
						} else {
							cr.setSuccess(false);
							cr.setCode(Return.RT_Fail);
							cr.setInfo("系统判断为不同人");
							Log4jUtil.log.warn("身份证，判断相似度未通过");
						}
					}
				}else{
					if ( iSim >= 0 && iSim <= 1000) {
						if (iSimilarity >= otherISim) {
							cr.setSuccess(true);
							cr.setCode(Return.RT_Success);
							cr.setInfo("比对通过");
							Log4jUtil.log.warn("其他证件，判断相似度未通过");
						} else {
							cr.setSuccess(false);
							cr.setCode(Return.RT_Fail);
							cr.setInfo("系统判断为不同人");
							Log4jUtil.log.warn("其他证件，判断相似度未通过");
						}
					}
				}
			}
		}
		
		if(cr.getCode().equals(Return.RT_Not_Compare) && !Check.IsStringNULL(cr.getReturn_code())){
			if(EReturnCompare.map.containsKey(cr.getReturn_code())){
				cr.setInfo(EReturnCompare.map.get(cr.getReturn_code()));
			}
		}
		
		/**
		 * 当身份证比对为同一人时，调用
		 * 多源认证接口服务
		 */
//		if(cr.isSuccess() && compareMap.get("id_type").equals("1")){
//			//本地两照比对为同一人
//			if(cr.getCode().equals(Return.RT_Success)){
//				Log4jUtil.log.warn("调用多源认证接口服务开始");
//				sourceCompare(compareMap, cr);
//				if(cr.getsCode().equals(EReturnSource.RT_InError) || cr.getsCode().equals(EReturnSource.RT_Timeout)){
//					cr.setSuccess(false);
//					cr.setCode( Return.RT_Source_Error );
//					cr.setInfo("多源认证接口调用失败");
//				}else if( !cr.getsCode().equals(EReturnSource.RT_Success)){
//					cr.setSuccess(false);
//					cr.setCode( Return.RT_Source_Fail );
//					cr.setInfo("多源认证接口调用结果:" + cr.getsInfo());
//				}
//			}
//		}
		return cr;
	}
	
	/**
	 * 多源认证接口服务
	 * @param viBean
	 * @param bean
	 */
	private  void sourceCompare(Map<String,String> compareMap, CompareReturn cr){
		Date beginDate = new Date();
		String resultString = TestIDPhotoAuthService.runQryIDPhoto(compareMap.get("name"),compareMap.get("id_number"), compareMap.get("photo_id"));
		cr.setSholdTime( new Date().getTime() - beginDate.getTime());
		if( !Check.IsStringNULL(resultString)){
			try {
				JSONObject json = new JSONObject(resultString);
				if(json.has("Result") && !Check.IsStringNULL(json.getString("Result")) ){
					cr.setsCode(json.getString("Result"));
				}
				if(json.has("Return") && !Check.IsStringNULL(json.getString("Return")) ){
					cr.setsInfo(json.getString("Return"));
				}
			} catch (JSONException e) {
				Log4jUtil.log.error("解析多源认证接口返回json异常", e);
				cr.setsCode(EReturnSource.RT_InError);
			}
		}else{
			cr.setsCode(EReturnSource.RT_Timeout);
		}
	}
	
	public boolean saveCompareLog(Map<String,String> compareMap, CompareReturn cr){
		return compareDao.saveCompareLog(compareMap, cr);
	}
}
