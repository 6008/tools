import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;


public class DeletionDetector {

	public static final String fileListName = "pile_up_list";
	public static final String outputFileName ="deletion_detector.output";
	
	public static int charCounter(String content, char target) {
		int count = 0;
		if (!content.isEmpty()) {
			for (int i = 0; i < content.length(); i++) {
				if (target == content.charAt(i)) {
					count++;
				}
			}
		}
		return count;
	}
	
	public static void singlePileupFile(String fileName, PrintWriter pw) throws Exception {
		BufferedReader br;
		if (fileName.endsWith("gz")) {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName)), "UTF-8"));
		} else {
			br = new BufferedReader(new FileReader(new File(fileName)));
		}
		String line = null;
		
		while (null != (line = br.readLine())) {
			String[] infos = line.split("\t");
			if (infos.length > 6) {
				StringBuffer sb = new StringBuffer();
				sb.append(infos[1]).append('\t').append(infos[3]).append('\t').append(charCounter(infos[4], '*'));
				pw.println(sb.toString());
			}
		}
		
		br.close();
	}
	
	public static void run() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileListName)));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(outputFileName)));
		String pileupFileName = null;
		while (null != (pileupFileName = br.readLine())) {
			singlePileupFile(pileupFileName, pw);
		}
		pw.close();
		br.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		run();
	}

}
