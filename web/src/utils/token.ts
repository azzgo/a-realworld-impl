export function getToken() {
  return localStorage.getItem('token');
}

export function hasToken() {
  return getToken() != null;
}

export function persistToken(token: string) {
  localStorage.setItem('token', token);
}

export function clearToken() {
  localStorage.removeItem('token');
}
