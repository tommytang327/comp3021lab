package base;

import java.util.Date;
//import java.util.List;

//import java.util.Comparator;

public class Note implements Comparable<Note>{

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

	//lab3
	@Override
	public int compareTo(Note o) {
		// TODO Auto-generated method stub
		
		if(this.getDate().before(o.getDate()))
		{
			return 1;
		}
		else if(this.getDate().after(o.getDate()))
		{
			return -1;
		}
		return 0;
	}
	
	/*public static Comparator<Note> NoteCreatedDateComparator = new Comparator<Note>(){
		public int compare(Note note1, Note note2){
			return note1.compareTo(note2);
		}
	};
	*/
}
