/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.log4j.Logger;

import cn.csdb.commons.util.ConfigurationUtils;
import cn.csdb.commons.util.StringKeyMap;

/*
 * ����صĹ����࣬���Թ���һ����������ء� �������������ʹ��addPool()�����һ������أ�Ҳ���Ե���loadFromXML()
 * ��һ��pool.xml�����ļ��ж�ȡ������������ء�
 * 
 * @author bluejoe
 */
public class PoolManager
{
	private static PoolManager _instance;

	/**
	 * ȱʡPool����ֵ
	 */
	private Map<String, String> _defaultPoolAttributes = new StringKeyMap<String>();

	private Map<String, Exception> _exceptions = new StringKeyMap<Exception>();

	private long _pingInterval = 60000;

	private PoolPinger _poolPinger;

	private Map<String, Pool> _pools = new StringKeyMap<Pool>();

	private PoolManager()
	{
		_defaultPoolAttributes.put(PoolProperties.UPPER_SIZE, "20");
		_defaultPoolAttributes.put(PoolProperties.MAX_WAIT, "2000");

		_defaultPoolAttributes.put(PoolProperties.MONITOR_INTERVAL, "60000");
		_defaultPoolAttributes.put(PoolProperties.WAIT_CHIP, "100");
		_defaultPoolAttributes.put(PoolProperties.MAX_ACTIVE_TIME, "300000");
		_defaultPoolAttributes.put(PoolProperties.MAX_IDLE_TIME, "300000");

		_poolPinger = new PoolPinger(this);
		_poolPinger.start();
	}

