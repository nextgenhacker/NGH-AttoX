import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class GUI extends JFrame implements AppletStub {

	private static final long serialVersionUID = 7290678744249645100L;
	private String prefix = "";
	private String referer = "http://www.google.com/#hl=en&source=hp&q=runescape&btnG=Google+Search&meta=&aq=f&oq=runescape&fp=324c8a90db775e43";
	private final HashMap<String, String> parameters = new HashMap<String, String>();

	public GUI(String prefix) {
		try {
			this.prefix = prefix;
			loadParams();
			loadClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readPage(URL url) throws IOException, InterruptedException {
		String s = readPage(url, referer);
		referer = url.toExternalForm();
		return s;
	}

	private String readPage(URL url, String referer) throws IOException,
			InterruptedException {
		URLConnection uc = url.openConnection();
		uc
				.addRequestProperty(
						"Accept",
						"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		uc.addRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		uc.addRequestProperty("Accept-Encoding", "gzip,deflate");
		uc.addRequestProperty("Accept-Language", "en-gb,en;q=0.5");
		uc.addRequestProperty("Connection", "keep-alive");
		uc.addRequestProperty("Host", "www.runescape.com");
		uc.addRequestProperty("Keep-Alive", "300");
		if (referer != null)
			uc.addRequestProperty("Referer", referer);
		uc
				.addRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.8.0.6) Gecko/20060728 Firefox/1.5.0.6");
		DataInputStream di = new DataInputStream(uc.getInputStream());
		byte[] buffer = new byte[uc.getContentLength()];
		di.readFully(buffer);
		di.close();
		Thread.sleep(250 + (int) Math.random() * 500);
		return new String(buffer);
	}

	private final void loadParams() throws MalformedURLException, IOException,
			InterruptedException {
		String html = readPage(new URL("http://" + prefix
				+ ".runescape.com/a2,m0,j0,o0"));
		try {
			Pattern regex = Pattern.compile(
					"<param name=([^\\s]+)\\s+value=\"{0,1}(.*?)\"{0,1}>",
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher regexMatcher = regex.matcher(html);
			while (regexMatcher.find())
				if (!parameters.containsKey(regexMatcher.group(1)))
					parameters
							.put(regexMatcher.group(1), regexMatcher.group(2));
		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
	}

	private final void loadClient() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			MalformedURLException {
		URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(
				"file:runescape.jar") });
		Applet client = (Applet) classLoader.loadClass("client").newInstance();
		client.setStub(this);
		client.init();
		client.start();

		JMenuBar jMenu = new JMenuBar();
		JMenu menu = new JMenu("Bot");
		JMenu menu1 = new JMenu("Debug");
		JMenu menu2 = new JMenu("Preferences");
		JMenu menu3 = new JMenu("About");
		JMenuItem jmi = new JMenuItem("New Bot");
		
		menu.add(jmi);
		jMenu.add(menu);
		jMenu.add(menu1);
		jMenu.add(menu2);
		jMenu.add(menu3);
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JPanel backPanel = new JPanel(new BorderLayout());
		backPanel.setPreferredSize(new Dimension(768, 560));
		setPreferredSize(new Dimension(768, 658));
		backPanel.add(client);
		getContentPane().add(client, BorderLayout.CENTER);
		getContentPane().add(jMenu, BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(769, 531);
	}

	@Override
	public void appletResize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public AppletContext getAppletContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL("http://" + prefix + ".runescape.com/");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public URL getDocumentBase() {
		return getCodeBase();
	}

	@Override
	public String getParameter(String name) {
		return parameters.get(name);
	}
}