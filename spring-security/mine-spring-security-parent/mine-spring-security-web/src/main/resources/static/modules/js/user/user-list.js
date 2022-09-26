$(function () {
    let options = {
        url: contextPath + 'user/page',
        pageNumber: 1,
        pageSize: 3,
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
                field: 'username', title: '用户名'
            },
            {
                field: 'nickName', title: '用户昵称'
            },
            {
                field: 'mobile', title: '手机号'
            },
            {
                field: 'email', title: '邮箱'
            },
            {
                field: 'accountNonExpired', title: '是否过期', formatter: statusFormatter
            },
            {
                field: 'accountNonLocked', title: '是否锁定', formatter: statusFormatter
            },
            {
                field: 'credentialsNonExpired', title: '密码过期', formatter: statusFormatter
            },
            {
                field: 'action',
                title: '操作',
                visible: false,
                width: 50,
                align: 'center',
                formatter: $.operationFormatter
            }
        ]
    }
    $.pageTable(options)
})

//角色页面的搜索功能
function searchForm() {
    let $table = $("#table")
    let query = {
        size: 3, //每页显示多少条
        current: 1, //当前页面
        username: $('#username').val().trim(), //搜索条件：用户名
        mobile: $('#mobile').val().trim(), //搜索条件：手机号
    }
    $table.bootstrapTable("refresh", {query: query})
}

function statusFormatter(value, row, index) {
    return value === true ? '<span class="badge bg-success">否</span>' : '<span class="badge bg-danger">是</span>'
}