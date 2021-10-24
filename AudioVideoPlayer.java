package metro;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class AudioVideoPlayer extends Thread {
	//String[] arrAudio ;
	//String[] arrVideo ;
	int currAudChan = View.currAudChan; 
	int currVidChan = View.currVidChan; 
	String type ;
	public AudioVideoPlayer(String type) {
		this.type = type;
	}
	/**
	 * Initialising the Arrays with the Audio and Video files.
	 */
	@Override
	public void run() {
		String[] arrVideo = {
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\0.mp4",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\1.mp4",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\2.mp4",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\3.mp4",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\4.mp4",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Video\\5.mp4"
							};
		String[] arrAudio = {
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\0.mp3",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\1.mp3",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\2.mp3",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\3.mp3",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\4.mp3",
				"C:\\Users\\Mohamed Bahgat\\Desktop\\OS\\Project\\Audio\\5.mp3"
							};
		
		// Playing the specific the Video/Audio
		if(type == "Video") {
			try {
			Desktop.getDesktop().open(new File(arrVideo[currVidChan]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type == "Audio"){
			try {
				Desktop.getDesktop().open(new File(arrAudio[currAudChan]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	public void kill(String type) {
		if(type == "Video") {
			try {
				Runtime.getRuntime().exec("taskkill /IM GOM.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(type == "Audio"){
			try {
				Runtime.getRuntime().exec("taskkill /IM wmplayer.exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
