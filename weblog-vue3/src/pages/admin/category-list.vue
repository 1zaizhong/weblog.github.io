<template>
    <div>
        <el-card shadow="never" class="mb-5">
            <div class="flex items-center">
                <el-text>专栏名称</el-text>
                <div class="ml-3 w-52 mr-5"><el-input v-model="searchCategoryName" placeholder="请输入名称" clearable /></div>

                <el-text>创建日期</el-text>
                <div class="ml-3 w-30 mr-5">
                    <el-date-picker v-model="pickDate" type="daterange" range-separator="至" start-placeholder="开始时间"
                        end-placeholder="结束时间" size="default" :shortcuts="shortcuts" @change="datepickerChange"/>
                </div>

                <el-button type="primary" class="ml-3" :icon="Search" @click="getTableData">查询</el-button>
                <el-button class="ml-3" :icon="RefreshRight" @click="reset">重置</el-button>
            </div>
        </el-card>

        <el-card shadow="never">
            <div class="mb-5" v-if="isAdmin">
                <el-button type="primary" @click="addCategoryBtnClick">
                    <el-icon class="mr-1"><Plus /></el-icon>新增官方专栏
                </el-button>
            </div>

            <el-table :data="tableData" border stripe style="width: 100%" v-loading="tableLoading">
                <el-table-column prop="name" label="专栏名称" width="180" align="center" />
                <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
                <el-table-column label="操作" align="center">
                    <template #default="scope">
                        <el-button size="small" @click="goCategoryArticle(scope.row)">
                            <el-icon class="mr-1"><FolderOpened /></el-icon>管理文章
                        </el-button>

                        <el-button v-if="isAdmin" type="danger" size="small" @click="deleteCategorySubmit(scope.row)">
                            <el-icon class="mr-1"><Delete /></el-icon>删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="mt-10 flex justify-center">
                <el-pagination v-model:current-page="current" v-model:page-size="size" :page-sizes="[10, 20, 50]"
                    :background="true" layout="total, sizes, prev, pager, next, jumper"
                    :total="total" @size-change="handleSizeChange" @current-change="getTableData" />
            </div>
        </el-card>

        <FormDialog ref="formDialogRef" title="新增官方专栏" destroyOnClose @submit="onSubmit">
            <el-form ref="formRef" :rules="rules" :model="form">
                <el-form-item label="专栏名称" prop="name" label-width="80px" size="large">
                    <el-input v-model="form.name" placeholder="请输入专栏名称" maxlength="20" show-word-limit clearable/>
                </el-form-item>
            </el-form>
        </FormDialog>
    </div>
</template>

<script setup>
import { Search, RefreshRight, Plus, Delete, FolderOpened } from '@element-plus/icons-vue'
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getCategoryPageList, addCategory, deleteCategory } from '@/api/admin/category'
import moment from 'moment'
import { showMessage, showModel } from '@/composables/util'
import FormDialog from '@/components/FormDialog.vue'

const router = useRouter()

// --- 权限控制逻辑 ---
const isAdmin = computed(() => {
    const userStr = localStorage.getItem('user')
    if (userStr) {
        const userObj = JSON.parse(userStr)
        // 这里的字段名需对应你登录接口存入 localStorage 的结构
        const userID = userObj.userInfo?.userID || userObj.id 
        return userID === 1
    }
    return false
})

// --- 页面跳转逻辑 ---
const goCategoryArticle = (row) => {
    router.push({
        path: '/admin/category/article', // 这是你接下来需要创建的路由
        query: { 
            id: row.id,
            name: row.name 
        }
    })
}

// --- 原有逻辑保持并优化 ---
const searchCategoryName = ref('')
const pickDate = ref('')
const startDate = ref(null)
const endDate = ref(null)

const datepickerChange = (e) => {
    if (e) {
        startDate.value = moment(e[0]).format('YYYY-MM-DD')
        endDate.value = moment(e[1]).format('YYYY-MM-DD')
    } else {
        startDate.value = null
        endDate.value = null
    }
}

const tableLoading = ref(false)
const tableData = ref([])
const current = ref(1)
const total = ref(0)
const size = ref(10)

function getTableData() {
    tableLoading.value = true
    getCategoryPageList({
        current: current.value, 
        size: size.value, 
        startDate: startDate.value, 
        endDate: endDate.value, 
        name: searchCategoryName.value
    })
    .then((res) => {
        if (res.success) {
            tableData.value = res.data
            total.value = res.total
        }
    })
    .finally(() => tableLoading.value = false)
}
getTableData()

const handleSizeChange = (chooseSize) => {
    size.value = chooseSize
    getTableData()
}

const reset = () => {
    searchCategoryName.value = ''
    pickDate.value = ''
    startDate.value = null
    endDate.value = null
    getTableData()
}

const formDialogRef = ref(null)
const addCategoryBtnClick = () => formDialogRef.value.open()

const formRef = ref(null)
const form = reactive({ name: '' })
const rules = {
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' }]
}

const onSubmit = () => {
    formRef.value.validate((valid) => {
        if (!valid) return
        formDialogRef.value.showBtnLoading()
        addCategory(form).then((res) => {
            if (res.success) {
                showMessage('添加成功')
                form.name = ''
                formDialogRef.value.close()
                getTableData()
            } else {
                showMessage(res.message, 'error')
            }
        }).finally(() => formDialogRef.value.closeBtnLoading())
    })
}

const deleteCategorySubmit = (row) => {
    showModel('确定要删除该专栏吗？此操作不可逆。').then(() => {
        deleteCategory(row.id).then((res) => {
            if (res.success) {
                showMessage('删除成功')
                getTableData()
            } else {
                showMessage(res.message, 'error')
            }
        })
    }).catch(() => {})
}

// 快捷选项
const shortcuts = [
    { text: '最近一周', value: () => [new Date(Date.now() - 3600 * 1000 * 24 * 7), new Date()] },
    { text: '最近一个月', value: () => [new Date(Date.now() - 3600 * 1000 * 24 * 30), new Date()] }
]
</script>