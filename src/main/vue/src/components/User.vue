<template>
  <div class="bg">
    <el-main>
      <div class="content">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">我的故事地图</el-menu-item>
          <el-menu-item index="2">消息中心</el-menu-item>
          <el-menu-item index="3">账号管理</el-menu-item>
        </el-menu>

        <div class="menu-content menu-content-maps" v-if="activeIndex == '1'">
          <div class="map" v-for="i in [1,2,3,4,5,6,7]">
            <div class="name">我的故事地图</div>
            <div class="desc">我的故事地图的描述</div>
            <i class="el-icon-edit" @click="dialogVisible = true"></i>
          </div>
          <div class="map-create" @click="addDialogVisible = true">
            <span>+</span>
          </div>
        </div>
        <div class="menu-content" v-else-if="activeIndex == '2'">

        </div>
        <div class="menu-content" v-else-if="activeIndex == '3'">
          <el-row>
            <el-col :sm="24" :md="12">
              <h4>修改密码</h4>
              <el-form :model="passwordModify" :rules="modifyRules" ref="passwordModify">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input v-model="passwordModify.oldPassword" autocomplete="off" type="password"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordModify.newPassword" autocomplete="off" type="password"></el-input>
                </el-form-item>
                <el-form-item label="密码确认" prop="confirm">
                  <el-input v-model="passwordModify.confirm" autocomplete="off" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="modifyPasswd">修改</el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
        </div>
      </div>
      <el-dialog
              title="修改故事地图"
              :visible.sync="dialogVisible"
              width="50%">
        <el-form :model="mapEdit" :rules="mapRules" ref="mapEdit">
          <el-form-item label="地图名称" prop="name">
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
      <el-dialog
              title="新增故事地图"
              :visible.sync="addDialogVisible"
              width="50%">
        <el-form :model="mapNew" :rules="mapRules" ref="mapNew">
          <el-form-item label="地图名称" prop="name">
            <el-input v-model="mapNew.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地图描述">
            <el-input v-model="mapNew.description" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelNew">取 消</el-button>
          <el-button type="primary" @click="saveNew">确 定</el-button>
        </span>
      </el-dialog>
    </el-main>
  </div>
</template>

<script>
import {post} from '../tools/http_client'

export default {
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (this.passwordModify.password !== value) {
        callback('两次输入的密码不同!');
      } else {
        callback();
      }
    };
    return {
      activeIndex: '1',
      dialogVisible: false,
      addDialogVisible: false,
      mapEdit: {
        id: '',
        name: '',
        description: '',
      },
      mapNew: {
        name: '',
        description: ''
      },
      passwordModify: {
        oldPassword: '',
        newPassword: '',
        confirm: ''
      },

      mapRules: {
        name: [
          { required: true, message: '请填写用户地图名称', trigger: 'blur' }
        ]
      },
      modifyRules: {
        oldPassword: [
          { required: true, message: '请填写原密码', trigger: 'blur' },
          { min: 6, max: 30, message: '长度在3到30个字符', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请填写原密码', trigger: 'blur' },
          { min: 6, max: 30, message: '长度在3到30个字符', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请再次输入密码!', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      this.activeIndex = key;
    },
    cancelEdit() {
      this.dialogVisible = false;
    },
    saveEdit() {
      this.$refs['mapEdit'].validate((valid) => {
        if (valid) {
          post('map/modify', this.mapEdit)
            .then(res => {
              console.log(res);
              if (!res.success) {
                this.$message.error(res.message);
              } else {
                this.dialogVisible = false;
              }
            })
            .catch(err => {
              this.$message.error('网络错误!');
            });
        }
      })
    },
    cancelNew() {
      this.addDialogVisible = false;
    },
    saveNew() {
      this.$refs['mapNew'].validate((valid) => {
        if (valid) {
          post('map/create', this.mapNew)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
              } else {
                this.$message({
                  message: '创建成功!',
                  type: 'success'
                });
                this.addDialogVisible = false;
              }
            })
            .catch(err => {
              this.$message.error('网络错误!');
            });
        }
      });
    },
    modifyPasswd() {
      this.$refs['passwordModify'].validate((valid) => {
        if (valid) {
          post('', this.passwordModify)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
              } else {
                this.$message({
                  message: '修改成功',
                  type: 'success'
                });
              }
            })
            .catch(err => {
              this.$message.error('网络错误!');
            });
        }
      });
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

.content {
  padding: 10px;
  background: white;
  border-radius: 5px;
}

.menu-content {
  padding: 10px;
}

.menu-content-maps {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.map {
  position: relative;
  width: 240px;
  height: 120px;
  margin: 10px 10px 0 0;
  background: #eeeeee;
  padding: 15px;
  border-radius: 5px;
  box-sizing: border-box;
}

.map .name {
  font-weight: bold;
  cursor: pointer;
}

.map .desc {
  margin-top: 10px;
  font-size: 14px;
}

.map i {
  display: none;
  opacity: 0;
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

.map-create {
  position: relative;
  width: 240px;
  height: 120px;
  margin: 10px 10px 0 0;
  box-sizing: border-box;
  border: 2px dashed #dddddd;

  display: flex;
  justify-content: center;
  align-items: center;

  color: #dddddd;
}

.map-create span {
  font-size: 100px;
  font-weight: lighter;
}

.map-create:hover {
  color: #aaaaaa;
  border: 2px dashed #aaaaaa;
  cursor: pointer;
}
</style>
