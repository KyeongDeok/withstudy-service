<template>
  <v-app-bar dark
             app
  >
    <router-link :to="'/'">
      <v-toolbar-title class="font-weight-bold" style="color: white">WITH STUDY</v-toolbar-title>
    </router-link>
    <router-link tag="li" class="mx-2" :to="`/study/mine`">
      내 스터디
    </router-link>
    <router-link tag="li" class="mx-2" :to="`/study/list`">
      스터디 목록
    </router-link>

    <v-spacer></v-spacer>

    <router-link tag="li" class="mx-2" :to="`/study/list`">
      <a>
        <v-icon>mdi-bell-outline</v-icon>
      </a>
    </router-link>
    <v-btn text color="grey">
      <v-icon>mdi-bell-outline</v-icon>
    </v-btn>
    <v-btn text color="grey">
      <span>로그아웃</span>
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
  export default {
    name: "Header"
  }
</script>

<style scoped>
  li * {
    color: grey;
  }

  .router-link-active {
    color: white;
  }

  li {
    list-style: none;
  }
</style>
