package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Folder> folders;
	
	public NoteBook(){
		folders = new ArrayList<Folder>();
	}
	
	//lab5 
	/**
	 * 
	 * Constructor of an object NoteBook from an object serialization on disk
	 * 
	 * @param file, the path of the file for loading the object serialization
	 * 
	 */
	public NoteBook(String file){
		
		//TODO
		//How to load object in memory from file
		FileInputStream fis = null;
		ObjectInputStream in = null;
		
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject();
			this.folders = n.getFolders();
			//this = (NoteBook) in.readObject();?????
			in.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		//TODO
	}
	
	public boolean createTextNote(String folderName, String title){
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}
	
	public boolean createImageNote(String folderName, String title){
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
		
	}
	
	public boolean insertNote(String folderName, Note note){
		Folder f = null;
		for(Folder f1: folders){
			if(f1.getName().equals(folderName))
				f = f1;
		}
		
		if(f == null){
			f = new Folder(folderName);
			folders.add(f);
		}
		
		for(Note n: f.getNotes()){
			if(note.equals(n))
			{
				System.out.println("Creating note " + note.getTitle() + " under folder "+ folderName + " failed");
				return false;
			}
		}
		
		f.addNote(note);
		return true;
	}
	
	public boolean modifyTextNoteContent(String folderName, String title, String content){
		System.out.println(folderName);
		System.out.println(title);
		System.out.println(content);
		System.out.println("==========");
		Note note = new Note(title);
		
		Folder f = null;
		for(Folder f1: folders){
			if(f1.getName().equals(folderName))
				f = f1;
			//System.out.println("===fsfsdfsdf===");
		}
		
		for(Note n: f.getNotes()){
			if(note.equals(n))
			{
				if(n instanceof TextNote){
					((TextNote)n).content = content;
					return true;
				}
				else if(n instanceof ImageNote){
					
				}
			}
		}
		
		return false;
	}
	
	public boolean deleteNote(String folderName, String title){
		/*System.out.println(folderName);
		System.out.println(title);
		System.out.println(content);
		System.out.println("==========");*/
		Note note = new Note(title);
		
		Folder f = null;
		for(Folder f1: folders){
			if(f1.getName().equals(folderName))
				f = f1;
		}
		
		for(Note n: f.getNotes()){
			if(note.equals(n))
			{
				n = null;
				System.out.println("===fsfsdfsdf===");
			}
		}
		
		return false;
	}
	
	public boolean insertFolder(String folderName){
		boolean noSuchFolder = true;
		for(Folder f1: folders){
			if(f1.getName().equals(folderName))
				noSuchFolder = false;
		}
		
		if(noSuchFolder){		
			Folder f = new Folder(folderName);
			folders.add(f);
			System.out.println("add folder: " + folderName);
			return true;
		}
		else{
			return false;
		}
	}
	
	public ArrayList<Folder> getFolders(){
		return this.folders;
	}
	
	public void sortFolders(){
		for(Folder folder: folders)    //???
		{
			folder.sortNotes();
		}
		
		
		//folders.stream().forEach(folder->folder.sortNotes());   //lamda expression
		Collections.sort(folders);
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> matchedNotes = new ArrayList<Note>();
		
		for(Folder folder: folders){
			matchedNotes.addAll(folder.searchNotes(keywords));
		}
		
		return matchedNotes;
	}
	
	//Overloading method createTextNote
	public boolean createTextNote(String folderName, String title, String content){
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	
	//lab5
	/**
	 * method to save the NoteBook instance to file
	 * 
	 * @param file, the path of the file where to save the object serialization 
	 * @return true if save on file is successful, false otherwise
	 */
	
	public boolean save(String file){
		//TODO
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try	{
			//TODO
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
			
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
