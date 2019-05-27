/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

import java.util.Map;

import cn.csdb.commons.util.StringKeyMap;

/*
 * ����ص��������ԡ�
 * 
 * @author bluejoe
 */
public class PoolProperties
{
	/**
	 * ÿ����������ʱ��
	 */
	public static final String MAX_ACTIVE_TIME = "max-active-time";

	/**
	 * ÿ�������������ʱ��
	 */
	public static final String MAX_IDLE_TIME = "max-idle-time";

	/**
	 * ��ȡ����ʱ�����ȴ�ʱ��
	 */
	public static final String MAX_WAIT = "max-wait";

	/**
	 * PoolPinger�Ĺ������ʱ��
	 */
	public static final String MONITOR_INTERVAL = "ping-interval";

	/**
	 * ��������������Ŀ
	 */
	public static final String UPPER_SIZE = "upper-size";

	/**
	 * ��ȡ������Ҫ�ȴ�ʱ��ÿ����Ϣ��ʱ��
	 */
	public static final String WAIT_CHIP = "wait-chip";

	public static final String CONNECT_TIMEOUT = "connect-timeout";

	/**
	 * ����صĲ���
	 */
	private Map<String, String> _map = new StringKeyMap<String>();

	public PoolProperties(Map<String, String> pp)
	{
		_map.putAll(pp);
	}

	/**
	 * ��ȡ����ص�����
	 * 
	 * @return
	 */
	public String getName()
	{
		return getProperty("name");
	}

	/**
	 * ��ȡ���еĲ���
	 * 
	 * @return
	 */
	public Map getPropertiesMap()
	{
		return _map;
	}

	/**
	 * ��ȡָ�����ֵĲ���ֵ
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		return _map.get(name);
	}
}
