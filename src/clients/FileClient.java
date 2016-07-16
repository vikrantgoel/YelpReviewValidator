package clients;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileClient {

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
	public int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	public void jsonToTsv(String commaSeparatedColumns, String sourceFilename, String destFilename, String delimiter, int autoIncrementPosition) throws IOException, ParseException{

		JSONParser parser = new JSONParser();
		JSONObject obj;
		File infile = new File(sourceFilename);
		Scanner sc = new Scanner(infile);

		File outFile = new File (destFilename);
		if(outFile.exists() && !outFile.isDirectory())
			outFile.delete();
		PrintWriter pWriter = new PrintWriter (new FileWriter (outFile));

		StringBuilder sb;
		
		int autoincrement = 0;
		while (sc.hasNextLine()) {
			sb = new StringBuilder();

			// READ FROM FILE
			obj = (JSONObject)parser.parse(sc.nextLine().replaceAll("\\\\n", " "));
			String [] temp = commaSeparatedColumns.split(",");
			
			if(temp.length < autoIncrementPosition)
				 autoIncrementPosition = -1;
			
			for(int i=0 ; i<temp.length; i++){
				if(i == autoIncrementPosition)
					sb.append((autoincrement++) + delimiter);
				else
					sb.append(obj.get(temp[i].trim()).toString() + delimiter);
			}
			sb.append("\n");

			// Not println
			pWriter.print(sb.toString());
		}

		sc.close();
		pWriter.close();
	}
}
