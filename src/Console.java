import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cbcdownloader.CommunicationException;
import cbcdownloader.DownloadConfiguration;
import cbcdownloader.Downloader;
import cbcdownloader.DummyDownloader;
import cbcdownloader.NetworkDownloader;
import cbcdownloader.USBDownloader;


public class Console {
	private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	
	static {
		downloaders.put("net", new NetworkDownloader());
		downloaders.put("usb", new USBDownloader());
		downloaders.put("virtual", new DummyDownloader());
	}
	
	public static void usage() {
		System.out.println("java Console <downloader>");
	}
	
	private static void printUsageInfo(Map<String, Downloader> downloaders) {
		System.out.println("Usage: java Download <file> <destination> <downloader> <args>");
		System.out.println("To run in an interactive mode: java Download interactive");
		for(String i : downloaders.keySet()) {
			DownloadConfiguration config = downloaders.get(i).getConfigurationObject();
			System.out.println();
			System.out.println("Download Plugin: " + i);
			for(String k : config.getRequirements()) {
				System.out.println(
					"    -" + k + " <" + config.getDescriptionFor(k) + ">    " +
					(config.hasDefaultFor(k) ?
						"Default value is \"" + config.getDefaultFor(k) + "\"" :
						"There is no default. " +
							"You must specify a value for this."
					)
				);
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) throws CommunicationException {
		if(args.length < 1) {
			usage();
			System.exit(1);
		}
		
		Downloader downloader = downloaders.get(args[0]);
		
		if(downloader == null) {
			System.out.println(
				"No such downloader \"" + args[0] + "\"!"
			);
			usage();
			System.exit(1);
		}
		
		if(!downloader.supportsExecution()) {
			System.out.println("This downloader does not support execution.");
			System.exit(1);
		}
		
		DownloadConfiguration config = downloader.getConfigurationObject();
		
		for(int i = 1; i < args.length; ++i) {
			if(args[i].charAt(0) != '-') {
				System.out.println("Error reading arguments list: " +
					"argument found without preceding \"-\".");
				printUsageInfo(downloaders);
				System.exit(1);
			}
			config.setValueFor(args[i].substring(1), args[++i]);
		}
		
		downloader.setup(config);
		downloader.connect();
		String command = "";
		Scanner reader = new Scanner(System.in);
		while(true) {
			String pwd = downloader.execute("env PWD");
			System.out.print("CBC:" + pwd + "$ ");
			command = reader.nextLine();
			if(command.equals("exit")) {
				break;
			}
			System.out.println(downloader.execute(command));
		}
		downloader.disconnect();
	}
	
}
