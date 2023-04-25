document.write("<script type='text/javascript' src='/js/vendor/datepicker/Simplepicker.js'><" + "/script>");
document.write("<script type='text/javascript' src='/js/vendor/datepicker/Datepicker.js'><" + "/script>");
document.write("<script type='text/javascript' src='/js/vendor/filepond/FileUploader.js'><" + "/script>");

var App = App || {};
App.ProjectDto = {};

App.ProjectDto.ProjectForm = function(options){
    function formValidate(options){
        if (!(options.hasOwnProperty('title')
            && options.hasOwnProperty('url')
            && options.hasOwnProperty('submit')
            && options.hasOwnProperty('startDate')
            && options.hasOwnProperty('endDate')
            && options.hasOwnProperty('member')
            && options.hasOwnProperty('skills')
            && options.hasOwnProperty('summary')
            && options.hasOwnProperty('files'))) {
            throw Error('Invalid Argument Exception');
        }
    };

    function elementSelectAll(options) {
        var title = document.querySelector(options['title']);
        var member = document.querySelector(options['member']);
        var startDate = document.querySelector(options['startDate']);
        var endDate = document.querySelector(options['endDate']);
        var skills = document.querySelector(options['skills']);
        var summary = document.querySelector(options['summary']);
        var submit = document.querySelector(options['submit']);
        return {
            title: title,
            member: member,
            startDate: startDate,
            endDate: endDate,
            skills: skills,
            summary: summary,
            submit: submit
        };
    }

    function clearErrorText() {
        document.querySelectorAll(".form-error")
            .forEach(function (error) {
                error.textContent = '';
            });
    }

    function printError(error) {
        error.responseJSON.errors.forEach(function (error) {
            var element = document.getElementById(error.field + "Error");
            element.textContent = error.reason;
        });
    }

    formValidate(options);

    var datePicker = App.DatePicker()
        .addSelector(options['startDate'])
        .addSelector(options['endDate']);

    var fileUploader = App.FileUploader({
        files: options['files']
    });

    var selectedElements = elementSelectAll(options);
    var title = selectedElements.title;
    var member = selectedElements.member;
    var startDate = selectedElements.startDate;
    var endDate = selectedElements.endDate;
    var skills = selectedElements.skills;
    var summary = selectedElements.summary;
    var submit = selectedElements.submit;

    return {
        submit: submit,
        clearErrorText: clearErrorText,
        getFormData: function(){
            var selectedSkills = [];

            skills.querySelectorAll('option:checked')
                .forEach(function(option){
                    selectedSkills.push(option.value);
                });

            var formData = new FormData();
            formData.append('title', title.value);
            formData.append('member', member.value);
            formData.append('timeRange.startDate', startDate.value);
            formData.append('timeRange.endDate', endDate.value);
            formData.append('summary', summary.value);
            formData.append('description', $('#description').trumbowyg('html'));

            fileUploader.getFiles()
                .forEach(function(file){
                    formData.append('files', file);
                });
            selectedSkills.forEach(function(skill){
                formData.append('skills', skill);
            });

            return formData;
        },
        printError: printError,
        fileUploader: fileUploader
    };
};

App.ProjectDto.ProjectAddFormReq = function(options){

    var projectForm = App.ProjectDto.ProjectForm(options);

    projectForm.submit.addEventListener('click', function(e){
        e.preventDefault();
        projectForm.clearErrorText();

        $.ajax({
            url: options['url'],
            type: 'post',
            data: projectForm.getFormData(),
            cache: false,
            contentType: false,
            processData: false,
            success: function () {
                window.location.href = options['redirectUrl'];
            },
            error: function (error) {
                projectForm.printError(error);
            }
        });
    });
};

App.ProjectDto.ProjectUpdateFormReq = function(options){

    var projectForm = App.ProjectDto.ProjectForm(options);
    var id = document.querySelector(options['submit']).getAttribute('data-id');

    projectForm.submit.addEventListener('click', function(e){
        e.preventDefault();
        projectForm.clearErrorText();

        $.ajax({
            url: options['url'] + "/" + id,
            type: 'put',
            data: projectForm.getFormData(),
            cache: false,
            contentType: false,
            processData: false,
            success: function () {
                window.location.href = options['redirectUrl'];
            },
            error: function (error) {
                projectForm.printError(error);
            }
        });
    });

    var preloadedImages = document.querySelector(options['preloadedImages']);
    preloadedImages.querySelectorAll('span').forEach(function(image){
        projectForm.fileUploader.addFile(image.dataset.path);
    });
};

