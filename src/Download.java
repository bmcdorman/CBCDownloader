//To compile:
//  javac -cp .:../jsch-0.1.42.jar:../RXTXcomm.jar -d ../bin Download.java
//To run:
//  cd ../bin
//  java -cp .:../jsch-0.1.42.jar:../RXTXcomm.jar Download

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cbcdownloader.DownloadConfiguration;
import cbcdownloader.IDownloader;
import cbcdownloader.NetworkDownloader;

public class Download {
	private static Map<String, IDownloader> downloaders = new HashMap<String, IDownloader>();
	
	static {
		downloaders.put("net", new NetworkDownloader());
	}
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("No arguments specified. Entering interactive mode.");
			System.out.println();
			//to be implemented
		} else if(args.length < 3) {
			printUsageInfo(downloaders);
			System.exit(0);
		} else {
			IDownloader downloader = downloaders.get(args[2]);
			
			if(downloader == null) {
				System.out.println(
					"No such downloader \"" + args[2] + "\"!"
				);
				printUsageInfo(downloaders);
				System.exit(1);
			}
			
			DownloadConfiguration config = downloader.getConfigurationObject();
			
			if((args.length-3)%2 == 1) {
				System.out.println("Error: Bad arguments length.");
				printUsageInfo(downloaders);
				System.exit(1);
			}
			
			for(int i = 3; i < args.length; ++i) {
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
			downloader.download(args[1], new File(args[0]));
			downloader.disconnect();
		}
	}
	
	private static void printUsageInfo(Map<String, IDownloader> downloaders) {
		System.out.println("Usage: java Download <file> <destination> <downloader> <args>");
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
	}

}
