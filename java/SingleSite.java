import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SingleSite {

	public static final String fileName = "/home/xinping/gemsreport/15208082";
	public static final String output = fileName + ".out";
	
	public static void func() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(output)));
		
		String line = null;
		
		while (null != (line = br.readLine())) {
			
			String data = br.readLine();	
			String quality = br.readLine();
			br.readLine();
			
			StringBuffer sb = new StringBuffer();
			sb.append(line);
			
			int refCount = 0;
			double refQuality = 0.0;
			
			int[] altCount = new int[4];
			double[] altQuality = new double[4];
			
			for (int i = 0; i < 4; i++) {
				altCount[i] = 0;
				altQuality[i] = 0.0;
			}
			System.out.println(line);
			System.out.println(data);
			System.out.println(quality);
			
			for (int i = 0; i < data.length(); i++) {
				char c = data.charAt(i);
				switch (c) {
				case '.':
					refCount++;
					refQuality += (quality.charAt(i) - 33);
					break;
				case ',':
					refCount++;
					refQuality += (quality.charAt(i) - 33);
					break;
				case 'a':
					altCount[0]++;
					altQuality[0] += (quality.charAt(i) - 33);
					break;
				case 'A':
					altCount[0]++;
					altQuality[0] += (quality.charAt(i) - 33);
					break;
				case 'c':
					altCount[1]++;
					altQuality[1] += (quality.charAt(i) - 33);
					break;
				case 'C':
					altCount[1]++;
					altQuality[1] += (quality.charAt(i) - 33);
					break;
				case 'g':
					altCount[2]++;
					altQuality[2] += (quality.charAt(i) - 33);
					break;
				case 'G':
					altCount[2]++;
					altQuality[2] += (quality.charAt(i) - 33);
					break;
				case 't':
					altCount[3]++;
					altQuality[3] += (quality.charAt(i) - 33);
					break;
				case 'T':
					altCount[3]++;
					altQuality[3] += (quality.charAt(i) - 33);
					break;
				}
			}
			
			sb.append("\t").append(refCount).append("\t").append(refQuality/refCount);
			
			for (int i = 0 ; i < 4; i++) {
				if (altCount[i] > 0) {
					sb.append("\t").append(altCount[i]).append("\t").append(altQuality[i]/altCount[i]);
				} else {
					sb.append("\t").append(0).append("\t").append(0);
				}
			}

			pw.println(sb.toString());
		}
		
		br.close();
		pw.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		func();
	}

}
