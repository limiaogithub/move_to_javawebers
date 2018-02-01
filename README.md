# yt_mybatis

</br>
请加QQ群~ 489333310，群里有一些大神~</h3>
</br>
<h3>介绍</h3>
yt_mybatis是基于mybaits封装的CURD项目；也同时提供了从web端请求到数据库，再返回给前端的一整套解决方案</br>

当然您可以仅使用yt_mybatis的CURD部分。如果您是新项目，欢迎您使用整体解决方案；如果您的历史项目，您可以很快集成CURD。</br>

您可以下载https://github.com/limiaogithub/yt_mybatis_example 直接运行示例代码。</br>
您还可以下载https://github.com/limiaogithub/yt_antd_pro 结合前后端一起运行查看增删改查效果。</br>

<h3>特性</h3>
1.一键接入增删改查</br>
2.支持CURD单表操作，多表请写xml</br
3.支持domain默认值自动注入，可以自定义注入值</br>
4.支持limit分页，可以自定义获取limit offset值</br>
5.支持spring-boot</br>
6.提供BaseAccidentException和BaseErrorException</br>
7.近期支持代码生成器</br>

<h3>准备</h3>
1.mysql数据库</br>
2.idea开发工具</br>
3.大约10分钟</br>

<h3>集成</h3>
<h4>a.简单CURD的集成(仅需两步)</h4></br>
1.引入maven依赖</br>
&lt;dependency&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;</br>
&lt;/dependency&gt;</br>
</br>
2.你的mapper继承BaseMapper<T></br>
public interface TestMapper extends BaseMapper<MemberT> {</br>
</br>
}</br>

3.配置完毕，现在你可以使用TestMapper进行curd操作。</br>

</hr>
<h4>b.整体解决方案的集成</h4></br>
1.引入maven依赖</br>
&lt;dependency&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;</br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;1.0&lt;/version&gt;</br>
&lt;/dependency&gt;</br>
</br>
2.你的mapper继承BaseMapper<T></br>
public interface TestMapper extends BaseMapper<MemberT> {</br>
</br>
}</br>

3.你的service继承BaseService&lt;T&gt;,</br>
4.你的serviceImpl继承ServiceSupport&lt;T, yourMapper&gt;,</br>
5.你的controller集成BaseController</br>
6.配置完毕，现在你可以体验从请求到返回的所有操作！</br>





