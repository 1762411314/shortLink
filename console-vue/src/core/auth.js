import Cookies from 'js-cookie'

const TokenKey = 'token'

export function getToken() {
  return Cookies.get(TokenKey)
}
export function getUsername() {
  return Cookies.get('username')
}
// export function getUserId() {
//   return Cookies.get('userId')
// }
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function setUsername(username) {
  return Cookies.set('username', username)
}
// export function setUserId(userId) {
//   return Cookies.set('userId', userId)
// }
export function removeKey() {
  return Cookies.remove(TokenKey)
}

export function removeUsername() {
  return Cookies.remove('username')
}


