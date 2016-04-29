package test;

import org.testng.Assert;

import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLException;

public class ReadWebpage {

	public static int count = 0, countBuffer = 0, countLine = 0;

	// public static String filePath = "/home/surabhi/Desktop/output.txt";
	public static BufferedReader br = null;

	public static String line = "";

	public static String Server = "https://www.makaan.com";

	@Test
	public static void RobotFile() throws Exception {
		String filePath = "./File/output.txt";
		String URL = Server + "/robots.txt";
		// SSlHandle();
		FileWriter(filePath, URL);
		if (!(ReadFileRobot(filePath))) {
			Assert.fail("Robot File Read Test case failed");
		}

	}

	public static boolean ReadFileRobot(String Filepath) {
		try {
			br = new BufferedReader(new FileReader(Filepath));
			try {
				while ((line = br.readLine()) != null) {
					countLine++;
					// System.out.println(line);
					String[] words = line.split(" ");

					for (String word : words) {

						if (word.equals("Disallow:")) {
							count++;
							countBuffer++;
						}
					}
				}
				if (countBuffer > 0) {
					countBuffer = 0;

					System.out.println("Disallow found" + count + "times");
					br.close();
					return false;

				}

				br.close();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
			return false;

		}

	}

	public static void FileWriter(String FilePath, String URL) throws SSLException {

		URL url;

		try {
			// get URL content

			url = new URL(URL);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;

			// save to this filename
			// String fileName = "/home/surabhi/Desktop/output.txt";
			File file = new File(FilePath);

			if (!file.exists()) {
				file.createNewFile();
			}

			// use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
				bw.newLine();

			}

			bw.close();
			br.close();

			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
