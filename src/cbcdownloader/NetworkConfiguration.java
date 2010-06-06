package cbcdownloader;

public class NetworkConfiguration extends DownloadConfiguration {
	public NetworkConfiguration() {
		addRequirement("ip", "The IPv4 address of the device");
	}
}
