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
		System.out.println("Client\n\n");
		int choiceVar;
		do
		{	
			System.out.println("Make Your Choice : Press 1 for Sending, Press 2 For Downloading.");
			Scanner scn = new Scanner(System.in);
			
					
			choiceVar = scn.nextInt();
			
			if(choiceVar == 1)
			{				
				try 
				{
					sendFile();
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(choiceVar == 2)
			{	
				try 
				{
					download();
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
				break;
			}
		}while(true);
	}
	
	private static void download() throws Exception
	{	
		// creating socket and outPutstream
		Socket clientSocket = new Socket(ip, port);
		DataOutputStream dataOutputStreamObj = new DataOutputStream(clientSocket.getOutputStream());
		Scanner sc = new Scanner(System.in);
		
		// sending file info
		String fileName = sc.next();
		
		byte[] fileNameInBytes = new byte[fileName.length()];
		
		fileNameInBytes = fileName.getBytes();
			
		dataOutputStreamObj.writeInt(2); // telling server our choice
		dataOutputStreamObj.writeInt(fileNameInBytes.length); // sending size of fileName
		dataOutputStreamObj.write(fileNameInBytes); // sending fileName
		
		// Receiving file and creating a copy on client side
		File fileObj = new File(Constants.clientDownloadingPath + fileName);
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
	
	private static void sendFile() throws Exception
	{	
		// file which to be sent
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Name of File : ");
		String fileName = sc.nextLine();
		
		System.out.println("Enter Text For File : ");
		String contentOfFile = sc.nextLine();
		
		byte[] fileNameInBytes = new byte[(fileName.length())];
		byte[] contentOfFileInBytes = new byte[contentOfFile.length()];
				
				
		// filling byte array with fileName
		fileNameInBytes = fileName.getBytes();
		contentOfFileInBytes  = contentOfFile.getBytes();
						
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
