var webRecruiterApp = angular.module("webRecruiterApp", ['ui.router']);

//configurarea rutelor
webRecruiterApp.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login');
    $urlRouterProvider.when('/admin/jobs', '/admin/jobs/create');
    $stateProvider 
            .state("home", {
                url: '/login',
                templateUrl: "view/login.html",
                controller: "loginController"
            })
            .state("forgot_password", {
                url: "/forgot_password",
                templateUrl: "view/forgot_password.html",
                controller: "forgotPasswordContoller"
            })
            .state("register", {
                url: "/register",
                templateUrl: "view/register.html",
                controller: "registerController"
            })
            .state("candidate", {
                url: "/candidate",
                templateUrl: "view/candidate.html",
                resolve: {
                    security: ['$q', '$window', function($q, $window){
                            if($window.sessionStorage["token"] === null || $window.sessionStorage["token"] === undefined ||
                                    $window.sessionStorage["authenticatedUserRole"] === "admin"){
                               return $q.reject("Not Authorized");
                            }
                        }]
                }
            })
            .state("admin", {
                url: "/admin",
                templateUrl: "view/admin.html",
                resolve: {
                    security: ['$q','$window', function($q, $window){
                            if($window.sessionStorage["token"] === null || $window.sessionStorage["token"] === undefined ||
                                    $window.sessionStorage["authenticatedUserRole"] === 'candidate'){
                               return $q.reject("Not Authorized");
                            }
                        }]
                }
            })
            .state("admin.candidates", {
               url: "/candidates",
               templateUrl: "view/admin.candidates.html"         
            })
            .state("admin.jobs", {
                url: "/jobs",
                templateUrl: "view/admin.jobs.html"
            })
            .state("admin.jobs.create", {
                url: "/create",
                templateUrl: "view/admin.jobs.create.html"
            })
            .state("admin.jobs.modify", {
                url: "/modify",
                templateUrl: "view/admin.jobs.modify.html"
            });
});

webRecruiterApp.run(function($transitions, $state){
    $transitions.onError({}, function($transition$) {
        if ($transition$.$from().name !== 'home' && $transition$.error().detail === "Not Authorized") {
            $state.transitionTo('home');
        }
    });
});

//login page controller
webRecruiterApp.controller("loginController",function($scope, httpLoginRequestsService, $http, $state, $window) {
    var loginError = "Credentiale incorecte!";
    $scope.submitForm = function() {
        var data = {
            "userName":$scope.userName,
            "userPassword":$scope.pass
        };
        var dataToJson = angular.toJson(data);
        var url = "loginModule/login";
        httpLoginRequestsService.submitForm(url, dataToJson).then(
                function (response) {
                    if (response.data.token !== null) {
                       document.getElementById("errorLabel_login").innerHTML = '';
                       $window.sessionStorage["token"] = response.data.token;
                       $window.sessionStorage["authenticatedUserRole"] = response.data.userRole;
                       switch (response.data.userRole) {
                           case "admin":
                               $state.go("admin");
                               break;
                           case "candidate":
                               $state.go("candidate");
                               break;
                           default:
                       }
                    } else {
                        document.getElementById("errorLabel_login").innerHTML = '<i class="fas fa-exclamation-triangle"></i>'+loginError;
                    }
                },
                function (error) {
                    document.getElementById("errorLabel_login").innerHTML = '<i class="\fas fa-exclamation-triangle\"></i>'+error.data.message;
                }
        );
   };
});

