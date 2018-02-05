# yt_mybatis

</br>
欢迎加入QQ群~ 489333310，我们一起进步，不限于mybatis。
</br>
<h3>介绍</h3>
yt_mybatis是基于mybaits封装的CURD项目；也同时提供了从web端请求到数据库，再返回给前端的一整套解决方案</br>

当然您可以仅使用yt_mybatis的CURD部分。如果您是新项目，欢迎您使用整体解决方案；如果您是历史项目，您可以很快集成CURD。</br>

您可以下载https://github.com/limiaogithub/yt_mybatis_example 直接运行示例代码。</br>
您还可以下载https://github.com/limiaogithub/yt_antd_pro 结合前后端一起运行查看增删改查效果，这是目前最新的基于react的ant design技术，前后端分离最佳实践</br>

<h3>特性</h3>
1.免费开源</br>
2.一键接入增删改查</br>
3.支持CURD单表操作，多表请写xml</br>
4.支持domain默认值自动注入，可以自定义注入值</br>
5.支持limit分页，可以重写获取前台请求limit,offset方法</br>
6.支持spring-boot</br>
7.提供BaseAccidentException和BaseErrorException</br>
<b>8.提供全新代码生成器</b></br>
<b>9.提供example工程，一键测试运行</b></br>

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
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.3&lt;/version&gt;
&lt;/dependency&gt;
</pre>
2.你的mapper继承BaseMapper<T></br>
这里的泛型是你的domain</br>
<pre>
public interface TestMapper extends BaseMapper&lt;MemberT&gt; {
</br>
}
</pre>
3.配置完毕，现在你可以使用TestMapper进行curd操作，示例如下</br>
<pre>
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    public void test() {

        //测试save
        MemberT member = new MemberT().setUserName("测试name2").setPhone("18888888888").setAge(30);
        testMapper.save(member);

        //测试update
        member.setUserName("修改名称");
        testMapper.updateForSelective(member);

        //测试findById
        member = testMapper.find(MemberT.class, member.getMemberId());

        // //测试findAll，= 、in
        QueryHandler queryHandler = new QueryHandler();
        List<String> queryList = new ArrayList<>();
        queryList.add("18888888888");
        queryList.add("18888888889");
        queryHandler.addWhereSql("t.age=#{data.age1} and t.phone in" + QueryHandler.getInSql("phone1", queryList.size()));
        queryHandler.addExpandData("age1", 30);
        queryHandler.addExpandData("phone1", queryList.toArray());
        List<MemberT> list = testMapper.findAll(new MemberT(), queryHandler);

        //测试分页查询
        MemberT memberT = new MemberT();
        memberT.setPhone("18888888888");
        List<MemberT> list1 = testMapper.findAll(memberT, queryHandler.configPage().setStart(0).setLimit(10));
        Long total = testMapper.pageTotalRecord(memberT, queryHandler);
        System.out.println(total);

        //测试逻辑删除（需要你的domain继承BaseEntity）
        testMapper.logicDelete(MemberT.class, member.getMemberId());

        //测试delete
        testMapper.delete(MemberT.class, member.getMemberId());
    }
}
</pre>
输出结果：
<pre>
 ==>  Preparing: INSERT INTO MemberT (userName, age, phone, memberId) VALUES (?, ?, ?, 'b9d8cbd6664640b18eab932d88379b2d') 
 ==> Parameters: 测试name2(String), 30(Integer), 18888888888(String)
 <==    Updates: 1
 ==>  Preparing: UPDATE MemberT SET userName = ?, age = ?, phone = ? WHERE (memberId = ?) 
 ==> Parameters: 修改名称(String), 30(Integer), 18888888888(String), b9d8cbd6664640b18eab932d88379b2d(String)
 <==    Updates: 1
 ==>  Preparing: SELECT * FROM MemberT WHERE (memberId = ?) 
 ==> Parameters: b9d8cbd6664640b18eab932d88379b2d(String)
 <==      Total: 1
 ==>  Preparing: SELECT t.* FROM MemberT t WHERE (t.age=? and t.phone in(?,?)) 
 ==> Parameters: 30(Integer), 18888888888(String), 18888888889(String)
 <==      Total: 1
 ==>  Preparing: SELECT t.* FROM MemberT t WHERE (t.phone = ? AND t.age=? and t.phone in(?,?)) limit 0 , 10 
 ==> Parameters: 18888888888(String), 30(Integer), 18888888888(String), 18888888889(String)
 <==      Total: 1
 ==>  Preparing: SELECT count(distinct t.memberId) FROM MemberT t WHERE (t.phone = ? AND t.age=? and t.phone in(?,?)) 
 ==> Parameters: 18888888888(String), 30(Integer), 18888888888(String), 18888888889(String)
 <==      Total: 1

 ==>  Preparing: UPDATE MemberT SET deleteFlag=1 WHERE (memberId = ?) 
 ==> Parameters: b9d8cbd6664640b18eab932d88379b2d(String)
 <==    Updates: 1
 ==>  Preparing: DELETE FROM MemberT WHERE (memberId = ?) 
 ==> Parameters: b9d8cbd6664640b18eab932d88379b2d(String)
 <==    Updates: 1
</pre>

</hr>
<h4>b.整体解决方案的集成</h4></br>
一般的j2ee后台结构包括controller、service、serviceImpl、mapper、domain，本文按照这种结构提供示例。</br>
本文domain以MemberT作为示例</br>

1.引入maven依赖</br>
<pre>
&lt;dependency&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;com.github.limiaogithub&lt;/groupId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;yt_mybatis&lt;/artifactId&gt;
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.3&lt;/version&gt;
&lt;/dependency&gt;
</pre>
2.你的mapper继承BaseMapper<T></br>
<pre>
public interface TestMapper extends BaseMapper&lt;MemberT&gt; {
</br>
}
</pre>
3.你的service继承BaseService&lt;T&gt;,</br>
<pre>
public interface TestService extends BaseService&lt;MemberT&gt;{
</br>
}
</pre>

4.你的serviceImpl继承ServiceSupport&lt;T, yourMapper&gt;,这里需要实现getMapper()方法</br>
<pre>
@Service
public class TestServiceImpl extends ServiceSupport&lt;MemberT, TestMapper&gt; implements TestService {

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
代码较多，请访问https://github.com/limiaogithub/yt_mybatis_example 直接运行示例代码。


<h3>代码生成器</h3>
1.新建JavaBuild类，代码示例如下，运行test()方法即可，代码会直接生成在modulePackage配置目录下</br>

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


