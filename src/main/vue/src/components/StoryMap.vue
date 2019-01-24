<template>
  <div class="bg">
    <!--<el-button v-if="!storyMap || storyMap.length === 0">新建卡片</el-button>-->
    <div class="main-story-wrapper">
      <div class="main-story" v-for="i in storyMap">
        <div class="story-row">
          <div class="story story-blue">
            <p>{{ i.story_name }}</p>
            <el-tag size="mini" v-bind:type="getTypeColor(i.story_status)">{{ i.story_status }}</el-tag>
            <i class="el-icon-delete" @click="deleteStory(i)"></i>
            <i class="el-icon-edit" @click="handleEdit(i)"></i>
          </div>
          <div class="story-add-right">
            <i class="el-icon-circle-plus-outline" @click="handleNew(i.id, 'GOAL')"></i>
          </div>
        </div>
        <div class="story-row">
          <div class="story story-yellow" v-for="j in i.streams">
            <p>{{ j.story_name }}</p>
            <el-tag size="mini" v-bind:type="getTypeColor(j.story_status)">{{ j.story_status }}</el-tag>
            <i class="el-icon-delete" @click="deleteStory(j)"></i>
            <i class="el-icon-edit" @click="handleEdit(j)"></i>
          </div>
          <!--<div class="story story-yellow">-->
            <!--<p>账户登录</p>-->
            <!--<i class="el-icon-edit" @click="dialogVisible = true"></i>-->
          <!--</div>-->
          <!--<div class="story story-yellow">-->
            <!--<p>修改账户信息</p>-->
            <!--<i class="el-icon-edit" @click="dialogVisible = true"></i>-->
          <!--</div>-->
          <div class="story-add-bottom" v-if="!i.streams || i.streams.length === 0">
            <i class="el-icon-circle-plus-outline" @click="handleNew(i.id, 'STREAM')"></i>
          </div>
          <div class="story-add-right" v-if="i.streams && i.streams.length !== 0">
            <i class="el-icon-circle-plus-outline" @click="handleNew(i.streams[i.streams.length - 1].id, 'STREAM')"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="releases-wrapper">
      <div class="release">
        UnScheduled
      </div>
      <div class="story-cols-wrapper">
        <div class="story-cols" v-for="i in storyMap">
          <div class="story-col" v-if="!i.streams || i.streams.length === 0">
            <div class="story-add-bottom">

            </div>
          </div>
          <div class="story-col" v-for="j in i.streams">
            <div class="story-add-bottom" v-if="!j.storys || j.storys.length === 0">
              <i class="el-icon-circle-plus-outline" @click="handleNew(j.id, 'STORY')"></i>
            </div>
            <div class="story story-white" v-for="k in j.storys">
              <p>{{ k.story_name }}</p>
              <el-tag size="mini" v-bind:type="getTypeColor(k.story_status)">{{ k.story_status }}</el-tag>
              <i class="el-icon-delete" @click="deleteStory(k)"></i>
              <i class="el-icon-edit" @click="handleEdit(k)"></i>
            </div>
            <div class="story-add-bottom" v-if="j.storys && j.storys.length !== 0">
              <i class="el-icon-circle-plus-outline" @click="handleNew(j.storys[j.storys.length - 1].id, 'STORY')"></i>
            </div>
          </div>
          <!--<div class="story-col">-->
            <!--<div class="story story-white">-->
              <!--<p>输入用户名和密码登录</p>-->
              <!--<i class="el-icon-edit" @click="dialogVisible = true"></i>-->
            <!--</div>-->
            <!--<div class="story-add-bottom">-->
              <!--<i class="el-icon-circle-plus-outline"></i>-->
            <!--</div>-->
          <!--</div>-->
          <!--<div class="story-col">-->
            <!--<div class="story story-white">-->
              <!--<p>修改密码</p>-->
              <!--<i class="el-icon-edit" @click="dialogVisible = true"></i>-->
            <!--</div>-->
            <!--<div class="story story-white">-->
              <!--<p>修改密码2</p>-->
              <!--<i class="el-icon-edit" @click="dialogVisible = true"></i>-->
            <!--</div>-->
            <!--<div class="story-add-bottom">-->
              <!--<i class="el-icon-circle-plus-outline"></i>-->
            <!--</div>-->
          <!--</div>-->
          <div class="story-add-right">

          </div>
        </div>
      </div>
    </div>
    <el-dialog
            title="新增故事"
            :visible.sync="addDialogVisible"
            width="50%" :before-close="beforeCloseNew">
      <el-form :model="storyNew" :rules="storyRules" ref="storyNew">
        <el-form-item label="故事名称" prop="story_name">
          <el-input v-model="storyNew.story_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="故事描述" prop="description">
          <el-input type="textarea" v-model="storyNew.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelNew">取 消</el-button>
        <el-button type="primary" @click="confirmNew">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
            title="故事信息"
            :visible.sync="dialogVisible"
            width="50%">
      <el-form :model="storyEdit" :rules="storyRules" ref="storyEdit">
        <el-form-item label="故事名称" prop="story_name">
          <el-input v-model="storyEdit.story_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="故事描述" prop="description">
          <el-input type="textarea" v-model="storyEdit.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="故事状态" prop="story_status">
          <el-select v-model="storyEdit.story_status" placeholder="请选择">
            <el-option :key="'TODO'" :label="'TODO'" :value="'TODO'"></el-option>
            <el-option :key="'DOING'" :label="'DOING'" :value="'DOING'"></el-option>
            <el-option :key="'DONE'" :label="'DONE'" :value="'DONE'"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取 消</el-button>
        <el-button type="primary" @click="saveEdit">保 存</el-button>
      </span>
    </el-dialog>
    <el-dialog
            title="确认删除"
            :visible.sync="confirmDeleteDialog"
            width="20%" center>
      你将删除该用户故事！
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancelDelete">取消</el-button>
          <el-button @click="confirmDelete" type="primary">确认</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {get, post} from '../tools/http_client';