	/**
	 * ����PoolManager���󣬸÷���ȷ��ϵͳ��ֻ����һ��PoolManagerʵ����
	 * 
	 * @throws Exception
	 */
	static public synchronized PoolManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new PoolManager();
		}

		return _instance;
	}

	/**
	 * ���һ������أ���������ָ������صĲ�����
	 * 
	 * @param props
	 *            ���ش����Ķ���ض���
	 */
	public Pool addPool(Map<String, String> props) throws Exception
	{
		Map<String, String> allProps = new StringKeyMap<String>();

		// ���ȱʡ����
		allProps.putAll(_defaultPoolAttributes);
		allProps.putAll(props);
		PoolProperties pp = new PoolProperties(allProps);

		try
		{
			_exceptions.remove(pp.getName());
			return addPool(pp);
		}
		catch (Exception e)
		{
			_exceptions.put(pp.getName(), e);
			throw e;
		}
	}

	/**
	 * ���ݲ������������
	 * 
	 * @param props
	 *            ���ش����Ķ���ض���
	 * @throws Exception
	 */
	private Pool addPool(PoolProperties pp) throws Exception
	{
		// �������ʹ������ʵ�Pool
		String poolName = pp.getProperty("name");
		if (poolName == null)
		{
			return null;
		}

		if (_pools.containsKey(poolName))
		{
			// throw new Exception("Pool `" + poolName + "` already exists!");
		}

		String poolClassName = pp.getProperty("class");
		if (poolClassName == null)
		{
			throw new Exception(MessageFormat.format(
					"unknown pool class, `class` must not be null",
					new Object[] {}));
		}

		Class poolClass = Class.forName(poolClassName);

		Pool cp = null;
		try
		{
			cp = (Pool) poolClass.newInstance();
		}
		catch (ClassCastException e)
		{
			throw new Exception(MessageFormat.format(
					"mismatched pool class, `{0}` should implement `{1}`",
					new Object[] { poolClass.getName(), Pool.class.getName() }));
		}

		cp.init(pp);

		// ׷��������Map
		removePool(poolName);
		_pools.put(poolName, cp);

		return cp;
	}

	/**
	 * ��dom��Ϣ�ж�ȡ������Ϣ���������ж���ء�
	 * 
	 * @throws Exception
	 */
	private int addPools(Configuration conf) throws Exception
	{
		int count = 0;

		// ��ȡpoolȱʡ����
		Configuration pse = conf.getChild("pools");
		if (pse != null)
		{
			Configuration[] cs = pse.getChildren();
			for (int i = 0; i < cs.length; i++)
			{
				Configuration c = cs[i];
				String nodeName = c.getName().toLowerCase();
				if (!"pool".equals(nodeName))
				{
					// ����ȱʡֵ������defaultPoolAttributes
					_defaultPoolAttributes.put(nodeName, c.getValue());
				}
			}

			// ������pool
			cs = pse.getChildren("pool");
			for (int i = 0; i < cs.length; i++)
			{
				Configuration c = cs[i];

				try
				{
					addPool(ConfigurationUtils.loadProperties(c));
					count++;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Logger.getLogger(this.getClass()).error(e);
					continue;
				}
			}
		}

		return count;
	}

	/**
	 * �������ж�����ն����
	 */
	public void destroy()
	{
		removePools();
		_poolPinger.stopRunning();

		_instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		destroy();
		super.finalize();
	}

	/**
	 * ���ص�һ�����ӵ�����
	 * 
	 * @return
	 */
	public String getDefaultPoolName()
	{
		Iterator it = _pools.keySet().iterator();
		if (it.hasNext())
		{
			return (String) it.next();
		}

		return null;
	}

	/**
	 * ��ȡping�ļ��ʱ�䡣
	 * 
	 * @return
	 */
	public long getPingInterval()
	{
		return _pingInterval;
	}

	/**
	 * ���ָ�����ֵĶ���ء�
	 * 
	 * @param name
	 *            ���������
	 * @return ����أ����û�У�����null
	 */
	public Pool getPool(String name)
	{
		// ���쳣����
		if (_exceptions.containsKey(name))
		{
			throw new RuntimeException(MessageFormat.format(
					"error happened while building pool `{0}`",
					new Object[] { name }), (Throwable) _exceptions.get(name));
		}

		return (Pool) _pools.get(name);
	}

	/**
	 * ������еĶ���ء�
	 * 
	 * @return ���еĶ���أ�Map�ļ���Ϊpool�����֣���ֵΪpool����
	 */
	public Map getPools()
	{
		return _pools;
	}

	/**
	 * ��ȡ��ǰ�汾�š�
	 * 
	 * @return
	 */
	public String getVersion()
	{
		return "2.4(build 20050730)";
	}

	public int loadFromResource(String xmlPathInClassPath) throws Exception
	{
		URL url = this.getClass().getClassLoader().getResource(
				xmlPathInClassPath);
		if (url == null)
		{
			throw new Exception(MessageFormat.format(
					"failed to find file `{0}` in class path",
					new Object[] { xmlPathInClassPath }));
		}

		// ����url��ȡ��ǰ��·��
		String classPath;

		// file:/C:/Program%20Files/Java/j2re1.4.1_01/lib/rt.jar!/csdb-support/.version
		// file:/home/aocl/resin-2.1.13/webapps/codata/WEB-INF/lib/csdb-tags(1).jar!/csdb-support/.version
		String resourcePath = url.getFile();
		int i = resourcePath.indexOf(".jar!");
		if (i < 0)
		{
			classPath = resourcePath;
		}
		else
		{
			classPath = resourcePath.substring(0, i);
		}

		i = classPath.indexOf("file:");
		if (i >= 0)
			classPath = classPath.substring(i + 5);

		return loadFromXML(url.openStream(), URLDecoder.decode(classPath,
				"gb2312"));
	}

	/**
	 * �������ļ��ж�ȡ������Ϣ�� ����LOG��־�͵�����Ϣ�����λ�����Լ����Զ����ˢ��Ƶ�ʵȡ�
	 */
	public int loadFromXML(InputStream is, String logDir) throws Exception
	{
		// ��ȡxml�ļ�
		Configuration conf;

		Configuration root = new DefaultConfigurationBuilder().build(is);
		is.close();

		conf = root.getChild("pool-config");
		return addPools(conf);
	}

	public synchronized int loadFromXML(String xmlPath) throws Exception
	{
		File xmlFile = new File(xmlPath);
		if (!xmlFile.exists())
		{
			throw new Exception(MessageFormat.format(
					"failed to find file `{0}`", new Object[] { xmlPath }));
		}

		return loadFromXML(new FileInputStream(xmlFile), xmlFile.getParent());
	}

	/**
	 * ɾ��ָ������ء�
	 * 
	 * @param poolName
	 *            ���������
	 */
	public void removePool(String poolName)
	{
		try
		{
			Pool pool = (Pool) _pools.get(poolName);
			if (pool != null)
				pool.destroy();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		_exceptions.remove(poolName);
		_pools.remove(poolName);
	}

	/**
	 * ɾ�����еĶ���ء�
	 */
	public void removePools()
	{
		Iterator it = _pools.entrySet().iterator();
		while (it.hasNext())
		{
			try
			{
				Pool pool = (Pool) ((Map.Entry) (it.next())).getValue();
				pool.destroy();
			}
			catch (Exception e)
			{
				continue;
			}
		}

		_exceptions.clear();
		_pools.clear();
	}

	public Logger getLogger()
	{
		return Logger.getLogger(PoolManager.class);
	}
}
