function isUploadFile(fileId) {
    var files = document.getElementById(fileId).files;
    if (files.length == 0)
        return false;
    else
        return true;
}

function inputIsNull(inputId) {
    return document.getElementById(inputId).value == "";
}