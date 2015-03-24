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

Input selected (sorted) pileup file

Output site list file

## SimpleFilter.java

Filter out special sites based on pilup files and site list files.

Input orgin pileup files and site list files

Output selected pileup files (and use linux command to sort)

## SingleSite.java

Calculate single sites' R and N's ratio and bq

Input filtered single site pileup file

Ooutput result file

## com.watsoncui.ucr.stat package

Have some problem. Replace by BuildData.java and other R scripts.


## alldataprocess.R

Continue the work of BuildData.java. Produce fina_data datatable.

## emDebug.R

Test the process of GeMS's EM algorithm. Depend on em.R which is the core functions of GeMS.

## getForm.R

Calculate TPR and HM according to quntile and property(mq, bq, coverage) for each chromosome

# getFormGiab.R getFormupenn.R

Calculate  TPR and HM according to quntile and property(mq, bq, coverage) for GiaB(upenn) dataset

