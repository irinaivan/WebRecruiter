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
        $state.go("candidate.takeTest");
    };
});

webRecruiterApp.controller("takeTestController", function (tokenRequestsService, $scope) {
    if (candidateSelectedJob.jobInfo !== undefined) {
        var questionsUrl = "candidateModule/candidateJobQuestions";
        var parametersList = {
            "candidateSelectedJob": candidateSelectedJob.jobInfo
        };
        tokenRequestsService.getRequestWithParams(questionsUrl, parametersList).then(
                function (response) {
                    console.log("OK");
                },
                function (error) {
                    console.log("Error");
                }
        );
    }
});


