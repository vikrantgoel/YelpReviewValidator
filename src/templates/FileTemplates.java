package templates;

import clients.FileClient;

public class FileTemplates {

	/**
	 * 
	 * @param commaSeparatedColumns: Comma separated column names in sequence for destination file
	 * @param sourceFilename
	 * @param destFilename
	 * @param delimiter: For the destination file
	 * @param fileClient
	 * @throws Exception 
	 */
	public void transformJsonToTsv(String commaSeparatedColumns, String sourceFilename, String destFilename, String delimiter, FileClient fc, int autoIncrementPosition) throws Exception{
		int sourceFileCount = fc.countLines(sourceFilename);
		fc.jsonToTsv(commaSeparatedColumns, sourceFilename, destFilename, delimiter, autoIncrementPosition);
		int destFileCount = fc.countLines(destFilename);
		
		if(sourceFileCount != destFileCount)
			throw new Exception("Size of source file: " + sourceFileCount + " is not equal to size of destination file: " + destFileCount);
	}
	public void transformJsonToTsv(String commaSeparatedColumns, String sourceFilename, String destFilename, String delimiter, FileClient fc) throws Exception{
		transformJsonToTsv(commaSeparatedColumns, sourceFilename, destFilename, delimiter, fc, -1);
	}
}
