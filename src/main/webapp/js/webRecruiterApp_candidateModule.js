var candidateSelectedJob = {};
var testResponse = {};

webRecruiterApp.controller("chooseTestController", function (tokenRequestsService, $scope, $state, $window) {
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
            "userName": $window.sessionStorage["userName"],
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

webRecruiterApp.controller("takeTestController", function (tokenRequestsService, convertQuestionInfo, $scope, $state, $window) {
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
                    $state.go("candidate.tabs.chooseTest");
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
            "candidate": $window.sessionStorage["userName"],
            "candidateSelectedJob": candidateSelectedJob.jobInfo,
            "question1Answer": $scope.responseQuestion1,
            "question2Answer": $scope.responseQuestion2,
            "question3Answer": $scope.responseQuestion3,
            "question4Answer": $scope.responseQuestion4,
            "question5Answer": $scope.responseQuestion5
        };
        tokenRequestsService.postRequest(url, testData).then(
                function (response) {
                    testResponse = response.data;
                    $state.go("candidate.uploadCV");
                },
                function (error) {
                    $state.go("candidate.tabs.chooseTest");
                }
        );
    };
});

webRecruiterApp.controller("uploadCVController", function ($http, $scope, $state, $window) {
    if (testResponse.passed === "true") {
        document.getElementById("successLabel_uploadCV").innerHTML = '<i class="fa fa-check"></i>' + testResponse.message;
    } else {
        if (testResponse.passed === "false") {
            document.getElementById("errorLabel_uploadCV").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + testResponse.message;
        }
    }
    $scope.checkTestPoints = function () {
        if (testResponse.passed === "true") {
            $("#cvFile").addClass('inputfile_design');
            return false;
        } else {
            $("#cvFile").addClass('inputfile_disabled');
            return true;
        }
    };
    $scope.uploadFile = function (files) {
        var formData = new FormData();
        var file = files[0];
        var fileName = files[0].name;
        var fileUrl = "candidateModule/uploadCV";
        $("#label_span").text(fileName);
        $("#processingCV").show();
        formData.append("cv", file);
        formData.append("userName", $window.sessionStorage["userName"]);
        formData.append("candidateSelectedJob", candidateSelectedJob.jobInfo);
        $http.post(fileUrl, formData, {
            withCredentials: true,
            headers: {'Content-Type': undefined, 'Authorization': 'Bearer ' + $window.sessionStorage["token"]},
            transformRequest: angular.identity
        }).then(
                function (response) {
                    $("#processingCV").hide();
                    $("#cvFile").prop('disabled', true);
                    $("#cvFileLabel").addClass("upload_CV");
                    document.getElementById("errorLabel_uploadCV").innerHTML = "";
                    document.getElementById("successLabel_uploadCV").innerHTML = '<i class="fa fa-check"></i>' + response.data.message;
                },
                function (error) {
                    $("#processingCV").hide();
                    $("#cvFile").prop('disabled', true);
                    $("#cvFileLabel").addClass("upload_CV");
                    document.getElementById("successLabel_uploadCV").innerHTML = "";
                    document.getElementById("errorLabel_uploadCV").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + error.data.message;
                }
        );
    };
});


