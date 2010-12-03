import java.io.IOException;


public class Starter {

	public static void main(String[] args) throws IOException {
		Downloader d = new Downloader();
		GUI g = new GUI("world33");
		g.setVisible(true);
	}
}