App.ProjectDto.ProjectRes = function(options){
    function vaildateOptions(options){
        if (!(options.hasOwnProperty('updateButton')
            && options.hasOwnProperty('deleteButton')
            && options.hasOwnProperty('fetchUrl')
            && options.hasOwnProperty('updateUrl')
            && options.hasOwnProperty('deleteUrl')
            && options.hasOwnProperty('container'))){
            throw Error('Invalid Argument Exception');
        }
    }

    function timeRangeConverter(timeRange){
        var splits = timeRange.split('-');
        return splits[0] + '.' + splits[1];
    }

    var container = document.querySelector(options['container']);
    var toast = App.Toast({
        target: '.bhhan-toast',
        text: '.bhhan-toast__text'
    });
    var id;

    container.addEventListener('click', updateProject);
    container.addEventListener('click', deleteProject);

    function updateProject(e){
        if(e.target.classList.contains(options['updateButton'])){
            window.location.href = options['updateUrl'] + '/' + e.target.getAttribute('data-id');
        }
    }

    document.querySelector(options['deleteSubmit'])
        .addEventListener('click', function(){
            $.ajax({
                url: options['deleteUrl'] + "/" + id,
                type: 'delete',
                success: function () {
                    window.location.href = options['redirectUrl'];
                },
                error: function () {
                    toast.show("Project Delete Fail!!!");
                }
            });
        });

    function deleteProject(e){
        if(e.target.classList.contains(options['deleteButton'])){
            id = e.target.getAttribute('data-id');
        }
    }

    function init(){
        $.ajax({
            url: options['fetchUrl'],
            type: 'get',
            success: function (res) {

                if(res.content.length === 0){
                    document.querySelector(options['emptyProject'])
                        .classList.toggle('d-none');
                }

                res.content.forEach(function(data){
                    var divElement = document.createElement('div');
                    divElement.classList.add('col-lg-3', 'col-md-4', 'col-sm-6', 'p-3');
                    divElement.innerHTML =
                            `<div class="projects__card text-gray-800 shadow">
                                <div class="dropdown no-arrow projects__edit" style="display: ${options['admin'] === false ? "none" : "block"}">
                                    <a class="btn btn-primary btn-sm dropdown-toggle" href="#" id="editDropdown" role="button" data-toggle="dropdown"
                                    aria-expanded="false">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <div class="dropdown-menu dropdown-menu-right shadow" aria-labelledby="editDropdown">
                                        <a class="dropdown-item updateProject" href="#" data-id="${data.id}">
                                            <i class="fas fa-pen-alt mr-2 text-gray-400"></i>
                                            Update Project
                                        </a>
                                        <a class="dropdown-item deleteProject" data-toggle="modal" data-target="#deleteProjectModal" href="#" data-id="${data.id}">
                                            <i class="fas fa-trash-alt mr-2 text-gray-400"></i>
                                            Delete Project
                                        </a>
                                    </div>
                                </div>
                                <div class="projects__card-imgbox">
                                    <img class="projects__card-img" alt="image" src="${data.images[0].path}">
                                </div>
                                <h5 class="projects__card-title px-2 mt-1 projectTitle">${data.title}</h5>
                                <p class="projects__card-description px-2 projectDescription">${data.summary}</p>
                                <div class="d-flex mb-1 flex-wrap">
                                    <div class="projects__card-period mx-2 mb-1 badge badge-primary">
                                        <span><i class="fas fa-calendar-week text-dark"></i></span>&nbsp;
                                        <span class="projectStart">${timeRangeConverter(data.timeRange.startDate)}</span> ~ <span class="projectEnd">${timeRangeConverter(data.timeRange.endDate)}</span>
                                    </div>
                                    <span class="projects__card-members mb-1 badge badge-primary">
                                        <i class="fas fa-user text-dark"></i>&nbsp;
                                        <span class="projectMember">${data.member}</span>
                                    </span>
                                </div>
                                <div class="projects__card-skills"></div>
                            </div>`;

                    var skills = divElement.querySelector('.projects__card-skills');
                    var skillsHtml = '';

                    data.skills.forEach(function(skill){
                        skillsHtml += `<img src="${skill.path}" alt="photo" class="projects__card-skill" style="background-color: white">`
                    });

                    skills.innerHTML = skillsHtml;
                    container.append(divElement);
                });
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
    init();
};
