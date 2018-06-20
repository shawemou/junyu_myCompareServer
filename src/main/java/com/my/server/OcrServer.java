package com.my.server;

import java.lang.reflect.Proxy;
import java.security.MessageDigest;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.codehaus.xfire.transport.http.EasySSLProtocolSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.my.common.OcrReturn;
import com.my.common.ReadSetting;
import com.my.dao.OCRDao;
import com.my.util.Check;
import com.my.util.Log4jUtil;

/**
 * @ClassName: OcrServer 
 * @Description: 身份证OCR服务 
 *
 */
@org.springframework.stereotype.Service
public class OcrServer {
	public static long TIME_OUT_CONNECT = 2000L;
	public static int TIME_OUT_Default = 1000 * 60;
	
	@Autowired
	private OCRDao orcDao;
	
	/**
	 * 发起服务方法
	 * @param ocrMap
	 * @param or
	 * @return
	 */
	public  OcrReturn ocr(Map<String,String> ocrMap, OcrReturn or){
		JSONObject object = new JSONObject();
		try {
			object.put("front_photo", ocrMap.get("id_photo"));
			object.put("head_option",0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String strReturn = ocr(object);
		Log4jUtil.log.warn(strReturn);
		
		or.setSuccess(false);
		
		if(!Check.IsStringNULL(strReturn)){
			try {
				JSONObject json = new JSONObject(strReturn);
				String code = String.valueOf(json.get("code"));
				
				if(!Check.IsStringNULL(code) && code.equals("0")){
					json = new JSONObject(String.valueOf(json.get("data")));
					or.setName(json.getString("name"));
					or.setId_number(json.getString("certid"));
					or.setSuccess(true);
					or.setInfo("身份证OCR识别成功");
				}else{
					or.setInfo("身份证OCR识别失败");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				or.setInfo("身份证OCR识别失败,数据解析异常");
			}
		}else{
			or.setInfo("身份证OCR识别失败,访问OCR接口异常");
		}
		return or;
	}
	
	public boolean saveOCRLog(Map<String,String> loginMap, OcrReturn or){
		return orcDao.saveOCRLog(loginMap, or);
	}
	
	/**
	 * 调用ocr，Webservice接口
	 * @param object
	 * @return
	 */
	public  String ocr(JSONObject object) {
		String result = null;
		try{
			String connection_timeout = ReadSetting.getInstance().getConnection_timeout();
			TIME_OUT_CONNECT = Long.valueOf(connection_timeout);
		}catch (Exception e) {
			Log4jUtil.log.error("ocr接口访问链接超时配置错误", e);
		}
		try{
			String so_timeout = ReadSetting.getInstance().getSo_timeout();
			TIME_OUT_Default = Integer.valueOf(so_timeout);
		}catch (Exception e) {
			Log4jUtil.log.error("ocr接口访问读取超时配置错误", e);
		}
		
		try {
			// 此为支持Xfire HTTPS的代码实现
			ProtocolSocketFactory easy = new EasySSLProtocolSocketFactory();
			Protocol protocol = new Protocol("https", easy, 443);
			Protocol.registerProtocol("https", protocol);

			Service service = new ObjectServiceFactory().create(IJYOcrWebservice.class);
			XFire xfire = XFireFactory.newInstance().getXFire();
			XFireProxyFactory factory = new XFireProxyFactory(xfire);
			IJYOcrWebservice client = (IJYOcrWebservice) factory.create(service, ReadSetting.getInstance().getService_url());

			Client xfireClient = ((XFireProxy) Proxy.getInvocationHandler(client)).getClient();
			HttpClientParams params = new HttpClientParams();
			//http client的重试次数由默认3改为禁用
			params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			//关闭长连接
			params.setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, Boolean.FALSE);
			//设置连接超时时间
			
			params.setLongParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, TIME_OUT_CONNECT);  
			//设置读取超时时间
			params.setIntParameter(HttpClientParams.SO_TIMEOUT, TIME_OUT_Default);
			
			xfireClient.setProperty(CommonsHttpMessageSender.DISABLE_KEEP_ALIVE, "1");
			xfireClient.setProperty(CommonsHttpMessageSender.DISABLE_EXPECT_CONTINUE, "1");
			xfireClient.setProperty(CommonsHttpMessageSender.HTTP_CLIENT_PARAMS, params);
			
			String strLicenseCode = ReadSetting.getInstance().getLicense();
			String strAppSecret = ReadSetting.getInstance().getSecret_key();
			result = client.ocr(object.toString(), strLicenseCode,
					string2MD5(object.toString() + strLicenseCode + strAppSecret));
		} catch (Exception e) {
			e.printStackTrace();
			Log4jUtil.log.error("ocr接口访问异常", e);
		}
		return result;
	}
	
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)// 如果第一个字符ascii码小于16的话 那么它转16进制的话会忽略掉前面的0
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	interface IJYOcrWebservice {
		public String ocr(String strJsonIn,String strLicenseCode,String strEncryValue);
	}
}
