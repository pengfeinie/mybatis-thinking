# 第1章 了解MyBatis的官网

mybatis的官方网站:  https://mybatis.org/mybatis-3/

mybatis-spring整合的网站：https://mybatis.org/spring

mybatis-spring-boot整合的网站：https://mybatis.org/spring/boot.html

# 第2章 模拟mybatis源码

如果要研究mybatis，你应该首先去研究mybatis的mapper。你应该想到，你提供的一个mapper是接口，但是mybatis却可以去执行这个接口，到底是怎么做到的呢？

提出疑问：

```java
public interface UserMapper {

    @Select(value = "select * from npf_user where id = #{id}")
    Map<String,String> getUserById(Long id);

}
```

我们这里有个UserMapper接口，里面有个方法，方法上面有注解，注解里面有SQL语句，那么mybatis是如何执行这个UserMapper的呢，这是mybatis最核心的问题。在模拟mybatis的源码之前，我们首先看一下mybatis是如何做的？正所谓装逼之前，要看别人是如何装逼的，这样我才可以更好地去装逼。

在mybatis的官方网站https://mybatis.org/mybatis-3/getting-started.html中有提到：

### ![image-20200104091326317](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200104091326317.png)

笔者就是按照这个简单的快速入门进行搭建的mybatis开发环境，如下动图你可以看到，从mybatis中获取到的UserMapper对象是JDK动态代理对象。

![image-20200104084456833](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\mapper-proxy.gif)



那么这个代理对象是在哪里产生的呢？请看如下动图，注意动图中出现的文字。

![image-20200104084456833](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\mapper-proxy-generate.gif)



接下来看笔者如何牛逼的模拟mybatis的源码，请看下面。

![image-20200104084456833](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\mock-mybatis.gif)

笔者已经完美的模拟出了mybatis的核心源码，接下来让我慢慢的来分析。

在这里，我们肯定知道产生了一个UserMapper的代理对象，但是这里代理对象到底长的啥鸟样呢？其实我们是可以看到的，请加上如下的参数：

```java
// 从源码中得知，设置这个值，可以把生成的代理类，输出出来。
System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
```

我知道你肯定不信，好吧，笔者在这里给你看看。

![image-20200104084456833](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\jdk-proxy.gif)

运行完之后，在项目的根目录下面，你赫然的看到有一个叫 $Proxy0.class的文件，可以看到，这个类继承了Proxy类并且实现UserMapper接口。$Proxy0.class中所有的方法内部都显示的调度了InvocationHandler对象的invoke方法，这里我不想说的太多，这完全是JDK动态代理的内容，如果你连这个都不知道的话，那笔者也就词穷了，完全可以证明你不需要看这篇文章，好吧。

# 第3章 如何把第三方的对象交给Spring管理

抛出问题：当mybatis和spring整合的时候，mybatis如何将产生的代理对象注入到spring容器当中？

假设市面上还没有mybatis的框架，你却开发出来了这款框架，特别的牛逼，非常的好用，但是别人却不会使用你的框架，为什么呢？因为你没有将你的框架和spring整合，同样，mybatis也遇到了和你一样的问题。

现在我换一种说法，如何把第三方的对或自己产生的对象交给spring管理？

```java
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Map<String,String> getUserById(Long id){
        return userMapper.getUserById(id);
    }
}
```

我们通常在开发的时候，就是直接在service中依赖了mapper，那么请问，mapper是如何交给spring管理的？如果你很简单的回答，加上@Service或者@Component注解就可以了，对不起，你真的不适合看这边文章，你也没有看下去的必要了。其实你的回答完全错误，你在一个类上面加上@Service或者@Component注解，其实是将这个类交给spring管理（spring根据这个类去实例化对象，然后将这个对象放到容器中管理），而不是将对象交给spring管理。

如果你把一个类交给spring管理，你会发现，你无法控制对象的产生过程。那么把第三方的对象交给spring管理到底有哪几种方法呢？

- @Bean 的注解
- 通过API的方式，registerSingleton
- factoryBean

第一种方式：@Bean 的注解

```java
@ComponentScan("com.niepengfei.mybatis")
@Configuration
public class AppConfig {

    @Bean
    public UserMapper userMapper(){
        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
        UserMapper mapper = npfMybatisSqlSession.getMapper(UserMapper.class);
        return mapper;
    }
}
```

请看如下动图，这种方式实现是可以的，请注意动态图上面显示的文字。

