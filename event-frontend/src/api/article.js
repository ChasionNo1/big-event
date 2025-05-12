import request from '@/utils/request'

// 提供查询文章分类的接口
export const articleCategoryService = () => {
    // 在pinia中定义的响应式数据，都不需要.value
    return request.get('/category')
}

// 添加文章分类
export const addArticleCategoryService = (categoryModel) => {
    // post
    return request.post('/category', categoryModel)

}

// 修改文章分类
export const updateArticleCategoryService = (categoryModel) => {
    return request.put('/category', categoryModel)
}

// 删除文章分类
export const deleteArticleCategoryService = (id) => {
    return request.delete('/category?id='+id)
}

// 获取文章列表
export const getArticleList = (pageNum, pageSize, categoryId, state) => {
    return request.get('/article', {
        params: {
            pageNum: parseInt(pageNum),
            pageSize: parseInt(pageSize),
            categoryId: categoryId?parseInt(categoryId):null,
            state: state?state:null
        }
    })
}

// 添加文章
export const addArticleService = (params) => {
    return request.post('/article', params)
}

// 修改文章
export const updateArticleService = (params) => {
    return request.put('/article', params)
}


// 删除文章
export const deleteArticleService = (id) => {
    return request.delete('/article?id='+id)
}