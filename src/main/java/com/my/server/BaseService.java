package com.my.server;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public abstract class BaseService<T> {
	//Spring4�ķ���ע��
	@Autowired
	private Mapper<T> mapper;
	
	public Class<T> clazz;
	
	{
		ParameterizedType type =  (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	/**
	 * 1,��������id��ѯ����
	 */
	public T queryById(Long id) {
		return this.mapper.selectByPrimaryKey(id);
	}

	/**
	 * 2,��ѯ��������
	 */
	public List<T> queryAll() {
		return this.mapper.select(null);
	}

	/**
	 * 3,����������ѯһ������
	 */
	public T queryOne(T record) {
		return this.mapper.selectOne(record);
	}

	/**
	 * 4,����������ѯ��������
	 */
	public List<T> queryListByWhere(T record) {
		return this.mapper.select(record);
	}

	/**
	 * 5,����������ҳ��ѯ��������
	 */
	public PageInfo<T> queryPageListByWhere(T record, Integer pageNum,
			Integer pageSize) {
		// ���÷�ҳ����
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = this.mapper.select(record);
		return new PageInfo<T>(list);
	}
	
	/**
	 * 5.1��ҳ��ѯ������
	 */
	public PageInfo<T> queryPageListOrderBy(String orderByClause, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Example example = new Example(clazz);
		example.setOrderByClause(orderByClause);
		List<T> list = this.mapper.selectByExample(example);
		return new PageInfo<>(list);
	}

	/**
	 * 6,��������
	 */
	public Integer save(T record){
		return this.mapper.insert(record);
	}
	
	/**
	 *7, ��������(ѡ��Ϊnull���ֶ�,�������ݿ�
	 */
	public Integer saveSelective(T record){
		return this.mapper.insertSelective(record);
	}
	
	/**
	 * 8,����������������
	 */
	public Integer Update(T record){
		return this.mapper.updateByPrimaryKey(record);
	}
	
	/**
	 * 9, �������ݣ�ѡ��Ϊnull���ֶΣ����в�����
	 * @param record
	 * @return
	 */
	public Integer updateSelective(T record){
		return this.mapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 10,��������ɾ������
	 */
	public Integer deleteById(T key){
		return  this.mapper.delete(key);
	}
	
	/**
	 * 11,����ɾ��
	 */
	public Integer deleteByIds(List<Object> ids, String property){
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, ids);
		return this.mapper.deleteByExample(example);
	}
	
	/**
	 * 12,��������ɾ������
	 */
	public Integer  deleteByWhere(T record){
		return this.mapper.delete(record);
	}


}
