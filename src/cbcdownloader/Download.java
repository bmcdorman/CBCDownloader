package cbcdownloader;

import java.util.HashMap;
import java.util.Map;

public class Download {
	private static Map<String, IDownloader> downloaders = new HashMap<String, IDownloader>();
	
	static {
		downloaders.put("net", new NetworkDownloader());
	}
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Usage: java ");
		}
	}

}
