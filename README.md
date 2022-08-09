# java-技术合集

## idea设置自动导包
file->settings->editor->general->auto import-> 勾选 add  unambiguous imports on the fly 和 optimize imports on the fly

## idea取消@autowire的检测校验
file->settings->editor->inspections->右侧spring->spring core->code->autowiring for bean class 取消勾选

## idea快捷键
ctrl+alt+b 当前接口的实现类有哪些
ctrl+h 打开当前类的实现类窗口
Ctrl+Alt+M 选中代码抽取为一个方法
Ctrl+单击方法或类，进入到父类中
Ctrl+Alt +单击方法或类，进入到子类中
打开Run Dashboard 集中管理运行的应用： View > Tool Windows >Run Dashboard
双击 Shift 键，框中直接搜你想搜的类或者方法
搜索本项目中的方法或者配置信息中的内容 CTRL + Shift + F
Ctrl+N 输入要搜索的类 ，想搜索的类包括在jar里面，需要勾选“include non-project itms”选项，就可以搜索出来

**npm install设置淘宝仓库**
npm install --registry=https://registry.npm.taobao.org