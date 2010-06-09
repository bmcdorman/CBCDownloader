import cbcdownloader.CommunicationException;
import cbcdownloader.DownloadConfiguration;
import cbcdownloader.USBDownloader;


public class TestExec {
	public static void main(String[] args) throws CommunicationException {
		USBDownloader downloader = new USBDownloader();
		DownloadConfiguration config = downloader.getConfigurationObject();
		config.setValueFor("port", "/dev/tty.usbmodemfa141");
		downloader.setup(config);
		downloader.connect();
		long startTime = System.currentTimeMillis();
		System.out.println("\nREAD DATA: \n\n" + downloader.execute("cat /mnt/usb/userhook0"));
		long endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
		downloader.disconnect();
	}
}