![image-20200104105842150](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\BeanToSpring1.gif)

但是这种方式有什么不好的地方？你可以想象一下，如果我们的mappper很多的情况下，你会不断地去写@Bean，代码非常的冗余，mybatis是没有采用这种方案的。

第二种方式：通过API的方式，registerSingleton

如果你对spring容器的初始化非常熟悉的情况下，你可以看到spring的初始化过程如下：

![image-20200104105842150](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200104105842150.png)

首先调用空参的构造方法，随后调用register(componentClasses)方法，最后调用refresh()方法。

在refresh()中，调用了prepareBeanFactory(beanFactory)方法，而后spring在这里方法中大量地调用了beanFactory.registerSingleton方法，这个方法就是往spring容器中注入单例的bean。那么我们怎么使用呢？我们可以通过如下方式使用：

```java
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(AppConfig.class);

        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
        UserMapper mapper = npfMybatisSqlSession.getMapper(UserMapper.class);
        ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
        beanFactory.registerSingleton("userMapper",mapper);

        UserService userService = (UserService)ac.getBean("userService");
        Map<String, String> map = userService.getUserById(2L);
        System.out.println(map);
    }
}
```

这种方案理论上是可以的，但是实际上不行，请看如下动图：

![image-20200104105842150](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\BeanRegisterError.gif)

那么为什么不行呢？你至少要对spring精通，才能想清楚为什么不行？不信的话，笔者分析给你看。spring的容器的初始化就凭借着一行代码搞定：

```java
AnnotationConfigApplicationContext ac =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

在这行代码里面，完成了所有的bean的初始化与依赖注入的问题。结果你在这行代码的后面，才将mybatis产生的代理对象加入到spring容器，那么其他依赖这些代理对象的bean还依赖个鸟啊，所以你这样写根本没有什么卵用，好吧。

那么该如何写呢？刚才我有分析到，上面的那行代码其实是有三行代码组成的，我们将这三行代码拆开来写。

```java
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext();
        ac.register(AppConfig.class);

        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
        UserMapper mapper = npfMybatisSqlSession.getMapper(UserMapper.class);
        ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
        beanFactory.registerSingleton("userMapper",mapper);

        //spring容器的初始化完全靠这行代码，我们将mybatis的代理对象，在这行代码之前，
        //加入到spring容器中
        ac.refresh();

        UserService userService = (UserService)ac.getBean("userService");
        Map<String, String> map = userService.getUserById(2L);
        System.out.println(map);
    }
}
```

如果你不信的话，请看如下的动图。

![image-20200104105842150](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\BeanRegisterSuccess.gif)

这种方案的弊端显而易见，如果要程序员这样去写代码的话，程序员会疯掉的。如果有很多mapper的话，你都要去注册一遍，而且你要将spring容器的初始化过程拆开，实在是太丑陋了。

第三种方式：factoryBean

首先需要说明的是，FactoryBean和BeanFactory虽然名字很像，但是这两者是完全不同的两个概念，用途上也是天差地别。BeanFactory是一个Bean工厂，在一定程度上我们可以简单理解为它就是我们平常所说的Spring容器(注意这里说的是简单理解为容器)，它完成了Bean的创建、自动装配，获取等过程，存储了创建完成的单例Bean。而FactoryBean通过名字看，我们可以猜出它是Bean，但它是一个特殊的Bean，究竟有什么特殊之处呢？它的特殊之处在我们平时开发过程中又有什么用处呢？

FactoryBean的特殊之处在于它可以向容器中注册两个Bean，一个是它本身，一个是FactoryBean.getObject()方法返回值所代表的Bean。先通过如下示例代码来感受下FactoryBean的用处吧。

自定义一个NpfMapperFactoryBean，让它实现了FactoryBean接口，重写了接口中的两个方法，在getObejct()方法中，返回了一个UserMapper的代理对象；在getObjectType()方法中返回了UserMapper.class。然后在NpfMapperFactoryBean添加了注解@Component注解，意思是将NpfMapperFactoryBean类交给Spring管理。

我们来运行看一下，发现了NPE异常，为什么呢？因为我们打印了UserMapper这个bean，实际上会去调用代理对象的toString()方法，但是toString()方法也被代理了，所以就发生了如下异常。

![image-20200104115236146](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\beanfactory-null.gif)

我们稍微修改一下代码：

![image-20200104115236146](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200104115236146.png)

再次请看如下的动态：

![image-20200104115236146](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\factory-success.gif)



我们可以先看下这两个问题：

- 在AppConfig类中我们只扫描了com.niepengfei.mybatis这个包下的类，按照我们的常规理解，这个时候应该只会有NpfMapperFactoryBean这个类被放进Spring容器中了，UserMapper并没有被扫描，因为UserMapper上面并没有加@Service或者@Component注解。而我们在测试时却可以从容器中获取到UserMapper这个Bean，为什么？
- 我们知道默认情况下，在我们没有自定义命名策略的情况下，我们自定义的类被Spring扫描进容器后，Bean在Spring容器中的beanName默认是类名的首字母小写，而NpfMapperFactoryBean的单例对象在容器中的beanName是npfMapperFactoryBean。所以这个时候我们调用方法getBean(beanName)通过beanName去获取Bean，这个时候理论上应该返回的是NpfMapperFactoryBean类的单例对象。然而，我们将结果打印出来，却发现，这个对象是UserMapper，为什么会出现这种情况呢？为什么不是NpfMapperFactoryBean类的实例对象呢？
- 既然通过npfMapperFactoryBean这个beanName无法获取到NpfMapperFactoryBean的单例对象，那么应该怎么获取呢？

以上3个问题的答案可以用一个答案解决，那就是FactoryBean是一个特殊的Bean。我们自定义的NpfMapperFactoryBean实现了FactoryBean接口，所以当NpfMapperFactoryBean被扫描进Spring容器时，实际上它向容器中注册了两个bean，一个是NpfMapperFactoryBean类的单例对象；另外一个就是getObject()方法返回的对象，我们重写的getObject()方法中，我们返回了UserMapper的代理对象，所以我们从容器中能获取到UserMapper的代理对象。如果我们想通过beanName去获取NpfMapperFactoryBean的单例对象，需要在beanName前面添加一个`&`符号，如下代码，这样就能根据beanName获取到原生对象了。

```java
AnnotationConfigApplicationContext ac =
        new AnnotationConfigApplicationContext(AppConfig.class);
