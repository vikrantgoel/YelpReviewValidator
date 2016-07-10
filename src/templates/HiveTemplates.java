package templates;

import java.io.IOException;

import clients.HiveClient;

public class HiveTemplates {

	/**
	 * This method constructs a bash command using the input hive query 
	 * and runs it as a process on command line
	 * 
	 * @param sourceFile
	 * @param destTable
	 * @param truncateTable
	 * @param hiveClient
	 * @return
	 * @throws IOException
	 */
	public String loadFromFileToTable(String sourceFile, String destTable, boolean truncateTable, HiveClient hc) throws IOException{
		StringBuilder sb = new StringBuilder();
		
		if(truncateTable)
			sb.append("TRUNCATE TABLE " + destTable + "; ");
		sb.append("LOAD DATA LOCAL INPATH '" + sourceFile + "' INTO TABLE " + destTable +";");
		String str = hc.executeHiveQuery(sb.toString());
		
		return str;
	}
}
