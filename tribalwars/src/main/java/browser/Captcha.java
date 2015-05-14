package browser;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Captcha {
	private String solution;
	private byte[] pic;

	public Captcha(byte[] pic) {
		this.pic = pic;
	}

	public void printImg(String picName) throws MalformedURLException,
			IOException {
		File picFile = new File(picName);
		FileOutputStream out = new FileOutputStream(picFile);
		out.write(pic);
		out.flush();
		out.close();
	}

	public String solveCapture() throws MalformedURLException, IOException {
		String regex = "^[0-9]+ .+";
		String solution = null;
		String apikey = js_api.readapikey("apikey.txt");
		js_api.createoutput();
		String id = js_api.postpic(js_api.encode(pic), apikey);

		boolean matches2 = id.matches(regex);
		if (matches2) {
			return null; 
		} else {
			try {
				Thread.sleep(20000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			do {
				solution = js_api
						.geturl("http://www.9kw.eu:80/index.cgi?action=usercaptchacorrectdata&source=javaapi&apikey="
								+ apikey + "&id=" + id);
			} while(solution.compareTo("") == 0);
			this.solution = solution;
			return solution;
		}
	}

	public String getSolution() {
		return solution;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		//Test Request
		InputStream in = new BufferedInputStream(new URL("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcS44G5i_EWD7cKVUrT0ngofaTYDdW3AZYvptpKc4loyYecb6vMb").openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		Captcha testCaptcha = new Captcha(response);
		testCaptcha.printImg("Hallo.jpg");
		System.out.println(testCaptcha.solveCapture());
	}
}
