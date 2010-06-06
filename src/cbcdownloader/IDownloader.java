package cbcdownloader;
import java.io.File;


public interface IDownloader {
	public boolean setup(DownloadConfiguration config);
	public void connect() throws CommunicationException;
	public void disconnect() throws CommunicationException;
	public boolean download(String destination, File file) throws CommunicationException;
	public boolean supportsExecution();
	public boolean execute(String exec) throws CommunicationException;
	public String executeStdIn(String exec) throws CommunicationException;
	public boolean supportsDeletion();
	public boolean delete(String destination) throws CommunicationException;
	public DownloadConfiguration getConfigurationObject();
}
