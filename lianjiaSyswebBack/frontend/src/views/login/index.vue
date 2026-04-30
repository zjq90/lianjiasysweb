<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <h2>恋家环保商户端</h2>
        <p>管理后台系统</p>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            size="large"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-tip">
        <p>默认账号: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/auth'
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    ...mapActions(['login']),
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          login(this.loginForm)
            .then(res => {
              this.login(res.data.token, res.data)
              this.$message.success('登录成功')
              const redirect = this.$route.query.redirect || '/'
              this.$router.push(redirect)
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
.login-container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    font-size: 24px;
    color: #333;
    margin-bottom: 10px;
  }
  
  p {
    color: #999;
    font-size: 14px;
  }
}

.login-form {
  .login-btn {
    width: 100%;
  }
}

.login-tip {
  text-align: center;
  margin-top: 20px;
  
  p {
    color: #999;
    font-size: 12px;
  }
}
</style>
