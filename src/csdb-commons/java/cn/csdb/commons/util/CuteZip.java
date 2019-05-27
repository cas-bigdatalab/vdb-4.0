package cn.csdb.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * �ļ�ѹ������ѹ���ࡣ
 */

public class CuteZip
{
	public final String _tempDirectoryName = "_cutezip_temporary_directory_";

	public final String _tempZipName = "_cutezip_temporary_file_.zip";

	/**
	 * ���н����ļ��к�ѹ������
	 * 
	 * @param:String srcFileName Դ�ļ� e:\\xxxx\\xxxx.xxx or e:\\xxxx �ļ������ļ���
	 *               String comment ���Ի�˵��
	 * @return:int ѹ���ļ��ĸ���(���ļ�����1)
	 * @since: 2004-03
	 * @version 1.0
	 */
	private int doZipDir(ZipOutputStream out, String srcFileName,
			String entryPath, String comment)
	{
		int fileNum = 0;
		srcFileName = srcFileName.replace('\\', '/');
		entryPath = entryPath.replace('\\', '/');
		if (srcFileName.lastIndexOf("/") == -1)
			return 0;

		try
		{
			// ʵ����Դ�ļ�
			File file = new File(srcFileName);
			if (!file.exists() && !file.isDirectory())
				return fileNum;

			String[] fileName = file.list(); // �������Ҫ����ת����,ʹ���˱��ر��룺��
			if (fileName.length == 0) // ���ļ���
			{
				// Ϊ���������ݴ���һ��zip��Ŀ��
				ZipEntry entry = new ZipEntry(new String((entryPath + "/")
						.getBytes("ISO_8859_1"), "GBK"));
				// ��zip��Ŀ�б�д�������
				out.putNextEntry(entry);
				if (comment != null && !comment.equalsIgnoreCase(""))
					out.setComment(comment);
				out.closeEntry();
			}
			else
			{
				for (int i = 0; i < fileName.length; i++)
				{
					File f1 = new File(srcFileName, fileName[i]);
					// ������ļ�������ѹ��
					if (f1.isFile())
					{
						fileNum++;
						// ���ļ�·���������Ҳ����ļ�!!
						String tempName = srcFileName + "/" + fileName[i];
						// �����ļ�������
						FileInputStream in = new FileInputStream(tempName);
						BufferedInputStream orign = new BufferedInputStream(in,
								5120);
						// Ϊ���������ݴ���һ��zip��Ŀ��
						ZipEntry entry = new ZipEntry(new String((entryPath
								+ "/" + fileName[i]).getBytes("ISO_8859_1"),
								"GBK"));
						// ��zip��Ŀ�б�д�������
						out.putNextEntry(entry);
						if (comment != null && !comment.equalsIgnoreCase(""))
							out.setComment(comment);
						int bufferSize = 0;
						byte[] buffer = new byte[5120];
						while ((bufferSize = orign.read(buffer)) != -1)
						{
							out.write(buffer, 0, bufferSize);
						}
						orign.close();
						out.closeEntry();
					}
					// �������Ŀ¼���еݹ���ã�
					else
					{
						String sub = f1.getAbsolutePath();
						fileNum += doZipDir(out, sub, entryPath + "/"
								+ fileName[i], comment);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileNum;
	}

	/**
	 * ���н����ļ���ѹ������
	 * 
	 * @param:String srcFileName Դ�ļ� e:\\xxxx or e:\\xxxx �������ļ� String comment
	 *               ���Ի�˵��
	 * @return:int ѹ���ļ��ĸ���
	 * @since: 2004-03
	 * @version 1.0
	 */
	private boolean doZipFile(ZipOutputStream out, String srcFileName,
			String comment)
	{
		if (out == null)
			return false;
		srcFileName = srcFileName.replace('\\', '/');
		if (srcFileName.lastIndexOf("/") == -1)
			return false;

		boolean ret = true;
		try
		{
			// �����ļ�������
			FileInputStream in = new FileInputStream(srcFileName);
			BufferedInputStream orign = new BufferedInputStream(in, 5120);
			// Ϊ���������ݴ���һ��zip��Ŀ��
			String entryName = srcFileName.substring(srcFileName
					.lastIndexOf("/") + 1);
			ZipEntry entry = new ZipEntry(new String((entryName + "/")
					.getBytes("GBK"), "ISO_8859_1"));
			// ��zip��Ŀ�б�д�������
			out.putNextEntry(entry);
			if (comment != null && !comment.equalsIgnoreCase(""))
				out.setComment(comment);
			int bufferSize = 0;
			byte[] buffer = new byte[5120];
			while ((bufferSize = orign.read(buffer)) != -1)
			{
				out.write(buffer, 0, bufferSize);
			}
			orign.close();
			out.closeEntry();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	/**
	 * ��ѹ����Ӧ��ѹ���ļ�
	 * 
	 * @param:String filePath ѹ���ļ��ľ���·��,String nickPath ��ѹ���ļ��ľ���·��Ŀ¼
	 * @return:void
	 * @since: 2004-03
	 * @version 1.0
	 */
	public void unzip(String filePath, String nickPath)
	{
		filePath = filePath.replace('\\', '/');
		nickPath = nickPath.replace('\\', '/'); // ͳһ����\\��/��ʽ
		if (filePath.indexOf("/") == -1 || nickPath.indexOf("/") == -1)
		{
			return;
		}

		if (filePath.charAt(filePath.length() - 1) == '/')
		{
			filePath = filePath.substring(0, filePath.length() - 1);
		}

		if (nickPath.charAt(nickPath.length() - 1) == '/')
		{
			nickPath = nickPath.substring(0, nickPath.length() - 1);
		}

		File fl = new File(filePath);
		if (!fl.exists())
		{
			return;
		}

		try
		{
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(fl);
			Enumeration e = zipfile.entries();
			File outFile = new File(nickPath);
			outFile.mkdir();
			while (e.hasMoreElements())
			{
				entry = (ZipEntry) e.nextElement();
				String tempFileName = nickPath
						+ System.getProperty("file.separator")
						+ entry.getName();
				File tempFile = new File(tempFileName);
				if (entry.isDirectory())
				{
					tempFile.mkdir();
					continue;
				}
				else
				{
					File aa = new File(tempFile.getParent());
					aa.mkdirs();
				}

				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[5120];
				FileOutputStream fos = new FileOutputStream(tempFile);
				dest = new BufferedOutputStream(fos, 5120);
				while ((count = is.read(data, 0, 5120)) != -1)
				{
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��Դ�ļ�(�ļ���)ѹ��������ΪdestFileName��ѹ����
	 * 
	 * @param:String srcFileName Դ�ļ� e:\\xxxx\\xxxx.xxx or e:\\xxxx,String
	 *               destFileName Ŀ��ѹ������ e:\\xxx.rar or e:\\xxx.zip(�������winrar
	 *               ���� zip��,��ֱ��ʹ��.rar����.zip,��ò�Ҫʹ�����е��ļ���׺eg:.txt֮��)
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String srcFileName, String destFileName)
	{
		return zip(srcFileName, destFileName, null);
	}

	/**
	 * ��Դ�ļ�(�ļ���)ѹ��������ΪdestFileName��ѹ����
	 * 
	 * @param:String srcFileName Դ�ļ� e:\\xxxx\\xxxx.xxx or e:\\xxxx,String
	 *               destFileName Ŀ��ѹ������ e:\\xxx.rar or e:\\xxx.zip(�������winrar
	 *               ���� zip��,��ֱ��ʹ��.rar����.zip,��ò�Ҫʹ�����е��ļ���׺eg:.txt֮��) String
	 *               comment ���Ի�˵��
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String srcFileName, String destFileName, String comment)
	{
		boolean ret = true;
		int fileNum = 0;
		try
		{
			if (srcFileName == null || srcFileName.equalsIgnoreCase("")
					|| destFileName == null
					|| destFileName.equalsIgnoreCase(""))
				return false;
			srcFileName = srcFileName.replace('\\', '/'); // ͳһ����\\��/��ʽ
			if (srcFileName.charAt(srcFileName.length() - 1) == '/')
				srcFileName = srcFileName
						.substring(0, srcFileName.length() - 1);

			if (destFileName.charAt(destFileName.length() - 1) == '/')
				destFileName = destFileName.substring(0,
						destFileName.length() - 1);

			File file = new File(srcFileName); // �ж�Դ�ļ��Ƿ����,�������򲻲���Ŀ���ļ�
			if (!file.exists())
				return false;
			FileOutputStream dest = new FileOutputStream(destFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			if (file.isDirectory())
				fileNum = doZipDir(out, srcFileName, srcFileName
						.substring(srcFileName.lastIndexOf("/") + 1), comment);
			else if (file.isFile())
			{
				if (doZipFile(out, srcFileName, comment))
					fileNum++;
			}

			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	/**
	 * ѹ��ͬʱ�������һ���ļ�,�����ļ��ŵ�ָ��ѹ��src��ͬ��һ��Ŀ¼
	 * 
	 * @param:String priSrc ��ѹ��Դ String addSrc ����ѹ���ļ�(��������ھͲ�����) String destĿ��
	 *               String comment ���Ի�����
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String priSrc, String addSrc, String destPath,
			String comment)
	{
		if (priSrc.equalsIgnoreCase(destPath))
			return false;
		if (addSrc.equalsIgnoreCase(destPath))
			return false;
		priSrc = priSrc.replace('\\', '/');
		addSrc = addSrc.replace('\\', '/'); // ͳһ����\\��/��ʽ
		String priName = "";
		String addName = "";
		boolean isFile = false;
		try
		{
			File pri = new File(priSrc);
			boolean priFile = pri.isFile();
			boolean isPriExists = pri.exists();
			if (!isPriExists)
				return false;
			priName = pri.getName();
			pri = new File(addSrc);
			addName = pri.getName();
			boolean isAddExists = true;
			if (pri.exists())
			{
				isAddExists = true;
				if (pri.isDirectory())
				{
					isFile = false;
				}
				else if (pri.isFile())
				{
					isFile = true;
				}
			}
			else
			{
				isAddExists = false;
			}

			FileOutputStream dest = new FileOutputStream(destPath);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			if (priFile)
				doZipFile(out, priSrc, comment);
			else
				doZipDir(out, priSrc, priName, comment);
			if (isPriExists && isAddExists)
			{
				if (isFile)
					doZipFile(out, addSrc, comment);
				else
					doZipDir(out, addSrc, addName, comment);
			}
			if (isPriExists)
				out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}