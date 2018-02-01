# yt_mybatis

</br>
求加QQ群~ 489333310</br>
好用请点赞~</br>
</br>

yt_mybatis不仅是基于mybaits封装的CURD项目，他提供了从web端请求到数据库，再返回给前端请求的一整套解决方案</br>

当然你可以仅使用yt_mybatis的CURD部分。如果您是新项目，欢迎您使用整体解决方案；如果您的历史项目，您可以很快集成CURD。</br>

<h3>a.简单CURD的集成</h3></br>
1.引入maven依赖</br>
</br>
&lt;dependency&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;</br>
&lt;/dependency&gt;</br>

2.你的mapper继承BaseMapper<T></br>
如：
public interface TestMapper extends BaseMapper<MemberT> {</br>
</br>
}</br>

3.配置完毕，现在你可以使用TestMapper进行curd操作。</br>

</hr>
<h3>b.整体解决方案的集成</h3></br>
1.引入maven依赖</br>
</br>
&lt;dependency&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;</br>
&lt;/dependency&gt;</br>

2.你的mapper继承BaseMapper<T></br>
如：
public interface TestMapper extends BaseMapper<MemberT> {</br>
</br>
}</br>

3.你的service继承BaseService&lt;T&gt;,</br>
4.你的serviceImpl继承ServiceSupport&lt;T, yourMapper&gt;,</br>
5.你的controller集成BaseController</br>
6.配置完毕，现在你可以体验从请求到返回的所有操作！</br>





您也可以下载https://github.com/limiaogithub/yt_mybatis_example 直接运行示例代码。</br>

接口示例：</br>
</br>
![Alt text](https://github.com/limiaogithub/yt_mybatis_example/blob/master/e1.png)</br>
![Alt text](https://github.com/limiaogithub/yt_mybatis_example/blob/master/e2.png)</br>
![Alt text](https://github.com/limiaogithub/yt_mybatis_example/blob/master/e3.png)</br>
