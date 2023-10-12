export function getToken() {
  return localStorage.getItem('token');
}

export function persistToken(token: string) {
  localStorage.setItem('token', token);
}