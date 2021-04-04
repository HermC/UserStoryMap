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
          <div class="story-add-bottom" v-if="!i.streams || i.streams.length === 0">
            <i class="el-icon-circle-plus-outline" @click="handleNew(i.id, 'STREAM')"></i>
          </div>
          <div class="story-add-right" v-if="i.streams && i.streams.length !== 0">
            <i class="el-icon-circle-plus-outline" @click="handleNew(i.streams[i.streams.length - 1].id, 'STREAM')"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="releases-wrapper" v-for="release of releases">
      <div class="release">
        <label @click="handleReleaseEdit(release)" style="cursor: pointer">{{ release.release_name }}</label>
        <span style="margin-left: 20px">Deadline: {{ release.deadline }}</span>
        <i class="el-icon-delete" style="margin-left: 10px; color: darkred"  @click="handleReleaseDelete(release)"></i>
      </div>
      <div class="story-cols-wrapper">
        <div class="story-cols" v-for="i in storyMap">
          <div class="story-col" v-if="!i.streams || i.streams.length === 0">
            <!--<div class="story-add-bottom">-->

            <!--</div>-->
          </div>
          <div class="story-col" v-for="j in i.streams">
            <div class="story-add-bottom" v-if="!j.storys || j.storys.length === 0">
              <i class="el-icon-circle-plus-outline" @click="handleNew(j.id, 'STORY')"></i>
            </div>
            <div class="story" style="height: 0"></div>
            <div v-for="k in j.storys">
              <div class="story story-white" v-if="k.release_id === release.id">
                <div v-if="k.release_id === release.id">
                  <p>{{ k.story_name }}</p>
                  <el-tag size="mini" v-bind:type="getTypeColor(k.story_status)">{{ k.story_status }}</el-tag>
                  <i class="el-icon-delete" @click="deleteStory(k)"></i>
                  <i class="el-icon-edit" @click="handleEdit(k)"></i>
                </div>
              </div>
            </div>
            <!--<div class="story-add-bottom" v-if="j.storys && j.storys.length !== 0">-->
              <!--<i class="el-icon-circle-plus-outline" @click="handleNew(j.storys[j.storys.length - 1].id, 'STORY')"></i>-->
            <!--</div>-->
          </div>
          <div class="story-add-right">

          </div>
        </div>
      </div>
    </div>
    <div class="releases-wrapper">
      <div class="release">
        UnScheduled
        <i class="el-icon-plus" @click="handleReleaseNew" style="cursor: pointer"></i>
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
            <div class="story" style="height: 0"></div>
            <div v-for="k in j.storys">
              <!--<div class="story" v-if="k.release_id !== -1">-->

              <!--</div>-->
              <div class="story story-white" v-if="k.release_id === -1">
                <p>{{ k.story_name }}</p>
                <el-tag size="mini" v-bind:type="getTypeColor(k.story_status)">{{ k.story_status }}</el-tag>
                <i class="el-icon-delete" @click="deleteStory(k)"></i>
                <i class="el-icon-edit" @click="handleEdit(k)"></i>
              </div>
            </div>
            <div class="story-add-bottom" v-if="j.storys && j.storys.length !== 0">
              <i class="el-icon-circle-plus-outline" @click="handleNew(j.storys[j.storys.length - 1].id, 'STORY')"></i>
            </div>
          </div>
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
          <el-select v-model="storyEdit.story_status" placeholder="请选择" style="width: 100%">
            <el-option :key="'TODO'" :label="'TODO'" :value="'TODO'"></el-option>
            <el-option :key="'DOING'" :label="'DOING'" :value="'DOING'"></el-option>
            <el-option :key="'DONE'" :label="'DONE'" :value="'DONE'"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属Release" prop="release_id">
          <el-select v-model="storyEdit.release_id" style="width: 100%">
            <el-option :key="-1" :label="'UnScheduled'" :value="-1"></el-option>
            <el-option v-for="release of releases"
                       :key="release.id" :label="release.release_name" :value="release.id"></el-option>
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
            width="20%">
      你将删除该用户故事！
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancelDelete">取消</el-button>
          <el-button @click="confirmDelete" type="primary">确认</el-button>
        </span>
    </el-dialog>
    <el-dialog
            title="新增Release"
            :visible.sync="addReleaseDialogVisible"
            width="50%">
      <el-form :model="releaseNew" :rules="releaseRules" ref="releaseNew">
        <el-form-item label="Release名称" prop="release_name">
          <el-input v-model="releaseNew.release_name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="Deadline" prop="deadline">
          <el-date-picker v-model="releaseNew.deadline"
                          type="datetime"
                          placeholder="选择日期和时间"
                          default-time="12:00:00" style="width: 100%">

          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelReleaseNew">取 消</el-button>
        <el-button type="primary" @click="confirmReleaseNew">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
            title="修改Release"
            :visible.sync="modifyReleaseDialogVisible"
            width="50%">
      <el-form :model="releaseEdit" :rules="releaseRules" ref="releaseEdit">
        <el-form-item label="Release名称" prop="release_name">
          <el-input v-model="releaseEdit.release_name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="Deadline" prop="deadline">
          <el-date-picker v-model="releaseEdit.deadline"
                          type="datetime"
                          placeholder="选择日期和时间"
                          default-time="12:00:00" style="width: 100%">

          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelReleaseEdit">取 消</el-button>
        <el-button type="primary" @click="confirmReleaseEdit">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
            title="确认删除"
            :visible.sync="confirmReleaseDeleteVisible"
            width="20%">
      你将删除该Release故事！
      <br>
      (请确保该Release下没有任何用户故事)
      <span slot="footer" class="dialog-footer">
          <el-button @click="cancelReleaseDelete">取消</el-button>
          <el-button @click="confirmReleaseDelete" type="primary">确认</el-button>
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

      releases: [],

      addDialogVisible: false,
      dialogVisible: false,
      confirmDeleteDialog: false,

      addReleaseDialogVisible: false,
      modifyReleaseDialogVisible: false,
      confirmReleaseDeleteVisible: false,

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

      releaseNew: {
        map_id: '',
        release_name: '',
        deadline: ''
      },
      releaseEdit: {
        map_id: '',
        release_name: '',
        deadline: ''
      },
      releaseDelete: null,

      storyRules: {
        story_name: [
          { required: true, message: '用户故事名称不能为空!', trigger: 'blur' }
        ]
      },

      releaseRules: {
        release_name: [
          { required: true, message: 'Release名称不能为空!', trigger: 'blur' }
        ],
        deadline: [
          { required: true, message: 'Deadline不能为空!', trigger: 'blur' }
        ]
      }
    }
  },
  created: function() {
    this.mapId = this.$route.params.map_id;
    if (!this.mapId) {
      this.$router.push('user');
    } else {
      this.getStories(this.mapId);
    }
  },
  methods: {
    getStories(id) {
      get(`story/list?map_id=${id}`)
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.initStoryMap(res.data.storyList, res.data.releaseList);
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('网络错误!');
        });
    },
    initStoryMap(storys, releases) {
      if (storys === null) {
        storys = [];
      }
      if (releases === null) {
        releases = [];
      }
      this.storyMap = [];
      this.releases = releases;
      // console.log(releases);
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
                        this.getStories(this.mapId);
                        this.addDialogVisible = false;
                      }
                    })
                    .catch(err => {
                      console.error(err);
                      this.$message.error('网络错误!');
                    });
                } else {
                  this.getStories(this.mapId);
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

              this.getStories(this.mapId);
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
            this.getStories(this.mapId);
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
    },
    handleReleaseNew() {
      this.releaseNew.map_id = this.mapId;
      this.releaseNew.release_name = '';
      this.releaseNew.deadline = '';
      this.addReleaseDialogVisible = true;
    },
    confirmReleaseNew() {
      this.$refs['releaseNew'].validate((valid) => {
        if (valid) {
          // console.log(this.releaseNew);
          post('release/create', this.releaseNew)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
                return;
              } else {
                this.$message.success('添加成功!');
                this.addReleaseDialogVisible = false;
                this.getStories(this.mapId);
              }
            });
        }
      });
      console.log(this.releaseNew);
    },
    handleReleaseEdit(release) {
      this.releaseEdit.map_id = this.mapId;
      this.releaseEdit.release_name = release.release_name;
      this.releaseEdit.deadline = release.deadline;
      this.modifyReleaseDialogVisible = true;
    },
    cancelReleaseNew() {
      this.addReleaseDialogVisible = true;
    },
    confirmReleaseEdit() {
      this.$refs['releaseEdit'].validate((valid) => {
        if (valid) {
          post('release/modify', this.releaseEdit)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.message);
                return;
              } else {
                this.$message.success('修改成功!');
                this.modifyReleaseDialogVisible = false;
                this.getStories(this.mapId);
              }
            });
        }
      });
      console.log(this.releaseEdit);
    },
    cancelReleaseEdit() {
      this.modifyReleaseDialogVisible = false;
    },
    handleReleaseDelete(release) {
      this.confirmReleaseDeleteVisible = true;
      this.releaseDelete = release;
      console.log(release);
    },
    confirmReleaseDelete() {
      get('release/delete', { release_id: this.releaseDelete.id })
        .then(res => {
          if (!res.success) {
            this.$message.error(res.message);
            return;
          } else {
            this.$message.success('删除成功!');
            this.confirmReleaseDeleteVisible = false;
            this.getStories(this.mapId);
          }
        })
    },
    cancelReleaseDelete() {
      this.confirmReleaseDeleteVisible = false;
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
  position: relative;
  border-bottom: 1px dashed lightgrey;
}

.story-cols-wrapper {
  margin-top: 10px;
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
.release .el-icon-delete {
  display: none;
}
.release:hover .el-icon-delete {
  display: inline-block;
  cursor: pointer;
}
</style>
