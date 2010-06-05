package cbcdownloader;
import java.io.File;
import java.io.IOException;


public class NetworkDownloader implements IDownloader {
	private String ip = null;
	private Ssh ssh = null;
	
	public void connect() throws CommunicationException {
		try {
			ssh = new Ssh(ip);
		} catch (IOException e) {
			throw new CommunicationException();
		}
	}
	
	@Override
	public boolean delete(String destination) throws CommunicationException {
		ssh.sendCommand("rm -Rf \"" + destination + "\"");
		return true;
	}

	@Override
	public boolean download(String destination, File file)
			throws CommunicationException {
		try {
			ssh.sendFile(destination, file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CommunicationException();
		}
		return true;
	}

	@Override
	public boolean execute(String exec) throws CommunicationException {
		ssh.sendCommand(exec);
		return true;
	}
	
	public String executeStdIn(String exec) throws CommunicationException {
		return ssh.sendCommandRet(exec);
	}

	@Override
	public boolean setup(DownloadConfiguration config) {
		if(config.getArgs().length != 1) return false;
		ip = config.getArgs()[0];
		return true;
	}

	@Override
	public boolean supportsDeletion() {
		return true;
	}

	@Override
	public boolean supportsExecution() {
		return true;
	}

	@Override
	public void disconnect() throws CommunicationException {
		ssh.close();
	}

}
