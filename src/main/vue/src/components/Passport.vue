<template>
  <div class="container">
    <el-card shadow="hover" class="login-card">
      <h4>USM账户</h4>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-alert :title="loginMessage.text" type="error" show-icon v-if="loginMessage.show" @close="closeLoginMessage()"></el-alert>
          <el-form status-icon :model="loginForm" :rules="loginRules" ref="loginForm">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="loginForm.password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button class="login" type="primary" @click="login()">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-alert :title="registerMessage.text" type="error" show-icon v-if="registerMessage.show" @close="closeRegisterMessage()"></el-alert>
          <el-form status-icon :model="registerForm" :rules="registerRules" ref="registerForm">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerForm.username"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerForm.email"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="registerForm.password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirm">
              <el-input type="password" v-model="registerForm.confirm"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button class="login" type="primary" @click="register()">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
  import {post} from '../tools/http_client';
  const cookies = require('js-cookie');

  export default {
    data() {
      const validateEmail = (rule, value, callback) => {
        const reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        if (!reg.test(value)) {
          callback('邮箱格式不正确!');
        } else {
          callback();
        }
      };
      const validateConfirm = (rule, value, callback) => {
        if (this.registerForm.password !== value) {
          callback('两次输入的密码不同!');
        } else {
          callback();
        }
      };
      return {
        activeTab: 'login',

        loginForm: {
          username: '',
          password: '',
        },
        registerForm: {
          username: '',
          email: '',
          password: '',
          confirm: ''
        },

        loginRules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 30, message: '长度在3到30个字符', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码!', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在6到30个字符', trigger: 'blur' }
          ]
        },

        registerRules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在3到30个字符', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { validator: validateEmail, trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码!', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在3到30个字符', trigger: 'blur' }
          ],
          confirm: [
            { required: true, message: '请再次输入密码!', trigger: 'blur' },
            { validator: validateConfirm, trigger: 'blur' }
          ]
        },

        loginMessage: {
          text: '',
          show: false
        },
        registerMessage: {
          text: '',
          show: false
        }
      }
    },
    methods: {
      closeLoginMessage: function() {
        this.loginMessage.show = false;
      },
      closeRegisterMessage: function() {
        this.registerMessage.show = false;
      },
      login: function() {
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
            let self = this;
            post('auth/token', this.loginForm)
              .then(res => {
                if (!res.success) {
                  self.loginMessage.text = res.message;
                  self.loginMessage.show = true;
                } else {
                  cookies.set('token', res.data.token);
                  self.$router.push('/');
                }
              });
          }
        })
      },
      register: function() {
        this.$refs['registerForm'].validate((valid) => {
          if (valid) {
            let self = this;
            post('user/register', this.registerForm)
              .then(res => {
                if (!res.success) {
                  self.registerMessage.text = res.message;
                  self.registerMessage.show = true;
                } else {
                  self.$message({
                    message: res.message,
                    type: 'success'
                  });
                  self.$router.push('passport');
                  self.$refs['registerForm'].resetFields();
                }
              })
          }
        })
      }
    }
  }
</script>
<style scoped lang="less">
  .container {
    margin-top: 60px;

    display: flex;
    justify-content: center;
    align-items: center;
  }
  .login-card {
    width: 25em;

    h4 {
      margin-top: 0.6em;
      margin-bottom: 0.6em;
    }
  }
  .login {
    float: right;
  }
</style>