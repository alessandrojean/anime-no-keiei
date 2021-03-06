package database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Manga;
import model.Publisher;
import model.Volume;

import org.apache.commons.io.FileUtils;

import utils.ExceptionUtils;
import api.mal.model.Item;
import api.mcd.model.ImageCover;

public class ImageDatabase
{

	public static final String DEFAULT_FOLDER = Database.DEFAULT_DATABASE_FOLDER + File.separator + "images";
	public static final String FILE_MANGA = DEFAULT_FOLDER + File.separator + "mangas" + File.separator + "%d.png";
	public static final String FILE_VOLUME = DEFAULT_FOLDER + File.separator + "volumes" + File.separator + "%d.png";
	public static final String FILE_PUBLISHER = DEFAULT_FOLDER + File.separator + "publishers" + File.separator + "%d.png";
	public static final String FILE_MYANIMELIST = DEFAULT_FOLDER + File.separator + "myanimelist" + File.separator + "%d.png";
	public static final String FILE_MANGA_COVER_DATABASE = DEFAULT_FOLDER + File.separator + "mcd" + File.separator + "%d_%d.png";

	public static void insertImage(Object object)
	{
		try
		{
			String file = "";
			File image = null;
			if (object instanceof Manga)
			{
				int id = ((Manga) object).getId();
				file = String.format(FILE_MANGA, id);
				image = ((Manga) object).getPoster();
			}
			else if (object instanceof Volume)
			{
				int id = ((Volume) object).getId();
				file = String.format(FILE_VOLUME, id);
				image = ((Volume) object).getPoster();
			}
			else if (object instanceof Publisher)
			{
				int id = ((Publisher) object).getId();
				file = String.format(FILE_PUBLISHER, id);
				image = ((Publisher) object).getLogo();
			}
			if (image == null)
				return;

			File f = new File(file);
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if (!f.toString().equals(image.toString()))
				FileUtils.copyFile(image, f);
		}
		catch (IOException e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}

	}

	
	public static File selectImage(Object object)
	{
		File result = null;
		if (object instanceof Manga)
		{
			int id = ((Manga) object).getId();
			result = new File(String.format(FILE_MANGA, id));
		}
		else if (object instanceof Volume)
		{
			int id = ((Volume) object).getId();
			result = new File(String.format(FILE_VOLUME, id));
		}
		else if (object instanceof Publisher)
		{
			int id = ((Publisher) object).getId();
			result = new File(String.format(FILE_PUBLISHER, id));
		}

		return result.exists() ? result : null;
	}

	public static void removeImage(Object object)
	{
		File result = null;
		if (object instanceof Manga)
		{
			int id = ((Manga) object).getId();
			result = new File(String.format(FILE_MANGA, id));
		}
		else if (object instanceof Volume)
		{
			int id = ((Volume) object).getId();
			result = new File(String.format(FILE_VOLUME, id));
		}
		else if (object instanceof Publisher)
		{
			int id = ((Publisher) object).getId();
			result = new File(String.format(FILE_PUBLISHER, id));
		}

		if (result != null)
			if (result.exists())
				result.delete();
	}
}
