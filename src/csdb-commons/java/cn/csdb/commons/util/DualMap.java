/*
 * Created on 2005-2-16
 */
package cn.csdb.commons.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DualMap������������˫�������ϵ��Map��
 * <p>
 * Ʃ�磺����ר��(E)������(A)֮��Ķ�Զ��ϵ����������DualMap���档
 * �û�����ͨ������line����ĳ��E(i)��A(i)�Ĺ�����ϵ��Ȼ��ͨ��DualMap��
 * getPeers(E(i))����getPeers(A(i))���ɻ�ȡָ������Ĺ��������б�
 * 
 * @author bluejoe
 */
public class DualMap
{
	private Map _map;

	public DualMap()
	{
		_map = new HashMap();
	}

	public void line(Object o1, Object o2)
	{
		Object[] o = { o1, o2 };

		for (int i = 0; i < 2; i++)
		{
			o1 = o[i];
			o2 = o[1 - i];

			Map lm = (Map) _map.get(o1);
			if (lm == null)
			{
				lm = new LinkedHashMap();
				_map.put(o1, lm);
			}

			lm.put(o2, new Object());
		}
	}

	public Map getPeers(Object o)
	{
		return (Map) _map.get(o);
	}
}
