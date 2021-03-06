package ui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	/**
	 * current note selected by the user
	 */
	String currentNote = "";
	
	Stage stage;

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		
		this.stage = stage;
		
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());   //top pane
		border.setLeft(addVBox());    //left Pane
		border.setCenter(addGridPane());    //center Pane

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
		
		
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	
	
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setDisable(false);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent event){
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialize Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				
				if(file!=null){
					loadNoteBook(file);
				}
			}
		});
		
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setDisable(false);
		buttonSave.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialize Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				

				
				if (file!=null && noteBook.save(file.getAbsolutePath())){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file "
					+ file.getName());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
			}
		});
		
		hbox.getChildren().addAll(buttonLoad, buttonSave);
		
		TextField textFieldSearch=new TextField();
		textFieldSearch.setPrefSize(200, 20);
		
		hbox.getChildren().add(new Label("Search: "));
		hbox.getChildren().add(textFieldSearch);
		
		//search button start
		Button buttonSearch = new Button("Search");
		buttonSearch.setPrefSize(60, 20);
		buttonSearch.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				currentSearch=textFieldSearch.getText();
				textAreaNote.setText("");
				currentNote = "";
				updateListView();
			}
		});
		
		//search button end
		
		//clear button start
		Button buttonClear = new Button("Clear Search");
		buttonClear.setPrefSize(100, 20);
		buttonClear.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				currentSearch="";
				textFieldSearch.setText("");
				textAreaNote.setText("");
				currentNote = "";
				updateListView();
			}
		});
		
		
		//clear button end
		
		hbox.getChildren().addAll(buttonSearch, buttonClear);


		return hbox;
	}
	
	

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		
		HBox hbox = new HBox();
		
		vbox.setPadding(new Insets(10)); // tSet all sides to 10
		vbox.setSpacing(8); // Gap btetween nodes

		//foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
		for(Folder f : noteBook.getFolders()){    //lab1 load folder name and store in combo boc list
			foldersComboBox.getItems().add(f.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				
				
				
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";
				
				int index=noteBook.getFolders().indexOf(new Folder(currentFolder));
				
				if(index >= 0){
					Folder folder = noteBook.getFolders().get(index);
					int noteIndex = folder.getNotes().indexOf(new TextNote(title));
					if(index >=0 ){
						content = ((TextNote)folder.getNotes().get(noteIndex)).content;
					}
				}
				
				textAreaNote.setText(content);
				currentNote = content;

			}
		});
		
		
		vbox.getChildren().add(new Label("Choose folder: "));
		
		//hbox -- group combo box and add a folder button together
		
		hbox.getChildren().add(foldersComboBox);
		
		Button buttonAddAFolder = new Button("Add a Folder");
		buttonAddAFolder.setOnAction(e->{
			/*Stage stage = new Stage();
			BorderPane border = new BorderPane();
			
			Scene scene = new Scene(border, 450, 450);
			
			stage.setTitle("Input");
			stage.setScene(scene);
			
			// add top, left and center
			//border.setTop(addHBox());   //top pane
			//border.setLeft(addVBox());    //left Pane
			//border.setCenter(addGridPane());    //center Pane
			
			stage.show();*/
			
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successfully saved");
			alert.setContentText("Please enter the name you want to create:   ");
			
			TextInputDialog dialog = new TextInputDialog("Add a Folder");
			dialog.setTitle("Input");
			dialog.setHeaderText("Add a new folder for your notebook");
			dialog.setContentText("Please enter the name you want to create:");
			
			//Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()){   //if click OK, do the following
				
				if(result.get().equals("")){
					Alert alertEmpty = new Alert(AlertType.WARNING);
					alertEmpty.setTitle("Warning");
					alertEmpty.setContentText("Please input an valid folder name");
					alertEmpty.showAndWait().ifPresent(rs-> {
						if(rs == ButtonType.OK){
							System.out.println("OK");
						}
					});
				}
				else{
					if(noteBook.insertFolder(result.get())){
						foldersComboBox.getItems().clear();
						
						for(Folder f : noteBook.getFolders()){    //lab1 load folder name and store in combo boc list
							foldersComboBox.getItems().add(f.getName());
						}
						
						foldersComboBox.setValue(result.get());
					}
					else{
						Alert alertSameName = new Alert(AlertType.WARNING);
						alertSameName.setTitle("Warning");
						alertSameName.setContentText("You already have a folder name with " + result.get());
						alertSameName.showAndWait().ifPresent(rs-> {
							if(rs == ButtonType.OK){
								System.out.println("OK");
							}
						});
					}
					
					
				}
			}
			
		});
		
		
		hbox.getChildren().add(buttonAddAFolder);
		
		
		vbox.getChildren().add(hbox);
		
		//vbox
		
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		
		Button buttonANote = new Button("Add a Note");
		
		buttonANote.setOnAction(e->{
			boolean folderComboBoxEmpty = (foldersComboBox.getSelectionModel().isEmpty()) && (foldersComboBox.getValue().toString().equals("-----"));
			
			if(folderComboBoxEmpty){    
				Alert alertComboEmpty = new Alert(AlertType.WARNING);
				alertComboEmpty.setTitle("Warning");
				alertComboEmpty.setContentText("Please choose a folder first!");
				alertComboEmpty.showAndWait().ifPresent(rs->{
					if(rs == ButtonType.OK){
						System.out.println("OK");
					}
				});
			}
			else{
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Input");
				alert.setContentText("Please enter the name of your note:   ");*/
				
				TextInputDialog dialog = new TextInputDialog("Add a Note");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new note to current folder");
				dialog.setContentText("Please enter the name of your note:");
				
				//Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if(result.isPresent()){
					boolean textNoteInsertSuccess = noteBook.createTextNote(foldersComboBox.getValue().toString(), result.get());
					
					if(textNoteInsertSuccess){
						Alert alertSuccess = new Alert(AlertType.INFORMATION);
						alertSuccess.setTitle("Successful!");
						alertSuccess.setContentText("Insert note " + result.get() + "to folder " + foldersComboBox.getValue().toString() + "successfully!");
						alertSuccess.showAndWait().ifPresent(rs-> {
							if(rs == ButtonType.OK){
								System.out.println("OK");
								updateListView();
							}
						});
					}
				}
			}
		});
		
		
		vbox.getChildren().add(buttonANote);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		
		int index = noteBook.getFolders().indexOf(new Folder(currentFolder));
		
		if(index >= 0){
			Folder folder = noteBook.getFolders().get(index);
			
			/*if (currentSearch!=""){		
				for(Note note : folder.searchNotes(currentSearch)){   //fetch a list of notes of the specified folder
					list.add(note.getTitle());
				}    //without search function
			}
			else {
				for(Note note : noteBook.getFolders().get(index).getNotes()){   //fetch a list of notes of the specified folder
					list.add(note.getTitle());
				}    //without search function
			
			}*/
			
			List<Note> notelist = folder.getNotes();
			if (!currentSearch.equals("")){
				notelist = folder.searchNotes(currentSearch);
			}
			
			for (Note n: notelist){
				list.add(n.getTitle());
			}
		}
		

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
		currentNote = "";
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		//save note and delete note button
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		
		Button buttonSaveNote = new Button("Save Note");
		buttonSaveNote.setOnAction(e->{
			boolean folderComboBoxEmpty = (foldersComboBox.getSelectionModel().isEmpty()) && (foldersComboBox.getValue().toString().equals("-----"));
			//boolean noteSelectedIsEmpty = titleslistView.selectedItems.Count;
			
			//System.out.println(folderComboBoxEmpty);
			//System.out.println(noteSelectedIsEmpty);
			
			if(folderComboBoxEmpty ){
				Alert alertEmpty = new Alert(AlertType.WARNING);
				alertEmpty.setTitle("Warning");
				alertEmpty.setContentText("Please select a folder and a note");
				alertEmpty.showAndWait().ifPresent(rs-> {
					if(rs == ButtonType.OK){
						System.out.println("OK");
					}
				});
			}
			else{
				noteBook.modifyTextNoteContent(foldersComboBox.getValue().toString(), titleslistView.getSelectionModel().getSelectedItem(), textAreaNote.getText());
			}
			
		});
		
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);

		Button buttonDeleteNote = new Button("Delete Note");
		buttonDeleteNote.setOnAction(e->{
			boolean folderComboBoxEmpty = (foldersComboBox.getSelectionModel().isEmpty()) && (foldersComboBox.getValue().toString().equals("-----"));
			//boolean noteSelectedIsEmpty = titleslistView.selectedItems.Count;
			
			//System.out.println(folderComboBoxEmpty);
			//System.out.println(noteSelectedIsEmpty);
			
			if(folderComboBoxEmpty ){
				Alert alertEmpty = new Alert(AlertType.WARNING);
				alertEmpty.setTitle("Warning");
				alertEmpty.setContentText("Please select a folder and a note");
				alertEmpty.showAndWait().ifPresent(rs-> {
					if(rs == ButtonType.OK){
						System.out.println("OK");
					}
				});
			}
			else{
				noteBook.deleteNote(foldersComboBox.getValue().toString(), titleslistView.getSelectionModel().getSelectedItem());
				updateListView();
			}
			
			
			
			//noteBook.deleteNote(foldersComboBox.getValue().toString(), titleslistView.getSelectionModel().getSelectedItem());
		});
		
		grid.add(saveView, 0, 0);   //column = 0, row = 0
		grid.add(buttonSaveNote, 1, 0);
		grid.add(deleteView, 2, 0);   //column = 3, row = 0
		grid.add(buttonDeleteNote, 3, 0);   
		
		textAreaNote.setEditable(false);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		grid.add(textAreaNote, 0, 1);    //column = 0, row = 1

		textAreaNote.setEditable(true);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called â€œthe most shocking play in NFL historyâ€� and the Washington Redskins dubbed the â€œThrowback Specialâ€�: the November 1985 play in which the Redskinsâ€™ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Awardâ€“winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everythingâ€”until it wasnâ€™t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliantâ€”a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwetherâ€™s Daddy Was a Number Runner and Dorothy Allisonâ€™s Bastard Out of Carolina, Jacqueline Woodsonâ€™s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthoodâ€”the promise and peril of growing upâ€”and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	
	private void loadNoteBook(File file){
		NoteBook nb = new NoteBook(file.getAbsolutePath());
		noteBook = nb;
	}

}
