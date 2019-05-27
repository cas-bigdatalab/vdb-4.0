/*
 * Created on 2005-8-21
 */
package cn.csdb.commons.sql;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.jdbc.impl.JdbcSourceImpl;

/*
 * JdbcManager is a manager which manages DataSource resources and provides
 * SQLSource wrappers. <p> in general, u may define a DataSource in JNDI
 * context, eg. in resin, u may define a `<resource-ref>` tag in the `web.xml`.
 * <p> DataSource may be defined as a DBPool in your `pool.xml`, too. in fact,
 * `DBPool` implements `DataSource` interface, so every DBPool is a DataSource.
 * <p> every DataSource should have its own name, e.g `jdbc/db` in `web.xml`, or
 * `test1` in `pool.xml`. just call JdbcManager.lookup(dataSourceName) to
 * retrieve it. <p> SQLSource is a wrapper of DataSource, u may call
 * JdbcManager.getJdbcSource(dataSource) to retrieve a JdbcSource of given
 * DataSource. <p>
 * 
 * @author bluejoe
 */
public class JdbcManager
{
	private static JdbcManager _instance;

	private Map<DataSource, JdbcSource> _jdbcSources = new HashMap<DataSource, JdbcSource>();

	private JdbcManager()
	{
	}

	/**
	 * ��ȡΨһʵ����
	 * 
	 * @return
	 */
	static public JdbcManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new JdbcManager();
		}

		return _instance;
	}

	/**
	 * ��������Դ��ȡJdbcSource����
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public JdbcSource getJdbcSource(DataSource dataSource) throws Exception
	{
		if (dataSource == null)
			return null;

		if (_jdbcSources.containsKey(dataSource))
		{
			return (JdbcSource) _jdbcSources.get(dataSource);
		}

		JdbcSourceImpl JdbcSource = new JdbcSourceImpl(dataSource);
		_jdbcSources.put(dataSource, JdbcSource);

		return JdbcSource;
	}

	/**
	 * �������ӳ����ֻ�ȡJdbcSource����
	 * 
	 * dataSourceNameΪDBPool����servlet���������ӳص�����
	 */
	public JdbcSource getJdbcSource(String dataSourceName) throws Exception
	{
		DataSource ds = lookupDataSource(dataSourceName);
		if (ds == null)
			throw new DataSourceNotFound(dataSourceName);

		return getJdbcSource(ds);
	}

	public boolean ifExists(String dataSourceName) throws Exception
	{
		return lookupDataSource(dataSourceName) != null;
	}

	/**
	 * �������ӳ����ֻ�ȡ����Դ
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public DataSource lookupDataSource(String dataSourceName)
	{
		// �ȴ�pool�����ȡ
		DataSource ds = (DataSource) PoolManager.getInstance().getPool(
				dataSourceName);

		if (ds == null)
		{
			// ��context��ȡ��
			try
			{
				Context env = (Context) new InitialContext()
						.lookup("java:comp/env");
				ds = (DataSource) env.lookup(dataSourceName);
			}
			catch (Exception e)
			{
			}
		}

		return ds;
	}

	/**
	 * ��־ĳ������Դ�Ĺ��ڣ��⽫����JdbcSource���¹���JdbcSource����
	 * 
	 * @param dataSource
	 */
	public void invalidate(DataSource dataSource)
	{
		if (dataSource != null)
			_jdbcSources.remove(dataSource);
	}

	/**
	 * ��־ĳ������Դ�Ĺ���
	 * 
	 * @param dataSourceName
	 */
	public void invalidate(String dataSourceName)
	{
		invalidate(lookupDataSource(dataSourceName));
	}
}
