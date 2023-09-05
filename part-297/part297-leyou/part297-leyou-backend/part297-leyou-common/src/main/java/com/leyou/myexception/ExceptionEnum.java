package com.leyou.myexception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空！"),
    CATEGORY_NOT_FOUND(404,"商品分类没有查到"),
    BRAND_NOT_FOUND(404,"品牌没有找到"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    CATEGORY_BRAND_SAVE_ERROR(500,"新增品牌分类失败"),
    UPLOAD_IMAGE_ERROR(500,"上传图片失败"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    ;
    private int code;
    private String msg;

}
