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
            enable: false // 不显示勾选框，默认为false
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
                console.log("tree click: ", treeNode)
                if (treeNode.id == $('#id').val()) {
                    layer.tips("自己不能作为父资源", '#' + treeId, {time: 1000})
                    return
                }
                //将选择的节点放到父资源处
                // $('#parentId').val(treeNode.id);
                // $('#parentName').val(treeNode.name);
                parentPermission(treeNode.id, treeNode.name)
            }
        }


    }

    // 查询所有的权限资源
    $.post(contextPath + "permission/list", function (data) {
        console.log("permission/list: ", data.data)
        let permissionTree = $.fn.zTree.init($("#permissionTree"), menuSettings, data.data)
        let parentIdValue = $("#parentId").val()
        console.log('parentIdValue: ', parentIdValue)
        if (parentIdValue && parentIdValue !== 0 && parentIdValue !== '0') {
            // 通过父节点id来获取这个id的节点对象
            let nodes = permissionTree.getNodesByParam("id", parentIdValue, null)
            console.log(nodes[0])
            $('#parentName').val(nodes[0].name)
        }

    })
}

function parentPermission(parentId = 0, parentName = '根菜单') {
    $('#parentId').val(parentId)
    $('#parentName').val(parentName)
}