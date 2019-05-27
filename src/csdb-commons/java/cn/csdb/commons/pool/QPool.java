/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

import java.nio.channels.ClosedByInterruptException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Level;

import cn.csdb.commons.util.ObjectProxy;

/*
 * Pool�ӿڵ�һ��ʵ�֣����������ͨ��pooling���ơ�
 * 
 * @author bluejoe
 */
public class QPool implements Pool
{
	/**
	 * ���ж���Map����ΪObject��ֵΪPooledObject
	 */
	private Map _allObjects = new HashMap();

	private long _connectTimeout;

	private static long _seq = 0;

	private ObjectOwner _holder;

	/**
	 * ���ö���ջ��ÿһ��ΪPooledObject
	 */
	private Stack _idleObjects = new Stack();

	/**
	 * ���еȴ�ʱ������
	 */
	private long _maxWait;

	/**
	 * ���������
	 */
	private String _name;

	private PoolProperties _properties;

	private long _requests = 0;

	private long _tryTimes;

	/**
	 * ���������
	 */
	private int _upperSize;

	private long _waitChip;

	/**
	 * ��ʼ������
	 * 
	 * @param creator
	 *            ���ö����creator
	 * @throws Exception
	 */
	public QPool(ObjectOwner holder) throws Exception
	{
		_holder = holder;
	}

	/**
	 * ���ٶ����
	 */
	public void destroy() throws Exception
	{
		Iterator it = _allObjects.entrySet().iterator();
		while (it.hasNext())
		{
			PooledObject po = (PooledObject) ((Map.Entry) it.next()).getValue();
			try
			{
				_holder.destroyObject(po.getObject());
			}
			catch (Exception e)
			{
			}
		}

		_idleObjects.clear();
		_allObjects.clear();
	}

	protected void finalize() throws Throwable
	{
		destroy();
		super.finalize();
	}

