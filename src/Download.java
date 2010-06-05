

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
		if(args.length < 4) {
			System.out.println("Usage: java Download <file> <destination> <downloader> <args>");
		}
		IDownloader downloader = downloaders.get(args[2]);
		if(downloader == null) {
			System.out.println("No such downloader \"" + args[2] + "\", aborting!");
			return;
		}
		String[] remain = Arrays.copyOfRange(args, 3, args.length);
		for(String r : remain) {
			System.out.println(r);
		}
		downloader.setup(new DownloadConfiguration(remain));
		downloader.connect();
		downloader.download(args[1], new File(args[0]));
		downloader.disconnect();
	}

}
