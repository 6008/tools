library(data.table)
load("final_data.rda")
load("snpcalls.rda")

## Each Chromosome
final_data_filter <- final_data[coverage < 500 & coverage > 2]
chroms_name <- rbind(c(rep("GiaB", 5), rep("upenn", 4)), c(18:22, 19:22))
property_name <- c("Coverage", "BQ", "MQ")
property_col <- c(5:7)
software_name <- c("freedf", "gatkdf", "samfltdf", "snpdf", "vardf")
for (i in 1 : dim(chroms_name)[2]) {
  subdata <- final_data_filter[dataset == chroms_name[1, i] & chrom == as.numeric(chroms_name[2, i])]
  subdatatrue <- subdata[snp == "true"]
  snpcall <- datas[dataset == chroms_name[1, i] & chrom == as.numeric(chroms_name[2, i])]
  snpcall <- snpcall[order(-snpcall$quality)]
  for (j in 1 : length(property_col)) {
    summarytrue <- summary(subdatatrue[[property_col[j]]])
    summaryall <- summary(subdata[[property_col[j]]])
    qunt <- c(summarytrue[[1]], (summarytrue[[1]] + summarytrue[[4]]) / 2, summarytrue[[4]], (summarytrue[[6]] + summarytrue[[4]]) / 2, summarytrue[[6]] + 1)
    
    print(paste("##", chroms_name[1, i], chroms_name[2, i], property_name[j], "SW", "TPR"))
    print("")
    print(paste(qunt[2], qunt[3], qunt[4]))
    print("")
    print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
    print("| -------- | ------- | ------- | ------- | ------- | ------- |")
    for (k in 1:(length(qunt) - 1)) {
      tlist <- subdatatrue[subdatatrue[[property_col[j]]] < qunt[k+1] & subdatatrue[[property_col[j]]] > qunt[k]]$site
      candidate <- subdata[subdata[[property_col[j]]] < qunt[k+1] & subdata[[property_col[j]]] > qunt[k]]$site
      str <- paste("| Region", k, "|")
      min_len <- length(candidate)
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        if (length(scall) < min_len) {
          min_len <- length(scall)
        }
      }
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        str <- paste(str, format(length(intersect(scall[1:min_len], tlist))/length(tlist), digits=5), "|")
      }
      print(str)
    }
    
    print("")
    print("---")
    print("")
    qunt <- c(summaryall[[1]], (summaryall[[1]] + summaryall[[4]]) / 2, summaryall[[4]], (summaryall[[6]] + summaryall[[4]]) / 2, summaryall[[6]] + 1)
    print(paste("##", chroms_name[1, i], chroms_name[2, i], property_name[j], "SW", "HM"))
    print("")
    print(paste(qunt[2], qunt[3], qunt[4]))
    print("")
    print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
    print("| -------- | ------- | ------- | ------- | ------- | ------- |")
    for (k in 1:(length(qunt) - 1)) {
      tlist <- subdatatrue[subdatatrue[[property_col[j]]] < qunt[k+1] & subdatatrue[[property_col[j]]] > qunt[k]]$site
      candidate <- subdata[subdata[[property_col[j]]] < qunt[k+1] & subdata[[property_col[j]]] > qunt[k]]$site
      str <- paste("| Region", k, "|")
      
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        str <- paste(str, format(2 * length(intersect(scall, tlist)) / (length(tlist) + length(scall)), digits=5), "|")
      }
      print(str)
    }
    
    print("")
    print("---")
    print("")
    
    qunt <- c(summarytrue[[1]], summarytrue[[2]], summarytrue[[3]], summarytrue[[5]], summarytrue[[6]] + 1)
    print(paste("##", chroms_name[1, i], chroms_name[2, i], property_name[j], "SC", "TPR"))
    print("")
    print(paste(qunt[2], qunt[3], qunt[4]))
    print("")
    print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
    print("| -------- | ------- | ------- | ------- | ------- | ------- |")
    for (k in 1:(length(qunt) - 1)) {
      tlist <- subdatatrue[subdatatrue[[property_col[j]]] < qunt[k+1] & subdatatrue[[property_col[j]]] > qunt[k]]$site
      candidate <- subdata[subdata[[property_col[j]]] < qunt[k+1] & subdata[[property_col[j]]] > qunt[k]]$site
      str <- paste("| Region", k, "|")
      min_len <- length(candidate)
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        if (length(scall) < min_len) {
          min_len <- length(scall)
        }
      }
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        str <- paste(str, format(length(intersect(scall[1:min_len], tlist))/length(tlist), digits=5), "|")
      }
      print(str)
    }
    
    print("")
    print("---")
    print("")
    
    qunt <- c(summaryall[[1]], summaryall[[2]], summaryall[[3]], summaryall[[5]], summaryall[[6]] + 1)
    print(paste("##", chroms_name[1, i], chroms_name[2, i], property_name[j], "SC", "HM"))
    print("")
    print(paste(qunt[2], qunt[3], qunt[4]))
    print("")
    print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
    print("| -------- | ------- | ------- | ------- | ------- | ------- |")
    for (k in 1:(length(qunt) - 1)) {
      tlist <- subdatatrue[subdatatrue[[property_col[j]]] < qunt[k+1] & subdatatrue[[property_col[j]]] > qunt[k]]$site
      candidate <- subdata[subdata[[property_col[j]]] < qunt[k+1] & subdata[[property_col[j]]] > qunt[k]]$site
      str <- paste("| Region", k, "|")
      
      for (s in 1:length(software_name)) {
        scall <- intersect(snpcall[soft == software_name[s]]$site, candidate)
        str <- paste(str, format(2 * length(intersect(scall, tlist)) / (length(tlist) + length(scall)), digits=5), "|")
      }
      print(str)
    }
    
    print("")
    print("---")
    print("")
  }
}
