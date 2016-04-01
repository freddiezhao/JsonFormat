#Json 数据格式化显示对话框

 显示效果 类似 [http://json.cn](http://json.cn) 
 主要目的就是方便测试，本来以为这个肯定很多了 居然没人做。效果我觉得还可以，只是在测试时候使用。所以性能上也不会有太高要求。
 
#运行截图
![](https://raw.githubusercontent.com/AlphaBoom/JsonFormat/master/screenshots/jsonformatdialog.gif)
#添加依赖
     compile 'com.anarchy.lib:library:0.5.0'
#使用方式

传入符合JSON标准的`String` 类型 即可：

        JsonFormatDialogFragment.newInstance(buffer.toString())
                               .show(getSupportFragmentManager(),"");
