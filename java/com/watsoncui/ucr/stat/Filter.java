package com.watsoncui.ucr.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Filter {
	public static final String fileName = ResultFilter.resultName;
	public static final String outputFileName = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".filter.csv";
	public static final String gemsFiltered = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".gems.csv";
	public static final String fbFiltered = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".fb.csv";
	public static final String vsFiltered = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".vs.csv";
	public static final String trueFiltered = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".true.csv";
	public static final String gatkFiltered = fileName.substring(0,
			fileName.indexOf('.'))
			+ ".gatk.csv";

	/**
	 * @throws Exception
	 */
	public static void filterZero() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(
				new File(fileName)));
		PrintWriter pw = new PrintWriter(new FileWriter(
				new File(outputFileName)));
		PrintWriter gemspw = new PrintWriter(new FileWriter(new File(
				gemsFiltered)));
		PrintWriter fbpw = new PrintWriter(new FileWriter(new File(fbFiltered)));
		PrintWriter vspw = new PrintWriter(new FileWriter(new File(vsFiltered)));
		PrintWriter truepw = new PrintWriter(new FileWriter(new File(
				trueFiltered)));
		PrintWriter gatkpw = new PrintWriter(new FileWriter(new File(
				gatkFiltered)));
		String line = br.readLine();
		pw.println(line);
		gemspw.println(line);
		fbpw.println(line);
		vspw.println(line);
		truepw.println(line);
		gatkpw.println(line);
		while (null != (line = br.readLine())) {
			String[] infos = line.split("\t");
			if (infos[5].equals("1")) {
				truepw.println(line);
				if (infos[6].equals("1")) {
					gemspw.println(line);
				}
				if (infos[7].equals("1")) {
					fbpw.println(line);
				}
				if (infos[8].equals("1")) {
					vspw.println(line);
				}
				if (infos[9].equals("1")) {
					gatkpw.println(line);
				}
			}
		}
		gatkpw.close();
		truepw.close();
		gemspw.close();
		vspw.close();
		fbpw.close();
		pw.close();
		br.close();
	}

	public static void main(String[] args) throws Exception {
		filterZero();
	}
}
