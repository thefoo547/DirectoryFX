package directory;

import directory.model.Person;
import directory.model.PersonWrappper;
import directory.view.PersonDialogControl;
import directory.view.PersonOverviewControl;
import directory.view.PersonStaticsControl;
import directory.view.RootLayoutControl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Directory FX");
        this.primaryStage.getIcons().add(new Image("file:resources/img/main_ico.png"));
        initRootLayout();
        showPersonOverview();

    }

    public void initRootLayout(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutControl controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getPersonPath();
        if (file != null) {
            loadDataFromFile(file);
        }
    }

    public void showPersonOverview(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            // Give the controller access to the main app.
            //Recuerden Amigos, no es lo mismo id que fx:id
            PersonOverviewControl controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonDialogControl controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //file preferences
    public File getPersonPath(){
        Preferences pr = Preferences.userRoot().node(this.getClass().getName());
        String path=pr.get("path", null);
        if(path != null){
            return new File(path);
        }else{
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    public void loadDataFromFile(File f){
        try{
            JAXBContext context=JAXBContext.newInstance(PersonWrappper.class);
            Unmarshaller um=context.createUnmarshaller();
            PersonWrappper pw=(PersonWrappper) um.unmarshal(f);
            personData.clear();
            personData.addAll(pw.getPeople());
            setPersonFilePath(f);
        }catch (Exception e){
            msgerr(e.getMessage());
        }
    }

    public void saveDataToFile(File f){
        try{
            JAXBContext context=JAXBContext.newInstance(PersonWrappper.class);
            Marshaller m=context.createMarshaller();
            PersonWrappper pw=new PersonWrappper();
            pw.setPeople(personData);
            m.marshal(pw,f);
            setPersonFilePath(f);
        }catch (Exception e){
            msgerr(e.getMessage());
        }
    }

    public void showPersonStatics(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonStatics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene sc = new Scene(page);
            dialogStage.setScene(sc);
            //set birthday statics
            PersonStaticsControl control=loader.getController();
            control.setPersonData(personData);
            dialogStage.show();
        }catch (IOException ex){
            msgerr(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void msgerr(String msg){
        Alert al=new Alert(Alert.AlertType.ERROR);
        al.setTitle("DirectoryFX");
        al.setHeaderText("ERROR");
        al.setContentText(msg);
        al.showAndWait();
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
