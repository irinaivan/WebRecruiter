webRecruiterApp.service('httpLoginRequestsService', function($http) {
    return {
        submitForm: function(url, data) {
            return $http.post(url, data);
        }
    };
});

webRecruiterApp.service('tokenRequestService', function($http, $window) {
    return {
        postRequest: function(url, data) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http.post(url, data);
        },
        getRequest: function(url) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http.get(url);
        }
    };
    
});
