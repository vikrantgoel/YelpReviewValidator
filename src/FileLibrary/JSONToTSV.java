package FileLibrary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import HiveLibrary.HiveClient;

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
	 * @param commaSeparatedStringColumns: Comma separated names of all the String columns
	 * @param commaSeparatedIntegerColumns: Comma separated names of all the Integer columns
	 * @param commaSeparatedFloatColumns: Comma separated names of all the Float columns
	 * @param commaSeparatedDoubleColumns: Comma separated names of all the Double columns
	 * @param commaSeparatedBooleanColumns: Comma separated names of all the Boolean columns
	 * @param sourceFilename
	 * @param destFilename
	 * @param delimiter: For the destination file
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */

	//	public void jsonToTsv(String commaSeparatedColumns, String commaSeparatedStringColumns, 
	//			String commaSeparatedIntegerColumns, String commaSeparatedFloatColumns, String commaSeparatedDoubleColumns, 
	//			String commaSeparatedBooleanColumns, String sourceFilename, String destFilename, String delimiter) throws IOException, ParseException{
	public void jsonToTsv(String commaSeparatedColumns, String sourceFilename, String destFilename, String delimiter) throws IOException, ParseException{

		//		int totalNumberOfColumns = StringUtils.countMatches(commaSeparatedColumns, ",") + 1;
		//		int totalNumberOfStringColumns = StringUtils.countMatches(commaSeparatedStringColumns, ",") + 1;
		//		int totalNumberOfIntegerColumns = StringUtils.countMatches(commaSeparatedIntegerColumns, ",") + 1;
		//		int totalNumberOfFloatColumns = StringUtils.countMatches(commaSeparatedFloatColumns, ",") + 1;
		//		int totalNumberOfDoubleColumns = StringUtils.countMatches(commaSeparatedDoubleColumns, ",") + 1;
		//		int totalNumberOfBooleanColumns = StringUtils.countMatches(commaSeparatedBooleanColumns, ",") + 1;

		// TODO data validation

		//		HashMap<String, String> dataTypeMap = new HashMap<>();
		//		for(String str : commaSeparatedColumns.split(",")){
		//			str = str.trim();
		//
		//			String temp = null;
		//			if(commaSeparatedStringColumns.contains(str))
		//				temp = "String";
		//			else if(commaSeparatedIntegerColumns.contains(str))
		//				temp = "Integer";
		//			else if(commaSeparatedFloatColumns.contains(str))
		//				temp = "Float";
		//			else if(commaSeparatedDoubleColumns.contains(str))
		//				temp = "Double";
		//			else if(commaSeparatedBooleanColumns.contains(str))
		//				temp = "Boolean";
		//			dataTypeMap.put(str, temp);
		//		}

		JSONParser parser = new JSONParser();
		JSONObject obj;
		File infile = new File(sourceFilename);
		Scanner sc = new Scanner(infile);

		File outFile = new File (destFilename);
		if(outFile.exists() && !outFile.isDirectory())
			outFile.delete();
		PrintWriter pWriter = new PrintWriter (new FileWriter (outFile));

		//		HashMap<String, String> valuesHashMap = new HashMap<>(); 
		StringBuilder sb;
		while (sc.hasNextLine()) {
			sb = new StringBuilder();

			// READ FROM FILE

			obj = (JSONObject)parser.parse(sc.nextLine().replaceAll("\\\\n", " "));

			//			for (Map.Entry<String, String> entry : dataTypeMap.entrySet()) {
			//			    if(entry.getValue().equals("String"))
			//			    	value = String.valueOf(obj.get(entry.getKey()).toString()).toString();
			//			    else if(entry.getValue().equals("Integer"))
			//			    	value = Integer.valueOf(obj.get(entry.getKey()).toString()).toString();
			//			    valuesHashMap.put(entry.getKey(), value);
			//			}
			for(String str: commaSeparatedColumns.split(",")){
				str = str.trim();
				//				valuesHashMap.put(str, obj.get(str).toString());
				sb.append(obj.get(str).toString() + delimiter);
			}
			sb.append("\n");

			// Not pritln
			pWriter.print(sb.toString());
		}

		sc.close();
		pWriter.close();
	}
	public static void businessJsonToTsv() throws ParseException, IOException{

		String businessId, name, address, city, state, categories, neighborhoods, attributes;
		boolean open;
		int reviewCount;
		float stars;
		double longitude, latitude;

		JSONParser parser = new JSONParser();
		JSONObject obj;
		File businessIn = new File("yelp_academic_dataset_business.json");
		Scanner sc = new Scanner(businessIn);

		File outFile = new File ("yelp_academic_dataset_business.txt");
		if(outFile.exists() && !outFile.isDirectory())
			outFile.delete();
		PrintWriter pWriter = new PrintWriter (new FileWriter (outFile));

		while (sc.hasNextLine()) {

			// READ FROM FILE
			obj = (JSONObject)parser.parse(sc.nextLine().replaceAll("\\\\n", " "));

			businessId= obj.get("business_id").toString();
			name = obj.get("name").toString();
			address = obj.get("full_address").toString();
			city = obj.get("city").toString();
			state = obj.get("state").toString();
			open = Boolean.valueOf(obj.get("open").toString());
			categories = JSONArrayToString((JSONArray)obj.get("categories"));
			reviewCount = Integer.parseInt(obj.get("review_count").toString());
			stars = Float.parseFloat(obj.get("stars").toString());
			neighborhoods = JSONArrayToString((JSONArray)obj.get("neighborhoods"));
			longitude = Double.parseDouble(obj.get("longitude").toString());
			latitude = Double.parseDouble(obj.get("latitude").toString());
			attributes = obj.get("attributes").toString();

			// WRITE TO FILE
			pWriter.println(businessId + "\t" + name + "\t" + address + "\t" + city + "\t" + state + "\t" + open + "\t" + categories + "\t" + reviewCount + "\t" + stars + "\t" + neighborhoods + "\t" + longitude + "\t" + latitude + "\t" + attributes);
		}
		sc.close();
		pWriter.close();

	}
	public static void businessLoadToHive() throws ParseException, IOException{
		JSONToTSV jt = new JSONToTSV();
		jt.jsonToTsv("business_id,name,full_address,city, state,open,categories,review_count,stars,neighborhoods,longitude,latitude,attributes", "yelp_academic_dataset_business.json", "yelp_academic_dataset_business.txt", "\t");	

	}
	public static void main(String[] args) throws Exception {
//		HiveClient hc = new HiveClient("yelp");
		businessLoadToHive();
//		System.out.println(new File("yelp_academic_dataset_business.txt").getAbsolutePath());
//		hc.executeHiveQuery("TRUNCATE TABLE business; LOAD DATA LOCAL INPATH '" + new File("yelp_academic_dataset_business.txt").getAbsolutePath() + "' INTO TABLE business;");
	}
}
