import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class DeletionFilter {

	public static final String fileName = "deletion_detector.output.sorted";
	public static final String filteredName = "deletion_filted.output";

	public static void run() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(filteredName)));
		
		String line = br.readLine();
		
		List<Integer> coverageBuffer = new ArrayList<Integer>();
		List<Integer> delCountBuffer = new ArrayList<Integer>();
		
		coverageBuffer.clear();
		delCountBuffer.clear();
		
		String[] infos = line.split("\t");
		String site = infos[0];
		Integer coverage = Integer.valueOf(infos[1]);
		Integer delCount = Integer.valueOf(infos[2]);
		coverageBuffer.add(coverage);
		delCountBuffer.add(delCount);
		
		int zeroNum;
		
		while (null != (line = br.readLine())) {
			infos = line.split("\t");
			if (site.equals(infos[0])) {
				coverageBuffer.add(Integer.valueOf(infos[1]));
				delCountBuffer.add(Integer.valueOf(infos[2]));
			} else {
				zeroNum = 0;
				for (Integer i:delCountBuffer) {
					if (i.equals(0)) {
						zeroNum++;
					}
				}
				if (zeroNum != delCountBuffer.size()) {
					StringBuffer sb = new StringBuffer();
					sb.append(site);
					for (int i = 0; i < delCountBuffer.size(); i++) {
						sb.append("\t").append(coverageBuffer.get(i)).append("\t").append(delCountBuffer.get(i));
					}
					pw.println(sb.toString());
				}
				coverageBuffer.clear();
				delCountBuffer.clear();
				site = infos[0];
				coverageBuffer.add(Integer.valueOf(infos[1]));
				delCountBuffer.add(Integer.valueOf(infos[2]));
			}
		}
		
		zeroNum = 0;
		for (Integer i:delCountBuffer) {
			if (i.equals(0)) {
				zeroNum++;
			}
		}
		if (zeroNum != delCountBuffer.size()) {
			StringBuffer sb = new StringBuffer();
			sb.append(site);
			for (int i = 0; i < delCountBuffer.size(); i++) {
				sb.append("\t").append(coverageBuffer.get(i)).append("\t").append(delCountBuffer.get(i));
			}
			pw.println(sb.toString());
		}
		
		pw.close();
		br.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stubdeletion_detector.output.sorted
		run();
	}

}
