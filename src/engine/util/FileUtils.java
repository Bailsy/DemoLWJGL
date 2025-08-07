package engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
	
	public static String loadAsString(String path) {
		
		//gathers the contents of a shaders file
		StringBuilder result = new StringBuilder();
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)))){
			
			System.out.println("reading...");
			String line = "";
			while ((line = reader.readLine()) != null){
				result.append(line).append("\n");
			
			}
		}catch(IOException e){
			System.out.println("can't find file!");
		}
		return result.toString();	
	}
	
}
