package cn.csdb.commons.sql;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.catalog.JdbcCatalog;
import cn.csdb.commons.sql.dialect.SqlDialect;
import cn.csdb.commons.sql.jdbc.ConnectionHandler;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.Sql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/*
 * DataSource�������࣬JdbcSource��װ�˳��õ����ݿ������
 * 
 * <p> JdbcSource�������ȡDataSource������������Ϣ����JdbcCatalog��
 * �����ͨ������getJdbcCatalog()����ȡ�������ӵ�Ŀ¼��Ϣ�����л�����⡢���ֶε�Ԫ������Ϣ��
 * <p>ʹ��JdbcManager.getJdbcSource()����ȡһ��JdbcSource������ͬ��JdbcSource����ᱻ�ظ�ʹ�á� <p>
 * JdbcSource��װ�˳�����SQL������������¼�Ĳ�ѯ�����ӡ�ɾ�����޸ģ��ȵȡ�
 * ����������Щ���������õĻ��������ʹ��JdbcSource.createStatement()����prepareStatement()
 * ��������ɸ���Ĺ��ܡ����������Щ�������õĻ�����ô���أ� <p>
 * JdbcSource�ṩ��handleConnection(connectionHandler)�������������DataSource�����Ӷ������κ�
 * ���������˹ر��������÷����Ĳ�����һ����Ҫʵ��ConnectionHandler�ӿڵĶ��� <p>
 * JdbcSource����û���ṩgetConnection()�������������Ա�����ֱ�������ӳ�
 * �򽻵�����Ҫ���ľ��ǵ���handleConnection()������JdbcSource��Ϊ��׼�������Ӷ��󣬲�����ʹ�����֮��ر����� <p>
 * JdbcSource�������ѭ����ԭ�� <br> 1.
 * ���ܸ������͵Ĳ������磺���insert/update������������ӽ�ȥһ���㹻��̬���������ͣ�Ʃ��
 * ��һ��BLOB�ֶ������ӽ���һ��File���󣬻�����һ�������ֶ������ӽ���һ��Calendar���ͣ�ֻҪ�п��ܣ�JdbcSource�����跨����������
 * <br> 2. ���ظ������͵Ľ�����磺���query���ص��ֶ�ֵ��JdbcSourceͳһ�˼���sql���ͣ���Ӧ���ı�/
 * ��ֵ/����/�����ƣ�JdbcSource�ֱ𷵻�String/Number/Date/byte[]���ͣ�������ͼ��������Blob����
 * long/boolean����������ת������ȡ�ֶ�ֵ�� <br> 3.
 * �ṩ����ȫ��Ԥ����ƣ�JdbcSource��������ӽ�ȥ���ֶ���ɸѡ������У�飬����㽫"abc"�Ӹ�һ
 * ��int�У������һ���쳣���������и����Ͳ����ڣ�JdbcSource�����Ե����еĸ�ֵ�� <br> 4.
 * �ṩ���꾡���쳣��Ϣ��������ҷ�����SQL�쳣��JdbcSource�ᾡ�����ܣ����쳣��λ�������ֳ��� ���ݿ⡢�������ֶ��ϣ� <p>
 * ��������ĺ����������μ�ÿ��������ע�͡�
 */
public interface JdbcSource
{
	public int countRecords(QuerySql sql) throws Exception;

	public int deleteRecords(String tableName, StringSql whereFilter)
			throws Exception;

	public void executeQuery(QuerySql sql, ResultSetHandler handler)
			throws SQLException;

	public void executeQuery(QuerySql sql, int beginning, int size,
			ResultSetHandler handler) throws SQLException;

	public int executeUpdate(Sql sql) throws Exception;

	public int executeUpdate(Sql sql, StatementHandler handler)
			throws Exception;

	public boolean existsRecord(QuerySql sql) throws Exception;

	public DataSource getDataSource();

	public String getQuotedIdentifier(String idName);

	public SqlDialect getSqlDialect();

	public JdbcCatalog getJdbcCatalog();

	public void handle(ConnectionHandler handler) throws SQLException;

	public int insertRecord(String tableName, Map columns) throws Exception;

	public int insertRecord(String tableName, Map columns,
			StatementHandler handler) throws Exception;

	public Map<String, Serializable> queryForObject(QuerySql sql)
			throws Exception;

	public <T> T queryForObject(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public List<Map<String, Serializable>> queryForObjects(QuerySql sql)
			throws Exception;

	public <T> List<T> queryForObjects(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public List<Map<String, Serializable>> queryForObjects(QuerySql sql,
			int beginning, int size) throws Exception;

	public <T> List<T> queryForObjects(QuerySql sql, int beginning, int size,
			ResultSetReader<T> reader) throws Exception;

	public Pageable<Map<String, Serializable>> createQuery(QuerySql sql)
			throws Exception;

	public <T> Pageable<T> createQuery(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public int updateRecords(String tableName, Map columns,
			StringSql whereFilter) throws Exception;
}