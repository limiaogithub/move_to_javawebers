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
3.支持CURD单表操作</br>
<b>4.支持级联join查询</b>推荐复杂join写xml</br>
5.支持domain默认值自动注入，可以自定义注入值</br>
6.支持limit分页，可以重写获取前台请求limit,offset方法</br>
7.支持spring-boot</br>
8.提供BaseAccidentException和BaseErrorException</br>
<b>9.提供全新代码生成器</b></br>
<b>10.提供example工程，一键测试运行</b></br>

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
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.4&lt;/version&gt;
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

        //测试findAll
        QueryHandler queryHandler = new QueryHandler();
        List<String> queryList = new ArrayList<>();
        queryList.add("18888888888");
        queryList.add("18888888889");
        queryHandler.addWhereSql("t.age=#{data.age1} and t.phone in" + QueryHandler.getInSql("data.phone1", queryList.size()));
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

        //测试级联查询，不建议复杂场景使用，不宜维护
        QueryHandler queryHandler2 = new QueryHandler();
        //queryHandler.configPage();
        List list2 = testMapper.findAll(new MemberT(), queryHandler2.addJoinHandle("cardt.*", SQLJoinHandler.JoinType.LEFT_OUTER_JOIN, "cardt cardt on t.memberId=cardt.memberId"));
        System.out.println(list2.size());
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
 ==>  Preparing: SELECT t.* , cardt.* FROM MemberT t LEFT OUTER JOIN cardt cardt on t.memberId=cardt.memberId 
 ==>  Parameters: 
 <==      Total: 2
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
&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;2.4&lt;/version&gt;
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
1.请先在数据库中创建表，yt_mybatis推荐您每张表都包含如下字段，您只需要继承BaseEntity对象即可。
<pre>
`founderId` varchar(36) DEFAULT '' COMMENT '创建人ID',
`founderName` varchar(30) DEFAULT '' COMMENT '创建人姓名',
`modifierId` varchar(36) DEFAULT '' COMMENT '修改人ID',
`modifierName` varchar(30) DEFAULT '' COMMENT '修改人姓名',
`createDateTime` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
`modifyDateTime` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
`deleteFlag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否 1是'
</pre>


2.新建JavaBuild类，代码示例如下，运行test()方法即可，代码会直接生成在modulePackage配置目录下</br>

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


<h3>其他配置</h3>
1.自定义字段注入来源</br>
创建MyFieldsConfig类，实现FieldsDefault接口，指定service名字为"ytFieldsConfig"。
<pre>
@Service("ytFieldsConfig")
public class MyFieldsConfig implements FieldsDefault {


    @Override
    public String getOperator() {
        return (String) getSessionAttr("Operator");
    }

    @Override
    public String getOperatorId() {
        return (String) getSessionAttr("OperatorId");
    }

    @Override
    public String getModifyOperator() {
        return (String) getSessionAttr("Operator");
    }

    @Override
    public String getModifyOperatorId() {
        return (String) getSessionAttr("OperatorId");
    }

    private Object getSessionAttr(String attr) {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession()
                .getAttribute(attr);
    }

}
</pre>

2.自定义分页处理类</br>
创建MyPageConfig类，实现PageConvert接口，指定service名字为"ytPageConfig"。
<pre>
@Service("ytPageConfig")
//这里的目的是设置queryHandler中的start(从第几条读)和 limit（读取多少条）；如果不设置这个service，将默认读取CommonPageService中的配置
public class MyPageConfig implements PageConvert {

    @Override
    public void convert(QueryHandler queryHandler, HttpServletRequest request) {
        queryHandler.setStart(0);
        queryHandler.setLimit(20);
    }
}
</pre>

