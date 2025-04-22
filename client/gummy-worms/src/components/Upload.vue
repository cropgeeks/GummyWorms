<template>
  <v-container class="d-flex flex-column align-center justify-center">
    <v-row class="d-flex flex-column align-center justify-center" style="width: 100%; max-width: 600px;">
      <v-card outlined class="mb-5" style="width: 100%; max-width: 600px;">
        <v-card-title>
          Upload File
        </v-card-title>

        <v-card-text>
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
        file: null
        }
      },
    methods: {
      async uploadFile() {
        if (!this.file) {
          alert("Please select a file to upload.");
          return;
        }

        const formData = new FormData();
        formData.append("file", this.file);

        await axios.get("/api/submit", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log("File uploaded successfully:", response.data);
          alert("File uploaded successfully!");
        })
        .catch((error) => {
          console.error("Error uploading file:", error);
          alert("Error uploading file. Please try again.");
        });
      }
    }
  }

</script>

<style>
</style>