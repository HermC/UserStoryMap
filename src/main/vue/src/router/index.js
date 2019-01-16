import Vue from 'vue'
import Router from 'vue-router'

import HelloWorld from '../components/HelloWorld'
import Passport from '../components/Passport'
import StoryMap from '../components/StoryMap'
import User from '../components/User'



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
  }, {
    path: '/map',
    name: 'StoryMap',
    component: StoryMap
  }, {
    path: '/user',
    name: 'User',
    component: User
  }
]

const router = new Router({routes: routes})
export default router