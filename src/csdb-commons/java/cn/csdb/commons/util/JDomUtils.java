/*
 * �������� 2005-5-9
 */
package cn.csdb.commons.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * @author bluejoe
 */
public class JDomUtils
{

	/**
	 * �ݹ��ȡdom�ڵ�
	 */
	public static Element getRecursiveNode(Document document,
			String abstractPath)
	{
		int depth = 0;
		// �ֽ�·��
		StringTokenizer st = new StringTokenizer(abstractPath, "/");
		Element e = null;

		while (st.hasMoreTokens())
		{
			String nodeName = st.nextToken();
			if (nodeName.length() == 0)
				continue;

			// �����
			if (depth == 0)
			{
				if (!document.hasRootElement())
				{
					e = new Element(nodeName);
					document.setRootElement(e);
				}
				else
				{
					e = document.getRootElement();
				}
			}
			else
			{
				Element pe = e;
				e = pe.getChild(nodeName);
				// �жϸýڵ��Ƿ����
				if (e == null)
				{
					e = new Element(nodeName);
					pe.addContent(e);
				}
			}

			depth++;
		}

		return e;
	}

	public static Map loadProperties(Element pe)
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
	public static Map loadProperties(Element pe, int caseSwitch)
	{
		Map props = new Properties();

		// ����
		List attributes = pe.getAttributes();
		for (int i = 0; i < attributes.size(); i++)
		{
			try
			{
				Attribute attribute = (Attribute) attributes.get(i);
				props.put(caseSwitch == 0 ? attribute.getName()
						: (caseSwitch == 1 ? attribute.getName().toLowerCase()
								: attribute.getName().toUpperCase()), attribute
						.getValue());
			}
			catch (Exception e)
			{
				continue;
			}
		}

		// Ԫ��
		List elements = pe.getChildren();
		for (int i = 0; i < elements.size(); i++)
		{
			try
			{
				Element element = (Element) elements.get(i);

				if (element.getAttributes().size() > 0)
					continue;

				if (element.getChildren().size() > 0)
					continue;

				String nodeName = element.getName();

				props.put(caseSwitch == 0 ? nodeName
						: (caseSwitch == 1 ? nodeName.toLowerCase() : nodeName
								.toUpperCase()), element.getTextNormalize());
			}
			catch (Exception e)
			{
				continue;
			}
		}

		return props;
	}
}
