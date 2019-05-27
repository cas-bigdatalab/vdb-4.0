/*
 * Created on 2006-6-23
 */
package cn.csdb.commons.util;

/**
 * ��Ҫ����final object�������
 * 
 * @author bluejoe
 */
public class ObjectProxy<T>
{
	private T _innerObject;

	public T getObject()
	{
		return _innerObject;
	}

	public ObjectProxy(T o)
	{
		_innerObject = o;
	}

	public ObjectProxy()
	{
		_innerObject = null;
	}

	public void setObject(T o)
	{
		_innerObject = o;
	}
}