//forgot password page controller
webRecruiterApp.controller("forgotPasswordContoller", function($scope, httpLoginRequestsService, $http, $state, $window) {
    $scope.pass1Tooltip = "Parola trebuie sa contina minim 8 caractere si un caracter special!";
    $scope.pass2Tooltip = "Parola diferita!";
    $scope.submitForm = function() {
        var data = {
            "userName":$scope.userName,
            "newUserPassword":$scope.newPassword1
        };
        var dataToJson = angular.toJson(data);
        var url = "loginModule/changePassword";
        httpLoginRequestsService.submitForm(url, dataToJson).then(
                function(response) {
                    document.getElementById("errorLabel_forgot_pass").innerHTML = '';
                    $state.go("home");
                },
                function (error) {
                    document.getElementById("errorLabel_forgot_pass").innerHTML = '<i class="fa fa-exclamation-triangle"></i>'+error.data.message;
                }
        );
    },
    $scope.testSamePasswords = function() {
        if (typeof $scope.newPassword2 === "undefined") {
            $scope.pass2Tooltip = "Completati campul!";
            document.getElementById("forgotPass2").innerHTML = '<i class=\"fa fa-exclamation-triangle \"></i>';
            $scope.forgotPassworForm.newPassword2.$setValidity("newPassword2", false);
        } else {
            if ($scope.newPassword1 !== $scope.newPassword2) {
               $scope.pass2Tooltip = "Parola diferita!";
               document.getElementById("forgotPass2").innerHTML = '<i class=\"fa fa-exclamation-triangle \"></i>';
               $scope.forgotPassworForm.newPassword2.$setValidity("newPassword2", false);
            } else {
                $("#forgotPass2").tooltip('hide');
                document.getElementById("forgotPass2").innerHTML = '';
                $scope.forgotPassworForm.newPassword2.$setValidity("newPassword2", true);
            }
        }
    },
    $scope.testPasswordContent = function() {
        var specialCharsRegEx = /[-!$%^&@#*()_+\|~=`{}\[\]:";'<>?,\\.\/]/;
        if (typeof $scope.newPassword1 === 'undefined' || $scope.newPassword1.length < 8 || !specialCharsRegEx.test($scope.newPassword1)) {
           document.getElementById("forgotPass1").innerHTML = '<i class=\"fa fa-exclamation-triangle \"></i>';
           $scope.forgotPassworForm.newPassword1.$setValidity("newPassword1", false);
           $scope.testSamePasswords();
        } else {
           $("#forgotPass1").tooltip('hide');
           document.getElementById("forgotPass1").innerHTML = '';
           $scope.forgotPassworForm.newPassword1.$setValidity("newPassword1", true);
        }
    };
});

//register page controller
webRecruiterApp.controller("registerController", function($scope, httpLoginRequestsService, $http, $state, $window) {
    $scope.emailTooltip = "Format incorect!";
    $scope.pass1Tooltip = "Parola trebuie sa contina minim 8 caractere si un caracter special!";
    $scope.pass2Tooltip = "Parola diferita!";
    $scope.submitForm = function () {
        var data = {
            "userName":$scope.userName,
            "firstName":$scope.firstName,
            "lastName":$scope.lastName,
            "email": $scope.email,
            "userPassword":$scope.password1
        };
        var dataToJson = angular.toJson(data);
        var url = "loginModule/register";
        httpLoginRequestsService.submitForm(url, dataToJson).then(
                function (response) {
                    document.getElementById("errorLabel_register").innerHTML = '';
                    $state.go("home");
                },
                function (error) {
                    document.getElementById("errorLabel_register").innerHTML = '<i class="fa fa-exclamation-triangle"></i>'+error.data.message;
                }
        );
    },
    
    $scope.testEmailContent = function () {
        var emailFormatRegEx = /^(("[\w-+\s]+")|([\w-+]+(?:\.[\w-+]+)*)|("[\w-+\s]+")([\w-+]+(?:\.[\w-+]+)*))(@((?:[\w-+]+\.)*\w[\w-+]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][\d]\.|1[\d]{2}\.|[\d]{1,2}\.))((25[0-5]|2[0-4][\d]|1[\d]{2}|[\d]{1,2})\.){2}(25[0-5]|2[0-4][\d]|1[\d]{2}|[\d]{1,2})\]?$)/i;
        if (!emailFormatRegEx.test($scope.email)) {
           document.getElementById("spanEmail").innerHTML = '<i class="fa fa-exclamation-triangle"></i>';
           $scope.registerForm.email.$setValidity("email", false);
        } else {
           $("#spanEmail").tooltip('hide');
           document.getElementById("spanEmail").innerHTML = '';
           $scope.registerForm.email.$setValidity("email", true);       
        }
    },
    
    $scope.testSamePasswords = function() {
        if (typeof $scope.password2 === "undefined") {
            $scope.pass2Tooltip = "Completati campul!";
            document.getElementById("registerPass2").innerHTML = '<i class=\"fa fa-exclamation-triangle \"></i>';
            $scope.registerForm.password2.$setValidity("password2", false);
        } else {
            if ($scope.password1 !== $scope.password2) {
               $scope.pass2Tooltip = "Parola diferita!";
               document.getElementById("registerPass2").innerHTML = '<i class=\"fa fa-exclamation-triangle \"></i>';
               $scope.registerForm.password2.$setValidity("password2", false);
            } else {
                $("#registerPass2").tooltip('hide');
                document.getElementById("registerPass2").innerHTML = '';
                $scope.registerForm.password2.$setValidity("password2", true);
            }
        }
    },
    
    $scope.testPasswordContent = function () {
        var specialCharsRegEx = /[-!$%^&@#*()_+\|~=`{}\[\]:";'<>?,\\.\/]/;
        if (typeof $scope.password1 === 'undefined' || $scope.password1.length < 8 || !specialCharsRegEx.test($scope.password1)) {
           document.getElementById("registerPass1").innerHTML = '<i class="fa fa-exclamation-triangle"></i>';
           $scope.registerForm.password1.$setValidity("password1", false);
           $scope.testSamePasswords();
        } else {
           $("#registerPass1").tooltip('hide');
           document.getElementById("registerPass1").innerHTML = '';
           $scope.registerForm.password1.$setValidity("password1", true);
        }
    };
});