	/**
	 * ��������
	 * 
	 * @param o
	 */
	public void freeObject(Object o) throws Exception
	{
		if (o == null)
		{
			return;
		}

		synchronized (this)
		{
			PooledObject po = (PooledObject) _allObjects.get(o);

			// ��������������Դ�����ѱ�ʶɾ���Ķ���
			if (po == null)
			{
				return;
			}

			_idleObjects.push(po);
			po.toIdle();
		}

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]<--[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));
	}

	public int getIdleCount()
	{
		return _idleObjects.size();
	}

	/**
	 * ��ȡ���ж���
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object getObject() throws Throwable
	{
		Object o = null;

		_requests++;
		if (_requests > 1000000L)
		{
			_requests = 0;
		}

		long requestID = _requests;
		long i = 0;

		while (true)
		{
			// ��ȡ������
			if (o != null)
			{
				// �����ã���ɾ��֮
				if (!_holder.isObjectAvailable(o))
				{
					PoolManager
							.getInstance()
							.getLogger()
							.log(
									Level.ALL,
									MessageFormat
											.format(
													"[{0}][{1,number,000000}]: found closed object and then removed it",
													new Object[] { _name,
															new Long(requestID) }));

					// ɾ���ö���
					destroyObject(o);
					o = null;
				}
				// ���ã�����ѭ��
				else
				{
					break;
				}
			}

			// �п��ж���
			synchronized (this)
			{
				if (!_idleObjects.isEmpty())
				{
					// ȡ���ö���
					PooledObject po = (PooledObject) _idleObjects.pop();
					po.toActive();
					o = po.getObject();

					// �ö������������ȵ���
					if (i > 0)
					{
						PoolManager
								.getInstance()
								.getLogger()
								.log(
										Level.ALL,
										MessageFormat
												.format(
														"[{0}][{1,number,000000}]: got available object after {2} tries ({3}ms).",
														new Object[] {
																_name,
																new Long(
																		requestID),
																new Long(i),
																new Long(
																		i
																				* _waitChip) }));
					}
				}
				// �޿��ж���
				else
				{
					// ������������
					if (_allObjects.size() < _upperSize)
					{
						createObject();
						continue;
					}
					// �������������˵ȴ�����������
					else
					{
						// ��ʼ�ȴ�
						if (i == 0)
						{
							PoolManager
									.getInstance()
									.getLogger()
									.log(
											Level.ALL,
											MessageFormat
													.format(
															"[{0}][{1,number,000000}]: no more object available, waiting...",
															new Object[] {
																	_name,
																	new Long(
																			requestID) }));
						}

						// �ȴ�һ��ʱ�俴��
						wait(_waitChip);
						i++;

						// �ȴ���ʱ
						if (_maxWait > 0 && i >= _tryTimes)
						{
							String msg = MessageFormat
									.format(
											"[{0}][{1,number,000000}]: unable to retrieve available object in limited time ({2}ms)!",
											new Object[] { _name,
													new Long(requestID),
													new Long(_maxWait) });

							PoolManager.getInstance().getLogger().error(msg);
							throw new Exception(msg);
						}
					}
				}
			}
		}

		PoolManager
				.getInstance()
				.getLogger()
				.log(
						Level.ALL,
						MessageFormat
								.format(
										"[{0}][{1,number,000000}]-->[{2}], idle: {3}, total: {4}",
										new Object[] {
												_name,
												new Long(requestID),
												o.toString(),
												new Integer(_idleObjects.size()),
												new Integer(_allObjects.size()) }));

		return o;
	}

	public int getPooledCount()
	{
		return _allObjects.size();
	}

	public PoolProperties getProperties()
	{
		return _properties;
	}

	/**
	 * �����������µĶ���
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object createObject() throws Throwable
	{
		// ���������Ŀ
		if (_allObjects.size() >= _upperSize)
			return null;

		Object o;

		try
		{
			o = createObjectInTime();
		}
		catch (Exception e)
		{
			String msg = MessageFormat.format(
					"[{0}]: failed to create new object due to {1}",
					new Object[] { _name, e.getMessage() });

			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg, e);
		}

		PooledObject po = new PooledObject(o, _properties);

		_idleObjects.push(po);
		_allObjects.put(o, po);

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]<-+[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));

		return null;
	}

	private Object createObjectInTime() throws Exception, InterruptedException,
			Throwable, TimeoutException
	{
		final ObjectProxy<Object> op = new ObjectProxy<Object>();
		final ObjectProxy<Throwable> tp = new ObjectProxy<Throwable>();
		class Lock
		{

		}

		final Object lock = new Lock();

		final ObjectOwner finalHolder = _holder;
		Thread threadObjectCreation = null;

		threadObjectCreation = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					//�����������
					//�ú����Ǹ���������
					op.setObject(finalHolder.createObject());
				}
				catch (ClosedByInterruptException e)
				{
					System.err.println(e.getMessage());
				}
				catch (InterruptedException e)
				{
					System.err.println(e.getMessage());
				}
				catch (Throwable e)
				{
					e.printStackTrace();
					tp.setObject(e);
				}

				//����
				synchronized (lock)
				{
					lock.notify();
				}
			}
		}, "create-object-thread-" + _seq);

		_seq++;

		threadObjectCreation.start();
		synchronized (lock)
		{
			lock.wait(_connectTimeout);
		}

		Throwable t = tp.getObject();
		//�����������
		if (t != null)
		{
			throw t;
		}

		//�����ǳ�ʱ
		Object o = op.getObject();
		if (o == null)
		{
			try
			{
				if (threadObjectCreation != null
						&& threadObjectCreation.isAlive())
					threadObjectCreation.interrupt();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			throw new TimeoutException(_connectTimeout);
		}

		return o;
	}

	/**
	 * ��ʼ���������
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public void init(PoolProperties pp) throws Exception
	{
		_properties = pp;
		_holder.init(pp);
		_name = pp.getName();

		try
		{
			_maxWait = Long.parseLong(pp.getProperty(PoolProperties.MAX_WAIT));
		}
		catch (NumberFormatException e)
		{
			_maxWait = 2000;
		}

		try
		{
			_upperSize = Integer.parseInt(pp
					.getProperty(PoolProperties.UPPER_SIZE));
		}
		catch (NumberFormatException e)
		{
			_upperSize = 20;
		}

		try
		{
			_waitChip = Integer.parseInt(pp
					.getProperty(PoolProperties.WAIT_CHIP));
		}
		catch (NumberFormatException e)
		{
			_waitChip = 100;
		}

		try
		{
			_connectTimeout = Long.parseLong(pp
					.getProperty(PoolProperties.CONNECT_TIMEOUT));
		}
		catch (Exception e)
		{
			_connectTimeout = 60000;
		}

		if (_waitChip == 0)
			_waitChip = 100;

		_tryTimes = _maxWait / _waitChip;
	}

	public void ping() throws Exception
	{
		List toBeRemoved = new Vector();
		Iterator it = _allObjects.entrySet().iterator();
		while (it.hasNext())
		{
			PooledObject po = (PooledObject) ((Map.Entry) it.next()).getValue();
			// ǿ�ƶ�س�ʱ��δ�����Ķ��󣬲������¼��
			Object o = po.getObject();
			if (po.isTooActive())
			{
				toBeRemoved.add(o);
				PoolManager
						.getInstance()
						.getLogger()
						.log(
								Level.ALL,
								MessageFormat
										.format(
												"[{0}]: found object [{1}] which was in use for too long time\r\n{2}",
												new Object[] { _name, o,
														po.getStackPoint() }));
			}

			// ǿ�ƹرճ�ʱ��δʹ�õĶ���
			if (po.isTooIdle())
			{
				toBeRemoved.add(o);
				PoolManager
						.getInstance()
						.getLogger()
						.log(
								Level.ALL,
								MessageFormat
										.format(
												"[{0}]: found object [{1}] which was on idle for too long time",
												new Object[] { _name, o }));
			}
		}

		for (int i = 0; i < toBeRemoved.size(); i++)
		{
			destroyObject(toBeRemoved.get(i));
		}
	}

	/**
	 * ɾ��ָ������
	 * 
	 * @param o
	 */
	private void destroyObject(Object o) throws Exception
	{
		_holder.destroyObject(o);

		synchronized (this)
		{
			PooledObject po = (PooledObject) _allObjects.get(o);
			_idleObjects.remove(po);
			_allObjects.remove(o);
		}

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]x->[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));
	}

	public long getConnectTimeout()
	{
		return _connectTimeout;
	}

	public void setConnectTimeout(long connectTimeout)
	{
		_connectTimeout = connectTimeout;
	}
}