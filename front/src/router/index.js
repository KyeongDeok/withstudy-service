import Vue from 'vue'
import Router from 'vue-router'
import StudyView from "../components/study/StudyView";
import StudyList from "../components/study/StudyList";
import StudyMine from "../components/study/StudyMine";
import StudyCreate from "../components/study/StudyCreate";

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: `/`,
      component: StudyView
    },
    {
      path: `/study/list`,
      component: StudyList
    },
    {
      path: `/study/:id`,
      component: StudyView
    },
    {
      path: `/study/new`,
      component: StudyCreate
    },
    {
      path: `/study/mine`,
      component: StudyMine
    },
  ]
})
