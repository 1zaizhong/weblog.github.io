<template>
    <div>
        <!-- 卡片组件， shadow="never" 指定 card 卡片组件没有阴影 -->
        <el-card shadow="never">
            <el-form ref="formRef" :model="form" label-width="160px" :rules="rules">
                <el-form-item>
                    <h2 class="font-bold text-base mb-1">基础设置</h2>
                </el-form-item>
                <el-form-item label="博客名称" prop="name">
                    <el-input v-model="form.name" clearable />
                </el-form-item>
                <el-form-item label="作者名" prop="author">
                    <el-input v-model="form.author" clearable />
                </el-form-item>
                <el-form-item label="博客 LOGO" prop="logo">
                    <el-upload class="avatar-uploader" action="#" :on-change="handleLogoChange" :auto-upload="false"
                        :show-file-list="false">
                        <img v-if="form.logo" :src="form.logo" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="作者头像" prop="avatar">
                    <el-upload class="avatar-uploader" action="#" :on-change="handleAvatarChange" :auto-upload="false"
                        :show-file-list="false">
                        <img v-if="form.avatar" :src="form.avatar" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="介绍语" prop="introduction">
                    <el-input v-model="form.introduction" type="textarea" />
                </el-form-item>

                <!-- 分割线 -->
                <el-divider />

                

                <el-form-item>
                    <h2 class="font-bold text-base mb-1">评论设置</h2>
                </el-form-item>
                <el-form-item label="敏感词过滤">
                    <el-switch v-model="form.isCommentSensiWordOpen" inline-prompt :active-icon="Check" :inactive-icon="Close"
                    @change="sensiWordSwitchChange"/>
                    <div class="flex items-center ml-3">
                        <el-icon class="mr-2" color="#909399"><InfoFilled /></el-icon>
                        <el-text class="mx-1" type="info"  size="small">开启后，系统自动对发表的每条评论进行敏感词过滤</el-text>
                    </div>
                </el-form-item>
                <el-form-item label="开启审核">
                    <el-switch v-model="form.isCommentExamineOpen" inline-prompt :active-icon="Check" :inactive-icon="Close"
                    @change="examineSwitchChange"/>
                    <div class="flex items-center ml-3">
                        <el-icon class="mr-2" color="#909399"><InfoFilled /></el-icon>
                        <el-text class="mx-1" type="info"  size="small">开启后，评论需要博主后台审核通过后，才会展示出来</el-text>
                    </div>
                </el-form-item>
            

                <el-form-item>
                    <el-button type="primary" :loading="btnLoading" @click="onSubmit">保存</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { Check, Close } from '@element-plus/icons-vue'
import { getBlogSettingsDetail, updateBlogSettings } from '@/api/admin/blogsettings'
import { uploadFile } from '@/api/admin/file'
import { showMessage } from '@/composables/util'


// 是否显示保存按钮的 loading 状态，默认为 false
const btnLoading = ref(false)

// 表单引用
const formRef = ref(null)
// 表单对象
const form = reactive({
    name: '',
    author: '',
    logo: '',
    avatar: '',
    introduction: '',
    isCommentSensiWordOpen: true, // 是否开启评论敏感词过滤
    isCommentExamineOpen: false, // 是否开启评论审核
})

// 规则校验
const rules = {
    name: [{ required: true, message: '请输入博客名称', trigger: 'blur' }],
    author: [{ required: true, message: '请输入作者名', trigger: 'blur' }],
    logo: [{ required: true, message: '请上传博客 LOGO', trigger: 'blur' }],
    avatar: [{ required: true, message: '请上传作者头像', trigger: 'blur' }],
    introduction: [{ required: true, message: '请输入介绍语', trigger: 'blur' }],
}



// 初始化博客设置数据，并渲染到页面上
function initBlogSettings() {
    getBlogSettingsDetail().then((e) => {
        if (e.success) {
            // 设置表单数据
            form.name = e.data.name
            form.author = e.data.author
            form.logo = e.data.logo
            form.avatar = e.data.avatar
            form.introduction = e.data.introduction

           
            form.isCommentSensiWordOpen = e.data.isCommentSensiWordOpen
            form.isCommentExamineOpen = e.data.isCommentExamineOpen
         
        }
    })
}
initBlogSettings()

// 上传 logo 图片
const handleLogoChange = (file) => {
    // 表单对象
    let formData = new FormData()
    // 添加 file 字段，并将文件传入 
    formData.append('file', file.raw)
    uploadFile(formData).then((e) => {
        // 响参失败，提示错误消息
        if (e.success == false) {
            let message = e.message
            showMessage(message, 'error')
            return
        }

        // 成功则设置 logo 链接，并提示成功
        form.logo = e.data.url
        showMessage('上传成功')
    })
}

// 上传作者头像
const handleAvatarChange = (file) => {
    // 表单对象
    let formData = new FormData()
    // 添加 file 字段，并将文件传入 
    formData.append('file', file.raw)
    uploadFile(formData).then((e) => {
        // 响参失败，提示错误消息
        if (e.success == false) {
            let message = e.message
            showMessage(message, 'error')
            return
        }

        // 成功则设置作者头像链接，并提示成功
        form.avatar = e.data.url
        showMessage('上传成功')
    })
}

// 保存当前博客设置
const onSubmit = () => {
    // 先验证 form 表单字段
    formRef.value.validate((valid) => {
        if (!valid) {
            console.log('表单验证不通过')
            return false
        }

        // 显示保存按钮 loading
        btnLoading.value = true
        updateBlogSettings(form).then((res) => {
            if (res.success == false) {
                // 获取服务端返回的错误消息
                let message = res.message
                // 提示错误消息
                showMessage(message, 'error')
                return
            }
            
            // 重新渲染页面中的信息
            initBlogSettings()
            showMessage('保存成功')
        }).finally(() => btnLoading.value = false) // 隐藏保存按钮 loading
    })
}

// 评论敏感词过滤 switch 组件 change 事件
const sensiWordSwitchChange = (checked) => form.isCommentSensiWordOpen = checked
// 评论审核 switch 组件 change 事件
const examineSwitchChange = (checked) => form.isCommentExamineOpen = checked


</script>

<style scoped>
.avatar-uploader .avatar {
    width: 100px;
    height: 100px;
    display: block;
}
</style>

<style>
/* 解决 textarea :focus 状态下，边框消失的问题 */
.el-textarea__inner:focus {
    outline: 0 !important;
    box-shadow: 0 0 0 1px var(--el-input-focus-border-color) inset !important;
}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
}
</style>