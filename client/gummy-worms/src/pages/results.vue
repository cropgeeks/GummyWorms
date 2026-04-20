<template>
    <p>Results Page</p> 
    <v-btn @click="showResultsTable = !showResultsTable">
        Toggle Results Table
    </v-btn>
    <v-btn @click="downloadResults()">Download Results</v-btn>
    <v-btn @click="showUnknownsList = !showUnknownsList">
        Toggle Unknowns List
    </v-btn>
    <v-btn @click="downloadUnknowns()">Download Unknowns</v-btn>

    <ResultsTable :folder="$route.query.folder" v-if="showResultsTable"/>
    <UnknownsList :folder="$route.query.folder" v-if="showUnknownsList"/>
</template>

<script>
import ResultsTable from '@/components/ResultsTable.vue';
import UnknownsList from '@/components/UnknownsList.vue';

export default {
  name: "ResultsPage",
  components: {
    ResultsTable,
    UnknownsList
  },
    data() {
        return {
        showResultsTable: true,
        showUnknownsList: true
        }
    },
    methods: {
        downloadResults() {
            const folder = this.$route.query.folder;
            window.open('/api/downloadResults/?folder=' + folder, '_blank');
        },
        downloadUnknowns() {
            const folder = this.$route.query.folder;
            window.open('/api/downloadUnknowns/?folder=' + folder, '_blank');
        }
    }
}
</script>