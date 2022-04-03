import http from './../../../base/api/public'
import querystring from 'querystring'


let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;

//查询课程列表
//我的课程列表
export const findCourseList = (page, size, params) => {
//使用工具类将json对象转成key/value
  let queries = querystring.stringify(params)
  return http.requestQuickGet(apiUrl + "/course/courseBase/list/" + page + "/" + size + "?" + queries)
}

//课程基本信息
export function getCourseBaseById(courseId) {
  return http.requestQuickGet(apiUrl + "/course/courseBase/info/" + courseId)
}

//更新课程基本信息
export function updateCourseBase(courseId, courseBase) {
  return http.requestPut(apiUrl + "/course/courseBase/" + courseId, courseBase)
}

//课程营销信息
export function getCourseMarketById(courseId) {
  return http.requestQuickGet(apiUrl + "/course/courseMarket/" + courseId)
}

//更新课程营销信息
export function updateCourseMarket(courseId,courseMarket) {
  return http.requestPut(apiUrl + "/course/courseMarket/" + courseId,courseMarket)
}


//查询课程分类
export const category_findlist = () => {
  return http.requestQuickGet(apiUrl + '/category/list')
}
/*添加课程基础信息*/
export const addCourseBase = params => {
  return http.requestPost(apiUrl + '/course/coursebase/add', params)
}
/*查询课程计划*/
export const findTeachPlanList = courseid => {
  return http.requestQuickGet(apiUrl + '/course/teachPlan/list/' + courseid)
}
/*添加课程计划*/
export const addTeachPlan = teachPlan => {
  return http.requestPost(apiUrl + '/course/teachPlan/add', teachPlan)
}

//保存课程图片地址到课程数据 库
export const addCoursePic = (courseId, pictureId) => {
  return http.requestPost(apiUrl + '/course/coursePicture/add?courseId=' + courseId + "&pictureId=" + pictureId)
}
//查询课程图片
export const findCoursePicList = courseId => {
  return http.requestQuickGet(apiUrl + '/course/coursePicture/list/' + courseId)
}

//删除课程图片
export const deleteCoursePic = courseId => {
  return http.requestDelete(apiUrl + '/course/coursePicture/delete/' + courseId)
}
/*预览课程*/
export const preview = id => {
  return http.requestPost(apiUrl + '/course/preview/' + id);
}
/*发布课程*/
export const publish = id => {
  return http.requestPost(apiUrl + '/course/publish/' + id);
}
//查询课程信息
export const findCourseView = courseId => {
  return http.requestQuickGet(apiUrl + '/course/courseview/' + courseId)
}

/*保存媒资信息*/
export const savemedia = teachplanMedia => {
  return http.requestPost(apiUrl + '/course/savemedia', teachplanMedia);
}
