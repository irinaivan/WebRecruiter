var modifyFormVisibility = false;
var deleteButtonVisibility = false;

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

webRecruiterApp.controller("modifyOrDeleteJobController", function (tokenRequestsService, convertAllJobInfo, $scope, $state) {
    var jobsComboInfoUrl = "commonModule/jobsForCombo";
    var initialJobInfo = {};
    tokenRequestsService.getRequest(jobsComboInfoUrl).then(
            function (response) {
                $scope.jobList = response.data;
            },
            function (error) {
                //empty combo
                $scope.jobList = [];
            }
    );

    $scope.checkSelectedJob = function () {
        if ($scope.selectedJob === undefined) {
            return true;
        } else {
            return false;
        }
    };

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
    $scope.prepareModifyFormIfNecessary = function () {
        if ($scope.modifyDeleteJob === "modify") {
            $scope.getJobAllInfo();
        }
    };
    $scope.deleteJob = function () {
        var deleteJobUrl = "adminModule/deleteJob";
        var parametersList = {
            "jobInfo": $scope.selectedJob.jobInfo
        };
        tokenRequestsService.getRequestWithParams(deleteJobUrl, parametersList).then(
                function (response) {
                    document.getElementById("errorLabel_modifyJob").innerHTML = '';
                    $state.reload();
                },
                function (error) {
                    document.getElementById("errorLabel_modifyJob").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + error.data.message;
                }
        );
    };
    $scope.getJobAllInfo = function () {
        var allJobInfoUrl = "adminModule/allJobInfo";
        var parametersList = {
            "jobInfo": $scope.selectedJob.jobInfo
        };
        tokenRequestsService.getRequestWithParams(allJobInfoUrl, parametersList).then(
                function (response) {
                    var convertedServerData = convertAllJobInfo.convertJobInfo(response.data);
                    $scope.modifyJobForm.jobName.$$scope.jobData = convertedServerData;
                    angular.copy(convertedServerData, initialJobInfo);
                },
                function (error) {
                    //do nothing
                }
        );
    };
    $scope.updateJob = function () {
        var updateJobUrl = "adminModule/updateJob";
        var jobData = $scope.modifyJobForm.jobName.$$scope.jobData;
        var jobDataToJson = angular.toJson(jobData);
        tokenRequestsService.putRequest(updateJobUrl, jobDataToJson).then(
                function (response) {
                    document.getElementById("errorLabel_modifyJob").innerHTML = '';
                    $state.reload();
                },
                function (error) {
                    document.getElementById("errorLabel_modifyJob").innerHTML = '<i class="fa fa-exclamation-triangle"></i>' + error.data.message;
                }
        );
    };
    $scope.checkModifyBtn = function () {
        if ($scope.modifyJobForm.$invalid || !$scope.modifyJobForm.$dirty || angular.equals(initialJobInfo, $scope.modifyJobForm.jobName.$$scope.jobData)) {
            return true;
        } else {
            return false;
        }
    };
});

webRecruiterApp.controller("listOfJobsController", function (tokenRequestsService, $scope) {
    var url = "commonModule/listOfJobs";
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

webRecruiterApp.controller("candidatesController", function (tokenRequestsService, $scope) {
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
    $scope.populateCandidatesTable = function () {
        console.log("Populating table");
    };
    $scope.downloadCV = function () {
        console.log("Download CV");
    };
});
