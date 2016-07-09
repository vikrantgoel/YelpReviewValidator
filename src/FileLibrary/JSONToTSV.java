package FileLibrary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONToTSV {

	// don't need it right now
	public static String JSONArrayToString(JSONArray array){
		if(array.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<array.size(); i++){
			sb.append(array.get(i) + ", ");
		}
		return sb.toString().substring(0, sb.length()-2);
	}

	/**
	 * 
	 * @param commaSeparatedColumns: Comma separated column names in sequence for destination file
	 * @param sourceFilename
	 * @param destFilename
	 * @param delimiter: For the destination file
	 * @throws IOException
	 * @throws ParseException
	 */
	public void jsonToTsv(String commaSeparatedColumns, String sourceFilename, String destFilename, String delimiter) throws IOException, ParseException{


		JSONParser parser = new JSONParser();
		JSONObject obj;
		File infile = new File(sourceFilename);
		Scanner sc = new Scanner(infile);

		File outFile = new File (destFilename);
		if(outFile.exists() && !outFile.isDirectory())
			outFile.delete();
		PrintWriter pWriter = new PrintWriter (new FileWriter (outFile));

		StringBuilder sb;
		while (sc.hasNextLine()) {
			sb = new StringBuilder();

			// READ FROM FILE
			obj = (JSONObject)parser.parse(sc.nextLine().replaceAll("\\\\n", " "));
			for(String str: commaSeparatedColumns.split(",")){
				str = str.trim();
				sb.append(obj.get(str).toString() + delimiter);
			}
			sb.append("\n");

			// Not println
			pWriter.print(sb.toString());
		}

		sc.close();
		pWriter.close();
	}
}
