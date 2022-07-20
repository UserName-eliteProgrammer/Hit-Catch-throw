import java.io.Serializable;

public class Request implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int choice;
	public String fileName;
	public String fileContent;
	
	Request(int choice, String fileName, String fileContent)
	{
		this.choice = choice;
		this.fileName = fileName;
		this.fileContent = fileContent;
	}
}
