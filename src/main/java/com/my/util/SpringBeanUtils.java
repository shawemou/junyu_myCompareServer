package com.my.util;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
  
  
/** 
 * ��ȡSpring������Beanʵ���Ĺ�����(Java���ͷ���ʵ��)�� 
 * 
 * @author leiwen@FansUnion.cn 
 */  
@Service  
public class SpringBeanUtils implements BeanFactoryAware {  
  
    private static BeanFactory beanFactory;  
  
    /** 
     * ע��BeanFactoryʵ�� 
     */  
    @Override  
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {  
        SpringBeanUtils.beanFactory = beanFactory;  
    }  
  
    /** 
     * ����bean�����ƻ�ȡ��Ӧ���͵Ķ��� 
     * 
     * @param beanName 
     *            bean������ 
     * @return Object���͵Ķ��� 
     */  
    public static Object getBean(String beanName) {  
        return beanFactory.getBean(beanName);  
    }  
  
    /** 
     * ����bean�����ͻ�ȡ��Ӧ���͵Ķ���û��ʹ�÷��ͣ���ý������Ҫǿ��ת��Ϊ��Ӧ������ 
     * 
     * @param clazz 
     *            bean�����ͣ�û��ʹ�÷��� 
     * @return Object���͵Ķ��� 
     */  
    public static Object getBean(Class clazz) {  
        WebApplicationContext wac = ContextLoader  
                .getCurrentWebApplicationContext();  
        Object bean = wac.getBean(clazz);  
        return bean;  
    }  
  
    /** 
     * ����bean�����ƻ�ȡ��Ӧ���͵Ķ���ʹ�÷��ͣ���ý���󣬲���Ҫǿ��ת��Ϊ��Ӧ������ 
     * 
     * @param clazz 
     *            bean�����ͣ�ʹ�÷��� 
     * @return T���͵Ķ��� 
     */  
    public static <T> T getBean2(Class<T> clazz) {  
        WebApplicationContext wac = ContextLoader  
                .getCurrentWebApplicationContext();  
        T bean = wac.getBean(clazz);  
        return bean;  
    }  
  
}  