wget ftp://ftp.ncbi.nlm.nih.gov/genomes/refseq/bacteria/assembly_summary.txt
awk -F "\t" '$12=="Complete Genome" && $11=="latest"{print $20}' assembly_summary.txt > ftpdirpaths
#Change the filesuffix according to your case
awk 'BEGIN{FS=OFS="/";filesuffix="genomic.gbff.gz"}{ftpdir=$0;asm=$10;file=asm"_"filesuffix;print ftpdir,file}' ftpdirpaths > ftpfilepaths
#Then edit the file ftpfilepaths or use some other shell script to download
