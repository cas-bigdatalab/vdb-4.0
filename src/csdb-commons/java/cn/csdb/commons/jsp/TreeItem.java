package cn.csdb.commons.jsp;

import java.util.List;
import java.util.Vector;

/**
 * ���������������ڵ㡣
 * 
 * <pre>
 *      TreeItem��Ƴ���TreeCtrl�޹��ԣ�TreeItem��������TreeCtrl�����ڡ�
 *      �����ڵ�ʱ��ָ���ýڵ����ʾ���֣���Ҫʱ������ָ���ýڵ��ID��
 *      ���û��ָ����ϵͳ���Զ�����һ��ΨһID��
 *      �����ڵ�֮�󻹿��Ե�����Ӧ�������øýڵ�����ӣ�LOGOͼƬ����ʾ���֣�
 *      Ҳ�������øýڵ��ѡ�񡢱�ǡ�չ��״̬��
 * </pre>
 * 
 * @author bluejoe
 */
public class TreeItem
{
	private boolean checked;

	private boolean radioed;

	private String itemImage;

	private String itemTitle;

	private String itemID;

	private Object itemData;

	// �ýڵ������
	private boolean isFolder;

	// ��ʾ����
	private String itemText;

	// ���ӵ�ַ
	private String href;

	private String target;

	// ���ڵ�
	private TreeItem parent;

	// �ӽڵ��б�
	private List children;

	private boolean expanded = false;

	private static long ITEM_UID = 0x1224;

	public TreeItem(String itemText)
	{
		init(null, itemText, null, true);
	}

	public TreeItem(String itemText, String itemID)
	{
		init(null, itemText, itemID, true);
	}

	private TreeItem(TreeItem parent, String itemText, String itemID,
			boolean isFolder)
	{
		init(parent, itemText, itemID, isFolder);
	}

	/**
	 * ��ɽڵ�ĳ�ʼ��
	 * 
	 * @param tree
	 * @param parent
	 * @param itemText
	 * @param href
	 * @param isFolder
	 */
	private void init(TreeItem parent, String itemText, String itemID,
			boolean isFolder)
	{
		this.parent = parent;
		this.itemText = itemText;

		if (itemID != null)
		{
			this.itemID = itemID;
		}
		else
		{
			this.itemID = "" + ITEM_UID;
			ITEM_UID++;
		}

		this.isFolder = isFolder;
		children = new Vector();
	}

	/**
	 * ���������ַ�����
	 * 
	 * �����������ַ���ʹ�ñ����������б����£� $ITEM_ID �ýڵ��ID $ITEM_TEXT �ýڵ������ $ITEM_TITLE
	 * �ýڵ����ʾ����
	 * 
	 * @param href
	 */
	public void setItemURL(String href)
	{
		this.href = href;
	}

	public void setItemTarget(String target)
	{
		this.target = target;
	}

	public void setItemURL(String href, String target)
	{
		setItemURL(href);
		setItemTarget(target);
	}

	public void setItemImage(String image)
	{
		this.itemImage = image;
	}

	public void append(TreeItem ti)
	{
		ti.parent = this;
		children.add(ti);
	}

	/**
	 * ����һ��Ŀ¼�ڵ�
	 * 
	 * @param itemText
	 *            ��ʾ����
	 * @param href
	 *            ���ӵ�ַ��null����ʾ����
	 */
	public TreeItem appendFolder(String itemText, String itemID)
	{
		TreeItem ti = new TreeItem(this, itemText, itemID, true);
		children.add(ti);

		return ti;
	}

	public TreeItem appendFolder(String itemText)
	{
		return appendFolder(itemText, null);
	}

	/**
	 * ����һ��Ҷ�ڵ�
	 * 
	 * @param itemText
	 *            ��ʾ����
	 * @param href
	 *            ���ӵ�ַ��null����ʾ����
	 */
	public TreeItem appendLeaf(String itemText, String itemID)
	{
		TreeItem ti = new TreeItem(this, itemText, itemID, false);
		children.add(ti);

		return ti;
	}

	public TreeItem appendLeaf(String itemText)
	{
		return appendLeaf(itemText, null);
	}

	/**
	 * ��ȡ�ӽڵ��б�
	 */
	public List getChildren()
	{
		return children;
	}

	public boolean hasChildren()
	{
		return (children.size() != 0);
	}

	public void expand(boolean expand)
	{
		this.expanded = expand;

		// ͬʱչ�����ڵ�
		if (expand && (parent != null))
		{
			parent.expand(expand);
		}
	}

	public String getItemImage(TreeCtrl tree)
	{
		if (itemImage != null && !"".equals(itemImage))
			return itemImage;

		return tree.getDefaultItemImage(isFolder);
	}

	public String toHTML(TreeCtrl tree)
	{
		return toHTML(tree, true);
	}

