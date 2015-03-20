import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class SitesFinder {

	public static final String ROOTDIR = "/home/xinping/Desktop/6008/datas/";
	public static final String GIAB = "GiaB/chr";
	public static final String UPENN = "upenn/chr";
	public static final String SITES = "/allsites.out";
	public static final String OUTFILE = "/AS_filtered.out.new";
	
	public static int count_char(String uppercase) {
		int sum = 0;
		for (int i = 0; i < uppercase.length(); i++) {
			if ((uppercase.charAt(i) >= 'A') && (uppercase.charAt(i) <= 'Z')) {
				sum++;
			}
		}
		
		return sum;
	}
	
	public static void filter(String dataset, int chrom, int sample) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(ROOTDIR + dataset + String.valueOf(chrom) + SITES)));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(ROOTDIR + dataset + String.valueOf(chrom) + OUTFILE)));
		
		String line = null;
		
		Map<String, Integer> site_counter = new HashMap<String, Integer>();
		
		while (null != (line = br.readLine())) {
			String[] infos = line.split("\t");
			int lower_base = (int) (Integer.parseInt(infos[4]) * 0.4);
			int higher_base = (int) (Integer.parseInt(infos[4]) * 0.5);
			String dat = infos[5].toUpperCase();
			if ((count_char(dat) < higher_base) && (count_char(dat) > lower_base)) {
				if (site_counter.containsKey(infos[2])) {
					site_counter.put(infos[2], site_counter.get(infos[2]) + 1);
				} else {
					site_counter.put(infos[2], 1);
				}
			}
		}
		
		for (String key:site_counter.keySet()) {
			if (site_counter.get(key) == (int) (sample * 0.9)) {
				pw.println(key);
			}
		}
		
		br.close();
		pw.close();
	}
	
	public static void run() throws Exception {
		filter(GIAB, 18, 10);
		filter(GIAB, 19, 10);
		filter(GIAB, 20, 10);
		filter(GIAB, 21, 10);
		filter(GIAB, 22, 10);
		filter(UPENN, 19, 33);
		filter(UPENN, 20, 33);
		filter(UPENN, 21, 33);
		filter(UPENN, 22, 33);
		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		run();
	}

}
