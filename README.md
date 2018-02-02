# yt_mybatis

</br>
欢迎加入QQ群~ 489333310，我们一起进步，不限于mybatis。
</br>
<h3>介绍</h3>
yt_mybatis是基于mybaits封装的CURD项目；也同时提供了从web端请求到数据库，再返回给前端的一整套解决方案</br>

当然您可以仅使用yt_mybatis的CURD部分。如果您是新项目，欢迎您使用整体解决方案；如果您是历史项目，您可以很快集成CURD。</br>

您可以下载https://github.com/limiaogithub/yt_mybatis_example 直接运行示例代码。</br>
您还可以下载https://github.com/limiaogithub/yt_antd_pro 结合前后端一起运行查看增删改查效果。</br>

<h3>特性</h3>
1.一键接入增删改查</br>
2.支持CURD单表操作，多表请写xml</br>
3.支持domain默认值自动注入，可以自定义注入值</br>
4.支持limit分页，可以自定义获取limit offset值</br>
5.支持spring-boot</br>
6.提供BaseAccidentException和BaseErrorException</br>
7.支持代码生成器</br>

<h3>准备</h3>
1.mysql数据库</br>
2.idea开发工具</br>
3.大约10分钟</br>

<h3>集成</h3>
<h4>a.简单CURD的集成(仅需两步)</h4></br>
1.引入maven依赖</br>
<pre>
&lt;dependency&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.1&lt;/version&gt;
&lt;/dependency&gt;
</pre>
2.你的mapper继承BaseMapper<T></br>
<pre>
public interface TestMapper extends BaseMapper<MemberT> {
</br>
}
</pre>
3.配置完毕，现在你可以使用TestMapper进行curd操作。</br>

</hr>
<h4>b.整体解决方案的集成</h4></br>
1.引入maven依赖</br>
<pre>
&lt;dependency&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.1&lt;/version&gt;
&lt;/dependency&gt;
</pre>
2.你的mapper继承BaseMapper<T></br>
<pre>
public interface TestMapper extends BaseMapper<MemberT> {
</br>
}
</pre>
3.你的service继承BaseService&lt;T&gt;,</br>
<pre>
public interface TestService extends BaseService<MemberT>{
</br>
}
</pre>

4.你的serviceImpl继承ServiceSupport&lt;T, yourMapper&gt;,这里需要实现getMapper()方法</br>
<pre>
@Service
public class TestServiceImpl extends ServiceSupport<MemberT, TestMapper> implements Test2Service {

    @Resource
    private TestMapper testMapper;


    @Override
    public TestMapper getMapper() {
        return testMapper;
    }
}
</pre>
5.你的controller集成BaseController</br>
<pre>
public class MemberController extends BaseController
</pre>

6.配置完毕，现在你可以体验从请求到返回的所有操作！</br>


<h3>代码生成器</h3>
1.新建JavaBuild类，代码示例如下，运行test()方法即可。</br>

<pre>
import com.github.yt.generator.JavaCodeGenerator;

public class JavaBuild {

    @org.testng.annotations.Test
    public void test() {

        JavaCodeGenerator generator = new JavaCodeGenerator(
                "root",
                "1234",
                "antdpro",
                "jdbc:mysql://localhost:3307/antdpro");

        String tableName = "OrderT";
        String codeName = "订单";
        String moduleName = "订单";
        String modulePackage = "com.github.yt.example.order";
        generator.create(tableName, codeName, moduleName, modulePackage
                //  , "html"
                , "bean"
                , "controller"
                , "service"
                , "mapper"
                , "mapper_xml"
        );
    }

}
</pre>

