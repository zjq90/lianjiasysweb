<template>
  <view class="family-form-page">
    <!-- 基础信息区域 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">🏠 基础信息</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">家庭名称</text>
        <input
          class="form-input"
          type="text"
          placeholder="请输入家庭名称，如：张三家"
          v-model="formData.name"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">户型类型</text>
        <picker
          :value="roomTypeIndex"
          :range="roomTypeList"
          @change="onRoomTypeChange"
        >
          <view class="picker-content">
            <text class="picker-value">{{ formData.roomType || '请选择' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">房屋面积</text>
        <view class="input-with-unit">
          <input
            class="form-input unit-input"
            type="digit"
            placeholder="请输入"
            v-model="formData.area"
            maxlength="5"
          />
          <text class="unit-text">㎡</text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="form-label">详细地址</text>
        <textarea
          class="form-textarea"
          placeholder="请输入详细地址"
          v-model="formData.address"
          maxlength="200"
        />
      </view>
    </view>

    <!-- 家庭成员区域 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">👥 家庭成员</text>
        <view class="add-member-btn" @tap="addMember">
          <text class="add-icon">+</text>
          <text class="add-text">添加成员</text>
        </view>
      </view>
      
      <view class="members-list">
        <view
          class="member-item"
          v-for="(member, index) in formData.members"
          :key="member.id || index"
        >
          <view class="member-avatar">
            <text class="avatar-text">{{ member.name ? member.name.charAt(0) : '?' }}</text>
          </view>
          <view class="member-info">
            <input
              class="member-input"
              type="text"
              placeholder="姓名"
              v-model="member.name"
              maxlength="10"
            />
            <picker
              :value="memberRelationIndex[member.id || index] || 0"
              :range="relationList"
              @change="onRelationChange($event, index)"
            >
              <view class="relation-picker">
                <text class="relation-text">{{ member.relation || '关系' }}</text>
                <text class="picker-arrow">›</text>
              </view>
            </picker>
          </view>
          <view class="member-remove" @tap="removeMember(index)">
            <text class="remove-icon">×</text>
          </view>
        </view>
        
        <view class="empty-members" v-if="formData.members.length === 0">
          <text class="empty-text">暂无家庭成员，点击上方"添加成员"按钮添加</text>
        </view>
      </view>
    </view>

    <!-- 特殊情况 -->
    <view class="form-section card">
      <view class="section-header">
        <text class="section-title">⚠️ 特殊情况</text>
      </view>
      
      <view class="form-item switch-item">
        <view class="switch-info">
          <text class="switch-label">家中有小孩</text>
          <text class="switch-desc">服务时需特别注意安全防护</text>
        </view>
        <switch
          :checked="formData.hasChildren === 1"
          @change="onHasChildrenChange"
          color="#4CAF50"
        />
      </view>
      
      <view class="form-item switch-item">
        <view class="switch-info">
          <text class="switch-label">家中有宠物</text>
          <text class="switch-desc">服务时需注意宠物安全</text>
        </view>
        <switch
          :checked="formData.hasPets === 1"
          @change="onHasPetsChange"
          color="#4CAF50"
        />
      </view>
    </view>

    <!-- 设为默认 -->
    <view class="form-section card">
      <view class="form-item switch-item">
        <view class="switch-info">
          <text class="switch-label">设为默认家庭</text>
          <text class="switch-desc">创建服务单时默认选中</text>
        </view>
        <switch
          :checked="formData.isDefault === 1"
          @change="onDefaultChange"
          color="#4CAF50"
        />
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="action-section safe-area-bottom">
      <button class="btn btn-primary btn-block" @tap="submitForm">
        {{ isEdit ? '保存修改' : '新增家庭' }}
      </button>
      <button class="btn btn-outline btn-block" @tap="cancelForm" v-if="isEdit">
        取消
      </button>
    </view>
  </view>
</template>

<script>
import storage from '@/common/utils/storage.js';
import navigator from '@/common/utils/navigator.js';

export default {
  data() {
    return {
      isEdit: false,
      familyId: null,
      familyInfo: {},
      roomTypeList: ['一室一厅', '两室一厅', '三室一厅', '三室两厅', '四室两厅', '别墅', '其他'],
      roomTypeIndex: 0,
      relationList: ['本人', '配偶', '父亲', '母亲', '儿子', '女儿', '兄弟', '姐妹', '其他'],
      memberRelationIndex: {},
      formData: {
        name: '',
        roomType: '',
        area: '',
        address: '',
        members: [],
        hasChildren: 0,
        hasPets: 0,
        isDefault: 0
      }
    };
  },
  onLoad(options) {
    console.log('家庭表单页面 onLoad, options:', options);
    
    if (options && options.id) {
      this.isEdit = true;
      this.familyId = options.id;
      this.loadFamilyInfo();
    }
    
    uni.setNavigationBarTitle({
      title: this.isEdit ? '编辑家庭' : '新增家庭'
    });
  },
  methods: {
    loadFamilyInfo() {
      console.log('加载家庭信息, familyId:', this.familyId);
      const familyInfo = storage.FamilyStorage.getById(this.familyId);
      console.log('获取到的家庭信息:', familyInfo);
      
      if (familyInfo) {
        this.familyInfo = familyInfo;
        this.formData = {
          name: familyInfo.name || '',
          roomType: familyInfo.roomType || '',
          area: familyInfo.area ? String(familyInfo.area) : '',
          address: familyInfo.address || '',
          members: familyInfo.members ? [...familyInfo.members] : [],
          hasChildren: familyInfo.hasChildren || 0,
          hasPets: familyInfo.hasPets || 0,
          isDefault: familyInfo.isDefault || 0
        };
        
        // 设置户型选择索引
        const roomTypeIdx = this.roomTypeList.indexOf(this.formData.roomType);
        if (roomTypeIdx !== -1) {
          this.roomTypeIndex = roomTypeIdx;
        }
        
        // 初始化成员关系索引
        this.formData.members.forEach((member, index) => {
          const relationIdx = this.relationList.indexOf(member.relation);
          if (relationIdx !== -1) {
            this.memberRelationIndex[member.id || index] = relationIdx;
          }
        });
      } else {
        uni.showToast({
          title: '家庭不存在',
          icon: 'none'
        });
        setTimeout(() => {
          navigator.navigateBack();
        }, 1500);
      }
    },
    
    onRoomTypeChange(e) {
      console.log('户型选择变化:', e);
      this.roomTypeIndex = e.detail.value;
      this.formData.roomType = this.roomTypeList[e.detail.value];
    },
    
    onRelationChange(e, index) {
      console.log('关系选择变化:', e, index);
      const member = this.formData.members[index];
      if (member) {
        const relationIdx = e.detail.value;
        member.relation = this.relationList[relationIdx];
        this.memberRelationIndex[member.id || index] = relationIdx;
      }
    },
    
    onHasChildrenChange(e) {
      console.log('有小孩开关变化:', e);
      this.formData.hasChildren = e.detail.value ? 1 : 0;
    },
    
    onHasPetsChange(e) {
      console.log('有宠物开关变化:', e);
      this.formData.hasPets = e.detail.value ? 1 : 0;
    },
    
    onDefaultChange(e) {
      console.log('默认家庭开关变化:', e);
      this.formData.isDefault = e.detail.value ? 1 : 0;
    },
    
    addMember() {
      console.log('添加家庭成员');
      const newMember = {
        id: Date.now(),
        name: '',
        relation: ''
      };
      this.formData.members.push(newMember);
    },
    
    removeMember(index) {
      console.log('移除家庭成员, index:', index);
      uni.showModal({
        title: '确认移除',
        content: '确定要移除该家庭成员吗？',
        success: (res) => {
          if (res.confirm) {
            this.formData.members.splice(index, 1);
          }
        }
      });
    },
    
    submitForm() {
      console.log('提交表单, formData:', this.formData);
      
      // 验证表单
      if (!this.formData.name || this.formData.name.trim() === '') {
        uni.showToast({
          title: '请输入家庭名称',
          icon: 'none'
        });
        return;
      }
      
      if (!this.formData.address || this.formData.address.trim() === '') {
        uni.showToast({
          title: '请输入详细地址',
          icon: 'none'
        });
        return;
      }
      
      uni.showLoading({
        title: this.isEdit ? '保存中...' : '新增中...'
      });
      
      setTimeout(() => {
        try {
          // 处理面积数据
          const area = this.formData.area ? parseFloat(this.formData.area) : 0;
          
          // 过滤无效成员
          const validMembers = this.formData.members.filter(m => m.name && m.name.trim() !== '');
          
          if (this.isEdit) {
            // 编辑模式
            const result = storage.FamilyStorage.update(this.familyId, {
              name: this.formData.name.trim(),
              roomType: this.formData.roomType,
              area: area,
              address: this.formData.address.trim(),
              members: validMembers,
              memberCount: validMembers.length,
              hasChildren: this.formData.hasChildren,
              hasPets: this.formData.hasPets,
              isDefault: this.formData.isDefault
            });
            
            if (result) {
              uni.hideLoading();
              uni.showToast({
                title: '修改成功',
                icon: 'success'
              });
              
              setTimeout(() => {
                navigator.navigateBack();
              }, 1500);
            } else {
              uni.hideLoading();
              uni.showToast({
                title: '修改失败',
                icon: 'none'
              });
            }
          } else {
            // 新增模式
            const result = storage.FamilyStorage.add({
              name: this.formData.name.trim(),
              roomType: this.formData.roomType,
              area: area,
              address: this.formData.address.trim(),
              members: validMembers,
              memberCount: validMembers.length,
              hasChildren: this.formData.hasChildren,
              hasPets: this.formData.hasPets,
              isDefault: this.formData.isDefault
            });
            
            if (result) {
              uni.hideLoading();
              uni.showToast({
                title: '新增成功',
                icon: 'success'
              });
              
              setTimeout(() => {
                navigator.navigateBack();
              }, 1500);
            } else {
              uni.hideLoading();
              uni.showToast({
                title: '新增失败',
                icon: 'none'
              });
            }
          }
        } catch (error) {
          uni.hideLoading();
          console.error('保存家庭失败:', error);
          uni.showToast({
            title: '操作失败',
            icon: 'none'
          });
        }
      }, 300);
    },
    
    cancelForm() {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消编辑吗？修改的内容将不会保存。',
        success: (res) => {
          if (res.confirm) {
            navigator.navigateBack();
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.family-form-page {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding: 30rpx;
  padding-bottom: 180rpx;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 区域标题 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #E0E0E0;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.add-member-btn {
  display: flex;
  align-items: center;
  color: #4CAF50;
}

.add-icon {
  font-size: 32rpx;
  margin-right: 4rpx;
}

.add-text {
  font-size: 26rpx;
}

/* 表单项样式 */
.form-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.form-item:last-child {
  border-bottom: none;
}

.form-label {
  width: 180rpx;
  font-size: 28rpx;
  color: #333333;
  flex-shrink: 0;
}

.form-input {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
  text-align: right;
}

.form-input::placeholder {
  color: #CCCCCC;
}

.form-textarea {
  flex: 1;
  height: 120rpx;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
  text-align: right;
}

.form-textarea::placeholder {
  color: #CCCCCC;
}

/* 带单位的输入框 */
.input-with-unit {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.unit-input {
  flex: 0;
  width: 120rpx;
  text-align: right;
}

.unit-text {
  font-size: 28rpx;
  color: #666666;
  margin-left: 8rpx;
}

/* 选择器样式 */
.picker-content {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.picker-value {
  font-size: 28rpx;
  color: #333333;
}

.picker-value:empty::before {
  content: '请选择';
  color: #CCCCCC;
}

.picker-arrow {
  font-size: 36rpx;
  color: #CCCCCC;
  margin-left: 8rpx;
}

/* 开关样式 */
.switch-item {
  justify-content: space-between;
}

.switch-info {
  display: flex;
  flex-direction: column;
}

.switch-label {
  font-size: 28rpx;
  color: #333333;
}

.switch-desc {
  font-size: 22rpx;
  color: #999999;
  margin-top: 4rpx;
}

/* 家庭成员列表 */
.members-list {
  padding: 20rpx 30rpx;
}

.member-item {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 72rpx;
  height: 72rpx;
  background-color: #E8F5E9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.avatar-text {
  font-size: 32rpx;
  color: #4CAF50;
  font-weight: 500;
}

.member-info {
  flex: 1;
  display: flex;
  align-items: center;
}

.member-input {
  width: 120rpx;
  font-size: 28rpx;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
}

.member-input::placeholder {
  color: #CCCCCC;
}

.relation-picker {
  display: flex;
  align-items: center;
  margin-left: 16rpx;
}

.relation-text {
  font-size: 26rpx;
  color: #666666;
}

.relation-text:empty::before {
  content: '关系';
  color: #CCCCCC;
}

.member-remove {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 16rpx;
}

.remove-icon {
  font-size: 36rpx;
  color: #CCCCCC;
}

.empty-members {
  padding: 40rpx 0;
  display: flex;
  justify-content: center;
}

.empty-text {
  font-size: 24rpx;
  color: #999999;
}

/* 操作按钮区域 */
.action-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background-color: #FFFFFF;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx 40rpx;
  border-radius: 50rpx;
  font-size: 30rpx;
  font-weight: 500;
  border: none;
  margin-bottom: 16rpx;
}

.btn:last-child {
  margin-bottom: 0;
}

.btn-primary {
  background-color: #4CAF50;
  color: #FFFFFF;
}

.btn-outline {
  background-color: transparent;
  color: #4CAF50;
  border: 2rpx solid #4CAF50;
}

.btn-block {
  width: 100%;
}

/* 安全区域 */
.safe-area-bottom {
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}
</style>
