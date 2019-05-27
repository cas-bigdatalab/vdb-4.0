/*
 * �������� 2003-11-12
 * 
 */
package cn.csdb.commons.sequence;

/**
 * ʵ��������Oracle Sequence���ܡ� <br>
 * ͨ������getNextValue()������ȡ��һ������ֵ��
 * 
 * @see SequenceBuilder
 * 
 * @author wkc,bluejoe
 */
public class Sequence
{
	private long current;

	private long end;

	private long beginning;

	private long interval;

	private String sequenceName;

	private SequenceBuilder sb;

	/**
	 * ����Sequence����
	 * 
	 * @param name
	 * @param range
	 */
	public Sequence(SequenceBuilder sb, String name, long beginning,
			long interval)
	{
		this.sb = sb;
		this.sequenceName = name;
		this.beginning = (beginning / interval) * interval;
		this.interval = interval;

		this.current = beginning;
		this.end = beginning + interval;
	}

	/**
	 * ������һ������ֵ������֮��ϵͳ���Զ�����ֵ����Ϊ��ǰ����ֵ long ��һ������ֵ
	 */
	public long getNextValue()
	{
		current++;

		// �ѿ�ʼ��һ����
		if (current > beginning)
		{
			beginning += interval;
			// ��¼
			try
			{
				sb.serialize();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// �����յ�
		if (current == end)
		{
			end += interval;
		}

		return current;
	}

	/**
	 * ���ص�ǰ����ֵ long ����ֵ
	 */
	public long getCurrentValue()
	{
		return current;
	}

	public String getName()
	{
		return sequenceName;
	}

	/**
	 */
	public long getBeginning()
	{
		return beginning;
	}

	/**
	 */
	public long getInterval()
	{
		return interval;
	}

	/**
	 * @param l
	 */
	public void setCurrentValue(long l)
	{
		current = l;
		beginning = (current / interval) * interval;
	}

}
