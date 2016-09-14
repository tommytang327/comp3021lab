package base;

import java.util.Date;

public class Note {

	private Date date;
	private String title;
	
	public Note(String newTitle)
	{
		this.title = newTitle;
		date = new Date(System.currentTimeMillis());
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public Date getDate()
	{
		return this.date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		Note otherObj = (Note) obj;
		if(title.equals( otherObj.getTitle()))
			return true;

		return false;
		/*
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
		*/
	}
}
