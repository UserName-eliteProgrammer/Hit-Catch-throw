import java.io.*;
import java.net.*;


public class ServerSide 
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Server");
		int portNum = Constants.port, choice;
		ServerSocket serverSocketObj = new ServerSocket(portNum);
		Socket serverSideSocket =  serverSocketObj.accept();
		DataInputStream dataInputStreamObj = new DataInputStream(serverSideSocket.getInputStream());
		
		choice = dataInputStreamObj.readInt();
		
		if(choice == 1)
		{
			// receiving file
			recievingFile(serverSideSocket);
		}
		else // wrong choice is tackled at client side
		{
			sendFile(serverSideSocket);
		}
		serverSideSocket.close();
		serverSocketObj.close();
	}
	
	
	private static void recievingFile(Socket serverSideSocket) throws Exception
	{	
		int fileNameLen, fileContentLen;
		DataInputStream dataInputStreamObj = new DataInputStream(serverSideSocket.getInputStream());
		
		fileNameLen = dataInputStreamObj.readInt();
		if(fileNameLen > 0)
		{
			// dealing with file title
			byte[] fileNameInBytes = new byte[fileNameLen];
			dataInputStreamObj.readFully(fileNameInBytes, 0, fileNameLen); // read data from input stream upTo given length
			
			
			// dealing with content of file
			fileContentLen = dataInputStreamObj.readInt();
			byte[] fileContentInBytes = new byte[fileContentLen];
			dataInputStreamObj.readFully(fileContentInBytes, 0, fileContentLen);
			
			// creating a file at server end and writing it
			File fileRecieved = new File(Constants.serverStoragePath);
			FileOutputStream fileOutputStreamObj = new FileOutputStream(fileRecieved);
			fileOutputStreamObj.write(fileContentInBytes);
			fileOutputStreamObj.close();
		}
	}
	
	private static void sendFile(Socket serverSideSocket) throws Exception
	{
		// send file
		
		DataInputStream dataInputStreamObj = new DataInputStream(serverSideSocket.getInputStream());
		String pathOfStorage = Constants.serverStoragePath;
		int fileNameLen = dataInputStreamObj.readInt();
					
				
		byte[] fileNameInBytes = new byte[fileNameLen];
					
		// reading name from input stream
		dataInputStreamObj.readFully(fileNameInBytes, 0, fileNameLen);
		String fileName = new String(fileNameInBytes);

					
		// checking file exists or not
		File fileObj = new File(Constants.serverStoragePath);
		if(fileObj.exists() == true)
		{
			int contentLen = (int)fileObj.length();
						

						
			FileInputStream fileInpStreamObj = new FileInputStream(fileObj.getAbsolutePath());
						
			// filling array with content
			byte[] contentOfFileInBytes = new byte[contentLen];
			fileInpStreamObj.read(contentOfFileInBytes);
						
			// send file content
			DataOutputStream dataOutputStreamObj = new DataOutputStream(serverSideSocket.getOutputStream());
						
			dataOutputStreamObj.writeInt(contentLen);
			System.out.println(contentLen);
			dataOutputStreamObj.write(contentOfFileInBytes);
						
		}
		else 
		{
			System.out.println("File Do not exist at Server Side");
		}
	}
}
