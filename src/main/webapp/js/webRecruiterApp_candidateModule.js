var candidateSelectedJob = {};

webRecruiterApp.controller("chooseTestController", function (tokenRequestsService, $scope, $state) {
    var jobsComboInfoUrl = "commonModule/jobsForCombo";
    tokenRequestsService.getRequest(jobsComboInfoUrl).then(
            function (response) {
                $scope.jobList = response.data;
            },
            function (error) {
                //empty combo
                $scope.jobList = [];
            }
    );
    $scope.validateSelectedJob = function () {
        if ($scope.selectedJob === undefined) {
            return true;
        } else {
            return false;
        }
    };
    $scope.takeTest = function () {
        angular.copy($scope.selectedJob, candidateSelectedJob);
        var checkTestAlreadyTakenUrl = "candidateModule/checkTestAlreadyTaken";
        var checkData = {
            "userName": "iivan",
            "candidateSelectedJob": candidateSelectedJob.jobInfo
        };
        tokenRequestsService.postRequest(checkTestAlreadyTakenUrl, checkData).then(
                function (response) {
                    document.getElementById("errorLabel_chooseTest").innerHTML = '';
                    $state.go("candidate.takeTest");
                },
                function (error) {
                    document.getElementById("errorLabel_chooseTest").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + error.data.message;
                }
        );
    };
});

webRecruiterApp.controller("takeTestController", function (tokenRequestsService, convertQuestionInfo, $scope) {
    if (candidateSelectedJob.jobInfo !== undefined) {
        var questionsUrl = "candidateModule/candidateJobQuestions";
        var parametersList = {
            "candidateSelectedJob": candidateSelectedJob.jobInfo
        };
        tokenRequestsService.getRequestWithParams(questionsUrl, parametersList).then(
                function (response) {
                    var convertedServerData = convertQuestionInfo.convertQuestions(response.data);
                    $scope.jobQuestions = convertedServerData;
                },
                function (error) {
                    //do nothing
                }
        );
    }
    ;
    $scope.checkRadioButtons = function () {
        if ($scope.responseQuestion1 === undefined || $scope.responseQuestion2 === undefined || $scope.responseQuestion3 === undefined || $scope.responseQuestion4 === undefined || $scope.responseQuestion5 === undefined) {
            return true;
        } else {
            return false;
        }
    };
    $scope.submitTestAnswers = function () {
        var url = "candidateModule/candidateAnswers";
        var testData = {
            "candidate": "iivan",
            "candidateSelectedJob": candidateSelectedJob.jobInfo,
            "question1Answer": $scope.responseQuestion1,
            "question2Answer": $scope.responseQuestion2,
            "question3Answer": $scope.responseQuestion3,
            "question4Answer": $scope.responseQuestion4,
            "question5Answer": $scope.responseQuestion5
        };
        tokenRequestsService.postRequest(url, testData).then(
                function (response) {
                    console.log("OK");
                },
                function (error) {
                    console.log("Error");
                }
        );
    };
});

webRecruiterApp.controller("uploadCVController", function ($http, $scope, $state, $window) {
    $scope.uploadFile = function (files) {
        debugger;
        var formData = new FormData();
        var file = files[0];
        var fileName = files[0].name;
        var fileUrl = "candidateModule/uploadCV";
        $("#label_span").text(fileName);
        formData.append("cv", file);
        formData.append("userName", "iivan");
        //formData.append("candidateSelectedJob", candidateSelectedJob.jobInfo);
        formData.append("candidateSelectedJob", "Java Developer-Indigo Pro");
        $http.post(fileUrl, formData, {
            withCredentials: true,
            headers: {'Content-Type': undefined, 'Authorization': 'Bearer ' + $window.sessionStorage["token"]},
            transformRequest: angular.identity
        }).then(
                function (response) {
                    console.log("OK");
                },
                function (error) {
                    console.log("Error");
                }
        );
    };
});


