<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
<!--html中的注释-->
Hello ${name}!
<#-- freemarker中的注释 -->
<#-- abc -->
<br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>钱包</td>
        <td>?date</td>
        <td>?time</td>
        <td>?string(YYYY年MM月dd日)</td>
    </tr>
    <#list stus as stu>
        <tr>
            <td>${stu_index + 1}</td>
            <td <#if stu.name =='小明'>style="background:red;"</#if>>${stu.name}</td>
            <td>${stu.age}</td>
            <td <#if stu.mondy gt 300>style="background: gold" </#if>>${stu.mondy}</td>
            <td <#if (stu.mondy > 300)>style="background: gold" </#if>>${stu.mondy}</td>

            <td >${stu.birthday?date}</td>
            <td >${stu.birthday?time}</td>
            <td >${stu.birthday?string('yyyy年MM月dd日')}</td>
        </tr>
    </#list>

</table>
<h3>内建函数</h3>
<h5>变量?size:获得list的大小</h5>
${stus?size}<br>
<h5>日期: 变量?date 变量?time 变量?string(format)</h5>
生日：${stu1.birthday?date}<br/>
生日：${stu1.birthday?time}<br/>
生日：${stu1.birthday?string('yyyy年MM月dd日')}<br/>
<h5>默认数字带分隔符</h5>
${point}
<h5>使用内建函数c，将数字型转成字符串输出</h5>
${point?c}

<br/><br/>
输出stu1的学生信息：<br/>
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
<#--缺省值, !():'',存在则显示，不存在则用空字符串-->
姓名：${(stuMap['stu1'].name)!''}<br/>
年龄：${(stuMap['stu1'].age)!''}<br/>
姓名：${(abc.bdc.name)!''}<br/>
年龄：${(abc.bdc.age)!''}<br/>
输出stu1的学生信息：<br/>
姓名：${stu1.name}<br/>
年龄：${stu1.age}<br/>
遍历输出两个学生信息：<br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list stuMap?keys as k>
        <tr>
            <td>${k_index + 1}</td>
            <td>${stuMap[k].name}</td>
            <td>${stuMap[k].age}</td>
            <td>${stuMap[k].mondy}</td>
        </tr>
    </#list>
</table>
</br>
<table>
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>出生日期</td>
        <td>钱包</td>
        <td>最好的朋友</td>
        <td>朋友个数</td>
        <td>朋友列表</td>
    </tr>
    <#--??判断stus是否存在-->
    <#--variable??如果存在，返回true，否则返回false-->
    <#if stus??>
        <#list stus as stu>
            <tr>
                <td>${stu.name!''}</td>
                <td>${stu.age}</td>
                <td>${(stu.birthday?date)!''}</td>
                <td>${stu.mondy}</td>
                <td>${(stu.bestFriend.name)!''}</td>
                <td>${(stu.friends?size)!0}</td>
                <td>
                    <#if stu.friends??>
                        <#list stu.friends as firend>
                            ${firend.name!''}<br/>
                        </#list>
                    </#if>
                </td>
            </tr>
        </#list>
    </#if>

</table>
<h5>assign标签：定义一个变量</h5>
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<h5>变量?eval: 把json字符串转变成json对象</h5>
<#assign data=text?eval />
开户行：${data.bank} 账号：${data.account}

</body>
</html>