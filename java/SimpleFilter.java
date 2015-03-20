package simpleFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class SimpleFilter {

	public static final String root = "/media/xinping/c05235cc-f399-4c8c-acb4-1c27ba037df3/upennmed/";
	public static final String chr18 = "chr18/";
	public static final String chr19 = "chr19/";
	public static final String chr20 = "chr20/";
	public static final String chr21 = "chr21/";
	public static final String chr22 = "chr22/";
	public static final String pileuplist = "pileuplist";
	public static final String sitelist = "sitelist";
	public static final String truesnplist = "truesnplist";
	public static final String difflist = "difflist";
	public static final String siteOutput = "allsites.out";
	public static final String snpOutput = "snpsites.out";
	public static final String diffOutput = "diffsites.out";
	
	public static void fillSet(String fileName, Set<String> set) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		
		String line = null;
		
		while (null != (line = br.readLine())) {
			if(!set.contains(line)) {
				set.add(line);
			}
		}
		
		System.out.println(set.size());
		
		br.close();
	}
	
	public static void pileupSearch(
			String fileName, Set<String> siteSet, Set<String> trueSet, Set<String> diffSet, 
			PrintWriter sitepw, PrintWriter snppw, PrintWriter diffpw, String sampleName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName)), "UTF-8"));
		
		String line = null;
		
		while (null != (line = br.readLine())) {
			String[] infos = line.split("\t");
			
			if (infos.length > 1) {
				System.out.println(infos[1]);
				if(siteSet.contains(infos[1])) {
					sitepw.print(sampleName);
					sitepw.print("\t");
					sitepw.println(line);
					
					if (trueSet.contains(infos[1])) {
						snppw.print(sampleName);
						snppw.print("\t");
						snppw.println(line);
					}
					
					if (diffSet.contains(infos[1])) {
						diffpw.print(sampleName);
						diffpw.print("\t");
						diffpw.println(line);
					}
				}
			}
			
		}
		
		br.close();
	}
	
	public static void filter(String chr) throws Exception {
		Set<String> siteSet = new HashSet<String>();
		Set<String> trueSet = new HashSet<String>();
		Set<String> diffSet = new HashSet<String>();	
		
		fillSet(root + chr + sitelist, siteSet);
		fillSet(root + chr + truesnplist, trueSet);
		fillSet(root + chr + difflist, diffSet);
		
		BufferedReader br = new BufferedReader(new FileReader(new File(root + chr + pileuplist)));
		PrintWriter sitepw = new PrintWriter(new FileWriter(new File(root + chr + siteOutput)));
		PrintWriter snppw = new PrintWriter(new FileWriter(new File(root + chr + snpOutput)));
		PrintWriter diffpw = new PrintWriter(new FileWriter(new File(root + chr + diffOutput)));
		
		String pileupFileName = null;
		
		while (null != (pileupFileName = br.readLine())) {
			pileupSearch(root + chr + pileupFileName, siteSet, trueSet, diffSet, sitepw, snppw, diffpw, pileupFileName);
		}
		
		sitepw.close();
		snppw.close();
		diffpw.close();
		br.close();
	}
	
	public static void run() throws Exception {
		filter(chr19);
		filter(chr20);
		filter(chr21);
		filter(chr22);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		run();
	}

}
