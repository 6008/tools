package com.watsoncui.ucr.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ImproveTest {
	public static final String outputFile = "/home/xinping/simpleResults.txt";
	public static final String rootDir = "/media/xinping/c05235cc-f399-4c8c-acb4-1c27ba037df3/upennmed/";
	public static final String chr22 = "chr22/";
	public static final String chr21 = "chr21/";
	public static final String chr20 = "chr20/";
	public static final String chr19 = "chr19/";
	public static final String trueSnpFile = "results.map";
	public static final String pileupFilell = "pool.pile.gz";
	public static final String multiGemsFileList = "multigemslist";
	public static final String varscan = "varscan.vcf.gz";
	public static final String freebayes = "freebayes.vcf.gz";
	public static final String gatk = "gatk.vcf.gz";
	public static final String samtools = "sam.flt.vcf.gz";
	public static final String[] chromosomeList = {chr19, chr20, chr21, chr22};
	public static final int[] chromIds = {19, 20, 21, 22};
	
	public static final boolean DEBUG = true;
	
	public static float strToFloat(String s) {
		float f = 0.0f;
		try {
			f = Float.parseFloat(s);
		} catch (NumberFormatException nfe) {
			f = -1.0f;
		} catch (NullPointerException npe) {
			f = -1.0f;
		}
		return f;
	}
	
	public static int strToInteger(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			i = -1;
		} catch (NullPointerException npe) {
			i = -1;
		}
		return i;
	}
	
	public static void readTrueSNPFile(Map<Long, Format> resultMap) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(rootDir + trueSnpFile)));
		String line = null;
		
		while (null != (line = br.readLine())) {
			
			if (!(line.startsWith("19") || line.startsWith("20") ||line.startsWith("21") ||line.startsWith("22"))) {
				continue;
			}
			
			String[] infos = line.split("\t");
			if (infos.length >= 4) {
				int chrNum = strToInteger(infos[0]);
				if (chrNum < 0) {
					continue;
				}
				int pos = strToInteger(infos[3]);
				if (pos < 0) {
					continue;
				}
				Long key = 10000000000l * chrNum + pos;
				if (resultMap.containsKey(key)) {
					Format format = resultMap.get(key);
					format.setTrueSnp(1);
					resultMap.put(key, format);
				} else {
					Format format = new Format(0, 0.0f);
					format.setTrueSnp(1);
					resultMap.put(key, format);
				}
			}
		}
		
		br.close();
	}
	
	public static void readCalcSNP(String chromosome, String fileName, int number, Map<Long, Format> resultMap) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(rootDir + chromosome + fileName)), "UTF-8"));
		String line = null;
		
		while (null != (line = br.readLine())) {

			if (line.startsWith("#")) {
				continue;
			}
			String[] infos = line.split("\t");
			
			if (infos.length >= 2) {
				int chrNum = strToInteger(infos[0]);
				if (chrNum < 0) {
					continue;
				}
				int pos = strToInteger(infos[1]);
				if (pos < 0) {
					continue;
				}
				Long key = 10000000000l * chrNum + pos;
				
				Format format;
				
				if (resultMap.containsKey(key)) {
					format = resultMap.get(key);
				} else {
					format = new Format(0, 0.0f);
				}
				
				if ((infos.length >= 7) && (infos[3] != "N") && (infos[3].length() == 1) && (infos[4].length() == 1) && (!infos[4].equals(infos[3]))) {
					float quality = strToFloat(infos[5]);
					if ((quality >= 20.0f) || infos[6].equals("PASS")) {
						format.updateSoftResultArray(number);
					}
				}
			
				resultMap.put(key, format);
			}
			
			
		}
		
		br.close();
	}
	
	public static void ReadGemsResults(String chromosome, String fileName, int number, Map<Long, Format> resultMap, int chromId) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(rootDir + chromosome + fileName)), "UTF-8"));
		String line = null;
		
		while (null != (line = br.readLine())) {
			
			String[] infos = line.split("\t");
			
			if (infos.length > 0) {
				int pos = strToInteger(infos[0]);
				if (pos < 0) {
					continue;
				}
				Long key = 10000000000l * chromId + pos;
				Format format;
				if (resultMap.containsKey(key)) {
					format = resultMap.get(key);
				} else {
					format = new Format(0, 0.0f);
				}
				
				if ((infos.length >= 20)) {
					
					float q = strToFloat(infos[infos.length - 1]);
					if (q < -0.01f) {
						continue;
					}
					
					float quality = 999.999f;
					if (q > Math.pow(10, -100)) {
						quality = (float) (-10 * Math.log10(q));
					}
					
					if (quality >= 20.0f) {
						format.updateSoftResultArray(number);
					}
				}
				
				resultMap.put(key, format);
			}
			
		}
		
		br.close();
	}
	
	public static void readGemsFiles(String chromosome, String fileName, int number, Map<Long, Format> resultMap, int chromId) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(rootDir + chromosome + fileName)));
		String line;
		
		while (null != (line = br.readLine())) {
			ReadGemsResults(chromosome, line, number, resultMap, chromId);
		}
		
		br.close();
	}
	
	public static void readPileupFile(String chromosome, Map<Long, Format> resultMap) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(rootDir + chromosome + pileupFile)), "UTF-8"));
		String line = null;
		
		while (null != (line = br.readLine())) {
			
			String[] infos = line.split("\t");
			if (6 <= infos.length) {
				int chrNum = strToInteger(infos[0]);
				if (chrNum < 0) {
					continue;
				}
				int pos = strToInteger(infos[1]);
				if (pos < 0) {
					continue;
				}
				
				Long key = 10000000000l * chrNum + pos;
				
				if (resultMap.containsKey(key)) {
					int coverage = strToInteger(infos[3]);
					if (coverage < 0.0f) {
						continue;
					}
					String qualities = infos[5];
					
					float qualitySum = 0.0f;
					for (int i = 0; i < qualities.length(); i++) {
						qualitySum += qualities.charAt(i);
					}
					float averageQuality = qualitySum / qualities.length();
					
					float qualityDev = 0.0f;

					for (int i = 0; i < qualities.length(); i++) {
						qualityDev += Math.pow(qualities.charAt(i) - averageQuality, 2);
					}
					
					float qualityStdev = (float) Math.sqrt(qualityDev / qualities.length());
					
					
					averageQuality -= 33;
					
					Format format = resultMap.get(key);
					format.setCoverage(coverage);
					format.setAverageQuality(averageQuality);
					format.setQualityStdev(qualityStdev);
					resultMap.put(key, format);
				}
			}
		}
		
		br.close();
	}
	
	
	public static void outputResults(String fileName, Map<Long, Format> resultMap)  throws Exception {
		PrintWriter pw = new PrintWriter(new FileWriter(new File(fileName)));
		pw.println("#CHROM\tPOS\tCOVER\tAVQU\tQUSTDEV\tSNP\tMGEMS\tFREEB\tVARS\tGATK");
		Iterator<Map.Entry<Long, Format>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Format> pair = (Map.Entry<Long, Format>)it.next();
			StringBuffer sb = new StringBuffer();
			sb.append(pair.getKey() / 10000000000l).append("\t").append(pair.getKey() % 10000000000l);
			sb.append("\t").append(pair.getValue().getCoverage());
			sb.append("\t").append(pair.getValue().getAverageQuality());
			sb.append("\t").append(pair.getValue().getQualityStdev());
			sb.append("\t").append(pair.getValue().getTrueSnp());
			sb.append("\t").append(pair.getValue().getSoftSnpResult(Software.MULTIGEMS.getValue()));
			sb.append("\t").append(pair.getValue().getSoftSnpResult(Software.FREEBAYES.getValue()));
			sb.append("\t").append(pair.getValue().getSoftSnpResult(Software.VARSCAN.getValue()));
			sb.append("\t").append(pair.getValue().getSoftSnpResult(Software.GATK.getValue()));
			
			pw.println(sb.toString());
		}
		pw.close();
	}
	
	public static void generateResult() throws Exception {
		
		Map<Long, Format> resultMap = new HashMap<Long, Format>();
		
		if (DEBUG) {
			System.out.println("Running ...");
			System.out.println();
			System.out.println("Loading all true SNPs ...");
		}
		
		readTrueSNPFile(resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println();
		}
		
		for (int i = 0; i < chromosomeList.length; i++) {
			
			
			
			if (DEBUG) {
				System.out.println("In chromosome " + chromIds[i]);
				System.out.println("Loading all SNPs by varscan ...");
			}
			
			readCalcSNP(chromosomeList[i], varscan, Software.VARSCAN.getValue(), resultMap);
			
			if (DEBUG) {
				System.out.println("SUCCESS");
				System.out.println("Loading all SNPs by freebayes ...");
			}
			
			readCalcSNP(chromosomeList[i], freebayes, Software.FREEBAYES.getValue(), resultMap);
			
			if (DEBUG) {
				System.out.println("SUCCESS");
				System.out.println("Loading all SNPs by gatk ...");
			}
			
			readCalcSNP(chromosomeList[i], gatk, Software.GATK.getValue(), resultMap);
			
			if (DEBUG) {
				System.out.println("SUCCESS");
				System.out.println("Loading all SNPs by multiGeMS ...");
			}
			
			readGemsFiles(chromosomeList[i], multiGemsFileList, Software.MULTIGEMS.getValue(), resultMap, chromIds[i]);
			
			if (DEBUG) {
				System.out.println("SUCCESS");
			}
		}
		
		
		if (DEBUG) {
			System.out.println("Loading pileup information of chr19");
		}
		
		readPileupFile(chr19, resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println("Loading pileup information of chr20");
		}
		
		readPileupFile(chr20, resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println("Loading pileup information of chr21");
		}
		
		readPileupFile(chr21, resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println("Loading pileup information of chr22");
		}
		
		readPileupFile(chr22, resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println();
			System.out.println("Printing results ...");
		}
		
		outputResults(outputFile, resultMap);
		
		if (DEBUG) {
			System.out.println("SUCCESS");
			System.out.println();
			System.out.println("Mission complete");
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//test();
			generateResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
