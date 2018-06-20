package com.my.common;

public class EnumInstance {
	
	public final static class Return {
		public final static String RT_Success = "1"; // 比对通过
		public final static String RT_Fail = "0";// 系统判断为不同人
		public final static String RT_Not_Compare = "-1";//未比对（无人脸，多张人脸，服务器错误，链接超时）
		
		public final static String RT_Source_Fail = "-2";//多源认证接口调用结果（不为同一人，身份证号码不存在，姓名身份证不匹配）
		public final static String RT_Source_Error = "-3";//多源认证接口调用失败
	}
	
	public final static class EReturn {
		// 成功
		public final static String RT_Success = "0"; // 成功,是同一人
		public final static String RT_Unsure = "1";// 无法判定
		public final static String RT_Fail = "2";// 不是同一人
		
		public final static String RT_Unqualified = "-3";// 照片质量不合格
		public final static String RT_Fail_IDPhoto = "-4";// 二代身份证照片不合格(原来是-8)
		public final static String RT_Fail_Feature = "-2";// 提取特征码失败
		public final static String RT_Fail_Feature_IdPhoto = "-6";// 提取证件照特征值失败
		public final static String RT_NotMatch_Format_IDPhoto = "-7";// 1张证件照解码失败
		public final static String RT_NotMatch_Format_Photo = "-8";// 多张抓拍照解码失败
		public final static String RT_More_Face_IDPhoto = "-9";// 1张证件照检测到多张人脸
		public final static String RT_More_Face_Photo = "-10";// 多张抓拍照检测到多张人脸
		
		public final static String RT_InError = "-1300";// 服务器内部错误
		public final static String RT_Timeout = "-1301";// 远程连接超时错误
		
		public final static String RT_NotMount = "12002";//服务未挂载
	}
	
	//多源认证接口返回码
	public final class EReturnSource {
		public final static String RT_Success = "0"; // 成功,是同一人
		
		public final static String RT_InError = "-1400";// 服务器内部错误
		public final static String RT_Timeout = "-1401";// 远程连接超时错误
	}
}
