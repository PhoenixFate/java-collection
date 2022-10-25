export default ({$axios}, inject) => {
    // 上传图片
    inject('uploadImg', data => $axios.$post('/file/file/upload', data))

    // 删除图片 /article/file/delete?fileUrl=xxx
    inject('deleteImg', fileUrl => $axios.$delete('/file/file/delete', {params: {fileUrl} }))
}
