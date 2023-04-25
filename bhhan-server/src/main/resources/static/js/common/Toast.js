var App = App || {};

App.Toast = function(options){
    var target = document.querySelector(options['target']);

    return {
        show: function(message){
            target.classList.toggle('d-none');
            target.querySelector(options['text']).textContent = message;
            setTimeout(function(){
                target.classList.toggle('d-none');
            }, 2000);
        }
    };
};