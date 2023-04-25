document.write("<script type='text/javascript' src='/js/filepond-plugin-file-validate-type/dist/filepond-plugin-file-validate-type.min.js'><" + "/script>");
document.write("<script type='text/javascript' src='/js/filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.js'><" + "/script>");
document.write("<script type='text/javascript' src='/js/filepond/dist/filepond.min.js'><" + "/script>");

var App = App || {};

App.FileUploader = function(options){
    FilePond.registerPlugin(FilePondPluginImagePreview);
    FilePond.registerPlugin(FilePondPluginFileValidateType);

    var pond = FilePond.create(document.querySelector(options['files']), {
        maxFiles: options['maxFile'] || 5,
        itemInsertInterval: 200,
        acceptedFileTypes: ['image/*'],
        imagePreviewHeight: 150
    });

    function guid() {
        function s4() {
            return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    }

    var uploader = {
        getFiles: function(){
            var files = pond.getFiles();

            if(files.length > 0){
                return files.map(function(file){
                    if(file.file instanceof File){
                        return file.file;
                    }
                    else{
                        return new File([file.file], file.filename, {
                            type: file.fileType
                        });
                    }
                });
            }else {
                return [];
            }
        },
        removeFiles: function(){
            pond.removeFiles();
        },
        addFile: function(path){
            pond.addFile(path + "?guid=" + guid());
        }
    };

    return uploader;
};