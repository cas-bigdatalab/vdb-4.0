package cn.csdb.commons.pool;

/*
 * �����������ػ��Ķ��� ÿ��ԭʼ���󽫻ᱻ���ӳذ�װ��PooledObject��
 * 
 * @author bluejoe
 */
class PooledObject
{
	/**
	 * �Ƿ����ڴ��ڻ״̬
	 */
	private boolean _active;

	/**
	 * ��������ʱ��
	 */
	private long _maxActiveTime;

	/**
	 * �����������ʱ��
	 */
	private long _maxIdleTime;

	/**
	 * ԭʼ����
	 */
	private Object _rawObject;

	/**
	 * ����ջ��
	 */
	private Throwable _stackPoint;

	/**
	 * ����ʱ���
	 */
	private long _timePoint;

	/**
	 * ����һ���ػ�����
	 * 
	 * @param o
	 * @param properties
	 */
	public PooledObject(Object o, PoolProperties properties)
	{
		_rawObject = o;

		try
		{
			_maxActiveTime = Long.parseLong(properties
					.getProperty(PoolProperties.MAX_ACTIVE_TIME));
		}
		catch (Exception e)
		{
			_maxActiveTime = 300000;
		}

		try
		{
			_maxIdleTime = Long.parseLong(properties
					.getProperty(PoolProperties.MAX_IDLE_TIME));
		}
		catch (Exception e)
		{
			_maxIdleTime = 300000;
		}

		toIdle();
	}

	/**
	 * ��ȡ��ʼ����/���ʱ��
	 * 
	 * @return
	 */
	private long getElapse()
	{
		return System.currentTimeMillis() - _timePoint;
	}

	/**
	 * ��ȡԭʼ����
	 * 
	 * @return
	 */
	public Object getObject()
	{
		return _rawObject;
	}

	/**
	 * ��ȡ��¼�����Ϣ�����ڴ�ӡ��
	 * 
	 * @return
	 */
	public String getStackPoint()
	{
		String sp = "";
		if (_stackPoint != null)
		{
			StackTraceElement[] stes = _stackPoint.getStackTrace();
			if (stes.length >= 3)
			{
				for (int i = 3; i < stes.length; i++)
				{
					sp += "\tat " + stes[i].toString() + "\r\n";
				}
			}
		}

		return sp;
	}

	/**
	 * �ö����Ƿ����ڱ�ʹ�ã�
	 * 
	 * @return
	 */
	public boolean isActive()
	{
		return _active;
	}

	/**
	 * �ö����Ƿ����ڿ��У�
	 * 
	 * @return
	 */
	public boolean isIdle()
	{
		return !_active;
	}

	/**
	 * �ö����Ƿ�ʱ�䱻ʹ�ã�
	 * 
	 * @return
	 */
	public boolean isTooActive()
	{
		return _active && getElapse() > _maxActiveTime;
	}

	/**
	 * �ö����Ƿ�ʱ�䴦�ڿ���״̬��
	 * 
	 * @return
	 */
	public boolean isTooIdle()
	{
		return !_active && getElapse() > _maxIdleTime;
	}

	/**
	 * ���û״̬
	 */
	public void toActive()
	{
		_active = true;
		_timePoint = System.currentTimeMillis();

		_stackPoint = new Throwable();
	}

	/**
	 * ���ÿ���״̬
	 */
	public void toIdle()
	{
		_active = false;
		_timePoint = System.currentTimeMillis();
		_stackPoint = null;
	}
}
