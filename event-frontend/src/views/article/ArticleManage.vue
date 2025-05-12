<script setup>
import { Edit, Delete, Plus  } from "@element-plus/icons-vue";
import { ref } from "vue";
import { articleCategoryService, getArticleList, addArticleService, updateArticleService, deleteArticleService } from "@/api/article";
import { format } from "date-fns";
import { ElMessage } from 'element-plus'
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { useTokenStore } from "@/stores/token";
//文章分类数据模型
const categories = ref([]);

//用户搜索时选中的分类id
const categoryId = ref("");

//用户搜索时选中的发布状态
const state = ref("");

//文章列表数据模型
const articles = ref([]);

// 弹出框标题
const title = ref('添加文章')

//分页条数据模型
const pageNum = ref(1); //当前页
const total = ref(20); //总条数
const pageSize = ref(10); //每页条数

//控制抽屉是否显示
const visibleDrawer = ref(false);
//添加表单数据模型
const articleModel = ref({
  id:"",
  title: "",
  categoryId: "",
  coverImg: "",
  content: "",
  state: "",
});
const tokenStore = useTokenStore()

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size;
  getArticles();
};
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num;
  getArticles();
};

// 获取文章分类数据
const getCategories = async () => {
  let result = await articleCategoryService();
  categories.value = result.data;
};
// 调用函数
getCategories();

// 获取文章列表
const getArticles = async () => {
  let result = await getArticleList(
    pageNum.value,
    pageSize.value,
    categoryId.value,
    state.value
  );
  articles.value = result.data.items;
  //为列表中添加categoryName属性
  for (let i = 0; i < articles.value.length; i++) {
    let article = articles.value[i];
    article.createTime = format(article.createTime, "yyyy-MM-dd HH:mm:ss");
    for (let j = 0; j < categories.value.length; j++) {
      if (article.categoryId === categories.value[j].id) {
        article.categoryName = categories.value[j].categoryName;
      }
    }
  }
  //渲染总条数
  total.value = result.data.total;
};
getArticles();

// 重置函数
const resetCondition = () => {
  categoryId.value = "";
  state.value = "";
  getArticles();
};

// 上传前校验文件类型和大小
const beforeUpload = (file) => {
      const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        ElMessage.error('只能上传图片文件(JPEG/PNG/GIF)!');
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!');
      }
      return isImage && isLt2M; // 返回true才会继续上传
    }


// 图片上传成功回调函数
const handleUploadSuccess =  (result) => {
    articleModel.value.coverImg = result.data
}

// 添加文章
const addArticle = async (state) => {
    title.value = '添加文章'
    let result = await addArticleService({
        title: articleModel.value.title,
        categoryId: parseInt(articleModel.value.categoryId),
        coverImg: articleModel.value.coverImg,
        content: articleModel.value.content,
        state: state
    })
    // 清空数据，恢复状态
    visibleDrawer.value = false
    articleModel.value = {
        title: "",
        categoryId: "",
        coverImg: "",
        content: "",
        state: "",
    }
    // 刷新页面
    getArticles();
}
// 数据回显
const updateArticleEcho = (row) => {
    title.value = '修改文章'
    visibleDrawer.value = true
    articleModel.value.id = row.id
    articleModel.value.title = row.title
    articleModel.value.categoryId = row.categoryId
    articleModel.value.coverImg = row.coverImg
    articleModel.value.content = row.content
    articleModel.value.state = row.state
}
// 修改文章
const updateArticle = async (state) => {
    await updateArticleService({
        id: parseInt(articleModel.value.id),
        title: articleModel.value.title,
        categoryId: parseInt(articleModel.value.categoryId),
        coverImg: articleModel.value.coverImg,
        content: articleModel.value.content,
        state: state
    })
    visibleDrawer.value = false
    articleModel.value = {
        id: "",
        title: "",
        categoryId: "",
        coverImg: "",
        content: "",
        state: "",
    }
    getArticles()
}
// 删除文章
const deleteArticle = async (row) => {
    await deleteArticleService(row.id)
    // 刷新
    getArticles()
}
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章管理</span>
        <div class="extra">
          <el-button type="primary" @click="visibleDrawer = true"
            >添加文章</el-button
          >
        </div>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>
      <el-form-item label="文章分类：">
        <el-select
          placeholder="请选择"
          v-model="categoryId"
          style="width: 240px"
        >
          <el-option
            v-for="c in categories"
            :key="c.id"
            :label="c.categoryName"
            :value="c.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="发布状态：">
        <el-select placeholder="请选择" v-model="state" style="width: 240px">
          <el-option label="已发布" value="已发布"></el-option>
          <el-option label="草稿" value="草稿"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getArticles">搜索</el-button>
        <el-button @click="resetCondition">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 文章列表 -->
    <el-table :data="articles" style="width: 100%">
      <el-table-column
        label="文章标题"
        width="400"
        prop="title"
      ></el-table-column>
      <el-table-column label="分类" prop="categoryName"></el-table-column>
      <el-table-column label="发表时间" prop="createTime"> </el-table-column>
      <el-table-column label="状态" prop="state"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="updateArticleEcho(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[3, 5, 10, 15]"
      layout="jumper, total, sizes, prev, pager, next"
      background
      :total="total"
      @size-change="onSizeChange"
      @current-change="onCurrentChange"
      style="margin-top: 20px; justify-content: flex-end"
    />

    <!-- 抽屉 -->
    <el-drawer
      v-model="visibleDrawer"
      :title="title"
      direction="rtl"
      size="50%"
    >
      <!-- 添加文章表单 -->
      <el-form :model="articleModel" label-width="100px">
        <el-form-item label="文章标题">
          <el-input
            v-model="articleModel.title"
            placeholder="请输入标题"
          ></el-input>
        </el-form-item>
        <el-form-item label="文章分类">
          <el-select placeholder="请选择" v-model="articleModel.categoryId">
            <el-option
              v-for="c in categories"
              :key="c.id"
              :label="c.categoryName"
              :value="c.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文章封面">
          <!-- 
            将来当点击+图标，选择本地图片后，el-upload这个组件会自动发送请求，把图片上传到指定的服务器上，
            而不需要我们自己使用axios发送异步请求，所以需要给el-upload标签添加一些属性，控制请求的发送
            auto-upload:是否自动上传
            action: 服务器接口路径
            name: 上传的文件字段名
            headers: 设置上传的请求头
            on-success: 上传成功的回调函数
            
            -->
          <el-upload
            class="avatar-uploader"
            :auto-upload="true"
            :show-file-list="false"
            action="/api/upload"
            :headers="{'Authorization': tokenStore.token}"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"     
            :name="file"
          >
            <img
              v-if="articleModel.coverImg"
              :src="articleModel.coverImg"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="文章内容">
          <div class="editor">
            <quill-editor
              theme="snow"
              v-model:content="articleModel.content"
              contentType="html"
            >
            </quill-editor>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="title === '添加文章'?addArticle('已发布'):updateArticle('已发布')">发布</el-button>
          <el-button type="info" @click="title === '添加文章'?addArticle('草稿'):updateArticle('草稿')">草稿</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
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
/* 抽屉样式 */
.avatar-uploader {
  :deep() {
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
    }
  }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>
