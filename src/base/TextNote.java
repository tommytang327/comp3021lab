package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class TextNote extends Note implements Serializable{
	public String content;
	private static final long serialVersionUID = 1L;
	
	public TextNote(String title)
	{
		super(title);
		
	}
	
	/**
	* load a TextNote from File f
	*
	* the tile of the TextNote is the name of the file
	* the content of the TextNote is the content of the file
	*
	* @param File f
	*/
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	/**
	* get the content of a file
	*
	* @param absolutePath of the file
	* @return the content of the file
	*/
	private String getTextFromFile(String absolutePath) {
		try{
			String result = new String(Files.readAllBytes(Paths.get(absolutePath)));
			return result;
		} catch(Exception e){
			return "";
		}
	}
	
	/**
	* export text note to file
	*
	*
	* @param pathFolder path of the folder where to export the note
	* the file has to be named as the title of the note with extension ".txt"
	*
	* if the tile contains white spaces " " they has to be replaced with underscores "_"
	*
	*
	*/
	
	public TextNote(String title, String content)
	{
		super(title);
		this.content = content;
	}
	
	public void exportTextToFile(String pathFolder) {
		//TODO
		
		
		//File file = new File( pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		//System.out.println(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		File file = new File( this.getTitle().replaceAll(" ", "_") + ".txt");
		
		try{
			// if file doesnt exists, then create it 
		    if (!file.exists())
		    {
		        file.createNewFile();
		    }
		    
		    FileWriter fw = new FileWriter( file.getAbsoluteFile( ) );
		    BufferedWriter bw = new BufferedWriter( fw );
		    bw.write(this.content);
		    bw.close();
		    
		} catch(IOException e){
			e.printStackTrace();
		}
	    
		// TODO
		
		
	}
	
	
	/*public String getContent(){    //added by me
		return content;
	}*/
}
