document.write("<script type='text/javascript' src='/js/vendor/filepond/FileUploader.js'><" + "/script>");
document.write("<script type='text/javascript' src='/js/common/Toast.js'><" + "/script>");

var App = App || {};
App.SkillDto = {};

App.SkillDto.SkillForm = function(options){

    function formValidate(options){
        if (!(options.hasOwnProperty('name')
            && options.hasOwnProperty('description')
            && options.hasOwnProperty('type')
            && options.hasOwnProperty('use')
            && options.hasOwnProperty('submit')
            && options.hasOwnProperty('url')
            && options.hasOwnProperty('redirectUrl')
            && options.hasOwnProperty('file'))) {
            throw Error('Invalid Argument Exception');
        }
    }

    function elementSelectAll(options){
        var name = document.querySelector(options['name']);
        var description = document.querySelector(options['description']);
        var type = document.querySelector(options['type']);
        var use = document.querySelector(options['use']);
        var submit = document.querySelector(options['submit']);
        return {
            name: name,
            description: description,
            type: type,
            use: use,
            submit: submit
        };
    }

    function clearSkillForm(){
        name.value = '';
        description.value = '';
        type.options[0].selected = true;
        use.options[0].selected = true;
        fileUploader.removeFiles();
        clearFormError();
    }

    function clearFormError(){
        document.querySelectorAll(".form-error")
            .forEach(function (error) {
                error.textContent = '';
            });
    }

    formValidate(options);

    var fileUploader = App.FileUploader({
        files: options['file'],
        maxFile: 1
    });

    var selectedElements = elementSelectAll(options);
    var name = selectedElements.name;
    var description = selectedElements.description;
    var use = selectedElements.use;
    var type = selectedElements.type;
    var submit = selectedElements.submit;
    var modal = document.querySelector(options['modal']);

    document.querySelectorAll(options['close'])
        .forEach(function(closeBtn){
            closeBtn.addEventListener('click', function(){
                clearSkillForm();
            });
        });

    modal.addEventListener('blur', function(){
        if(!modal.classList.contains("show")){
            clearSkillForm();
        }
    });

    return {
        name: name,
        description: description,
        type: type,
        use: use,
        modal: modal,
        submit: submit,
        fileUploader: fileUploader,
        getFormData: function(){
            var formData = new FormData();
            formData.append('name', name.value);
            formData.append('description', description.value);
            formData.append('type', type.value);
            formData.append('use', use.value);
            fileUploader.getFiles()
                .forEach(function(file){
                    formData.append('file', file);
                });

            return formData;
        },
        clearErrorText: function(){
            clearFormError();
        },
        printError: function(error){
            error.responseJSON.errors.forEach(function (error) {
                var element = document.getElementById(options['prefix'] + error.field + "Error");
                element.textContent = error.reason;
            });
        }
    };
};

App.SkillDto.SkillFormReq = function(options){

    var skillForm = App.SkillDto.SkillForm(options);

    skillForm.submit.addEventListener('click', function(e){
        e.preventDefault();
        skillForm.clearErrorText();

        $.ajax({
            url: options['url'],
            type: 'post',
            data: skillForm.getFormData(),
            cache: false,
            contentType: false,
            processData: false,
            success: function () {
                window.location.href = options['redirectUrl'];
            },
            error: function (error) {
                skillForm.printError(error);
            }
        });
    });
};

App.SkillDto.SkillFormUpdateReq = function(options){
    var skillForm = App.SkillDto.SkillForm(options);
    var id;

    document.querySelectorAll(options['edit'])
        .forEach(function (edit) {
            edit.addEventListener('click', function(){
                id = this.getAttribute('data-id');

                $.ajax({
                    url: options['url'] + "/" + id,
                    type: 'get',
                    success: function (data) {
                        skillForm.name.value = data.name;
                        skillForm.description.value = data.description;
                        skillForm.use.value = data.use;
                        skillForm.type.value = data.type;
                        skillForm.fileUploader.addFile(data.path);
                    },
                    error: function (error) {
                        console.log(error);
                    }
                });
            });
        });

    skillForm.submit.addEventListener('click', function(e){
        e.preventDefault();
        skillForm.clearErrorText();

        var formData = skillForm.getFormData();

        $.ajax({
            url: options['url'] + "/" + id,
            type: 'put',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function () {
                window.location.href = options['redirectUrl'];
            },
            error: function (error) {
                skillForm.printError(error);
            }
        });
    });
};

App.SkillDto.SkillFormDeleteReq = function(options){

    var id;

    var toast = App.Toast({
        target: '.bhhan-toast',
        text: '.bhhan-toast__text'
    });

    document.querySelectorAll(options['delete'])
        .forEach(function(element){
            element.addEventListener('click', function(){
                id = this.getAttribute('data-id');
            });
    });

    document.querySelector(options['submit'])
        .addEventListener('click', function(){

            $.ajax({
                url: options['url'] + "/" + id,
                type: 'delete',
                success: function () {
                    window.location.href = options['redirectUrl'];
                },
                error: function () {
                    toast.show("Skill Delete Fail!!!");
                }
            });
        });
};
