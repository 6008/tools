package com.watsoncui.ucr.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ResultFilter {
	
	public static final String resultName = ImproveTest.outputFile + ".csv";

	public static void filter() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(ImproveTest.outputFile)));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(resultName)));
		
		String line = null;
		
		while (null != (line = br.readLine())) {
			if (!line.endsWith("0\t0\t0\t0\t0")) {
				pw.println(line);
			}
		}
		
		br.close();
		pw.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		filter();
	}

}
