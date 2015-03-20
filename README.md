# tools
Some small useful tools

## BiuldData.java

Input selected (sorted) pileup file

Output the raw file of final R data.table with column

dataset chrom site sample coverage bq mq (snp)

## ProduceR.java

This code will produce R code for region analysis.

Each line of input file starts as this:

"chr18:1000-100000"

The output R code depends on other R data.

## SitesFinder.java

Filter out special sites with specified ratio of non-reference.

## SimpleFilter.java

Filter out special sites based on pilup files and site list files.
