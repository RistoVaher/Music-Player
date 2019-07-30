package main;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * 
 * @author Suic Start Date: 9.06.2019
 * TODO: finish it.
 */
public final class MusicPlayerController implements Initializable {

	@FXML
	private Button testButton;

	@FXML
	private Slider volumeSlider;

	@FXML
	private TextField currentlyPlayingField, startTime, endTime;

	@FXML
	private ListView<String> songView;

	@FXML
	private CheckBox keepPlayingBox;

	private MediaPlayer mediaPlayer;

	@FXML
	private ProgressBar lengthBar;

	boolean isPlaying;

	private int start = 0; // custom start time
	private int end = 0; // custom end time

	private double currentVolume = 0.5;

	private double initialLength = 0;

	private final String FILE_PATH = "C:\\Users\\Suic\\Desktop\\songs\\";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		loadSongs(new File(FILE_PATH));

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> var1, Number var2, Number var3) {
				if (mediaPlayer == null)
					return;
				mediaPlayer.setVolume(var3.doubleValue() / 100);
				currentVolume = var3.doubleValue() / 100;
				System.out.println("Current volume: " + var3.intValue());
			}
		});

		testButton.setOnMouseClicked(x -> { // stop button
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				System.out.println("Stopped");
			} else {
				System.out.println("Was already null");
			}
		});

		songView.setOnMouseClicked(x -> {
			if (startTime.getText().length() > 0 && endTime.getText().length() < 1) {
				start = Integer.parseInt(startTime.getText());
				playFrom(songView.getSelectionModel().getSelectedItem(), start, keepPlayingBox.isSelected());
			} else if (startTime.getText().length() > 0 && endTime.getText().length() > 1) {
				start = Integer.parseInt(startTime.getText());
				end = Integer.parseInt(endTime.getText());
				playFrom(songView.getSelectionModel().getSelectedItem(), start, end, keepPlayingBox.isSelected());
			} else {
				playFrom(songView.getSelectionModel().getSelectedItem(), keepPlayingBox.isSelected());
			}
			System.out.println("Start time length: " + startTime.getText().length());
			System.out.println("End time length: " + endTime.getText().length());
			currentlyPlayingField.setText("Currently playing: " + songView.getSelectionModel().getSelectedItem());
			sequence();
		});
	}

	private final void loadSongs(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				loadSongs(fileEntry);
			} else {
				int index = fileEntry.getName().indexOf(SEPERATOR);
				songView.getItems().add(fileEntry.getName().substring(0, index));
			}
		}
	}

	private final void sequence() { // unfinished
		Runnable startAutoplay = new Runnable() {
			public void run() {
				lengthBar.setProgress(
						mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
				System.out.println("Progress atm: " + lengthBar.getProgress());
				System.out.println("Total duration: " + mediaPlayer.getTotalDuration().toSeconds());
				// }
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(startAutoplay, 2500, 100, TimeUnit.MILLISECONDS);
	}

	private final void playNextSong() { // not used atm
		int selectionIndex = songView.getSelectionModel().getSelectedIndex();
		System.out.println("Name at this index: " + songView.getSelectionModel().getSelectedIndex());
		songView.getSelectionModel().select(selectionIndex + 1);
		playFrom(songView.getSelectionModel().getSelectedItem(), false);
	}

	private final void playFrom(String songName, final int startTime, final int endTime, final boolean keepPlaying) {
		if (mediaPlayer != null) {
			System.out.println("Skipped current song.");
			mediaPlayer.stop();
		}
		String path = FILE_PATH + songName + SEPERATOR;
		Media song = new Media(Paths.get(path).toUri().toString());
		mediaPlayer = new MediaPlayer(song);
		mediaPlayer.setStartTime(Duration.seconds(startTime));
		mediaPlayer.setStopTime(Duration.seconds(endTime));
		initialLength = endTime - startTime;
		if (keepPlaying) {
			mediaPlayer.setCycleCount((int) Duration.INDEFINITE.toSeconds());
		}
		mediaPlayer.setVolume(currentVolume);
		mediaPlayer.play();
	}

	private final String SEPERATOR = ".mp3";

	private final void playFrom(final String songName, final int startTime, final boolean keepPlaying) {
		if (mediaPlayer != null) {
			System.out.println("Skipped current song.");
			mediaPlayer.stop();
		}
		String path = FILE_PATH + songName + SEPERATOR;
		Media song = new Media(Paths.get(path).toUri().toString());
		mediaPlayer = new MediaPlayer(song);
		mediaPlayer.setStartTime(Duration.seconds(startTime));
		initialLength = mediaPlayer.getTotalDuration().toSeconds() - startTime;
		if (keepPlaying) {
			mediaPlayer.setCycleCount((int) Duration.INDEFINITE.toSeconds());
		}
		mediaPlayer.setVolume(currentVolume);
		mediaPlayer.play();
	}

	private final void playFrom(final String songName, final boolean keepPlaying) {
		if (mediaPlayer != null) {
			System.out.println("Skipped current song.");
			mediaPlayer.stop();
		}
		String path = FILE_PATH + songName + SEPERATOR;
		Media song = new Media(Paths.get(path).toUri().toString());
		mediaPlayer = new MediaPlayer(song);
		initialLength = mediaPlayer.getTotalDuration().toSeconds();
		if (keepPlaying) {
			mediaPlayer.setCycleCount((int) Duration.INDEFINITE.toSeconds());
		}
		mediaPlayer.setVolume(currentVolume);
		mediaPlayer.play();
	}

}
