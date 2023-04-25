var App = App || {};

App.PeriodUtils = (function(){

    var _months = ['Jan', 'Feb', 'Mar',
        'Apr', 'May', 'Jun', 'Jul',
        'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    return {
        monthStringToNumber: function(monthStr){
            var index = _months.indexOf(monthStr);
            if(index === -1){
                return -1;
            }

            if(index < 9){
                return '0' + (index + 1);
            }

            return index + 1;
        }
    };
})();

App.DatePicker = function(){
    var _selectors = [],
        _selector;

    var datePicker = new App.SimplePicker(document.querySelector('body'));
    datePicker.zIndex = 100;
    datePicker.disableTimeSection();
    datePicker.on('submit', function (date) {
        var splits = date.toString().split(' ');
        var year = splits[3];
        var month = App.PeriodUtils.monthStringToNumber(splits[1]);
        var day = splits[2];
        var selectDate = year + '-' + month + '-' + day;
        document.querySelector(_selector).value = selectDate;
    });

    function _setSelector(selector){
        _selector = _selectors[_selectors.indexOf(selector)];
    }

    var selectManager = {
        addSelector: function(selector){
            document.querySelector(selector)
                .addEventListener('click', function(){
                    this.blur();
                    datePicker.reset(new Date());
                    datePicker.open();
                    _setSelector(selector);
                });
            _selectors.push(selector);
            return selectManager;
        }
    };

    return selectManager;
};