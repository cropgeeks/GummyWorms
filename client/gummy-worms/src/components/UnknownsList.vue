<template>
  <div v-for="(items, category) in responseData" :key="category" ml-4>
    <h3>{{ category }}</h3>

    <ul>
      <li v-for="(item, index) in items" :key="index">
        {{ item }}
      </li>
    </ul>
    <br>
  </div>
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
