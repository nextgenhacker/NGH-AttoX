import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Downloader {

	File f = null;
	int v;

	public File getFile() {
		return f;
	}

	public int getVersion() {
		return v;
	}

	public Downloader() throws IOException {
		URL url = new URL("http://rs-hacking.com/resources/rsversion");
		BufferedReader d = new BufferedReader(new InputStreamReader(url
				.openStream()));
		char[] bytes = new char[3];
		d.read(bytes);
		v = Integer.valueOf(new String(bytes));

		f = new File("runescape.jar");
		if (!f.exists()) {
			System.out.println("Downloading RuneScape JAR Client #" + v);
			InputStream in = new URL(
					"http://world1.runescape.com/runescape.jar").openStream();
			FileOutputStream out = new FileOutputStream(f);
			byte[] buf = new byte[700];
			int i;
			while ((i = in.read(buf)) != -1)
				out.write(buf, 0, i);
			in.close();
			out.close();
			System.out.println("Completed download");
		}
	}
}
