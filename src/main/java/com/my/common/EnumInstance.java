package com.my.common;

public class EnumInstance {
	
	public final static class Return {
		public final static String RT_Success = "1"; // �ȶ�ͨ��
		public final static String RT_Fail = "0";// ϵͳ�ж�Ϊ��ͬ��
		public final static String RT_Not_Compare = "-1";//δ�ȶԣ��������������������������������ӳ�ʱ��
		
		public final static String RT_Source_Fail = "-2";//��Դ��֤�ӿڵ��ý������Ϊͬһ�ˣ����֤���벻���ڣ��������֤��ƥ�䣩
		public final static String RT_Source_Error = "-3";//��Դ��֤�ӿڵ���ʧ��
	}
	
	public final static class EReturn {
		// �ɹ�
		public final static String RT_Success = "0"; // �ɹ�,��ͬһ��
		public final static String RT_Unsure = "1";// �޷��ж�
		public final static String RT_Fail = "2";// ����ͬһ��
		
		public final static String RT_Unqualified = "-3";// ��Ƭ�������ϸ�
		public final static String RT_Fail_IDPhoto = "-4";// �������֤��Ƭ���ϸ�(ԭ����-8)
		public final static String RT_Fail_Feature = "-2";// ��ȡ������ʧ��
		public final static String RT_Fail_Feature_IdPhoto = "-6";// ��ȡ֤��������ֵʧ��
		public final static String RT_NotMatch_Format_IDPhoto = "-7";// 1��֤���ս���ʧ��
		public final static String RT_NotMatch_Format_Photo = "-8";// ����ץ���ս���ʧ��
		public final static String RT_More_Face_IDPhoto = "-9";// 1��֤���ռ�⵽��������
		public final static String RT_More_Face_Photo = "-10";// ����ץ���ռ�⵽��������
		
		public final static String RT_InError = "-1300";// �������ڲ�����
		public final static String RT_Timeout = "-1301";// Զ�����ӳ�ʱ����
		
		public final static String RT_NotMount = "12002";//����δ����
	}
	
	//��Դ��֤�ӿڷ�����
	public final class EReturnSource {
		public final static String RT_Success = "0"; // �ɹ�,��ͬһ��
		
		public final static String RT_InError = "-1400";// �������ڲ�����
		public final static String RT_Timeout = "-1401";// Զ�����ӳ�ʱ����
	}
}