export default {
  data() {
    return {
      mapId: null,
      storys: null,
      storyMap: [],

      addDialogVisible: false,
      dialogVisible: false,
      confirmDeleteDialog: false,

      storyNew: {
        map_id: '',
        parent_story_id: null,
        story_name: '',
        story_type: null,
        story_status: null,
        description: ''
      },
      storyEdit: {
        id: null,
        map_id: '',
        parent_story_id: null,
        release_id: null,
        story_name: '',
        story_type: null,
        story_status: null,
        description: ''
      },
      storyDelete: null,

      storyRules: {
        story_name: [
          { required: true, message: '用户故事名称不能为空!', trigger: 'blur' }
        ]
      }
    }
  },
  created: function() {
    this.mapId = this.$route.params.map_id;
    if (!this.mapId) {
      this.$router.push('user');
    } else {
      this.getStorys(this.mapId);
    }
  },
  methods: {
    getStorys(id) {
      get(`story/list?map_id=${id}`)
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.initStoryMap(res.data.storyList);
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('网络错误!');
        });
    },
    initStoryMap(storys) {
      if (storys === null) {
        storys = [];
      }
      this.storyMap = [];
      // 先初始化GOAL类型的Story
      let parentId = -1;
      let index = 0;
      while (index < storys.length) {
        const story = storys[index];
        if (story.story_type === 'GOAL' && story.parent_story_id === parentId) {
          parentId = story.id;
          this.storyMap.push(story);
          index = 0;
          continue;
        }
        index += 1;
      }
      // 再初始化STREAM类型的Story
      for (let i = 0; i < this.storyMap.length; i++) {
        const goal = this.storyMap[i];
        goal.streams = [];
        parentId = goal.id;
        index = 0;
        while (index < storys.length) {
          const story = storys[index];
          if (story.story_type === 'STREAM' && story.parent_story_id === parentId) {
            parentId = story.id;
            goal.streams.push(story);
            index = 0;
            continue;
          }
          index += 1;
        }
      }
      // 再初始化STORY类型的Story
      for (let i = 0; i < this.storyMap.length; i++) {
        const goal = this.storyMap[i];
        for (let j = 0; j < goal.streams.length; j++) {
          const stream = goal.streams[j];
          stream.storys = [];
          // TODO: 此处对于release相关还未进行操作
          parentId = stream.id;
          index = 0;
          while (index < storys.length) {
            const story = storys[index];
            if (story.story_type === 'STORY' && story.parent_story_id === parentId) {
              parentId = story.id;
              stream.storys.push(story);
              index = 0;
              continue;
            }
            index += 1;
          }
        }
      }
      if (this.storyMap.length === 0) {
        this.initStoryNew(-1, 'GOAL', 'TODO');
        this.addDialogVisible = true;
      }

      this.storys = storys;
    },
    initStoryNew(parentId, storyType, storyStatus) {
      this.storyNew.map_id = this.mapId;
      this.storyNew.story_name = '';
      this.storyNew.description = '';
      this.storyNew.parent_story_id = parentId;
      this.storyNew.story_type = storyType;
      this.storyNew.story_status = storyStatus;
    },
    initStoryEdit(story) {
      this.storyEdit.map_id = this.mapId;
      this.storyEdit.id = story.id;
      this.storyEdit.story_name = story.story_name;
      this.storyEdit.description = story.description;
      this.storyEdit.parent_story_id = story.parent_story_id;
      this.storyEdit.release_id = story.release_id;
      this.storyEdit.story_type = story.story_type;
      this.storyEdit.story_status = story.story_status;
    },
    handleNew(parentId, type) {
      this.addDialogVisible = true;
      this.initStoryNew(parentId, type, 'TODO');
    },
    confirmNew() {
      this.$refs['storyNew'].validate((valid) => {
        if (valid) {
          post(`story/create`, this.storyNew)
            .then(res => {
              console.log(res);
              if (!res.success) {
                this.$message.error(res.message);
                return;
              } else {
                const newStory = res.data['story'];
                // 这里要重新更新一下用户故事的顺序
                let preNextStory = null;
                for (let i = 0; i < this.storys.length; i++) {
                  const story = this.storys[i];
                  if (story.parent_story_id === newStory.parent_story_id
                    && story.story_type === newStory.story_type
                    && story.id !== newStory.id) {
                    preNextStory = story;
                    break;
                  }
                }
                if (preNextStory !== null) {
                  this.initStoryEdit(preNextStory);
                  // 将原来的后一个用户故事接到新的用户故事上面
                  this.storyEdit.parent_story_id = newStory.id;
                  post(`story/modify?story_id=${preNextStory.id}`, this.storyEdit)
                    .then(res => {
                      if (!res.success) {
                        this.$message.error(res.message);
                        return;
                      } else {
                        this.getStorys(this.mapId);
                        this.addDialogVisible = false;
                      }
                    })
                    .catch(err => {
                      console.error(err);
                      this.$message.error('网络错误!');
                    });
                } else {
                  this.getStorys(this.mapId);
                  this.addDialogVisible = false;
                }
              }
            })
            .catch(err => {
              console.error(err);
              this.$message.error('网络错误!');
            });
        }
      });
    },
    cancelNew() {
      if (this.storyMap && this.storyMap.length > 0) {
        this.addDialogVisible = false;
      } else {
        this.$message.warning('你需要添加至少一个用户故事!');
      }
    },
    beforeCloseNew(done) {
      if (this.storyMap && this.storyMap.length > 0) {
        done(true);
      } else {
        this.$message.warning('你需要添加至少一个用户故事!');
        done(false);
      }
    },
    handleEdit(story) {
      this.initStoryEdit(story);
      this.dialogVisible = true;
    },
    saveEdit() {
      this.$refs['storyEdit'].validate((valid) => {
        post(`story/modify?story_id=${this.storyEdit.id}`, this.storyEdit)
          .then(res => {
            if (!res.success) {
              this.$message.error(res.message);
              return;
            } else {
              // TODO: 同样在迭代二要考虑release相关操作

              this.getStorys(this.mapId);
              this.dialogVisible = false;
            }
          })
          .catch(err => {
            console.error(err);
            this.$message.error('网络错误!');
          });
      });
      // this.dialogVisible = false;
    },
    cancelEdit() {
      this.dialogVisible = false;
    },
    deleteStory(story) {
      this.confirmDeleteDialog = true;
      this.storyDelete = story;
    },
    cancelDelete() {
      this.confirmDeleteDialog = false;
    },
    confirmDelete() {
      get(`story/delete?story_id=${this.storyDelete.id}`)
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.confirmDeleteDialog = false;
            this.getStorys(this.mapId);
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('网络错误!');
        });
    },
    getTypeColor(storyType) {
      switch (storyType) {
        case "TODO":
          return "success";
        case "DOING":
          return "warning";
        case "DONE":
          return "info";
        default:
          return "info";
      }
    }
  }
  }
