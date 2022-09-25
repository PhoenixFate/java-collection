// permissionTree
$(function () {
    loadPermissionTree()
})

// 加载权限树
function loadPermissionTree() {
    let menuSettings = {
        view: {
            showLine: true, // 显示连接线
        },
        check: {
            enable: true // 不显示勾选框，默认为false
        },
        data: {
            simpleData: {
                enable: true, // 开启简单模式，List自动转为json
                idKey: "id",   // id的标识属性
                pIdKey: "parentId", //父节点唯一的标识属性
                rootPId: 0         // 根节点数据
            },
            Key: {
                name: "name", //显示的节点名称对应的属性名称
                title: "name" //鼠标放上去显示的名称
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                // treeNode 代表的是点击的那个节点的json对象
                //被点击之后阻止跳转
                event.preventDefault()
            }
        }

    }

    // 查询所有的权限资源
    $.post(contextPath + "permission/list", function (data) {
        console.log("permission/list: ", data.data)
        // 初始化权限列表树
        let permissionTree = $.fn.zTree.init($("#permissionTree"), menuSettings, data.data)
        //将已经拥有的权限进行勾选

        //1.判断是否修改，判断id是否有值，如果有就是修改页面，否则就是新增页面
        let id = $("#id").val()
        if (id) {
            //2.获取当前角色所拥有的权限id
            let permissionIds = JSON.parse($("#permissionIds").val())
            //3.勾选所有的拥有的权限
            for (let i = 0; i < permissionIds.length; i++) {
                let nodes = permissionTree.getNodesByParam("id", permissionIds[i], null)
                //勾选当前选中的节点
                permissionTree.checkNode(nodes[0], true, false)
                //展开选中的节点
                permissionTree.expandNode(nodes[0], true, false)
            }
        }


    })
}

// 修改信息表单提交
$("#form").submit(function () {
    //收集权限树中所有选中的节点
    let treeObj = $.fn.zTree.getZTreeObj("permissionTree")
    //获取被选中的节点集合
    let nodes = treeObj.getCheckedNodes(true)
    let permissionIds = []
    for (let i = 0; i < nodes.length; i++) {
        permissionIds.push(nodes[i].id);
    }
    $("#permissionIds").val(permissionIds)

    //确认提交
    return true
})


