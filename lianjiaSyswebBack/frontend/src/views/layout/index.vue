<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <h3>恋家环保</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
        :unique-opened="true"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-menu"></i>
          <span slot="title">工作台</span>
        </el-menu-item>
        
        <el-submenu index="customer">
          <template slot="title">
            <i class="el-icon-user"></i>
            <span>客户管理</span>
          </template>
          <el-menu-item index="/customers">
            <span>客户列表</span>
          </el-menu-item>
        </el-submenu>
        
        <el-submenu index="data">
          <template slot="title">
            <i class="el-icon-s-data"></i>
            <span>数据管理</span>
          </template>
          <el-menu-item index="/data/export">
            <span>数据导出</span>
          </el-menu-item>
        </el-submenu>
        
        <el-submenu index="service">
          <template slot="title">
            <i class="el-icon-s-claim"></i>
            <span>服务管理</span>
          </template>
          <el-menu-item index="/service/orders">
            <span>服务订单</span>
          </el-menu-item>
          <el-menu-item index="/service/staff">
            <span>服务人员</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <span class="title">{{ currentTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <i class="el-icon-user"></i>
              {{ userInfo.username || '管理员' }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'Layout',
  computed: {
    ...mapGetters(['userInfo']),
    activeMenu() {
      return this.$route.path
    },
    currentTitle() {
      return this.$route.meta.title || '工作台'
    }
  },
  methods: {
    ...mapActions(['logout']),
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.logout()
          this.$router.push('/login')
          this.$message.success('已退出登录')
        }).catch(() => {})
      }
    }
  }
}
</script>

<style scoped lang="less">
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid #1f2d3d;
    
    h3 {
      color: #fff;
      font-size: 18px;
    }
  }
}

.layout-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  
  .header-left {
    .title {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
  }
  
  .header-right {
    .user-info {
      cursor: pointer;
      color: #606266;
      font-size: 14px;
      
      &:hover {
        color: #409EFF;
      }
    }
  }
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
