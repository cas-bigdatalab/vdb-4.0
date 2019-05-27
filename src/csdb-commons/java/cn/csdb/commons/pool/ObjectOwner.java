/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

/**
 * ����Ĵ����࣬�������ɡ����ٶ���
 * 
 * @author bluejoe
 */
public interface ObjectOwner
{
	/**
	 * ����һ���µĶ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object createObject() throws Exception;

	/**
	 * ����ָ���Ķ���
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void destroyObject(Object o) throws Exception;

	/**
	 * ��ʼ��
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public void init(PoolProperties pp) throws Exception;

	/**
	 * �ж�ָ�������Ƿ���Ч
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public boolean isObjectAvailable(Object o) throws Exception;
}
