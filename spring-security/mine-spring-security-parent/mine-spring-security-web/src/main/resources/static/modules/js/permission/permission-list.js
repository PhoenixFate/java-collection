let $table = $("#table");
$(function () {
    let options = {
        url: contextPath + 'permission/list',
        idField: 'id',
        parentIdField: 'parentId',
        treeShowField: 'name',
        columns: [
            {
                title: '序号', width: 20, formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {
                field: 'id', visible: false
            },
            {
                title: '名称', field: 'name', width: '200px'
            },
            {
                title: '地址', field: 'url', width: '300px'
            },
            {
                title: '权限标识', field: 'code', width: '300px'
            },
            {
                title: '图标', field: 'icon', width: '100px', formatter: iconFormatter
            },
            {
                title: '类型', field: 'type', width: '100px', formatter: function (value, row, index) {
                    return value === 1 ? '菜单' : '按钮'
                }
            },
            {
                title: '操作', visible: false, width: '100px'
            }
        ]
    }

    $.treeTable(options)

});

function iconFormatter(value, row, index) {
    return '<span class="' + value + '"></span>'
}
