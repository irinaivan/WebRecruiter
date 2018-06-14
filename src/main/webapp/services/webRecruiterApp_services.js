webRecruiterApp.service('httpLoginRequestsService', function ($http) {
    return {
        submitForm: function (url, data) {
            return $http.post(url, data);
        }
    };
});

webRecruiterApp.service('tokenRequestsService', function ($http, $window) {
    return {
        postRequest: function (url, data) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http.post(url, data);
        },
        getRequest: function (url) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http.get(url);
        },
        putRequest: function (url, data) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http.put(url, data);
        },
        getRequestWithParams: function (url, parameters) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http({
                url: url,
                method: "GET",
                params: parameters
            });
        },
        deteleRequest: function (url, parameters) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $window.sessionStorage["token"];
            return $http({
                url: url,
                method: "DELETE",
                params: parameters
            });
        }
    };

});

webRecruiterApp.service('convertAllJobInfo', function () {
    return {
        convertJobInfo: function (serverData) {
            var convertedData = {};
            convertedData.jobName = serverData.jobName;
            convertedData.jobProject = serverData.jobProject;
            convertedData.jobDescription = serverData.jobDescription;
            convertedData.jobRequirements = serverData.jobRequirements;
            for (var i = 0; i < serverData.questions.length; i++) {
                convertedData["question" + (i + 1)] = serverData.questions[i].questionText;
                convertedData["response1Q" + (i + 1)] = serverData.questions[i].questionAnswer1;
                convertedData["response2Q" + (i + 1)] = serverData.questions[i].questionAnswer2;
                convertedData["response3Q" + (i + 1)] = serverData.questions[i].questionAnswer3;
                convertedData["correctResponseQ" + (i + 1)] = serverData.questions[i].correctAnswer;
            }
            return convertedData;
        }
    };
});

webRecruiterApp.service('convertQuestionInfo', function () {
    return {
        convertQuestions: function (serverData) {
            var convertedData = {};
            for (var i = 0; i < serverData.length; i++) {
                convertedData["question" + (i + 1)] = (i+1)+". "+serverData[i].questionText;
                convertedData["response1Q" + (i + 1)] = serverData[i].questionAnswer1;
                convertedData["response2Q" + (i + 1)] = serverData[i].questionAnswer2;
                convertedData["response3Q" + (i + 1)] = serverData[i].questionAnswer3;
            }
            return convertedData;
        }
    };
});
