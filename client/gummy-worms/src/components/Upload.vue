<template>
  <v-container class="d-flex flex-column align-center justify-center">
    <v-row class="d-flex flex-column align-center justify-center" style="width: 100%; max-width: 600px;">
      <v-card outlined class="mb-5" style="width: 100%; max-width: 600px;">
        <v-card-title>
          Upload File
        </v-card-title>

        <v-card-text>
          <p>Please select which database your file is from, this will tell us essential things about the formatting of the file. You can find more information on how to format your file and generate results in the correct format <a href="https://github.com/cropgeeks/GummyWorms#gummyworms-workflow" target="_blank">here</a>.</p>
          <v-select label="Select database:" :items="['Nemataxa', 'Silva']" v-model="database" ></v-select>
          <v-file-input v-model="file" label="Select a file" accept=".tsv,.csv,"/>
        </v-card-text>

        <v-card-actions>
          <v-btn color="primary" @click="uploadFile">
            Upload
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";
  export default {
    data() {
      return {
        file: null,
        database: null,
        folder: null
        }
      },
    methods: {
      async uploadFile() {
        if (!this.file) {
          alert("Please select a file to upload.");
          return;
        }

        let formData = new FormData();
        formData.append("file", this.file);
        formData.append("database", this.database);

        await axios.post("/submit", formData, {
          headers: { "Content-Type": "multipart/form-data" }
        })
        .then((response) => {
          this.folder = response.data;
          this.$router.push({ name: 'results', query: { folder: this.folder } });
        })
        .catch((error) => {
          console.error("Error uploading file:", error);
        });
      }
    }
  }

</script>

<style>
</style>