</script>

<style scoped>
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

.main-story {
  /*border-right: 1px dashed lightgrey;*/
}

.main-story, .story-cols, .release {
  padding: 10px 0 0 10px;
}

.releases-wrapper {
  position: relative;
  box-sizing: border-box;
  padding-top: 1px;
}

.releases-wrapper .release {
  left: 0;
  width: 100%;
  position: fixed;
  border-bottom: 1px dashed lightgrey;
}

.story-cols-wrapper {
  margin-top: 32px;
}

.story-cols {
  display: flex;
  flex-direction: row;
  /*border-right: 1px dashed lightgrey;*/
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
  /*cursor: pointer;*/
}

.story p {
  cursor: pointer;
}

.story i {
  display: none;
  opacity: 0;
  position: absolute;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.story:hover i.el-icon-edit {
  display: block;
  opacity: 1;
  transition: 0.2s;
  right: 5px;
  bottom: 5px;
}

.story:hover i.el-icon-delete {
  display: block;
  opacity: 1;
  transition: 0.2s;
  right: 25px;
  bottom: 5px;
}

.story .el-tag {
  position: absolute;
  bottom: 5px;
  left: 5px;
}

.el-tag--success {
  background-color: rgba(103,194,58,.5);
  border-color: rgba(103,194,58,.7);
  color: black;
}

.el-tag--warning {
  background-color: rgba(230,162,60,.5);
  border-color: rgba(230,162,60,.7);
  color: black;
}

.el-tag--info {
  background-color: rgba(144,147,153,.5);
  border-color: rgba(144,147,153,.7);
  color: black;
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

.story-add-right {
  display: flex;
  align-items: center;
  margin-right: 8px;
  margin-bottom: 10px;
  width: 1.5em;
}

.story-add-right i {
  font-size: 1.5em;
  flex: 1;
  opacity: 0;
  cursor: pointer;
}

.story-add-right i:hover {
  opacity: 1;
}

.story-add-bottom {
  text-align: center;
  width: 120px;
  height: 90px;
  margin-right: 8px;
  margin-bottom: 10px;
  padding: 0 10px;
}

.story-add-bottom i {
  font-size: 1.5em;
  cursor: pointer;
  opacity: 0;
}

.story-add-bottom i:hover {
  opacity: 1;
}

.block {
  display: block;
}
</style>
