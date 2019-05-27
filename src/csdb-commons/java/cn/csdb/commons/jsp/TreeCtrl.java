/*
 * Created on 2003-7-28
 * 
 */
package cn.csdb.commons.jsp;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ������������һ��������HTML���Ϳؼ���
 * 
 * <pre>
 *      �ð汾��Tree֧�֣�
 *      1. �ڵ��ѡ��״̬������ʾ��
 *      2. �ڵ��ʼ״̬�Զ�չ����
 *      3. �ڽڵ�֮ǰ��ʾcheckbox��
 *      4. �ڵ��URL֧��ƥ������磺$ITEM_ID��ʾ�ýڵ��ID
 *      
 *      TreeCtrl��TreeItem���ʹ�ã�TreeItem������������ĳ���ڵ㣬�������ڽڵ㡣
 *      һ��ʹ�ò������£�
 *      1. ����һ��TreeCtrl����;
 *      2. ����TreeCtrl��createRoot����Ϊ��TreeCtrl����һ����,�÷������ظ�TreeCtrl�ĸ��ڵ�;
 *      3. ����TreeItem�����appendLeaf����appendFolder����Ϊ�������ӽڵ�;
 *      4. ͨ������setImageָ���ӽڵ��ͼ��·��;
 *      5. ͨ������setJsPath����JavaScript�ļ�·��;
 *      6. ����toHTML�������HTML����;
 *     
 *      ʾ���������£�
 *     
 *      		TreeCtrl tree = new TreeCtrl();
 *      		tree.setDefaultItemImage(
 *      			&quot;/images/logos/leaf.gif&quot;,
 *      			&quot;/images/logos/folder.gif&quot;);
 *      
 *      		tree.setNodeImage(
 *      			&quot;/images/node0.gif&quot;,
 *      			&quot;/images/node1.gif&quot;,
 *      			&quot;/images/node2.gif&quot;);
 *      		tree.setJsPath(&quot;/js/tree.js&quot;);
 *      		tree.setSpacingtImage(&quot;/images/vline.gif&quot;);
 *      
 *      		TreeItem ti = new TreeItem(&quot;temp&quot;);
 *      		ti.setURL(&quot;http://www.xinhuanet.com&quot;);
 *      		TreeItem ti1 = ti.appendLeaf(&quot;leaf8&quot;);
 *      		ti1.setURL(&quot;javascript:alert($ITEM_ID)&quot;);
 *      		TreeItem ti2 = ti.appendFolder(&quot;folder1&quot;);
 *      		TreeItem ti21 = ti2.appendLeaf(&quot;leaf1&quot;);
 *      
 *      		ti21.setItemURL(&quot;http://www.cnic.ac.cn&quot;);
 *      		ti21.setItemTitle(&quot;�п�Ժ��������&quot;);
 *      
 *      		tree.setRoot(ti);
 *      		tree.selectItem(tree.getItemByID(ti21.getItemID()));
 *      		tree.checkItem(ti21, true);
 *      
 *      		String ss2 = tree.toHTML();
 * </pre>
 * 
 * @author bluejoe
 */

public class TreeCtrl
{
	public String selectedItemID;

	private String checkBoxName;

	private String radioBoxName;

	private TreeItem root;

	private Map itemImages = new Hashtable();

	private Map nodeImages = new Hashtable();

	private String jsPath;

	private String target;

	private String spacingImage;

	private boolean hasCheckBox;

	private boolean hasRadioBox;

	private int lineSpacing = 2;

	private Map allItems = new Hashtable();

	public final static int EMPTY_NODE = 0;

	public final static int COLLAPSED_NODE = 1;

	public final static int EXPANDED_NODE = 2;

	/**
	 * ���췽��
	 */
	public TreeCtrl()
	{
	}

	/**
	 * ���ø����
	 */
	public void setRoot(TreeItem treeItem)
	{
		root = treeItem;
		allItems.clear();
		registerItem(root);
	}

	/**
	 * �ǼǸ��ڵ�
	 * 
	 * @param parent
	 */
	private void registerItem(TreeItem parent)
	{
		if (parent == null)
			return;

		allItems.put(parent.getItemID(), parent);

		for (int i = 0; i < parent.getChildren().size(); i++)
		{
			registerItem((TreeItem) parent.getChildren().get(i));
		}
	}

	public void setDefaultItemImage(String folderItemImage, String leafItemImage)
	{
		itemImages.put(("0"), leafItemImage);
		itemImages.put(("1"), folderItemImage);
	}

	public void setNodeImage(String emptyNodeImage, String collapsedNodeImage,
			String expanedNodeImage)
	{
		nodeImages.put("" + EMPTY_NODE, emptyNodeImage);
		nodeImages.put("" + COLLAPSED_NODE, collapsedNodeImage);
		nodeImages.put("" + EXPANDED_NODE, expanedNodeImage);
	}

	public String getNodeImage(int nodeStatus)
	{
		return (String) nodeImages.get("" + nodeStatus);
	}

	/**
	 * ��ȡ�����
	 */
	public TreeItem getRoot()
	{
		return root;
	}

	/**
	 * ��ȡͼ��·��
	 * 
	 * @param isFolder
	 *            �Ƿ�ΪĿ¼
	 */
	public String getDefaultItemImage(boolean isFolder)
	{
		if (isFolder)
		{
			return (String) itemImages.get("0");
		}
		else
		{
			return (String) itemImages.get("1");
		}
	}

	public String toHTML()
	{
		return toHTML(true);
	}

	/**
	 * ��ȡHTML��ʾ����
	 */
	public String toHTML(boolean showRoot)
	{
		String HTML = "";
		if (jsPath != null)
			HTML += "<script src=\"" + jsPath + "\"></script>\r\n";

		HTML += root.toHTML(this, showRoot);
		HTML += "<script>\r\n"
				+ "\tvar collapsedNodeImage = \""
				+ getNodeImage(COLLAPSED_NODE)
				+ "\";\r\n"
				+ "\tvar expandedNodeImage = \""
				+ getNodeImage(EXPANDED_NODE)
				+ "\";\r\n"
				+ (getSelectedItemID() == null ? "" : "\tHilight(\""
						+ getSelectedItemID() + "\");\r\n") + "</script>\r\n";

		return HTML;
	}

	/**
	 * @param string
	 */
	public void setJsPath(String jsPath)
	{
		this.jsPath = jsPath;
	}

	public void selectItem(TreeItem selectedItem)
	{
		if (selectedItem != null)
		{
			selectedItem.expand(true);
			selectedItemID = selectedItem.getItemID();
		}
	}

	/**
	 * ���ĳ��item. ���ĳ��item����ǣ���˷���ͬʱ��hasCheckBox
	 * 
	 * @param item
	 * @param checked
	 */
	public void checkItem(TreeItem item, boolean checked)
	{
		checkItem(item.getItemID(), checked);
	}

	/**
	 * ���ĳ��item. ���ĳ��item����ǣ���˷���ͬʱ��hasCheckBox
	 * 
	 * @param item
	 * @param checked
	 */
	public void checkItem(String itemID, boolean checked)
	{
		if (checked && !hasCheckBox)
			hasCheckBox = true;

		TreeItem item = (TreeItem) allItems.get(itemID);
		item.checkItem(checked);
		item.expand(true);
	}

	/**
	 * ���ĳ��item. ���ĳ��item����ǣ���˷���ͬʱ��hasCheckBox
	 * 
	 * @param item
	 * @param checked
	 */
	public void checkItems(List itemIDs, boolean checked)
	{
		clearCheckItems();
		for (int i = 0; i < itemIDs.size(); i++)
		{
			checkItem((String) itemIDs.get(i), checked);
		}

	}

	public void clearCheckItems()
	{
		Set set = allItems.keySet();
		Iterator it = set.iterator();
		while (it.hasNext())
		{
			checkItem((String) it.next(), false);
		}
	}

	/**
	 */
	public String getSpacingImage()
	{
		return spacingImage;
	}

	/**
	 * @param string
	 */
	public void setSpacingtImage(String indentImage)
	{
		this.spacingImage = indentImage;
	}

	public void radioItem(TreeItem item, boolean radioed)
	{
		radioItem(item.getItemID(), radioed);
	}

	/**
	 * ���ĳ��item. ���ĳ��item����ǣ���˷���ͬʱ��hasCheckBox
	 * 
	 * @param item
	 * @param checked
	 */
	public void radioItem(String itemID, boolean radioed)
	{
		if (radioed && !hasRadioBox)
			hasRadioBox = true;

		TreeItem item = (TreeItem) allItems.get(itemID);
		if (item != null)
		{
			item.radioItem(radioed);
			item.expand(true);
		}
	}

	/**
	 */
	public boolean hasCheckBox()
	{
		return hasCheckBox;
	}

	public void setCheckBox(String checkBoxName, boolean hasCheckBox)
	{
		this.checkBoxName = checkBoxName;
		setCheckBox(hasCheckBox);
	}

	public void setCheckBox(boolean hasCheckBox)
	{
		this.hasCheckBox = hasCheckBox;
	}

	public boolean hasRadioBox()
	{
		return hasRadioBox;
	}

	public void setRadioBox(String radioBoxName, boolean hasRadioBox)
	{
		this.radioBoxName = radioBoxName;
		setRadioBox(hasRadioBox);
	}

	public void setRadioBox(boolean hasRadioBox)
	{
		this.hasRadioBox = hasRadioBox;
	}

	public String getCheckBoxName()
	{
		return checkBoxName == null ? "$TREE_CHECK_" : checkBoxName;
	}

	public String getRadioBoxName()
	{
		return radioBoxName == null ? "$TREE_RADIO" : radioBoxName;
	}

	/**
	 */
	public String getSelectedItemID()
	{
		return selectedItemID;
	}

	public TreeItem getItemByID(String itemID)
	{
		if (itemID == null)
			return null;

		return (TreeItem) allItems.get(itemID);
	}

	/**
	 */
	public int getLineSpacing()
	{
		return lineSpacing;
	}

	/**
	 * @param i
	 */
	public void setLineSpacing(int i)
	{
		lineSpacing = i;
	}

	/**
	 */
	public String getBaseTarget()
	{
		return target;
	}

	/**
	 * @param string
	 */
	public void setBaseTarget(String target)
	{
		this.target = target;
	}

	public void expandAll(boolean expand)
	{
		Iterator it = allItems.entrySet().iterator();
		while (it.hasNext())
		{
			TreeItem ti = (TreeItem) ((Map.Entry) (it.next())).getValue();
			ti.expand(expand);
		}
	}
}