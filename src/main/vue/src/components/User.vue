<template>
  <div class="bg">
    <el-main>
      <div class="content">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">我的故事地图</el-menu-item>
          <el-menu-item index="2">消息中心</el-menu-item>
          <el-menu-item index="3">账号管理</el-menu-item>
        </el-menu>

        <div class="menu-content menu-content-maps" v-if="activeIndex === '1'">
          <div class="map" v-for="(map, index) of maps">
            <div class="name" @click="showMap(index)">{{ map.map_name }}</div>
            <div class="desc">{{ map.description }}</div>
            <i class="el-icon-edit" @click="showEditDialog(index)"></i>
            <i class="el-icon-delete" @click="handleDelete(index)"></i>
          </div>
          <div class="map-create" @click="addDialogVisible = true">
            <span>+</span>
          </div>
        </div>
        <div class="menu-content" v-else-if="activeIndex === '2'">
          <h3>协作邀请</h3>
          <el-table :data="invitations" style="width: 100%">
            <el-table-column
                    prop="map.map_name"
                    label="地图名称">

            </el-table-column>
            <el-table-column
                    prop="map.description"
                    label="地图描述">

            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="100">
              <template slot-scope="scope">
                <el-button @click="acceptInvitation(scope.row)" type="text" size="small">接受</el-button>
                <el-button @click="rejectInvitation(scope.row)" type="text" size="small">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="menu-content" v-else-if="activeIndex === '3'">
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
              v-if="selectedMap !== null"
              title="修改故事地图"
              :visible.sync="dialogVisible"
              width="50%">
        <el-form :model="mapEdit" :rules="mapRules" ref="mapEdit">
          <el-form-item label="地图名称" prop="name">
            <el-input v-model="mapEdit.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地图描述">
            <el-input v-model="mapEdit.description" autocomplete="off"></el-input>
          </el-form-item>
          <div v-if="mapEdit.collaborators">
            <h4>合作者</h4>
            <el-row>
              <el-autocomplete placeholder="搜索用户"
                               v-model="selectedUserString"
                               :fetch-suggestions="querySearchAsync"
                               @select="handleSelectedUser"
                               style="width: 200px"></el-autocomplete>
              &nbsp;
              <el-button type="primary" @click="inviteCollaborator">发送邀请</el-button>
            </el-row>
            <el-table
                    :data="mapEdit.collaborators">
              <el-table-column
                      prop="username"
                      label="用户名">

              </el-table-column>
              <el-table-column
                      prop="email"
                      label="邮箱">

              </el-table-column>
              <el-table-column
                      fixed="right"
                      label="操作"
                      width="100">
                <template slot-scope="scope">
                  <el-button @click="deleteCollaborator(scope.row)" type="text" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
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
      <el-dialog
              title="确认删除"
              :visible.sync="confirmDeleteDialog"
              width="20%" center>
        你将删除该用户故事地图！
        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelDelete">取消</el-button>
          <el-button @click="confirmDelete" type="primary">确认</el-button>
        </span>
      </el-dialog>
    </el-main>
  </div>
</template>

<script>
import {get, post} from '../tools/http_client'

