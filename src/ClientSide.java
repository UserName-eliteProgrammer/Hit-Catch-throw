import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide 
{	
	public static void main(String[] args) throws Exception
	{	
		System.out.println("Client");
		int port = Constants.port, choice;
		String ip = Constants.ip;
		while(true)
		{	
			Socket clientSocket = new Socket(ip, port);
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter Your Choice - 1 For Sending File and 2 For Downloading File : ");
			
			choice = sc.nextInt();
			sendReq(choice, clientSocket);
		}
	}
	
	
	public static void sendReq(int choice, Socket clientSocket) throws Exception
	{
		if(choice == 1)
		{
			sendFile(clientSocket);
		}
		else if(choice == 2)
		{
			receiveFile(clientSocket);
		}
		else
		{
			System.out.println("Invalid Choice");
		}
	}
	
	public static void sendFile(Socket clientSocket)
	{	
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter file Name : ");
		String name = sc.nextLine();
		System.out.println("Enter Content : ");
		String content = sc.nextLine();
		Request data = new Request(1, name, content);
		ObjectOutputStream outputStream;
		try 
		{
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			outputStream.writeObject(data);
			System.out.println("File Sent Successfully!!");
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Something Went Wrong");
			e.printStackTrace();
		}
		
	}
	
	
	public static void receiveFile(Socket clientSocket) throws Exception
	{
		ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter file Name : ");
		String name = sc.nextLine();
		
		Request data = new Request(2, name, "");
		// sending info
		outputStream.writeObject(data);
		
		
		// receiving file
		Request receivedData = (Request)inputStream.readObject();
		File fileToBeDownloaded = new File(Constants.clientStoragePath + receivedData.fileName);
		
		FileOutputStream fileOutputStream = new FileOutputStream(fileToBeDownloaded);
		fileOutputStream.write((receivedData.fileContent).getBytes());
	}
}
