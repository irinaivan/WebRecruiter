var modifyFormVisibility = false;
var deleteButtonVisibility = false;
webRecruiterApp.controller("modifyOrDeleteController", function ($scope, $state) {
    $scope.showModifyForm = function () {
        modifyFormVisibility = true;
        deleteButtonVisibility = false;
    };
    $scope.showDeleteBtn = function () {
        deleteButtonVisibility = true;
        modifyFormVisibility = false;
    };
    $scope.checkModifyFormVisibility = function () {
        if ($scope.modifyDeleteJob === undefined) {
            return false;
        } else {
            return modifyFormVisibility;
        }
    };
    $scope.checkDeleteBtnVisibility = function () {
        console.log($scope.modifyDeleteJob);
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


