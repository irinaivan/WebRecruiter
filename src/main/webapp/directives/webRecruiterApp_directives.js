webRecruiterApp.directive('tooltip', function(){
    return {
        link: function(scope, element, attrs){
            $(element).hover(function(){
                // on mouseenter
                $(element).tooltip('show'); 
            }, function(){
                // on mouseleave
                $(element).tooltip('hide');
            });
        }
    };
});


