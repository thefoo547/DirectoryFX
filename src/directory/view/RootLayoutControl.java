package directory.view;

import directory.MainApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class RootLayoutControl {
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private final FileChooser.ExtensionFilter ext=new FileChooser.ExtensionFilter(
            "XML files (*.xml)","*.xml");

    private MainApp mainApp;

    @FXML
    private void handleNew(){
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    @FXML
    private void handleOpen(){
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(ext);
        //save file dialog
        File f=fc.showOpenDialog(mainApp.getPrimaryStage());
        if(f != null){
            mainApp.loadDataFromFile(f);
        }
    }


    @FXML
    private void handleSave(){
        File pf=mainApp.getPersonPath();
        if(pf!=null){
            mainApp.saveDataToFile(pf);
        }else{
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs(){
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().add(ext);
        //save file dialog
        File f=fc.showSaveDialog(mainApp.getPrimaryStage());
        if(f != null){
            if(!f.getPath().endsWith(".xml")){
                f=new File(f.getPath()+".xml");
            }
            mainApp.saveDataToFile(f);
        }
    }

}
