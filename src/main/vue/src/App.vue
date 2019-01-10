<template>
  <div id="app">
    <el-header class="header">
      <h4>User Story Map</h4>
    </el-header>
    <el-main>
      <router-view/>
    </el-main>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
//
// export default {
//   name: 'app',
//   components: {
//     HelloWorld
//   }
// }
</script>

<style>
  body{
    margin:0;
    padding:0;
  }
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }
  .header {
    width: 100%;
    height: 60px;
    padding: 15px 30px;
    box-sizing: border-box;
    box-shadow: 0 0 2px 2px #eee;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>
