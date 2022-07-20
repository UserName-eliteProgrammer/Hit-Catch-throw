import java.io.*;
import java.net.*;

public class ServerSide 
{
	static int port = Constants.port;
	public static void main(String[] args) throws Exception
	{	
		System.out.println("Server");
		ServerSocket serverSocketObj = new ServerSocket(port);
		while(true)
		{	
			receivingRequest(serverSocketObj);
		}
	}
	
	public static void receivingRequest(ServerSocket serverSocketObj) throws Exception
	{
		Socket serverSideSocket = serverSocketObj.accept();
		ObjectOutputStream outputStream = new ObjectOutputStream(serverSideSocket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(serverSideSocket.getInputStream());
		
		Request data = (Request)inputStream.readObject();
		if(data.choice == 1)
		{
			// Receive file
			receiveFile(data, inputStream);
		}
		else
		{
			sendFile(data, outputStream);
		}
	}
	
	
	public static void receiveFile(Request data, ObjectInputStream inputStream) throws Exception
	{
		String fileName = data.fileName;
		String fileContent = data.fileContent;
		
		
		File newFile = new File(Constants.serverStoragePath + fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFile);
		
		fileOutputStream.write(fileContent.getBytes());
	}
	
	public static void sendFile(Request data, ObjectOutputStream outputStream) throws Exception
	{
		// sendFile
		File file = new File(Constants.serverStoragePath + data.fileName);
		if(file.exists() == true)
		{	
			byte[] fileContentInBytes = new byte[(int)(file.length())];
			FileInputStream fileInpStream = new FileInputStream(file);
			fileInpStream.read(fileContentInBytes);
						
// creating request object which contains info about file which have to send
			Request fileToSend = new Request(0, data.fileName, new String(fileContentInBytes));
						
				// sending object to client
			outputStream.writeObject(fileToSend);
						
		}
		else
		{
			System.out.println("File Not Exist");
		}

	}
}
