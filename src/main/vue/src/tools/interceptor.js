let oldFetch = global.fetch;

let newFetch = function (url, options = {}) {
  let request = {
    url,
    options
  };

  return new Promise((resolve, reject) => {

    if (this.interceptors.length > 0) {
      //执行请求前的拦截操作
      this.runInterceptors(0, request)
        .then(req => {
          oldFetchFun(this,req)
            .then((res)=>{
              resolve(res);
            })
            .catch(err => {
              reject(err)
            });
        })
    } else {
      oldFetchFun(this, request)
        .then((res)=>{
          resolve(res);
        })
        .catch(err => {
          reject(err)
        });
    }

  });
};

let oldFetchFun = function (that, request) {
  return new Promise((resolve, reject) => {
    //添加超时检测
    let timeout = request.options.timeout;
    let timer;
    if (timeout) {
      timer = setTimeout(function(){
        reject(new Error("fetch timeout"))
      }, timeout );
    }
    oldFetch(request.url, request.options)
      .then(res => {
        // 执行请求后的拦截操作
        let response = res;
        // fetch返回的promise在某些错误的http状态下如400、500等不会reject，相反它会被resolve；只有网络错误会导致请求不能完成时，fetch 才会被 reject；
        if (that.interceptors_after.length > 0) {
          that.runInterceptorsAfter(0, response)
            .then(data => {
              resolve(data);
            })
        }
      })
      .catch(err => {
        reject(err)
      });
  })
};

let breadFetch = function () {

};

breadFetch.prototype.newFetch = newFetch;

//fetch拦截器
breadFetch.prototype.interceptors = [];
breadFetch.prototype.interceptors_after = [];
breadFetch.prototype.runInterceptors = function (i, request) {
  let _that = this;
  if(i === 0) this.interceptors_after = [];

  return new Promise((resolve, reject) => {
    if (i >= this.interceptors.length) resolve(request);
    this.interceptors[i](request, function (callback) {
      if(callback){
        //callback 存入请求后执行的数组
        _that.interceptors_after.push(callback)
      }
      _that.runInterceptors(++i, request).then(req => {
        resolve(req)
      })
    })
  })
};

breadFetch.prototype.runInterceptorsAfter = function (i, response) {
  let _that = this;
  return new Promise((resolve, reject) => {
    if (i >= this.interceptors_after.length) resolve(response);
    this.interceptors_after[i](response, function () {
      _that.runInterceptorsAfter(++i, response).then(res => {
        resolve(res)
      })
    })
  })
};

let objFetch = new breadFetch();
let fetch = function (url, options = {}) {
  return new Promise((resolve, reject) => {
    objFetch.newFetch(url, options)
      .then(data => {
        resolve(data);
      })
      .catch(err => {
        reject(err)
      });
  })
};

export default objFetch
export { fetch }