import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class BiuldData {

	public static final String root = "/home/xinping/Desktop/6008/datas/";
	public static final String giab = "GiaB";
	public static final String upenn = "upenn";
	public static final String CHR = "/chr";
	public static final String allSites = "/allsites.sorted.out";
	public static final String snpSites = "/snpsites.sorted.out";
	public static final String diffSites = "/diffsites.sorted.out";
		
	public static void dataProcess(String fileName, PrintWriter pw, String dataset, int chromId) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		
		Map<String, int[]> siteMap = new TreeMap<String, int[]>();
		
		String line = null;
		
		while (null != (line = br.readLine())) {
			String[] infos = line.split("\t");
			if (infos.length <= 7) continue;
			
			int[] temp = new int[4];
			
			if (siteMap.containsKey(infos[2])) {
				temp = siteMap.get(infos[2]);
			} else {
				for (int i = 0; i < temp.length; i++) {
					temp[i] = 0;
				}
			}
				
			temp[0] += 1;
			temp[1] += Integer.parseInt(infos[4]);
			for (int i = 0; i < infos[6].length(); i++) {
				temp[2] += infos[6].charAt(i);
			}
			for (int i = 0; i < infos[7].length(); i++) {
				temp[3] += infos[7].charAt(i);
			}
			siteMap.put(infos[2], temp);
			
		}
		
		br.close();

		for (String key:siteMap.keySet()) {
			StringBuffer sb = new StringBuffer();
			int[] temp = siteMap.get(key);
			int samples = temp[0];
			double coverage = ((double) temp[1]) / ((double) temp[0]);
			double bqmean = ((double) temp[2]) / ((double) temp[1]);
			double mqmean = ((double) temp[3]) / ((double) temp[1]);
			sb.append(dataset).append("\t").append(chromId).append("\t").append(key).append("\t");
			sb.append(samples).append("\t").append(coverage).append("\t").append(bqmean).append("\t").append(mqmean);
			pw.println(sb.toString());
		}
	}
	
	public static void builddata(String dataset, String sitesFile, int[] chroms) throws Exception {
		String outputFilename = root + dataset + String.valueOf((new Date()).getTime()) + ".builddata";
		PrintWriter pw = new PrintWriter(new FileWriter(new File(outputFilename)));
		
		for (int i = 0; i < chroms.length; i++) {
			String fileName = root + dataset + CHR + String.valueOf(chroms[i]) + sitesFile;
			dataProcess(fileName, pw, dataset, chroms[i]);
		}
		
		pw.close();
	}
	
	public static void goodJob() throws Exception {
		int[] giabChroms = new int[5];
		int[] upennChroms = new int[4];
		giabChroms[0] = 18;
		upennChroms[0] = 19;
		for (int i = 1; i < 5; i++) {
			giabChroms[i] = giabChroms[i - 1] + 1;
		}
		for (int i = 1; i < 4; i++) {
			upennChroms[i] = upennChroms[i - 1] + 1;
		}
		builddata(giab, allSites, giabChroms);
		builddata(upenn, allSites, upennChroms);
		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		goodJob();
	}

}
