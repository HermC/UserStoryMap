<template>
   <div class="bg">
      <el-main>
         <div class="content">
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
               <el-menu-item index="1">我的故事地图</el-menu-item>
               <el-menu-item index="2">消息中心</el-menu-item>
               <el-menu-item index="3">账号管理</a></el-menu-item>
            </el-menu>
         
            <div class="menu-content menu-content-maps" v-if="activeIndex == '1'">
               <div class="map" v-for="i in [1,2,3,4,5,6,7]">
                  <div class="name">我的故事地图</div>
                  <div class="desc">我的故事地图的描述</div>
                  <i class="el-icon-edit" @click="dialogVisible = true"></i>
               </div>
            </div>   
            <div class="menu-content" v-else-if="activeIndex == '2'">

            </div>
            <div class="menu-content" v-else-if="activeIndex == '3'">

            </div>
         </div>
         <el-dialog
            title="修改故事地图"
            :visible.sync="dialogVisible"
            width="50%">
            <el-form :model="mapEdit">
               <el-form-item label="地图名称">
                  <el-input v-model="mapEdit.name" autocomplete="off"></el-input>
               </el-form-item>
               <el-form-item label="地图描述">
                  <el-input v-model="mapEdit.desc" autocomplete="off"></el-input>
               </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
               <el-button @click="cancelEdit">取 消</el-button>
               <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
         </el-dialog>
      </el-main>
   </div>
</template>

<script>
export default {
   data(){
      return{
         activeIndex:'1',
         dialogVisible:false,
         mapEdit:{
            name:'',
            desc:'',
         }
      }
   },
   methods:{
      handleSelect(key,keyPath){
         this.activeIndex = key;
      },
      cancelEdit(){
         dialogVisible = false;
      },
      saveEdit(){
         dialogVisible = false;
      }
   }
}
</script>

<style>
.bg{
   min-width: 100vw;
   min-height: 100vh;
   background: #eee;
   overflow-x: scroll;
}
.content{
   padding: 10px;
   background: white;
   border-radius: 5px;
}
.menu-content{
   padding:10px;
}
.menu-content-maps{
   display: flex;
   flex-direction: row;
   flex-wrap: wrap;
}
.map{
   position: relative;
   margin:10px 10px 0 0 ;
   width: 240px;
   height: 120px;
   margin-top: 10px;
   background: #eeeeee;
   padding: 15px;
   border-radius: 5px;
   box-sizing: border-box;
}
.map .name{
   font-weight:bold;
}
.map .desc{
   margin-top:10px;
   font-size:14px;
}
.map i{
   display: none;
   opacity:0;  
   position: absolute;
   right: 15px;
   bottom: 15px;
   color: #666;   
   font-size: 16px;
   cursor: pointer;
}
.map:hover i {
   display: block;
   opacity: 1;
   transition: 0.2s;
}
</style>