	/**
	 * ��ȡ�ýڵ��HTML����
	 */
	public String toHTML(TreeCtrl tree, boolean showMe)
	{
		String HTML = "";
		String spacing = "";
		String si = tree.getSpacingImage();

		if (si != null)
			spacing = " background=\"" + si + "\"";

		if (showMe)
		{
			// ��ʾ�ڵ�ͼ���Item

			// ��ʾ�ڵ�ͼ��
			HTML += "<table class=\"tree\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					+ "<tr>\r\n"
					+ "<td width=\"16\""
					+ spacing
					+ " style=\"padding-top: "
					+ tree.getLineSpacing()
					+ "; padding-bottom: " + tree.getLineSpacing() + "\">\r\n";

			if (!hasChildren())
			{
				HTML += "<img src=\"" + tree.getNodeImage(TreeCtrl.EMPTY_NODE)
						+ "\">";
			}
			else
			{
				HTML += "<img id=\"$NODE_"
						+ getItemID()
						+ "\" src=\""
						+ tree.getNodeImage(expanded ? TreeCtrl.EXPANDED_NODE
								: TreeCtrl.COLLAPSED_NODE)
						+ "\" style=\"cursor: hand\" onClick=\"Toggle('"
						+ getItemID() + "')\">";
			}

			HTML += "</td>\r\n";

			// ��ʾcheckbox
			if (tree.hasCheckBox())
			{
				HTML += "<td width=\"16\">\r\n" + "<input name=\""
						+ tree.getCheckBoxName() + getItemID()
						+ "\" type=\"checkbox\""
						+ (isChecked() ? " checked" : "") + " value=\""
						+ getItemID() + "\">\r\n" + "</td>\r\n";
			}
			// ��ʾcheckbox
			if (tree.hasRadioBox())
			{
				HTML += "<td width=\"16\">\r\n" + "<input name=\""
						+ tree.getRadioBoxName() + "\" type=\"radio\""
						+ (isRadioed() ? " checked" : "") + " value=\""
						+ getItemID() + "\">\r\n" + "</td>\r\n";
			}
			HTML += "<td width=\"16\">\r\n";

			// ��ʾitemͼ��
			String ii = getItemImage(tree);
			if (hasChildren())
			{
				HTML += (ii == null ? "" : "<img src=\"" + ii + "\">");
			}
			else
			{
				HTML += "<a>" + (ii == null ? "" : "<img src=\"" + ii + "\">")
						+ "</a>";
			}

			HTML += "</td>\r\n";

			// ��ʾitem����
			// ���
			HTML += "<td width=\"2\">\r\n" + "</td>\r\n";

			HTML += "<td align=\"left\" nowrap>\r\n" + getItemHTML(tree)
					+ "</td>\r\n";

			HTML += "</tr>\r\n" + "</table>\r\n";
		}

		// ��ʾ�ӽڵ�
		if (hasChildren())
		{
			if (showMe)
			{
				String display = expanded ? "block" : "none";
				HTML += "\r\n<div id=\"$BLOCK_" + getItemID()
						+ "\" style=\"display:" + display + "\">\r\n";

				// ����
				HTML += "<table class=\"tree\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">\r\n"
						+ "<col width=\"16\">\r\n"
						+ "<tr>\r\n"
						+ "<td width=\"16\""
						+ spacing
						+ ">\r\n"
						+ "</td>\r\n"
						+ "<td>\r\n";
			}

			for (int i = 0; i < children.size(); i++)
			{
				TreeItem ti = (TreeItem) children.get(i);
				HTML += ti.toHTML(tree, true);
			}

			if (showMe)
			{
				HTML += "</td></tr></table>\r\n";
				HTML += "</div>\r\n";
			}
		}

		return HTML;
	}

	/**
	 * ���ظ�Item��HTML����
	 */
	public String getItemHTML(TreeCtrl tree)
	{

		if (href == null)
			return getItemText();

		String it = href;

		it = it.replaceAll("\\$ITEM_ID", getItemID());
		it = it.replaceAll("\\$ITEM_TEXT", getItemText());
		it = it.replaceAll("\\$ITEM_TITLE", getItemTitle());

		String title = getItemTitle();
		String target = (this.target == null ? tree.getBaseTarget()
				: this.target);

		return "<a id=\"$ITEM_" + getItemID() + "\" onclick=\"Hilight('"
				+ getItemID() + "')\""
				+ (title == null ? "" : " title=\"" + title + "\"")
				+ " href=\"" + it + "\""
				+ (target == null ? "" : " target=\"" + target + "\"") + ">"
				+ itemText + "</a>";
	}

	/**
	 */
	public boolean isFolder()
	{
		return isFolder;
	}

	/**
	 */
	public boolean isChecked()
	{
		return checked;
	}

	public boolean isRadioed()
	{
		return radioed;
	}

	public void checkItem(boolean checked)
	{
		this.checked = checked;
	}

	public void radioItem(boolean radioed)
	{
		this.radioed = radioed;
	}

	/**
	 */
	public String getItemID()
	{
		return itemID;
	}

	/**
	 */
	public String getItemText()
	{
		return itemText;
	}

	/**
	 */
	public String getItemTitle()
	{
		return itemTitle;
	}

	/**
	 * @param string
	 */
	public void setItemTitle(String itemTitle)
	{
		this.itemTitle = itemTitle;
	}

	/**
	 */
	public Object getItemData()
	{
		return itemData;
	}

	/**
	 * @param object
	 */
	public void setItemData(Object object)
	{
		itemData = object;
	}
}