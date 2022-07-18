import java.util.*;
import java.io.*;
import java.net.Socket;


public class ClientSide 
{	
	static int port = Constants.port;
	static String ip = Constants.ip;
	
	public static void main(String[] args)
	{	
		// choice making
		int choiceVar;
		System.out.println("Client\n\n");
		System.out.println("Make Your Choice : Press 1 for Sending, Press 2 For Downloading.");
		Scanner sc = new Scanner(System.in);
		choiceVar = sc.nextInt();
		
		
		if(choiceVar == 1)
		{	
			String pathOfFile = Constants.clientFilePath;
//			System.out.print("Enter Path of File : ");
//			pathOfFile = sc.next();
			try 
			{
				sendFile(pathOfFile);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(choiceVar == 2)
		{	
			String fileName = "abc.txt";
//			fileName = sc.next();
			try 
			{
				download(fileName);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Invalid Choice");
		}
		sc.close();
	}
	
	private static void download(String fileName) throws Exception
	{	
		// creating socket and outPutstream
		Socket clientSocket = new Socket(ip, port);
		DataOutputStream dataOutputStreamObj = new DataOutputStream(clientSocket.getOutputStream());
		
		// sending file info
		byte[] fileNameInBytes = new byte[fileName.length()];
		
		fileNameInBytes = fileName.getBytes();
			
		dataOutputStreamObj.writeInt(2); // telling server our choice
		dataOutputStreamObj.writeInt(fileNameInBytes.length); // sending size of fileName
		dataOutputStreamObj.write(fileNameInBytes); // sending fileName
		
		// Receiving file and creating a copy on client side
		File fileObj = new File(Constants.clientDownloadingPath);
		DataInputStream dataInputStreamObj = new DataInputStream(clientSocket.getInputStream());
		
		// Receiving file
		int fileContentLen = dataInputStreamObj.readInt();
		
		
		byte[] fileContentInBytes = new byte[fileContentLen];
		dataInputStreamObj.readFully(fileContentInBytes, 0, fileContentLen);
		
		// writing file
		FileOutputStream fileOutputStreamObj = new FileOutputStream(fileObj);
		fileOutputStreamObj.write(fileContentInBytes);
		fileOutputStreamObj.close();
		
		clientSocket.close();
	}
	
	private static void sendFile(String path) throws Exception
	{	
		// file which to be sent
		File fileToSend = new File(path);
		String fileName = fileToSend.getName(); 
				
		// file to program stream
		FileInputStream fileInputStreamObj = new FileInputStream(fileToSend.getAbsolutePath()); 
		byte[] fileNameInBytes = new byte[(fileName.length())];
		byte[] contentOfFileInBytes = new byte[(int)(fileToSend.length())];
				
				
		// filling byte array with fileName
		fileNameInBytes = fileName.getBytes();	
				
		// read method fill the array with content of file
		fileInputStreamObj.read(contentOfFileInBytes);
		
		// Socket and sending file
		Socket clientSocket = new Socket(ip, port);
		DataOutputStream dataOutputStreamObj = new DataOutputStream(clientSocket.getOutputStream());  
				
		dataOutputStreamObj.writeInt(1); // telling server to our choice
				
		dataOutputStreamObj.writeInt(fileNameInBytes.length); // sending size of title of file to outputStream
		dataOutputStreamObj.write(fileNameInBytes); // sending content of file to outputStream
		dataOutputStreamObj.writeInt(contentOfFileInBytes.length); // sending content length of file to outputStream
		dataOutputStreamObj.write(contentOfFileInBytes); // sending content of file to outputStream
				
		clientSocket.close();
	}
}
