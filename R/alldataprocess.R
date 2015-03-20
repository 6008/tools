library(data.table)
names <- c("dataset", "chrom", "site", "sample", "coverage", "bq", "mq")
fullnames <- c("dataset", "chrom", "site", "sample", "coverage", "bq", "mq", "snp")
snp_giab <- data.table(read.table(file="GiaB1426630407273.builddata"))
all_giab <- data.table(read.table(file="GiaB1426634927657.builddata"))
setnames(snp_giab, colnames(snp_giab), names)
setnames(all_giab, colnames(all_giab), names)
g18 <- all_giab[all_giab$chrom==18]
g18t <- snp_giab[snp_giab$chrom==18]
fp18 <- setdiff(g18$site, g18t$site)
g18f <- g18[g18$site %in% fp18]
g19 <- all_giab[all_giab$chrom==19]
g19t <- snp_giab[snp_giab$chrom==19]
fp19 <- setdiff(g19$site, g19t$site)
g19f <- g19[g19$site %in% fp19]
g20 <- all_giab[all_giab$chrom==20]
g20t <- snp_giab[snp_giab$chrom==20]
fp20 <- setdiff(g20$site, g20t$site)
g20f <- g20[g20$site %in% fp20]
g21 <- all_giab[all_giab$chrom==21]
g21t <- snp_giab[snp_giab$chrom==21]
fp21 <- setdiff(g21$site, g21t$site)
g21f <- g21[g21$site %in% fp21]
g22 <- all_giab[all_giab$chrom==22]
g22t <- snp_giab[snp_giab$chrom==22]
fp22 <- setdiff(g22$site, g22t$site)
g22f <- g22[g22$site %in% fp22]
f_giab <- rbind(g18f, g19f, g20f, g21f, g22f)
f_giab <- cbind(f_giab, rep("false", length(f_giab$dataset)))
snp_giab <- cbind(snp_giab, rep("true", length(snp_giab$dataset)))
giab_data <- rbind(snp_giab, f_giab)
setnames(giab_data, colnames(giab_data), fullnames)

snp_upenn <- data.table(read.table(file="upenn1426630426751.builddata"))
all_upenn <- data.table(read.table(file="upenn1426634969185.builddata"))
setnames(snp_upenn, colnames(snp_upenn), names)
setnames(all_upenn, colnames(all_upenn), names)
u19 <- all_upenn[all_upenn$chrom==19]
u19t <- snp_upenn[snp_upenn$chrom==19]
fp19 <- setdiff(u19$site, u19t$site)
u19f <- u19[u19$site %in% fp19]
u20 <- all_upenn[all_upenn$chrom==20]
u20t <- snp_upenn[snp_upenn$chrom==20]
fp20 <- setdiff(u20$site, u20t$site)
u20f <- u20[u20$site %in% fp20]
u21 <- all_upenn[all_upenn$chrom==21]
u21t <- snp_upenn[snp_upenn$chrom==21]
fp21 <- setdiff(u21$site, u21t$site)
u21f <- u21[u21$site %in% fp21]
u22 <- all_upenn[all_upenn$chrom==22]
u22t <- snp_upenn[snp_upenn$chrom==22]
fp22 <- setdiff(u22$site, u22t$site)
u22f <- u22[u22$site %in% fp22]
f_upenn <- rbind(u19f, u20f, u21f, u22f)
f_upenn <- cbind(f_upenn, rep("false", length(f_upenn$dataset)))
snp_upenn <- cbind(snp_upenn, rep("true", length(snp_upenn$dataset)))
upenn_data <- rbind(snp_upenn, f_upenn)
setnames(upenn_data, colnames(upenn_data), fullnames)

final_data <- rbind(giab_data, upenn_data)
