/*
 * Created on 2005-7-29
 */
package cn.csdb.commons.pool;

/**
 * Pool������������صĽӿڡ� ������pool.xml�����pool������ʵ��Pool�ӿڡ�
 * 
 * @see PoolManager
 * @author bluejoe
 */
public interface Pool
{
	/**
	 * �ṩ������߳�ʹ��
	 * 
	 * @throws Exception
	 */
	public abstract void ping() throws Exception;

	/**
	 * ��ն���أ��������ж���
	 */
	public abstract void destroy() throws Exception;

	/**
	 * ��ȡ�ص�����
	 * 
	 * @return
	 */
	public abstract PoolProperties getProperties();

	/**
	 * ��ʼ���ö����
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public abstract void init(PoolProperties pp) throws Exception;
}