package test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

public class ReadWebpage {

	public static int count = 0, countBuffer = 0, countLine = 0;

	// public static String filePath = "/home/surabhi/Desktop/output.txt";
	public static BufferedReader br = null;

	public static String line = "";

	public static String Server = "https://mp-qa2.makaan-ws.com";
	@Test
	public static void RobotFile() {
		String filePath = "./File/output.txt";
		String URL = Server + "/robots.txt";
		FileWriter(filePath, URL);
		if (!(ReadFileRobot(filePath))) {
			Assert.fail("Disallow is present in the file");
		}

	}

	@Test
	public static void SecureSite() {
		String filePath = "./File/output2.xml";
		String URL = Server + "/sitemap/secure-sitemap-index.xml";
		FileWriter(filePath, URL);
		if (!(ReadFileXML(filePath))) {
			Assert.fail("There contains 404 in the xml File");
		}

	}

	@AfterClass(enabled = false)

	public static void SendMail() {

		final String username = "surabhi.jain@proptiger.com";
		final String password = "Nokia&1989";

		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("surabhi.jain@proptiger.com"));
			//message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("amit.sagar@proptiger.com"));
			//message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("vikas.kumar@proptiger.com"));
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("surabhi.jain@proptiger.com"));
			message.setSubject("Report of Robot and SiteMap File");
			message.setText("PFA");

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();

			messageBodyPart = new MimeBodyPart();
			String file = "./test-output/TestNGreport/emailable-report.html";
			String fileName = "attachment name";
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			System.out.println("Sending");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
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

	public static boolean ReadFileXML(String Filepath) {
		try {
			int count = 0;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(Filepath);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("sitemap");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String Value = eElement.getElementsByTagName("loc").item(0).getTextContent();
					System.out.println("loc : " + Value);
					if (Value.contains("404")) {
						System.out.println("success");
						count++;
					}
				}

			}
			if (count > 0) {
				System.out.println("404 found in file" + count + "times");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void FileWriter(String FilePath, String URL) {

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