System.out.println(ac.getBean("&npfMapperFactoryBean"));
```

需要告诉大家的是，mybatis就是使用这种方案将mapper对象交给spring容器管理的。不信的话，你请看：

https://mybatis.org/spring/mappers.html#register

![image-20200104120847415](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200104120847415.png)

接下来我们全程模拟mybatis，刚才笔者写的NpfMapperFactoryBean写的还不够灵活，接下来我们按照mybatis的思路进行改善。

![image-20200104121248372](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200104121248372.png)

现在我们可以在AppConfig做如下事情：

```
/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
@ComponentScan("com.niepengfei.mybatis")
@Configuration
public class AppConfig {

    @Bean
    public UserMapper userMapper() throws Exception{
        NpfMapperFactoryBean<UserMapper> factoryBean = new NpfMapperFactoryBean();
        factoryBean.setMapperInterface(UserMapper.class);
        return (UserMapper)factoryBean.getObject();
    }
}
```

![image-20200104121248372](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\factory-mybatis.gif)

可以看到，我们已经模拟了mybatis这一核心功能。这个并不是mybatis最牛逼的地方，mybatis最牛逼的地方在于你可以加一个注解，可以扫描包下面的所有的mapper，全部注入到spring容器中，而不是通过现在这种方式。

# 第4章 MyBatis如何把多个mapper注入容器

抛出问题：mybatis如何把多个mapper注入到spring容器？

- 假设配置factoryBean，就是上一个章节分析的那样，只能一次配置一个，无法配置多个。

- 假设你通过@Service注解，那就更不行了，因为mapper都无法被注入。

那么到底该怎么做呢？ 说白了，我们就是要把factoryBean放到spring容器中管理。有两个大的方向可以考虑：

一个是把类交给spring管理，另一个就是把对象交给spring管理。显然如果把对象交给spring管理，是需要程序员参与的，所以这种方法不需要考虑。而把类交给spring管理，可以有xml配置的方式和注解的方式，显然刚才分析的也是行不通的，那还有没有其他的方法呢？那就是通过sring提供的扩展，即BeanDefinition。那么我们把factoryBean所代表的那个BeanDefinition交给spring就可以。请看如下动图：

![image-20200104121248372](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\npfmapperscan.gif)



上述的代码还是写的不够灵活，可以通过改造NpfImportBeanDefinitionRegistrar，完全模仿mybatis进行mapper的扫描。如下所示：

![image-20200104121248372](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\scanmapper-full.gif)





















