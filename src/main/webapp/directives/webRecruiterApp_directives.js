webRecruiterApp.directive('tooltip', function () {
    return {
        link: function (scope, element, attrs) {
            $(element).hover(function () {
                // on mouseenter
                $(element).tooltip('show');
            }, function (scope, element, attrs) {
                // on mouseleave
                $(element).tooltip('hide');
            });
        }
    };
});


webRecruiterApp.directive('tooltipoverflow', function () {
    return {
        link: function (scope, element, attrs) {
            $(element).hover(function () {
                // on mouseenter
                if (element[0].scrollWidth > element[0].offsetWidth) {
                    $(element).tooltip('show');
                }
            }, function (scope, element, attrs) {
                // on mouseleave
                $(element).tooltip('hide');
            });
        }
    };
});
