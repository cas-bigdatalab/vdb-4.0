/*
 * Created on 2005-2-13
 */
package cn.csdb.commons.util;

/**
 * @author bluejoe
 */
public class FilePathTokenizer
{
	private String _filePath;

	private String _fileName = "";

	private String _parentPath = "";

	private String _fileTitle = "";

	private String _fileExtension = "";

	public FilePathTokenizer(String filePath)
	{
		if (filePath != null)
		{
			// �޳����ķָ���
			if (filePath.endsWith("/") || filePath.endsWith("\\"))
				filePath = filePath.substring(0, filePath.length() - 1);

			_filePath = filePath;
			parse();
		}
	}

	/**
	 * 
	 */
	private void parse()
	{
		// ����'/'����'\'
		int li1 = _filePath.lastIndexOf('\\') + 1;
		int li2 = _filePath.lastIndexOf('/') + 1;

		int li3 = (li1 < li2 ? li2 : li1);

		// �Ҳ���
		if (li3 > 0)
		{
			// ��ȡ�ļ���
			_parentPath = _filePath.substring(0, li3 - 1);
			_fileName = _filePath.substring(li3);
		}
		else
		{
			_parentPath = "";
			_fileName = _filePath;
		}

		// ����ļ�����ȡ����.
		int li14 = _fileName.lastIndexOf('.');
		// �Ҳ���.
		if (li14 < 0)
		{
			_fileTitle = _fileName;
		}
		else
		{
			_fileTitle = _fileName.substring(0, li14);
			_fileExtension = _fileName.substring(li14);
		}
	}

	/**
	 * @return
	 */
	public String getFileExtension()
	{
		return _fileExtension;
	}

	/**
	 * @return
	 */
	public String getFileName()
	{
		return _fileName;
	}

	/**
	 * @return
	 */
	public String getFileTitle()
	{
		return _fileTitle;
	}

	public String getParentPath()
	{
		return _parentPath;
	}

	/**
	 * @return
	 */
	public String getFilePath()
	{
		return _filePath;
	}
}
