webRecruiterApp.controller("commonNavBarController", function ($scope, $window) {
    $scope.logout = function () {
        $window.sessionStorage.clear();
    };
});

