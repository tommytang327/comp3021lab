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
	
	public boolean equals(Object otherFolder){
		if(this.name.equals(((Folder)otherFolder).getName()))
			return true;
		else
			return false;
	}

	//lab3
	@Override
	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		if(this.getName().compareTo(o.getName())<0)  //this>o
		{
			return 1;
		}
		else if(this.getName().compareTo(o.getName())>0)   //this<o
		{
			return -1;
		}
		return 0;
	}
	
	
	public void sortNotes(){
		//List<Note> notes = new ArrayList<Note>();
		Collections.sort(notes); 
		
		
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> matchedNotes = new ArrayList<Note>();
		
		
		for(Note note: notes){
			if(Pattern.compile(Pattern.quote(keywords), Pattern.CASE_INSENSITIVE).matcher(note.getTitle()).find()){
				matchedNotes.add(note);
			}
			else{
				if(note instanceof TextNote){
					if(Pattern.compile(Pattern.quote(keywords), Pattern.CASE_INSENSITIVE).matcher(((TextNote)note).content).find()){
						matchedNotes.add(note);
					}
				}
				
			}
		}
		
		return matchedNotes;
		//Pattern.compile(Pattern.quote(s2), Pattern.CASE_INSENSITIVE).matcher(s1).find();
	}
	
}
