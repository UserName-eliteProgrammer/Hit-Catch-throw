import java.io.*;
import java.net.*;


public class ServerSide 
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Server");
		// declaring and initializing required variables
		int portNum = 9999, fileNameLen, fileContentLen, choice;
		
		ServerSocket serverSocketObj = new ServerSocket(portNum);
		Socket serverSideSocket =  serverSocketObj.accept();
		DataInputStream dataInputStreamObj = new DataInputStream(serverSideSocket.getInputStream());
		
		choice = dataInputStreamObj.readInt();
		
		if(choice == 1)
		{
			// receiving file
			fileNameLen = dataInputStreamObj.readInt();
			if(fileNameLen > 0)
			{
				// dealing with file title
				byte[] fileNameInBytes = new byte[fileNameLen];
				dataInputStreamObj.readFully(fileNameInBytes, 0, fileNameLen); // read data from input stream upTo given length
				String fileName = new String(fileNameInBytes);
//				
				
				// dealing with content of file
				fileContentLen = dataInputStreamObj.readInt();
				byte[] fileContentInBytes = new byte[fileContentLen];
				dataInputStreamObj.readFully(fileContentInBytes, 0, fileContentLen);
				
				// creating a file at server end and writing it
				File fileRecieved = new File( "C:\\Users\\Manish Sharma\\Downloads\\" + fileName);
				FileOutputStream fileOutputStreamObj = new FileOutputStream(fileRecieved);
				fileOutputStreamObj.write(fileContentInBytes);
				fileOutputStreamObj.close();
			}	
		}
		else // wrong choice is tackled at client side
		{
			// send file
			String pathOfStorage = "C:\\Users\\Manish Sharma\\Downloads\\";
			fileNameLen = dataInputStreamObj.readInt();
			
			System.out.println("Len of file " + fileNameLen);
			
			byte[] fileNameInBytes = new byte[fileNameLen];
			
			// reading name from input stream
			dataInputStreamObj.readFully(fileNameInBytes, 0, fileNameLen);
			String fileName = new String(fileNameInBytes);
			System.out.println("File Name : " + fileName);
			
			// checking file exists or not
			File fileObj = new File(pathOfStorage + fileName);
			if(fileObj.exists() == true)
			{
				int contentLen = (int)fileObj.length();
				
				System.out.println("Content len : " + contentLen);
				
				FileInputStream fileInpStreamObj = new FileInputStream(fileObj.getAbsolutePath());
				
				// filling array with content
				byte[] contentOfFileInBytes = new byte[contentLen];
				fileInpStreamObj.read(contentOfFileInBytes);
				
				// send file content
				DataOutputStream dataOutputStreamObj = new DataOutputStream(serverSideSocket.getOutputStream());
				
				dataOutputStreamObj.writeInt(contentLen);
				dataOutputStreamObj.write(contentOfFileInBytes);
				
			}
			else 
			{
				System.out.println("File Do not exist at Server Side");
			}
		}
		serverSideSocket.close();
		serverSocketObj.close();
	}
}
