/*
 * �������� 2005-5-9
 */
package cn.csdb.commons.util;

import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;

/**
 * @author bluejoe
 */
public class ConfigurationUtils
{
	public static Map<String, String> loadProperties(Configuration pe)
	{
		return loadProperties(pe, 0);
	}

	/**
	 * ���������Ժ�Ԫ��ֵ����Map
	 * 
	 * @param pe
	 * @param caseSwitch
	 *            ��Сдģʽ��0-������Сд 1-ȫСд 2-ȫ��д
	 * @return
	 */
	public static Map<String, String> loadProperties(Configuration pe,
			int caseSwitch)
	{
		Map<String, String> props = new StringKeyMap<String>();

		// ����
		String[] names = pe.getAttributeNames();
		for (int i = 0; i < names.length; i++)
		{
			try
			{
				props.put(caseSwitch == 0 ? names[i]
						: (caseSwitch == 1 ? names[i].toLowerCase() : names[i]
								.toUpperCase()), pe.getAttribute(names[i]));
			}
			catch (Exception e)
			{
				continue;
			}
		}

		// Ԫ��
		Configuration[] elements = pe.getChildren();
		for (int i = 0; i < elements.length; i++)
		{
			try
			{
				Configuration element = (Configuration) elements[i];

				if (element.getAttributeNames().length > 0)
					continue;

				if (element.getChildren().length > 0)
					continue;

				String nodeName = element.getName();

				props.put(caseSwitch == 0 ? nodeName
						: (caseSwitch == 1 ? nodeName.toLowerCase() : nodeName
								.toUpperCase()), element.getValue());
			}
			catch (Exception e)
			{
				continue;
			}
		}

		return props;
	}
}
