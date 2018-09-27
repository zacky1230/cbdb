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

$.event.special.valueChange = {

    teardown: function (namespaces) {
        $(this).unbind('.valueChange');
    },

    handler: function (e) {
        $.event.special.valueChange.triggerChanged($(this));
    },

    add: function (obj) {
        $(this).on('keyup.valueChange cut.valueChange paste.valueChange input.valueChange', obj.selector, $.event.special.valueChange.handler)
    },

    triggerChanged: function (element) {
        var current = element[0].contentEditable === 'true' ? element.html() : element.val()
            ,
            previous = typeof element.data('previous') === 'undefined' ? element[0].defaultValue : element.data('previous')
        if (current !== previous) {
            element.trigger('valueChange', [element.data('previous')])
            element.data('previous', current)
        }
    }
}