import http from './public'
import qs from 'qs'
/*登陆*/
export const login = params => {
  let loginRequest = qs.stringify(params);
  return http.requestPostForm('/openapi/auth/user/login',loginRequest);
}
/*退出*/
export const logout = params => {
  return http.requestPost('/openapi/auth/user/logout');
}