export default {
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (this.passwordModify.newPassword !== value) {
        callback('两次输入的密码不同!');
      } else {
        callback();
      }
    };
    return {
      activeIndex: '1',
      dialogVisible: false,
      addDialogVisible: false,
      confirmDeleteDialog: false,

      new_only: 1,

      selectedMap: null,
      selectedDeleteMap: null,

      maps: [],
      invitations: [],

      selectedUserString: '',
      selectedUser: null,

      mapEdit: {
        id: '',
        name: '',
        description: '',
        collaborators: [],
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
  created: function() {
    this.getMaps();
    this.getInvitation(this.new_only);
  },
  methods: {
    getMaps() {
      get('map/list')
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.maps = res.data.map_list;
            console.log(this.maps);
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('网络错误!');
        });
    },
    getInvitation(new_only) {
      get('map/received_invitations', { new_only: new_only })
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.invitations = res.data.invitations;
            console.log(this.invitations);
          }
        });
    },
    showMap(i) {
      let map = this.maps[i];
      this.$router.push({name: 'StoryMap', params: {map_id: map.id}});
    },
    showEditDialog(i) {
      this.dialogVisible = true;
      this.selectedMap = this.maps[i];
      this.mapEdit.id = this.selectedMap.id;
      this.mapEdit.name = this.selectedMap.map_name;
      this.mapEdit.description = this.selectedMap.description;
      this.mapEdit.collaborators = this.selectedMap.collaborators;
    },
    handleSelect(key, keyPath) {
      this.activeIndex = key;
    },
    cancelEdit() {
      this.dialogVisible = false;
      this.selectedMap = null;
    },
    saveEdit() {
      this.$refs['mapEdit'].validate((valid) => {
        if (valid) {
          post(`map/modify?map_id=${this.mapEdit.id}`, this.mapEdit)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
                return;
              } else {
                this.dialogVisible = false;
                this.selectedMap = null;
                this.$message.success('修改成功!');
                this.getMaps();
              }
            })
            .catch(err => {
              console.error(err);
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
                return;
              } else {
                this.$message.success('创建成功!');
                this.addDialogVisible = false;
                this.mapNew.name = '';
                this.mapNew.description = '';
                this.getMaps();
              }
            })
            .catch(err => {
              console.error(err);
              this.$message.error('网络错误!');
            });
        }
      });
    },
    handleDelete(i) {
      this.confirmDeleteDialog = true;
      this.selectedDeleteMap = this.maps[i];
    },
    cancelDelete() {
      this.confirmDeleteDialog = false;
      this.selectedDeleteMap = null;
    },
    confirmDelete() {
      get(`map/delete?map_id=${this.selectedDeleteMap.id}`)
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('删除成功!');
            this.confirmDeleteDialog = false;
            this.selectedDeleteMap = null;
            this.getMaps();
          }
        })
        .catch(err => {
          console.log(err);
          this.$message.error('网络错误!');
        })
      this.confirmDeleteDialog = false;
    },
    modifyPasswd() {
      this.$refs['passwordModify'].validate((valid) => {
        if (valid) {
          post('auth/password/modify', this.passwordModify)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
                return;
              } else {
                this.$message.success('修改成功!');
              }
            })
            .catch(err => {
              console.error(err);
              this.$message.error('网络错误!');
            });
        }
      });
    },
    acceptInvitation(row) {
      console.log(row);
      get('map/response_invitation', { inv_id: row.id, response: 1 })
        .then(res => {
          console.log(res);
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('接受成功!');
            this.getInvitation(this.new_only);
            this.getMaps();
          }
        });
    },
    rejectInvitation(row) {
      console.log(row);
      get('map/response_invitation', { inv_id: row.id, response: 2 })
        .then(res => {
          console.log(res);
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('拒绝成功!');
            this.getInvitation(this.new_only);
          }
        });
    },
    deleteCollaborator(row) {
      console.log(row);
      get('map/remove_collaborator', { map_id: this.mapEdit.id, user_id: row.id })
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('删除成功!');
            // this.mapEdit.collaborators.remove(row);
            let index = -1;
            for (let i = 0; i < this.mapEdit.collaborators.length; i++) {
              if (this.mapEdit.collaborators[i].id === row.id) {
                index = i;
                break;
              }
            }
            if (index !== -1) {
              this.mapEdit.collaborators.splice(index, 1);
            }
          }
        });
    },
    querySearchAsync(queryString, cb) {
      // var restaurants = this.restaurants;
      // var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;

      // clearTimeout(this.timeout);
      // this.timeout = setTimeout(() => {
      //   cb(results);
      // }, 3000 * Math.random());
      if (!queryString) {
        return;
      }
      get('user/search', { username: queryString })
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            let users = res.data.users.map(u => {
              return { value: u.username, id: u.id };
            });
            cb(users);
          }
        });
    },
    inviteCollaborator() {
      console.log(this.selectedUser);
      if (!this.selectedUser) {
        return;
      }
      get('map/invite', { map_id: this.mapEdit.id, co_user_id:  this.selectedUser.id })
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('邀请成功!');
            this.selectedUserString = '';
            this.selectedUser = null;
          }
        });
    },
    handleSelectedUser(item) {
      this.selectedUser = item;
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

.map i.el-icon-edit {
  right: 45px;
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
