//To compile:
//  javac -cp .:../jsch-0.1.42.jar:../RXTXcomm.jar -d ../bin Download.java
//To run:
//  cd ../bin
//  java -cp .:../jsch-0.1.42.jar:../RXTXcomm.jar Download

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cbcdownloader.CommunicationException;
import cbcdownloader.DownloadConfiguration;
import cbcdownloader.Downloader;
import cbcdownloader.NetworkDownloader;
import cbcdownloader.DummyDownloader;
import cbcdownloader.USBDownloader;

public class Download {
	private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	
	static {
		downloaders.put("net", new NetworkDownloader());
		downloaders.put("usb", new USBDownloader());
		downloaders.put("virtual", new DummyDownloader());
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
		System.out.println();
		if(args.length == 1 && args[0].toLowerCase().equals("interactive")) {
			System.out.println("Entering interactive mode.");
			System.out.println();
			interactiveDownload();
		} else if(args.length < 3) {
			printUsageInfo(downloaders);
			System.exit(0);
		} else {
			argumentDownload(args);
		}
	}
	
	//returns the index of the user selected option
	private static int consoleMenu(String message, String[] options) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Menu: " + message);
		int maxDigits = getNumDigits(options.length);
		for(int i = 1; i < options.length+1; ++i) {
			System.out.println(
				"[" + charMultiplier(' ', maxDigits-getNumDigits(i)) + i +
					"] " + options[i-1]
			);
		}
		System.out.println("Type in corresponding number and hit Enter.");
		while(true) {
			int choice = sc.nextInt()-1;
			if(choice > -1 && choice < options.length) {
				return choice;
			}
			System.out.println(
				"Bad input. Valid range is from 1 to " + options.length
			);
		}
	}
	
	private static int getNumDigits(int num) {
		return Integer.toString(num, 10).length();
	}
	
	private static String charMultiplier(char character, int times) {
		StringBuilder sb = new StringBuilder(times);
		for(int i = 0; i < times; ++i) {
			sb.append(character);
		}
		return sb.toString();
	}
	
	private static void argumentDownload(String[] args) {
		Downloader downloader = downloaders.get(args[2]);
		
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
		try {
			downloader.connect();
			downloader.download(args[1], new File(args[0]));
		} catch(CommunicationException ex) {
			System.out.println(ex.getMessage());
		}
		downloader.disconnect();
	}
	
	public static void interactiveDownload() {
		String[] downloaderTypes = downloaders.keySet().toArray(new String[0]);
		Downloader dl = downloaders.get(
			downloaderTypes[consoleMenu("Select a Downloader", downloaderTypes)]
		);
		//more to do...
	}
}
