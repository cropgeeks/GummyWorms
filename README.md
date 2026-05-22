# Gummyworms workflow
## Import your data

This tutorial will show you how to preprocess you nematode amplicon data for use iwth Gummyworms a function feeding guild database that works with QIime2 outputs.

First you will need to import your data, you will need to make a manifest as a tsv that looks like this:

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

You will then need to do some quality checks on your data and error correct your reads. To do this we will use the follwoing script:
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
