<template>
  <v-app>
    <Header/>
    <Body></Body>
    <Footer></Footer>
  </v-app>
</template>

<script>
  import Header from "./Header";
  import Body from "./Body";
  import Footer from "./Footer";

  export default {
    name: "MainView",
    components: {
      Header,
      Body,
      Footer
    }
  }
</script>
