<template>
  <div class="bg">
    <div class="main-story-wrapper">
      <div class="main-story" v-for="i in [1,2,3,4]">
        <div class="story-row">
          <div class="story story-blue">
            <p>管理个人账户</p>
            <i class="el-icon-edit" @click="dialogVisible = true"></i>
          </div>
        </div>
        <div class="story-row">
          <div class="story story-yellow">
            <p>注册账户</p>
            <i class="el-icon-edit" @click="dialogVisible = true"></i>
          </div>
          <div class="story story-yellow">
            <p>账户登录</p>
            <i class="el-icon-edit" @click="dialogVisible = true"></i>
          </div>
          <div class="story story-yellow">
            <p>修改账户信息</p>
            <i class="el-icon-edit" @click="dialogVisible = true"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="releases-wrapper">
      <div class="release">
        UnScheduled
      </div>
      <div class="story-cols-wrapper">
        <div class="story-cols" v-for="i in [1,2,3,4]">
          <div class="story-col">
            <div class="story story-white">
              <p>设置用户名和密码</p>
            </div>
          </div>
          <div class="story-col">
            <div class="story story-white">
              <p>输入用户名和密码登录</p>
            </div>
          </div>
          <div class="story-col">
            <div class="story story-white">
              <p>修改密码</p>
            </div>
            <div class="story story-white">
              <p>修改密码2</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
            title="修改故事"
            :visible.sync="dialogVisible"
            width="50%">
      <el-form :model="storyEdit">
        <el-form-item label="故事名称">
          <el-input v-model="storyEdit.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="故事描述">
          <el-input v-model="storyEdit.desc" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="cancelEdit">取 消</el-button>
            <el-button type="primary" @click="saveEdit">确 定</el-button>
         </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        storyEdit: {
          name: '',
          desc: ''
        }
      }
    },
    methods: {
      saveEdit() {
        dialogVisible = false;
      },
      cancelEdit() {
        dialogVisible = false;
      }
    }
  }
</script>

<style>
.bg {
  min-width: 100vw;
  min-height: 100vh;
  background: #eee;
  overflow-x: scroll;
}

.main-story-wrapper, .story-cols-wrapper {
  display: flex;
  flex-direction: row;
}

.main-story, .story-cols, .release {
  padding: 10px 0 0 10px;
}

.story-cols {
  display: flex;
  flex-direction: row;
}

.story-row {
  display: flex;
  flex-direction: row;
}

.story-col {
  display: flex;
  flex-direction: column;
}

.story {
  position: relative;
  margin-right: 8px;
  margin-bottom: 10px;
  padding: 0 10px;
  width: 120px;
  height: 90px;
  font-size: 12px;
  cursor: pointer;
}

.story i {
  display: none;
  opacity: 0;
  position: absolute;
  right: 5px;
  bottom: 5px;
  color: #666;
  font-size: 14px;
}

.story:hover i.el-icon-edit {
  display: block;
  opacity: 1;
  transition: 0.2s;
}

.story-blue {
  background: rgb(182, 216, 231);
  border: rgb(155, 202, 224) 1px solid;
}

.story-yellow {
  background: rgb(242, 227, 112);
  border: rgb(228, 206, 69) 1px solid;
}

.story-white {
  background: rgb(255, 255, 255);
  border: rgb(206, 206, 206) 1px solid;
}
</style>
