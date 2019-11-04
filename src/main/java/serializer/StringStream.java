package serializer;

import java.io.*;

public class StringStream {

	/**
	 * Writes the given String to a given OutputStream
	 *
	 * @param str String to be written
	 * @param os  OutputStream to be written to
	 * @throws IOException Error occurred while writing to stream
	 */
	public static void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(str);
		bw.flush();
	}

	/**
	 * Reads the given InputStream to a String
	 *
	 * @param is InputStream to be read from
	 * @return String containing the converted stream
	 * @throws IOException Error occurred while reading from stream
	 */
	public static String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}
}
