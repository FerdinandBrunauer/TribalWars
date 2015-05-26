package browser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({ "unused", "resource" })
public class js_api {
	private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static int[] toInt = new int[128];

	static {
		for (int i = 0; i < ALPHABET.length; i++) {
			toInt[ALPHABET[i]] = i;
		}
	}

	static String readapikey(String filename) {
		StringBuffer sb = new StringBuffer();
		File file = new File(filename);

		if (file.exists()) {
			try {
				String lineSep = System.getProperty("line.separator");
				BufferedReader br = new BufferedReader(new FileReader(filename));
				String nextLine = "";

				while ((nextLine = br.readLine()) != null) {
					sb.append(nextLine);
					//sb.append(lineSep);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	static String getregex(String data, String regexcheck) {
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(regexcheck);
		Matcher matcher = pattern.matcher(data);

		if (matcher.find()) {
			sb.append(matcher.group(1));
		}

		return sb.toString();
	}

	static String getpic(String filename) {
		StringBuffer sb = new StringBuffer();
		String erg = "";

		try {
			byte[] x = getBytesFromFile(new File(filename));
			erg = encode(x);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return erg;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file.");
		}

		is.close();
		return bytes;
	}

	static void createoutput() {
		try {
			FileWriter fw = new FileWriter("output.txt");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void savepic(String imagedata, String filename) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(filename);
			out.write(decode(imagedata));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String geturl(String urlnext) {
		String back = "";
		InputStream is = null;
		try {
			URL url = new URL(urlnext);
			is = url.openStream();
			back = new Scanner(is).useDelimiter("\\Z").next();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
		}
		return back;
	}

	static String postpic(String pic, String apikey) {
		String back = "";

		try {
			String data = URLEncoder.encode("base64", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
			data += "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("usercaptchaupload", "UTF-8");
			data += "&" + URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apikey, "UTF-8");
			data += "&" + URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode("javaapi", "UTF-8");
			data += "&" + URLEncoder.encode("file-upload-01", "UTF-8") + "=" + URLEncoder.encode(pic, "UTF-8");

			URL url = new URL("http://www.9kw.eu:80/index.cgi");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			back = new Scanner(rd).useDelimiter("\\Z").next();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return back;
	}

	public static String encode(byte[] buf) {
		int size = buf.length;
		char[] ar = new char[((size + 2) / 3) * 4];
		int a = 0;
		int i = 0;
		while (i < size) {
			byte b0 = buf[i++];
			byte b1 = (i < size) ? buf[i++] : 0;
			byte b2 = (i < size) ? buf[i++] : 0;

			int mask = 0x3F;
			ar[a++] = ALPHABET[(b0 >> 2) & mask];
			ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
			ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
			ar[a++] = ALPHABET[b2 & mask];
		}
		switch (size % 3) {
		case 1:
			ar[--a] = '=';
		case 2:
			ar[--a] = '=';
		}
		return new String(ar);
	}

	public static byte[] decode(String s) {
		int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
		byte[] buffer = new byte[s.length() * 3 / 4 - delta];
		int mask = 0xFF;
		int index = 0;
		for (int i = 0; i < s.length(); i += 4) {
			int c0 = toInt[s.charAt(i)];
			int c1 = toInt[s.charAt(i + 1)];
			buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c2 = toInt[s.charAt(i + 2)];
			buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c3 = toInt[s.charAt(i + 3)];
			buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
		}
		return buffer;
	}
}
