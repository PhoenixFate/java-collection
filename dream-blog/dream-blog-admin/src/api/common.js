import request from '@/utils/request'

export default {

    // 上传图片
    uploadImg(data = {}) {
        return request({
            url: `/file/file/upload`,
            method: 'post',
            data
        })
    },

    // 删除图片 /article/file/delete?fileUrl=http://xxxxx
    deleteImg(imageUrl) {
        return request({
            url: `/file/file/delete`,
            method: 'delete',
            params: {'fileUrl': imageUrl}
        })
    },

}
