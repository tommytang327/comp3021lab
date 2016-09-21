package base;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.regex.Pattern;
//import java.util.Comparator;

public class Folder implements Comparable<Folder>{
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name){
		this.name = name;
		notes = new ArrayList<Note>();
	}
	
	public void addNote(Note note){
		notes.add(note);
	}

	public String getName(){
		return this.name;
	}
	
	public ArrayList<Note> getNotes(){
		return this.notes;
	}
	
	
	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;
		
		for(Note note: notes){
			if(note instanceof TextNote)
				nText++;
			else if(note instanceof ImageNote)
				nImage++;
		}
		
		return name + ":" + nText + ":" + nImage;
	}
	
	@Override
	public boolean equals(Object otherFolder){
		if(this.name.equals(((Folder)otherFolder).getName()))
			return true;
		else
			return false;
	}

	//lab3 to do
	@Override
	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		/*if(this.getName().compareTo(o.getName())<0)  //this>o
		{
			return 1;
		}
		else if(this.getName().compareTo(o.getName())>0)   //this<o
		{
			return -1;
		}
		return 0;
		*/
		
		return name.compareTo(o.name);
	}
	
	
	public void sortNotes(){
		//List<Note> notes = new ArrayList<Note>();
		Collections.sort(notes); 
		
		
	}
	
	public List<Note> searchNotes(String keywords){   	
		 List<Note> listOfNote = new ArrayList<Note>();
		 List<String> andCompo = new ArrayList<String>();
		 List<String> orCompo = new ArrayList<String>();
		 String[] splitKeys = keywords.split(" ");
		 
		 for(int i=0 ; i<splitKeys.length; i++){
			 if (splitKeys[i]!=null){  //****fixed null pointer problem
				 if(splitKeys[i].equalsIgnoreCase("OR")){    //extract elements next to OR 
					 orCompo.add(splitKeys[i+1].toLowerCase());
					 orCompo.add(splitKeys[i-1].toLowerCase()); 
					 splitKeys[i] = null;
					 splitKeys[i+1] = null;
					 splitKeys[i-1] = null;
				 }	 
			 }
		 }
	 
		 for(String temp:splitKeys){    //extract elements which are not next to OR
			 if (temp != null ) andCompo.add(temp.toLowerCase());
		 }
	 
		 for (Note note: notes){
			 
			 if(note instanceof TextNote){    //perform different operations according to their data type
				 boolean matchedTitle = true;
				 boolean matchedContent = true;
				 String noteTitle = note.getTitle().toLowerCase();
				 String noteContent = ((TextNote)note).content.toLowerCase();
	
				 for (String s: andCompo){
					 if (!(noteTitle.contains(s)))  matchedTitle = false;
				 }
			 
				 for(int j=0; j<orCompo.size() ; j+=2){
					 if(!(noteTitle.contains(orCompo.get(j)) || noteTitle.contains(orCompo.get(j+1))))  matchedTitle = false;
				 }
		 
				 for(String temp:andCompo){
					 if ((temp!=null && noteContent.contains(temp)))  matchedContent = false;
				 }
			 
				 for(int k=0 ; k<orCompo.size() ; k+=2){
					 if (!(noteContent.contains(orCompo.get(k)) || noteContent.contains(orCompo.get(k+1))))  matchedContent = false;
				 } 
				 if(matchedTitle || matchedContent) listOfNote.add(note); 
				 
			 }else if(note instanceof ImageNote){
				 boolean matchedTitle = true;
				 String noteTitle = note.getTitle().toLowerCase();
				 for(String s: andCompo){
					 if (!(noteTitle.contains(s))) matchedTitle = false;
				 }
			 
				 for(int j=0; j<orCompo.size() ; j+=2){
					 if (!(noteTitle.contains(orCompo.get(j)) || noteTitle.contains(orCompo.get(j+1))))  matchedTitle = false;
				 }		
				 if(matchedTitle) listOfNote.add(note); 
			 }
		 }
		 return listOfNote;
	}
}
		

