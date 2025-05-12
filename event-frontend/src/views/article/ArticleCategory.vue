<script setup>
import { Edit, Delete } from "@element-plus/icons-vue";
import { ref } from "vue";
import { articleCategoryService, addArticleCategoryService, updateArticleCategoryService, deleteArticleCategoryService } from "@/api/article";
import {ElMessageBox, ElMessage} from 'element-plus'

const categories = ref([]);
// 弹出框的标题
const title = ref('')

// 开始请求数据
const getCategories = async () => {
  // 调用api接口
  const result = await articleCategoryService();
  categories.value = result.data;
};
// 调用函数
getCategories();

//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
// 调用接口，添加表单
const addCategory = async () => {
    const result = await addArticleCategoryService(categoryModel.value)
    getCategories()
    dialogVisible.value = false
    categoryModel.value = {
         categoryName: '',
         categoryAlias: ''
    }
}
// 回显函数
const updateCategoryEcho = (row) => {
    title.value = '修改分类'
    dialogVisible.value = true
    //将row中的数据赋值给categoryModel
    categoryModel.value.categoryName=row.categoryName
    categoryModel.value.categoryAlias=row.categoryAlias
    //修改的时候必须传递分类的id，所以扩展一个id属性
    categoryModel.value.id=row.id
}

// 修改函数
const updateCategory = async () => {
    // 提交修改
    await updateArticleCategoryService(categoryModel.value)
    // 刷新一下
    getCategories()
    // 提交完之后做一些清理工作
    dialogVisible.value = false
    categoryModel.value = {
         categoryName: '',
         categoryAlias: ''
    }
}
// 删除文章分类
const deleteCategory = async (row) => {
    // 提示框
    ElMessageBox.confirm(
        '你确认删除该分类信息吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
             // 用户点击了确认，执行删除操作
        try {
            const result = await deleteArticleCategoryService(row.id);
            ElMessage({
                type: 'success',
                message: result.message || '删除成功',
            });
            // 
            getCategories(); // 刷新分类列表
        } catch (error) {
            // 处理删除请求失败的情况
            ElMessage({
                type: 'error',
                message: error.message || '删除失败',
            });
        }
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
    
}

</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章分类</span>
        <div class="extra">
          <el-button type="primary"  @click="dialogVisible = true;title='添加分类'">添加分类</el-button>
        </div>
      </div>
    </template>
    <el-table :data="categories" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"> </el-table-column>
      <el-table-column label="分类名称" prop="categoryName"></el-table-column>
      <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="updateCategoryEcho(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 添加分类弹窗 -->
    <el-dialog v-model="dialogVisible" :title="title" width="30%">
      <el-form
        :model="categoryModel"
        :rules="rules"
        label-width="100px"
        style="padding-right: 30px"
      >
        <el-form-item label="分类名称" prop="categoryName">
          <el-input
            v-model="categoryModel.categoryName"
            minlength="1"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="分类别名" prop="categoryAlias">
          <el-input
            v-model="categoryModel.categoryAlias"
            minlength="1"
            maxlength="15"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="title==='添加分类'? addCategory():updateCategory()"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>
