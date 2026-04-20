<template>
    <p v-for="value in responseData">{{ value }}</p>
</template>

<script>
import axios from 'axios';

export default {
  name: "Unknowns",
  props: {
    folder: {
      type: String,
      required: true
    }
  },
  data: () => ({
    responseData: null,
    folder: null
  }),
  methods: {
    getResults() {
        axios.get('/getUnknowns/?folder=' + this.folder)
        .then((response) => {
          this.responseData = response.data;
          console.log(this.responseData);
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
