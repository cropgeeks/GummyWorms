# Gummyworms workflow

This workflow uses QIIME2 and GummyWorms to generate functional summary data from nematode amplicon sequences. 
Please note that the developers of GummyWorms are independent of the QIIME2 developers. 

If you use this workflow, you must cite both GummyWorms and QIIME2 in any resulting work.

These workflows are simplified examples designed to help users process data with QIIME2 from raw all the way to GummyWorms. Therefore file names, paths, and parameters will need to be modified them to match your own dataset and computing environment.


## Import your data

This tutorial will show you how to preprocess you nematode amplicon data for use iwth Gummyworms a functional feeding guild database that works with Qiime2 outputs.

First you will need to import your data, to do this you will need to make a manifest as a tsv that looks like this:

```
sample-id    forward-absolute-filepath    reverse-absolute-filepath
Sample1    /path/to/fastq/Sample1_R1.fastq.gz    /path/to/fastq/Sample1_R2.fastq.gz
Sample2    /path/to/fastq/Sample2_R1.fastq.gz    /path/to/fastq/Sample2_R2.fastq.gz
Sample3    /path/to/fastq/Sample3_R1.fastq.gz    /path/to/fastq/Sample3_R2.fastq.gz
```

Then you need to load your samples into Qiime2 using that manifest:
```
# Set working directory
cd /home/user

# Activate QIIME2 environment, whatever name you gave it when you installed it.
source activate qiime2-amplicon-2026.1

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

You will then need to do some quality checks on your data and error correct your reads. To do this we will use the follwoing script, \
the parameters you use here should be dictated by the visualisations and outputs from the previous steps. Each data set is different and it is up to you and your data as to which parameters you choose \
below is an example :
```

# Activate QIIME2 environment, whatever name you gave it when you installed it.
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
## Assigntaxonomy to your data

You then need to make a database into a classifer to use with your data. 
The example below is Nemataxa, the input files are avalible at https://github.com/HCRU-Bioinformatics/NEMAtaxa

This is done by the following script:

```
# Activate QIIME2 environment, whatever name you gave it when you installed it.
conda activate qiime2-amplicon-2026.1

qiime tools import \
  --type 'FeatureData[Sequence]' \
  --input-path NemaTaxa_V1.fasta \
  --output-path NemaTaxa_V1_seq.qza

#then the taxonomy inforation
qiime tools import \
  --type 'FeatureData[Taxonomy]' \
  --input-format HeaderlessTSVTaxonomyFormat \
  --input-path NemaTaxa_V1.taxonomy \
  --output-path NemaTaxa_V1_taxonomy.qza

qiime feature-classifier fit-classifier-naive-bayes \
  --i-reference-reads NemaTaxa_V1_seq.qza \
  --i-reference-taxonomy NemaTaxa_V1_taxonomy.qza \
  --o-classifier NemaTaxa_2026.1_seq_classifier.qza
```

Afterwards you can use this newly created database to classify your sequences, you will also need your metadata file \
in a tab seperated format:

```

# Activate QIIME2 environment
source activate qiime2-amplicon-2025.10

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
Then upload the nematode_taxa_barplot.qzv output in the the Qiime2 view webpage https://view.qiime2.org/ using the drag and drop. 
Here you will see a taxa bar plot similar this this:
<img width="1551" height="1098" alt="image" src="https://github.com/user-attachments/assets/18d40ce2-e320-4877-a737-61a476b8ae76" />

Make sure you have selected the highest (level 6 or level 7) taxonomic resolution you can and click download CSV in the left hand corner. Convert the csv to a TSV. This can be done by opening it in excel as a csv and save as a Tab-delimited. 

## Using Gummyworms

Go to https://gummyworms.hutton.ac.uk/ and select the database you used, in our case this is NemaTaxa. Simply drag and drop your TSV. 

<img width="1135" height="579" alt="image" src="https://github.com/user-attachments/assets/ab870c4f-c027-4b3b-83c1-0c0b2de55cbd" />

