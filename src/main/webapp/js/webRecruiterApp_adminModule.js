var modifyFormVisibility = false;
var deleteButtonVisibility = false;

webRecruiterApp.controller("adminNavBarController", function ($scope, $window) {
    $scope.logout = function () {
        $window.sessionStorage.clear();
    };
});

webRecruiterApp.controller("createJobController", function (tokenRequestsService, $scope, $state) {
    $scope.createJob = function () {
        var jobData = $scope.createJobForm.jobName.$$scope.jobData;
        var jobDataToJson = angular.toJson(jobData);
        var url = "adminModule/createJob";
        tokenRequestsService.postRequest(url, jobDataToJson).then(
                function (response) {
                    document.getElementById("errorLabel_createJob").innerHTML = '';
                    //refresh form
                    $scope.createJobForm.jobName.$$scope.jobData = {};
                    $scope.createJobForm.$setPristine();
                    $scope.createJobForm.$setUntouched();
                },
                function (error) {
                    document.getElementById("errorLabel_createJob").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + error.data.message;
                }
        );
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

webRecruiterApp.controller("listOfJobsController", function (tokenRequestsService, $scope) {
    var url = "adminModule/listOfJobs";
    tokenRequestsService.getRequest(url).then(
            function (response) {
                $scope.jobs = response.data;
            },
            function (error) {
                //empty table
                $scope.jobs = [];
            }
    );
});
