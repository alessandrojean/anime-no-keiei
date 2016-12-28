package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

public class Manga
{
	protected int id;
	protected String nationalName;
	protected String originalName;
	protected Type type;
	protected String serialization;
	protected Date startDate;
	protected Date finishDate;
	protected String authors;
	protected Edition edition;
	protected String stamp;
	protected List<Gender> genders;
	protected int rating;
	protected String observations;

	private File poster;

	private List<Volume> volumes;

	public Manga()
	{
		super();
	}

	public Manga(int id)
	{
		super();
		this.id = id;
	}

	public Manga(int id, String nationalName)
	{
		super();
		this.id = id;
		this.nationalName = nationalName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNationalName()
	{
		return nationalName;
	}

	public void setNationalName(String nationalName)
	{
		this.nationalName = nationalName;
	}

	public String getOriginalName()
	{
		return originalName;
	}

	public void setOriginalName(String originalName)
	{
		this.originalName = originalName;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getSerialization()
	{
		return serialization;
	}

	public void setSerialization(String serialization)
	{
		this.serialization = serialization;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getFinishDate()
	{
		return finishDate;
	}

	public void setFinishDate(Date finishDate)
	{
		this.finishDate = finishDate;
	}

	public String getAuthors()
	{
		return authors;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	public Edition getEdition()
	{
		return edition;
	}

	public void setEdition(Edition edition)
	{
		this.edition = edition;
	}

	public String getStamp()
	{
		return stamp;
	}

	public void setStamp(String stamp)
	{
		this.stamp = stamp;
	}

	public List<Gender> getGenders()
	{
		return genders;
	}

	public String getGendersAsString()
	{
		String result = "";

		for (int i = 0; i < genders.size(); i++)
			result += genders.get(i).getValue() + (i == genders.size() - 1 ? "" : ";");

		return result;
	}

	public void setGenders(List<Gender> genders)
	{
		this.genders = genders;
	}

	public void setGenders(String genders)
	{
		List<Gender> gendersList = new ArrayList<Gender>();
		String[] gendersSplitted = genders.split(";");

		for (String s : gendersSplitted)
			gendersList.add(Gender.fromValue(Integer.parseInt(s)));
		

		this.genders = gendersList;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getObservations()
	{
		return observations;
	}

	public void setObservations(String observations)
	{
		this.observations = observations;
	}

	public List<Volume> getVolumes()
	{
		return volumes;
	}

	public void setVolumes(List<Volume> volumes)
	{
		this.volumes = volumes;
	}

	public File getPoster()
	{
		return poster;
	}

	public void setPoster(File poster)
	{
		this.poster = poster;
	}

	@Override
	public String toString()
	{
		return "Manga [id=" + id + ", nationalName=" + nationalName + ", type=" + type + ", edition=" + edition + ", stamp=" + stamp + "]";
	}
}
