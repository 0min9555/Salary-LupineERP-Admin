// axios-config.js
(function() {
    // Axios 인터셉터 설정
    axios.interceptors.response.use(function (response) {
        // 성공적인 요청에 대한 처리
        return response;
    }, function (error) {
        // 401 HTTP 상태 코드에 대한 처리 로직
        if (error.response && error.response.status === 401) {
            console.error('세션이 만료되었습니다. 로그인 페이지로 리다이렉트합니다.');
            window.location.href = '/admin/login';
        }
        return Promise.reject(error);
    });
})();