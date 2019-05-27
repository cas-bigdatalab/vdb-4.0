/*
 * �������� 2005-1-5
 */
package cn.csdb.commons.sequence;

import java.io.IOException;

/**
 * ���Դ���Sequence�Ľӿڡ�
 * 
 * @author wkc,bluejoe
 */
public interface SequenceBuilder
{
	static public final int ACTION_INSERT = 0;

	static public final int ACTION_UPDATE = 1;

	static public final int ACTION_DELETE = 2;

	/**
	 * ���һ��sequence��
	 * 
	 * @param sequence
	 */
	public abstract void addSequence(Sequence sequence);

	/**
	 * ����һ��sequence��
	 * 
	 * @param sequence
	 */
	public abstract void setSequence(Sequence sequence);

	/**
	 * ɾ��һ��sequence��
	 * 
	 * @param sequence
	 */
	public abstract void removeSequence(Sequence sequence);

	/**
	 * ������е�Sequence
	 */
	public abstract void removeAll();

	/**
	 * ��ȡһ��sequence
	 * 
	 * @param name
	 */
	public abstract Sequence getSequence(String sequenceName);

	/**
	 * ���sequence���ϵ����л�
	 * 
	 * @throws IOException
	 */
	public void serialize() throws IOException;
}