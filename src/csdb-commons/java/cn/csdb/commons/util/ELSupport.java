/*
 * �������� 2005-1-12
 */
package cn.csdb.commons.util;

/**
 * �������������������Ե����ԡ�
 * 
 * @author bluejoe
 */
public interface ELSupport
{
	/**
	 * ������������ȡ��Ӧ���͵�����ֵ�� ���������ָ�����������򷵻�null�� ���Է�����ֵ��ʱ�䡢�ı����͵�����ֵ��
	 * 
	 * @param attributeName
	 */
	public Object get(String key);

	/**
	 * ����ָ�������ԡ� �����֧�ָ�����ֵ�����ã��򷵻�false��
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public Object set(Object key, Object value);
}
