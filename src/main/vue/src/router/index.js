import Vue from 'vue'
import Router from 'vue-router'

import HelloWorld from '../components/HelloWorld'
import Passport from '../components/Passport'

Vue.use(Router)

const routes = [
  {
    path: '/passport',
    name: 'Passport',
    component: Passport
  }, {
    path: '/',
    name: 'Hello',
    component: HelloWorld
  }
]

const router = new Router({routes: routes})
export default router