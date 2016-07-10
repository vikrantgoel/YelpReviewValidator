package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HiveClient {

	private String defaultDatabase;
	final static String bashProfile = System.getenv("HOME") + "/.bash_profile";
	final static String hiveHome = "/usr/local/Cellar/hive/2.0.1";
	private static boolean LOGGING = false;
	
	public HiveClient(){ }
	public HiveClient(String defaultDatabase){
		this.defaultDatabase = defaultDatabase;
	}
	public HiveClient(String defaultDatabase, boolean logging){
		this.defaultDatabase = defaultDatabase;
		LOGGING = logging;
	}
	
	public String executeHiveQuery(String query) throws IOException {
		
		System.out.println("Running in HIVE: " + query);
		// Prepare the command to run in bash
		StringBuilder querysSB = new StringBuilder();
		querysSB.append("source " + bashProfile + "; ");
		querysSB.append("cd " + hiveHome + "; ");
		querysSB.append("./bin/hive -e \"");
		if(defaultDatabase.length() != 0)
			querysSB.append("USE " + defaultDatabase + "; ");
		querysSB.append(query);
		querysSB.append("\";");
		
		String[] queryArray = {"/bin/bash", "-c", querysSB.toString()};

		// Run the command in bash
		ProcessBuilder builder = new ProcessBuilder(queryArray);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		StringBuilder returnSB = new StringBuilder();
		while (true) {
			line = r.readLine();
			if (line == null) { break; }
			returnSB.append(line);
			
			if(LOGGING)
				System.out.println(line);
		}

		return returnSB.toString();
	}
}
