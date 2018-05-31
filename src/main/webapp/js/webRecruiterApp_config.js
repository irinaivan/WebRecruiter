var webRecruiterApp = angular.module("webRecruiterApp", ['ui.router']);

//configurarea rutelor
webRecruiterApp.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login');
    $urlRouterProvider.when('/admin/jobs', '/admin/jobs/listOfJobs');
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
                templateUrl: "view/candidate.html"
                        /*resolve: {
                         security: ['$q', '$window', function ($q, $window) {
                         if ($window.sessionStorage["token"] === null || $window.sessionStorage["token"] === undefined ||
                         $window.sessionStorage["authenticatedUserRole"] === "admin") {
                         return $q.reject("Not Authorized");
                         }
                         }]
                         }*/
            })
            .state("candidate.tabs", {
                url: "/info",
                templateUrl: "view/candidate.tabs.html"
            })
            .state("candidate.tabs.jobs", {
                url: "/jobs",
                templateUrl: "view/jobs.listOfJobs.html"
            })
            .state("candidate.tabs.chooseTest", {
                url: "/chooseTest",
                templateUrl: "view/candidate.tabs.chooseTest.html"
            })
            .state("candidate.takeTest", {
                url: "/takeTest",
                templateUrl: "view/candidate.takeTest.html"
            })
            .state("candidate.uploadCV", {
                url: "/uploadCV",
                templateUrl: "view/candidate.uploadCV.html"
            })
            .state("admin", {
                url: "/admin",
                templateUrl: "view/admin.html"
                        /*resolve: {
                         security: ['$q', '$window', function ($q, $window) {
                         if ($window.sessionStorage["token"] === null || $window.sessionStorage["token"] === undefined ||
                         $window.sessionStorage["authenticatedUserRole"] === 'candidate') {
                         return $q.reject("Not Authorized");
                         }
                         }]
                         }*/
            })
            .state("admin.candidates", {
                url: "/candidates",
                templateUrl: "view/admin.candidates.html"
            })
            .state("admin.jobs", {
                url: "/jobs",
                templateUrl: "view/admin.jobs.html"
            })
            .state("admin.jobs.listOfJobs", {
                url: "/listOfJobs",
                templateUrl: "view/jobs.listOfJobs.html"
            })
            .state("admin.jobs.create", {
                url: "/create",
                views: {
                    "": {
                        templateUrl: "view/admin.jobs.create.html"
                    },
                    "createJob@admin.jobs.create": {
                        templateUrl: 'view/admin.jobs.createOrModify.html'
                    }

                }
            })
            .state("admin.jobs.modify", {
                url: "/modify",
                views: {
                    "": {
                        templateUrl: "view/admin.jobs.modify.html"
                    },
                    "modifyJob@admin.jobs.modify": {
                        templateUrl: 'view/admin.jobs.createOrModify.html'
                    }
                }
            });
});

webRecruiterApp.run(function ($transitions, $state) {
    $transitions.onError({}, function ($transition$) {
        if ($transition$.$from().name !== 'home' && $transition$.error().detail === "Not Authorized") {
            $state.transitionTo('home');
        }
    });
    $transitions.onStart({}, function ($transition$) {
        if ($transition$.$to().name === 'admin') {
            $state.transitionTo('admin.jobs.listOfJobs');
        }
        if ($transition$.$to().name === 'candidate' || $transition$.$to().name === 'candidate.tabs') {
            $state.transitionTo('candidate.tabs.jobs');
        }
        if ($transition$.$from().name === 'candidate.takeTest') {
            $state.transitionTo('home');
        }
        if ($transition$.$to().name === 'candidate.takeTest' && $transition$.$from().name !== 'candidate.tabs.chooseTest') {
            $state.transitionTo('candidate.tabs.jobs');
        }
    });
});


