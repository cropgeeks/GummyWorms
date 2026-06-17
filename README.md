# GummyWorms workflow

This workflow uses QIIME 2 and GummyWorms to generate functional summary data from nematode amplicon sequences. 
Please note that the developers of GummyWorms are independent of the QIIME 2 developers. 

If you use this workflow, you must cite both GummyWorms and QIIME 2 in any resulting work.

These workflows are simplified examples designed to help users process data with QIIME2 from raw all the way to GummyWorms. Therefore file names, paths, and parameters will need to be modified to match your own dataset and computing environment.


## Import your data

This tutorial will show you how to preprocess you nematode amplicon data for use with GummyWorms a functional feeding guild database that works with QIIME2 outputs.

First you will need to import your data, to do this you will need to make a manifest as a TSV that looks like this:

```
sample-id    forward-absolute-filepath    reverse-absolute-filepath
Sample1    /path/to/fastq/Sample1_R1.fastq.gz    /path/to/fastq/Sample1_R2.fastq.gz
Sample2    /path/to/fastq/Sample2_R1.fastq.gz    /path/to/fastq/Sample2_R2.fastq.gz
Sample3    /path/to/fastq/Sample3_R1.fastq.gz    /path/to/fastq/Sample3_R2.fastq.gz
```

Then you need to load your samples into QIIME 2 using that manifest:
```
# Set working directory
cd /home/user

# Activate your QIIME 2 environment, using the name you gave it when you installed it.
conda activate qiime2-amplicon-2026.1

# Define input manifest and grab the basename to use it to name your outputs
manifest="nematode_manifest"
base_name=$(basename "$manifest" _manifest)

# Define base directory
workdir="/path/to/data"

# Import using manifest
qiime tools import \
  --type 'SampleData[PairedEndSequencesWithQuality]' \
  --input-path "${workdir}/${manifest}.tsv" \
  --input-format PairedEndFastqManifestPhred33V2 \
  --output-path "${workdir}/${base_name}_paired_end.qza"

# Summarise demultiplexed data
qiime demux summarize \
  --i-data "${workdir}/${base_name}_paired_end.qza" \
  --o-visualization "${workdir}/${base_name}_paired_end_viz.qzv"
```
## Denoise your data

You will then need to do some quality checks on your data and error correct your reads. To do this we will use the following script, the parameters you use here should be dictated by the visualisations and outputs from the previous steps. Each data set is different and it is up to you and your data as to which parameters you choose below is an example :
```

# Activate your QIIME 2 environment, using the name you gave it when you installed it.
conda activate qiime2-amplicon-2026.1

#go into the working directory
cd /path/to/data

# Define sample/run name
run="nematode"

# Run DADA2 denoising (paired-end)
qiime dada2 denoise-paired \
  --i-demultiplexed-seqs "${run}_paired_end.qza" \
  --p-trunc-len-f 250 \
  --p-trunc-len-r 200 \
  --p-trim-left-f 20 \
  --p-trim-left-r 20 \
  --o-table "${run}_table.qza" \
  --o-representative-sequences "${run}_rep-seqs.qza" \
  --o-denoising-stats "${run}_denoising-stats.qza"

# Visualise denoising stats
qiime metadata tabulate \
  --m-input-file "${run}_denoising-stats.qza" \
  --o-visualization "${run}_denoising-stats_viz.qzv"
```
## Assign taxonomy to your sequences

You then need to make a database into a classifier to use with your data. 
The example below is NemaTaxa, the input files are available at https://github.com/HCRU-Bioinformatics/NEMAtaxa

This is done by the following script:

```
# Activate your QIIME 2 environment, using the name you gave it when you installed it.
conda activate qiime2-amplicon-2026.1

#import the sequences
qiime tools import \
  --type 'FeatureData[Sequence]' \
  --input-path NemaTaxa_V1.fasta \
  --output-path NemaTaxa_V1_seq.qza

# import the taxonomy information
qiime tools import \
  --type 'FeatureData[Taxonomy]' \
  --input-format HeaderlessTSVTaxonomyFormat \
  --input-path NemaTaxa_V1.taxonomy \
  --output-path NemaTaxa_V1_taxonomy.qza

#create the classifier
qiime feature-classifier fit-classifier-naive-bayes \
  --i-reference-reads NemaTaxa_V1_seq.qza \
  --i-reference-taxonomy NemaTaxa_V1_taxonomy.qza \
  --o-classifier NemaTaxa_2026.1_seq_classifier.qza
```

Afterwards you can use this newly created database to classify your sequences, you will also need your metadata file 
in a tab separated format:

```

# Activate QIIME 2 environment
conda activate qiime2-amplicon-2026.1

# Define run/sample name
run="nematode"

# Classify sequences using pre-trained classifier
qiime feature-classifier classify-sklearn \
  --i-classifier NemaTaxa_2026.1_seq_classifier.qza \
  --i-reads "${run}_rep-seqs.qza" \
  --o-classification "${run}_taxonomy.qza"

# Visualise taxonomy assignments
qiime metadata tabulate \
  --m-input-file "${run}_taxonomy.qza" \
  --o-visualization "${run}_taxonomy_viz.qzv"

# Generate taxa bar plot
qiime taxa barplot \
  --m-metadata-file "${run}_metadata.tsv" \
  --i-table "${run}_table.qza" \
  --i-taxonomy "${run}_taxonomy.qza" \
  --o-visualization "${run}_taxa_barplot.qzv"
```
Then upload the nematode_taxa_barplot.qzv output in the the QIIME 2 view webpage https://view.qiime2.org/ using the drag and drop. 
Here you will see a taxa bar plot similar this this:
<img width="1551" height="1098" alt="image" src="https://github.com/user-attachments/assets/18d40ce2-e320-4877-a737-61a476b8ae76" />

Make sure you have selected the highest (level 6 or level 7) taxonomic resolution you can and click download CSV in the left-hand corner. Convert the CSV to a TSV. This can be done by opening it in excel as a CSV and save as a tab-delimited. 

The table will look somthing like this, where the top row are taxonomic names and the first column are your samples:

<img width="1105" height="422" alt="image" src="https://github.com/user-attachments/assets/0f13aa9a-4702-4f07-a322-8e0b178cff82" />


## Using GummyWorms

Go to https://gummyworms.hutton.ac.uk/ and select the database you used, in our case this is NemaTaxa. Simply drag and drop your TSV. 

<img width="1012" height="641" alt="image" src="https://github.com/user-attachments/assets/77d9445f-38e0-44eb-8744-d2686f1bef43" />


