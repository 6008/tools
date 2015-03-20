import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;


public class ProduceR {

	public static final String input_low = "/home/xinping/Desktop/6008/datas/upenn/low";
	public static final String input_high = "/home/xinping/Desktop/6008/datas/upenn/high";
	public static final String output = "/home/xinping/Desktop/6008/datas/upenn/";
	
	public static void calc(int num) throws Exception {
		BufferedReader low_br = new BufferedReader(new FileReader(new File(input_low + String.valueOf(num))));
		BufferedReader high_br = new BufferedReader(new FileReader(new File(input_high + String.valueOf(num))));
		
		PrintWriter pw = new PrintWriter(new FileWriter(new File(output + String.valueOf(num) + ".R")));
	
		String[] softs = new String[6];
		softs[0] = "true";
		softs[1] = "cpp";
		softs[2] = "free";
		softs[3] = "gatk";
		softs[4] = "samflt";
		softs[5] = "var";
		
		int lowcount = 0;
		int highcount = 0;
		
		String line;
		
		String chrom = "";
		
		pw.println("#Produced by ProduceR.java");
		
		while (null != (line = low_br.readLine())) {
			
			line = line.replace(':', ' ');
			line = line.replace('-', ' ');
			line = line.replaceAll("\"", "");
			
			lowcount++;
			String[] infos = line.split(" ");
			
			if(!infos[0].equals(chrom)) {
				if(infos[0].contains("chr")) {
					pw.println("load(\"regions." + infos[0] + ".rda\")");
				}
				else {
					pw.println("load(\"regions.chr" + infos[0] + ".rda\")");	
				}
			}
			
			chrom = infos[0];
			
			for (int i = 0; i < softs.length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(softs[i]).append("low").append(lowcount).append(" <- ").append(softs[i]);
				sb.append("snps[which(").append(softs[i]).append("snps < ").append(infos[2]);
				sb.append(" & ").append(softs[i]).append("snps >= ").append(infos[1]).append(")]");
				pw.println(sb.toString());

			}
		}
		
		while (null != (line = high_br.readLine())) {
			
			line = line.replace(':', ' ');
			line = line.replace('-', ' ');
			line = line.replaceAll("\"", "");
			
			System.out.println(line);
			
			highcount++;
			String[] infos = line.split(" ");
			
			if(!infos[0].equals(chrom)) {
				if(infos[0].contains("chr")) {
					pw.println("load(\"regions." + infos[0] + ".rda\")");
				}
				else {
					pw.println("load(\"regions.chr" + infos[0] + ".rda\")");	
				}
			}
			
			chrom = infos[0];
			
			for (int i = 0; i < softs.length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(softs[i]).append("high").append(highcount).append(" <- ").append(softs[i]);
				sb.append("snps[which(").append(softs[i]).append("snps < ").append(infos[2]);
				sb.append(" & ").append(softs[i]).append("snps >= ").append(infos[1]).append(")]");
				pw.println(sb.toString());

			}
		}
		
		
		for (int i = 1; i < softs.length; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(softs[i]).append("_low_values <- c(\n");
			
			for (int j = 1; j <= lowcount; j++) {
				sb.append("2*length(intersect(").append(softs[0]).append("low").append(j).append(", ");
				sb.append(softs[i]).append("low").append(j).append("))/(length(").append(softs[0]).append("low");
				sb.append(j).append(") + length(").append(softs[i]).append("low").append(j).append("))");
				if (j < lowcount) {
					sb.append(",\n");
				}
			}
			
			sb.append(")");
			
			pw.println(sb.toString());
		}
		
		for (int i = 1; i < softs.length; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(softs[i]).append("_high_values <- c(\n");
			
			for (int j = 1; j <= highcount; j++) {
				sb.append("2*length(intersect(").append(softs[0]).append("high").append(j).append(", ");
				sb.append(softs[i]).append("high").append(j).append("))/(length(").append(softs[0]).append("high");
				sb.append(j).append(") + length(").append(softs[i]).append("high").append(j).append("))");
				if (j < highcount) {
					sb.append(",\n");
				}
			}
			
			sb.append(")");
			
			pw.println(sb.toString());
		}
		
		for (int i = 1; i < softs.length; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(softs[i]).append("_overall <- c(2*(\n");
			for (int j = 1; j <= lowcount; j++) {
				sb.append("length(intersect(").append(softs[0]).append("low").append(j);
				sb.append(", ").append(softs[i]).append("low").append(j).append("))");
				if (j < lowcount) {
					sb.append(" +\n");
				} else {
					sb.append(") / (\n");
				}
			}
			
			for (int j = 1; j <= lowcount; j++) {
				sb.append("length(").append(softs[0]).append("low").append(j).append(") + length(");
				sb.append(softs[i]).append("low").append(j);
				if (j < lowcount) {
					sb.append(") +\n");
				} else {
					sb.append(")\n");
				}
			}
			
			sb.append("), 2*(\n");
			
			for (int j = 1; j <= highcount; j++) {
				sb.append("length(intersect(").append(softs[0]).append("high").append(j);
				sb.append(", ").append(softs[i]).append("high").append(j).append("))");
				if (j < highcount) {
					sb.append(" +\n");
				} else {
					sb.append(") / (\n");
				}
			}
			
			for (int j = 1; j <= highcount; j++) {
				sb.append("length(").append(softs[0]).append("high").append(j).append(") + length(");
				sb.append(softs[i]).append("high").append(j);
				if (j < highcount) {
					sb.append(") +\n");
				} else {
					sb.append(")))\n");
				}
			}
			
			pw.println(sb.toString());
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("save(");
		for (int i = 1; i < softs.length; i++) {
			sb.append(softs[i]).append("_overall, ").append(softs[i]).append("_low_values, ");
			sb.append(softs[i]).append("_high_values, ");
		}
		sb.append("file = \"frag_results.rda\")");
		
		pw.println(sb);
		
		pw.println("# Analyze results");
		
		sb = new StringBuffer();
		
		sb.append("low_values <-\n").append("rbind(").append(softs[1]).append("_low_values");
		for (int i = 2; i < softs.length; i++) {
			sb.append(", ").append(softs[i]).append("_low_values");
		}
		sb.append(")\ntable(row.names(low_values)[apply(low_values,2,which.max)])\n");

		sb.append("high_values <-\n").append("rbind(").append(softs[1]).append("_high_values");
		for (int i = 2; i < softs.length; i++) {
			sb.append(", ").append(softs[i]).append("_high_values");
		}
		sb.append(")\ntable(row.names(high_values)[apply(high_values,2,which.max)])");

		pw.println(sb.toString());
		
		pw.close();
		
		low_br.close();
		high_br.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		calc(10);
		//calc(15);
		//calc(20);
		//calc(25);
		//calc(50);
	}

}
