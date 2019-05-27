/*
 * Created on 2003-5-15
 */
package cn.csdb.commons.jsp;

import java.util.List;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;

/*
 * ����ʵ�����ݼ�¼��ҳ��ʾ��
 * 
 * @author bluejoe
 */
public class SqlPageViewer extends PageViewer
{
	private List _records;

	public SqlPageViewer(PageUrlBuilder url, JdbcSource sqlSource,
			QuerySql sql, int gotoPage, int pageSize) throws Exception
	{
		super(url, sqlSource.countRecords(sql), gotoPage, pageSize);
		_records = sqlSource
				.queryForObjects(sql, this.getBeginning(), pageSize);
	}

	/**
	 * ��ȡ���м�¼
	 * 
	 * @return
	 */
	public List getRecords()
	{
		return _records;
	}
}
