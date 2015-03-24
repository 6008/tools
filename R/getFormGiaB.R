library(data.table)
load("final_data.rda")
load("snpcalls.rda")

final_data_filter <- final_data[coverage < 500 & coverage > 2]
property_name <- c("Coverage", "BQ", "MQ")
property_col <- c(5:7)
software_name <- c("freedf", "gatkdf", "samfltdf", "snpdf", "vardf")

# GiaB
giab_data <- final_data_filter[dataset == "GiaB"]
giab_true_data <- giab_data[snp == "true"]
giab_snp_call <- datas[dataset == "GiaB"]
giab_snp_call <- giab_snp_call[order(-giab_snp_call$quality)]
for (j in 1 : length(property_col)) {
  summarytrue <- summary(giab_true_data[[property_col[j]]])
  summaryall <- summary(giab_data[[property_col[j]]])
  qunt <- c(summarytrue[[1]], (summarytrue[[1]] + summarytrue[[4]]) / 2, summarytrue[[4]], (summarytrue[[6]] + summarytrue[[4]]) / 2, summarytrue[[6]] + 1)
  
  print(paste("##", "GiaB", property_name[j], "SW", "TPR"))
  print("")
  print(paste(qunt[2], qunt[3], qunt[4]))
  print("")
  print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
  print("| -------- | ------- | ------- | ------- | ------- | ------- |")
  for (k in 1:(length(qunt) - 1)) {
    tlist18 <- giab_true_data[chrom == 18 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate18 <- giab_data[chrom == 18 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist19 <- giab_true_data[chrom == 19 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate19 <- giab_data[chrom == 19 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist20 <- giab_true_data[chrom == 20 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate20 <- giab_data[chrom == 20 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist21 <- giab_true_data[chrom == 21 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate21 <- giab_data[chrom == 21 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist22 <- giab_true_data[chrom == 22 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate22 <- giab_data[chrom == 22 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    true_length <- length(tlist18) + length(tlist19) + length(tlist20) + length(tlist21) + length(tlist22)
    
    str <- paste("| Region", k, "|")
    min_len <- length(candidate18) + length(candidate19) + length(candidate20) + length(candidate21) + length(candidate22)
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      sum_len <- length(scall18) + length(scall19) + length(scall20) + length(scall21) + length(scall22)
      if (sum_len < min_len) {
        min_len <- sum_len
      }
    }
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      
      call_data <- giab_snp_call[soft == software_name[s] & ((chrom == 18 & site %in% scall18) | (chrom == 19 & site %in% scall19) | (chrom == 20 & site %in% scall20) | (chrom == 21 & site %in% scall21) | (chrom == 22 & site %in% scall22))][1:min_len,]
      
      call_data_true <- call_data[(chrom == 18 & site %in% intersect(scall18, tlist18)) | (chrom == 19 & site %in% intersect(scall19, tlist19)) | (chrom == 20 & site %in% intersect(scall20, tlist20)) | (chrom == 21 & site %in% intersect(scall21, tlist21)) | (chrom == 22 & site %in% intersect(scall22, tlist22))]  
      
      
      str <- paste(str, format(dim(call_data_true)[1]/true_length, digits=5), "|")
    }
    print(str)
  }
  
  print("")
  print("---")
  print("")
  qunt <- c(summaryall[[1]], (summaryall[[1]] + summaryall[[4]]) / 2, summaryall[[4]], (summaryall[[6]] + summaryall[[4]]) / 2, summaryall[[6]] + 1)
  print(paste("##", "GiaB", property_name[j], "SW", "HM"))
  print("")
  print(paste(qunt[2], qunt[3], qunt[4]))
  print("")
  print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
  print("| -------- | ------- | ------- | ------- | ------- | ------- |")
  for (k in 1:(length(qunt) - 1)) {
    tlist18 <- giab_true_data[chrom == 18 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate18 <- giab_data[chrom == 18 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist19 <- giab_true_data[chrom == 19 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate19 <- giab_data[chrom == 19 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist20 <- giab_true_data[chrom == 20 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate20 <- giab_data[chrom == 20 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist21 <- giab_true_data[chrom == 21 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate21 <- giab_data[chrom == 21 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist22 <- giab_true_data[chrom == 22 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate22 <- giab_data[chrom == 22 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    true_length <- length(tlist18) + length(tlist19) + length(tlist20) + length(tlist21) + length(tlist22)
    
    str <- paste("| Region", k, "|")
    
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      
      call_data <- giab_snp_call[soft == software_name[s] & ((chrom == 18 & site %in% scall18) | (chrom == 19 & site %in% scall19) | (chrom == 20 & site %in% scall20) | (chrom == 21 & site %in% scall21) | (chrom == 22 & site %in% scall22))]
      
      call_data_true <- call_data[(chrom == 18 & site %in% intersect(scall18, tlist18)) | (chrom == 19 & site %in% intersect(scall19, tlist19)) | (chrom == 20 & site %in% intersect(scall20, tlist20)) | (chrom == 21 & site %in% intersect(scall21, tlist21)) | (chrom == 22 & site %in% intersect(scall22, tlist22))]
      
      str <- paste(str, format(2 * dim(call_data_true)[1]/(true_length + dim(call_data)[1]), digits=5), "|")
    }
    print(str)
  }
  
  print("")
  print("---")
  print("")
  
  qunt <- c(summarytrue[[1]], summarytrue[[2]], summarytrue[[3]], summarytrue[[5]], summarytrue[[6]] + 1)
  print(paste("##", "GiaB", property_name[j], "SC", "PPV"))
  print("")
  print(paste(qunt[2], qunt[3], qunt[4]))
  print("")
  print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
  print("| -------- | ------- | ------- | ------- | ------- | ------- |")
  for (k in 1:(length(qunt) - 1)) {
    tlist18 <- giab_true_data[chrom == 18 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate18 <- giab_data[chrom == 18 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist19 <- giab_true_data[chrom == 19 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate19 <- giab_data[chrom == 19 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist20 <- giab_true_data[chrom == 20 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate20 <- giab_data[chrom == 20 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist21 <- giab_true_data[chrom == 21 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate21 <- giab_data[chrom == 21 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist22 <- giab_true_data[chrom == 22 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate22 <- giab_data[chrom == 22 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    true_length <- length(tlist18) + length(tlist19) + length(tlist20) + length(tlist21) + length(tlist22)
    
    str <- paste("| Region", k, "|")
    min_len <- length(candidate18) + length(candidate19) + length(candidate20) + length(candidate21) + length(candidate22)
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      sum_len <- length(scall18) + length(scall19) + length(scall20) + length(scall21) + length(scall22)
      if (sum_len < min_len) {
        min_len <- sum_len
      }
    }
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      
      call_data <- giab_snp_call[soft == software_name[s] & ((chrom == 18 & site %in% scall18) | (chrom == 19 & site %in% scall19) | (chrom == 20 & site %in% scall20) | (chrom == 21 & site %in% scall21) | (chrom == 22 & site %in% scall22))][1:min_len,]
      
      call_data_true <- call_data[soft == software_name[s] & ((chrom == 18 & site %in% intersect(scall18, tlist18)) | (chrom == 19 & site %in% intersect(scall19, tlist19)) | (chrom == 20 & site %in% intersect(scall20, tlist20)) | (chrom == 21 & site %in% intersect(scall21, tlist21)) | (chrom == 22 & site %in% intersect(scall22, tlist22)))]  
      
      
      str <- paste(str, format(dim(call_data_true)[1]/true_length, digits=5), "|")
    }
    print(str)
  }
  
  print("")
  print("---")
  print("")
  
  qunt <- c(summaryall[[1]], summaryall[[2]], summaryall[[3]], summaryall[[5]], summaryall[[6]] + 1)
  print(paste("##", "GiaB", property_name[j], "SC", "HM"))
  print("")
  print(paste(qunt[2], qunt[3], qunt[4]))
  print("")
  print("| Software | FREEBAY | GATK    | SAMTOOL | MULTIGE | VARSCAN |")
  print("| -------- | ------- | ------- | ------- | ------- | ------- |")
  for (k in 1:(length(qunt) - 1)) {
    tlist18 <- giab_true_data[chrom == 18 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate18 <- giab_data[chrom == 18 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist19 <- giab_true_data[chrom == 19 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate19 <- giab_data[chrom == 19 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist20 <- giab_true_data[chrom == 20 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate20 <- giab_data[chrom == 20 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist21 <- giab_true_data[chrom == 21 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate21 <- giab_data[chrom == 21 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    tlist22 <- giab_true_data[chrom == 22 & giab_true_data[[property_col[j]]] < qunt[k+1] & giab_true_data[[property_col[j]]] > qunt[k]]$site
    candidate22 <- giab_data[chrom == 22 & giab_data[[property_col[j]]] < qunt[k+1] & giab_data[[property_col[j]]] > qunt[k]]$site
    
    true_length <- length(tlist18) + length(tlist19) + length(tlist20) + length(tlist21) + length(tlist22)
    
    str <- paste("| Region", k, "|")
    for (s in 1:length(software_name)) {
      scall18 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 18]$site, candidate18)
      scall19 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 19]$site, candidate19)
      scall20 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 20]$site, candidate20)
      scall21 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 21]$site, candidate21)
      scall22 <- intersect(giab_snp_call[soft == software_name[s] & chrom == 22]$site, candidate22)
      
      call_data <- giab_snp_call[soft == software_name[s] & ((chrom == 18 & site %in% scall18) | (chrom == 19 & site %in% scall19) | (chrom == 20 & site %in% scall20) | (chrom == 21 & site %in% scall21) | (chrom == 22 & site %in% scall22))]
      
      call_data_true <- call_data[(chrom == 18 & site %in% intersect(scall18, tlist18)) | (chrom == 19 & site %in% intersect(scall19, tlist19)) | (chrom == 20 & site %in% intersect(scall20, tlist20)) | (chrom == 21 & site %in% intersect(scall21, tlist21)) | (chrom == 22 & site %in% intersect(scall22, tlist22))]  
      
      
      str <- paste(str, format(2 * dim(call_data_true)[1]/(true_length + dim(call_data)[1]), digits=5), "|")
    }
    print(str)
  }
  
  print("")
  print("---")
  print("")
}


# ALL DATAS
