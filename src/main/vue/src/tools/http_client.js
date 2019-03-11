import breadFetch, { fetch } from './interceptor'

const HOST = process.env.VUE_APP_API_BASEURL;
const cookies = require('js-cookie');

function get(url, params = null, options = null) {
  let baseUrl = url;
  if (params) {
    baseUrl = baseUrl + '?' + Object.keys(params).map((key) => {
      return `${key}=${params[key]}`
    }).join('&')
  }
  let baseOptions = {
    method: 'GET',
    headers: {
      Authorization: cookies('token') ? cookies('token') : '',
      'Content-Type': 'application/json'
    },
    withCredentials: true
  };
  if (options) {
    for (let key in options) {
      baseOptions[key] = options[key];
    }
  }

  return fetch(baseUrl, baseOptions)
    .then(res => {
      return res.json();
    })
    .then(res => {
      if (res.code === 401) {
        handleUnauthorized();
      }
      return res;
    })
    .catch(err => {
      // console.error(err);
    });
}

function post(url, body = {}, options = null) {
  let baseUrl = url;
  let baseOptions = {
    method: 'POST',
    headers: {
      Authorization: cookies('token') ? cookies('token') : '',
      'Content-Type': 'application/json'
    },
    withCredentials: true,
    body: JSON.stringify(body)
  };
  if (options) {
    for (let key in options) {
      baseOptions[key] = options;
    }
  }

  return fetch(baseUrl, baseOptions)
    .then(res => {
      return res.json();
    })
    .then(res => {
      if (res.code === 401) {
        handleUnauthorized();
      }
      if (res.code === 500) {
        throw Error(res.message);
      }
      return res;
    });
    // .catch(err => {
    //   console.error(err);
    //   return err;
    // });
}

function handleUnauthorized() {
  location.href = '/';
}

function handleLogin() {
  location.href = '/'
}

breadFetch.interceptors.push((req, next) => {
  if (!req.url.startsWith('http://') && !req.url.startsWith('https://')) {
    req.url = HOST + '/' + req.url;
  }
  next((res, after) => {
    after()
  })
});

export { get, post };