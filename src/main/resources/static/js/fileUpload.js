/*
var uploadResult = $(".uploadResult ul");

    function showUploadedFile(uploadResultArr) {
        var str = "";
        $(uploadResultArr).each(function (i, obj) {
            if (!obj.image) {
                str += "<li><img src='/resources/static/img/attach.png'>" + obj.fileName + "</li>";
            } else {
                str += "<li>" + obj.fileName + "</li>";
            }
        });
        uploadResult.append(str);
    }

    $.ajax({
        url : '/uploadAjaxAction',
        processData: false,
        contentType : false,
        data : formData,
        type : 'POST',
        dataType : 'json',
        success : function (result) {
            console.log(result);
            showUploadedFile(result);
            $(".uploadDiv").html(cloneObj.html());
        }
    });*/

document.addEventListener('DOMContentLoaded', function() {
    const fileUpload = document.getElementById('file-upload');
    const fileList = document.getElementById('file-list');

    fileUpload.addEventListener('change', function() {
        const files = fileUpload.files;
        fileList.innerHTML = ''; // Clear the list first
        for (let i = 0; i < files.length; i++) {
            const li = document.createElement('li');
            li.textContent = files[i].name;
            fileList.appendChild(li);
        }
    });
});