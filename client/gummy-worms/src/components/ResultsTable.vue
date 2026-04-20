<template>
    <v-data-table
      :headers="headers"
      :items="responseData"></v-data-table>
</template>

<script>
import axios from 'axios';

export default {
  name: "ResultsTable",
  props: {
    folder: {
      type: String,
      required: true
    }
  },
  data: () => ({
    responseData: null,
    folder: null,
    headers: [
        { title: 'Sample', key: 'Sample' },
        { title: 'Bacterivore', key: 'Bacterivore' },
        { title: 'Bacterivore %', key: 'Bacterivore%' },
        { title: 'Fungivore', key: 'Fungivore' },
        { title: 'Fungivore %', key: 'Fungivore%' },
        { title: 'Herbivore', key: 'Herbivore' },
        { title: 'Herbivore %', key: 'Herbivore%' },
        { title: 'Omnivore', key: 'Omnivore' },
        { title: 'Omnivore %', key: 'Omnivore%' },
        { title: 'Omnivore-Predator', key: 'Omnivore-Predator' },
        { title: 'Omnivore-Predator %', key: 'Omnivore-Predator%' },
        { title: 'Unknown', key: 'Unknown' },
        { title: 'Unknown %', key: 'Unknown%' },
        { title: 'Non-Nematode', key: 'Non-Nematode' },
        { title: 'Non-Nematode %', key: 'Non-Nematode%' },
        { title: 'Not Genus Level', key: 'Not Genus Level' },
        { title: 'Not Genus Level %', key: 'Not Genus Level%' },
        { title: 'Unassigned', key: 'Unassigned' },
        { title: 'Unassigned %', key: 'Unassigned%' },
      ]
  }),
  methods: {
    getResults() {
        axios.get('/getResults/?folder=' + this.folder)
        .then((response) => {
          this.responseData = response.data;
        })
        .catch((error) => {
          console.error("Error fetching results:", error);
        });
    }
  },

  mounted() {
    this.folder = this.$route.query.folder;
    this.getResults();
  }
}
</script>
