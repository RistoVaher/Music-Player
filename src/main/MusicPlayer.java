package main;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MusicPlayer extends Application {
	
	@Override
	public void start(Stage arg0) throws Exception {

		String resourcePath = "MusicFXML.fxml";
		URL location = getClass().getResource(resourcePath);
		FXMLLoader loader = new FXMLLoader(location);
		try {
			arg0.setTitle("Quick Music App");
			arg0.setScene(new Scene(loader.load()));
		//	arg0.getIcons().add(new Image("C:\\Users\\Suici\\Desktop\\icons\\appicon.gif"));
			arg0.show();
		} catch (IOException e) {
			e.printStackTrace();
			Platform.exit();
		}

	}
	public static void main(String[] args) {
		
		launch(args);
	}

}