var modifyFormVisibility = false;
var deleteButtonVisibility = false;

webRecruiterApp.controller("adminNavBarController", function ($scope, $window) {
    $scope.logout = function () {
        $window.sessionStorage.clear();
    };
});

webRecruiterApp.controller("createJobController", function (tokenRequestsService, $scope) {
    $scope.createJob = function () {
        var jobData = $scope.createJobForm.jobName.$$scope.jobData;
        var jobDataToJson = angular.toJson(jobData);
        var url = "adminModule/createJob";
        tokenRequestsService.postRequest(url, jobDataToJson).then(
            function (response) {
                console.log("OK");
            },
            function (error) {
                console.log("Error");
            }
        );
        //refresh form
        $scope.createJobForm.jobName.$$scope.jobData = {};
        $scope.createJobForm.$setPristine();
        $scope.createJobForm.$setUntouched();
    };
});

webRecruiterApp.controller("modifyOrDeleteJobController", function ($scope) {
    $scope.checkModifyFormVisibility = function () {
        if ($scope.modifyDeleteJob === undefined) {
            return false;
        } else {
            return modifyFormVisibility;
        }
    };
    $scope.checkDeleteBtnVisibility = function () {
        if ($scope.modifyDeleteJob === undefined) {
            return false;
        } else {
            return deleteButtonVisibility;
        }
    };
    $scope.toggleFormDeleteBtn = function () {
        if ($scope.modifyDeleteJob === "modify") {
            modifyFormVisibility = true;
            deleteButtonVisibility = false;
        } else {
            deleteButtonVisibility = true;
            modifyFormVisibility = false;
        }
    };
});


