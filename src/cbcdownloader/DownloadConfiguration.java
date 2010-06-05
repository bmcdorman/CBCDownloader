package cbcdownloader;

public class DownloadConfiguration {
	private String[] args = null;
	public DownloadConfiguration(String[] args) {
		setArgs(args);
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	public String[] getArgs() {
		return args;
	}
}
