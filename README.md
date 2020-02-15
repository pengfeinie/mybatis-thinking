[TOC]



# 第1章 了解MyBatis的官网


mybatis的官方网站:  [https://mybatis.org/mybatis-3/](https://mybatis.org/mybatis-3/)
mybatis-spring整合的网站：[https://mybatis.org/spring](https://mybatis.org/spring)
mybatis-spring-boot整合的网站：[https://mybatis.org/spring/boot.html](https://mybatis.org/spring/boot.html)
本篇文章的github地址：[https://github.com/pengfeinie/npf-mybatis](https://github.com/pengfeinie/npf-mybatis)


# 第2章 模拟mybatis源码


如果要研究mybatis，你应该首先去研究mybatis的mapper。你应该想到，你提供的一个mapper是接口，但是mybatis却可以去执行这个接口，到底是怎么做到的呢？提出疑问：


```java
public interface UserMapper {

    @Select(value = "select * from npf_user where id = #{id}")
    Map<String,String> getUserById(Long id);

}
```

我们这里有个UserMapper接口，里面有个方法，方法上面有注解，注解里面有SQL语句，那么mybatis是如何执行这个UserMapper的呢，这是mybatis最核心的问题。在模拟mybatis的源码之前，我们首先看一下mybatis是如何做的？正所谓装逼之前，要看别人是如何装逼的，这样我才可以更好地去装逼。
在mybatis的官方网站  [https://mybatis.org/mybatis-3/getting-started.html](https://mybatis.org/mybatis-3/getting-started.html)  中有提到：


![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581751306740-bc7b334a-fb1e-486d-a1f9-00b418c41362.png)


笔者就是按照这个简单的快速入门进行搭建的mybatis开发环境，如下图你可以看到，从mybatis中获取到的UserMapper对象是JDK动态代理对象。


![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581751391133-97f72eba-f7ec-4481-bbeb-4a28c9050e5d.png)


那么这个代理对象是在哪里产生的呢？请看如下图。


![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581751563886-44a0d76c-d08b-4b04-a356-057c2b9ed4ab.png)


接下来看笔者如何牛逼的模拟mybatis的源码，请看下面。


首先我们需要一个产生JDK动态代理的逻辑，如下：

```java
public class NpfMybatisSqlSession {

    public static <T> T  getMapper(Class<?> interfaceClazz){
        return (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz},new NpfMapperProxy());
    }
}
```


其次我们需要一个实现被代理Mapper的逻辑：

```java
public class NpfMapperProxy implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")){
            return proxy.getClass().getInterfaces()[0].getName();
        }
        Select select = method.getAnnotation(Select.class);
        String value = select.value()[0];
        System.out.println("执行的sql语句是 : " +value );
        //假设这里执行数据库操作
        Map<String,String> map = new HashMap<String, String>();
        map.put("id","2");
        map.put("name","name2");
        map.put("password","password2");
        return map;
    }
}
```


然后编写我们的Mapper接口：

```java
public interface UserMapper {

    @Select(value = "select * from npf_user where id = #{id}")
    Map<String,String> getUserById(Long id);

}
```


最后进行测试：

```java
public class MockMybatisApp {

    public static void main(String[] args) {
        UserMapper userMapper = NpfMybatisSqlSession.getMapper(UserMapper.class);
        Map<String, String> userInfo = userMapper.getUserById(2L);
        System.out.println(userInfo);
    }
}
```

得到结果如下：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581752977779-1bdffc08-c9f3-4463-a437-0fe6b00bf0c9.png)


笔者已经完美的模拟出了mybatis的核心源码，接下来让我慢慢的来分析。


在这里，我们肯定知道产生了一个UserMapper的代理对象，但是这里代理对象到底长的啥鸟样呢？其实我们是可以看到的，请加上如下的参数：


```java
// 从源码中得知，设置这个值，可以把生成的代理类，输出出来。
System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
```


我知道你肯定不信，好吧，笔者在这里给你看看。

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.sun.proxy;

import com.niepengfei.mybatis.mapper.UserMapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;

public final class $Proxy0 extends Proxy implements UserMapper {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final Map getUserById(Long var1) throws  {
        try {
            return (Map)super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", 
                        Class.forName("java.lang.Object"));
            m3 = Class.forName("com.niepengfei.mybatis.mapper.UserMapper")
                .getMethod("getUserById", Class.forName("java.lang.Long"));
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}

```


运行完之后，在项目的根目录下面，你赫然的看到有一个叫 $Proxy0.class的文件，可以看到，这个类继承了Proxy类并且实现UserMapper接口。$Proxy0.class中所有的方法内部都显示的调度了InvocationHandler对象的invoke方法，这里我不想说的太多，这完全是JDK动态代理的内容，如果你连这个都不知道的话，那笔者也就词穷了，完全可以证明你不需要看这篇文章，好吧。


# 第3章 如何把第三方的对象交给Spring管理


抛出问题：当mybatis和spring整合的时候，mybatis如何将产生的代理对象注入到spring容器当中？


假设市面上还没有mybatis的框架，你却开发出来了这款框架，特别的牛逼，非常的好用，但是别人却不会使用你的框架，为什么呢？因为你没有将你的框架和spring整合，同样，mybatis也遇到了和你一样的问题。


现在我换一种说法，如何把第三方的对象或者自己产生的对象交给spring管理？


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


(1). [@Bean ]() 的注解
(2). 通过API的方式，registerSingleton
(3). FactoryBean


## 第一种方式：[@Bean ]() 的注解


```java
@ComponentScan("com.niepengfei.mybatis")
@Configuration
public class AppConfig {

    @Bean
    public UserMapper userMapper() {
        return NpfMybatisSqlSession.getMapper(UserMapper.class);
    }

    @Bean
    public RoleMapper roleMapper(){
        return NpfMybatisSqlSession.getMapper(RoleMapper.class);
    }
}
```


但是这种方式有什么不好的地方？你可以想象一下，如果我们的mappper很多的情况下，你会不断地去写@Bean，代码非常的冗余，mybatis是没有采用这种方案的。

你会发现，在这里我们一个Mapper对应了一个FactoryBean。
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581754326814-20094379-2be2-4e3b-a26a-4e20ec6447f7.png)




请看如下动图，这种方式实现是可以的，请注意动态图上面显示的文字。


## 第二种方式：通过API的方式，registerSingleton


如果你对spring容器的初始化非常熟悉的情况下，你可以看到spring的初始化过程如下：
首先调用空参的构造方法，随后调用register(componentClasses)方法，最后调用refresh()方法。
在refresh()中，调用了prepareBeanFactory(beanFactory)方法，而后spring在这里方法中大量地调用了beanFactory.registerSingleton方法，这个方法就是往spring容器中注入单例的bean。那么我们怎么使用呢？我们可以通过如下方式使用：


```java
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(AppConfig.class);
        UserMapper mapper = NpfMybatisSqlSession.getMapper(UserMapper.class);
        ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
        beanFactory.registerSingleton("userMapper",mapper);
        UserService userService = (UserService)ac.getBean("userService");
        Map<String, String> map = userService.getUserById(2L);
        System.out.println(map);
    }
}
```


这种方案理论上是可以的，但是实际上不行. 那么为什么不行呢？你至少要对spring精通，才能想清楚为什么不行？不信的话，笔者分析给你看。spring的容器的初始化就凭借着一行代码搞定：


```java
AnnotationConfigApplicationContext ac =
    new AnnotationConfigApplicationContext(AppConfig.class);
```


在这行代码里面，完成了所有的bean的初始化与依赖注入的问题。结果你在这行代码的后面，才将mybatis产生的代理对象加入到spring容器，那么其他依赖这些代理对象的bean还依赖个鸟啊，所以你这样写根本没有什么卵用。


那么该如何写呢？刚才我有分析到，上面的那行代码其实是有三行代码组成的，我们将这三行代码拆开来写。


```java
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext();
        ac.register(AppConfig.class);

        UserMapper mapper = NpfMybatisSqlSession.getMapper(UserMapper.class);
        ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
        beanFactory.registerSingleton("userMapper",mapper);

        //spring容器的初始化完全靠这行代码，我们将mybatis的代理对象，
        //在这行代码之前，加入到spring容器中
        ac.refresh();

        UserService userService = (UserService)ac.getBean("userService");
        Map<String, String> map = userService.getUserById(2L);
        System.out.println(map);
    }
}
```


这种方案的弊端显而易见，如果要程序员这样去写代码的话，程序员会疯掉的。如果有很多mapper的话，你都要去注册一遍，而且你要将spring容器的初始化过程拆开，实在是太丑陋了。


## 第三种方式：FactoryBean


首先需要说明的是，FactoryBean和BeanFactory虽然名字很像，但是这两者是完全不同的两个概念，用途上也是天差地别。BeanFactory是一个Bean工厂，在一定程度上我们可以简单理解为它就是我们平常所说的Spring容器(注意这里说的是简单理解为容器)，它完成了Bean的创建、自动装配，获取等过程，存储了创建完成的单例Bean。而FactoryBean通过名字看，我们可以猜出它是Bean，但它是一个特殊的Bean，究竟有什么特殊之处呢？它的特殊之处在我们平时开发过程中又有什么用处呢？FactoryBean的特殊之处在于它可以向容器中注册两个Bean，一个是它本身，一个是FactoryBean.getObject()方法返回值所代表的Bean。


自定义一个NpfMapperFactoryBean，让它实现了FactoryBean接口，重写了接口中的两个方法，在getObejct()方法中，返回了一个UserMapper的代理对象；在getObjectType()方法中返回了UserMapper.class。然后在NpfMapperFactoryBean添加了注解@Component注解，意思是将NpfMapperFactoryBean类交给Spring管理。


![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581755181804-4b756821-4b4f-4c88-bcfe-933d5f93550b.png)

我们来运行看一下，发现了NPE异常，为什么呢？因为我们打印了UserMapper这个bean，实际上会去调用代理对象的toString()方法，但是toString()方法也被代理了，所以就发生了如下异常。
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581755328815-9b0cae6f-0eb7-4d43-8a4a-e5f6c4fade25.png)

我们稍微修改一下代码：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581755410474-9381821d-4a04-4642-9bcd-b7f6517cf005.png)

再次请看如下图：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581755466356-f3a47b8a-bd70-4b2f-b818-cc80caa3a32d.png)


我们可以先看下如下问题：


a. 在AppConfig类中我们只扫描了com.niepengfei.mybatis这个包下的类，按照我们的常规理解，这个时候应该只会有NpfMapperFactoryBean这个类被放进Spring容器中了，UserMapper并没有被扫描，因为UserMapper上面并没有加@Service或者@Component注解。而我们在测试时却可以从容器中获取到UserMapper这个Bean，为什么？

b. 我们知道默认情况下，在我们没有自定义命名策略的情况下，我们自定义的类被Spring扫描进容器后，Bean在Spring容器中的beanName默认是类名的首字母小写，而NpfMapperFactoryBean的单例对象在容器中的beanName是npfMapperFactoryBean。所以这个时候我们调用方法getBean(beanName)通过beanName去获取Bean，这个时候理论上应该返回的是NpfMapperFactoryBean类的单例对象。然而，我们将结果打印出来，却发现，这个对象是UserMapper，为什么会出现这种情况呢？为什么不是NpfMapperFactoryBean类的实例对象呢？

c. 既然通过npfMapperFactoryBean这个beanName无法获取到NpfMapperFactoryBean的单例对象，那么应该怎么获取呢？


以上3个问题的答案可以用一个答案解决，那就是FactoryBean是一个特殊的Bean。我们自定义的NpfMapperFactoryBean实现了FactoryBean接口，所以当NpfMapperFactoryBean被扫描进Spring容器时，实际上它向容器中注册了两个bean，一个是NpfMapperFactoryBean类的单例对象；另外一个就是getObject()方法返回的对象，我们重写的getObject()方法中，我们返回了UserMapper的代理对象，所以我们从容器中能获取到UserMapper的代理对象。如果我们想通过beanName去获取NpfMapperFactoryBean的单例对象，需要在beanName前面添加一个`&`符号，如下代码，这样就能根据beanName获取到原生对象了。


```java
AnnotationConfigApplicationContext ac =
        new AnnotationConfigApplicationContext(AppConfig.class);
System.out.println(ac.getBean("&npfMapperFactoryBean"));
```


需要告诉大家的是，mybatis就是使用这种方案将mapper对象交给spring容器管理的。不信的话，你请看：
[https://mybatis.org/spring/mappers.html#register](https://mybatis.org/spring/mappers.html#register)


![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581755549535-d7901589-3fa0-4d28-94b7-f49205937588.png)


接下来我们全程模拟mybatis，刚才笔者写的NpfMapperFactoryBean写的还不够灵活，接下来我们按照mybatis的思路进行改善。


### 方法一：一个Mapper接口，写一个FactoryBean

![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581758397699-3510c6fc-5a38-4f67-b628-9b143e01e9a9.png)


这种方法的弊端显而易见，当我们的Mapper越来越多的时候，FactoryBean的类也会越来越多，而且需要配置的@Bean也会越来越多。

### 方法二：FactoryBean可配置Mapper接口

![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581758595477-430a37fd-9331-4e84-a6fa-283ee1909296.png)

我可以告诉你，mybatis官方也是这么做的，不信的话，请看：
[https://mybatis.org/spring/mappers.html#register](https://mybatis.org/spring/mappers.html#register)

![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581758767298-f5e6fc7f-1ba2-49c5-b8d9-878df2f6aef9.png)

这种方法虽然可以解决FactoryBean的类膨胀的问题，但是当我们的Mapper越来越多的时候，需要配置的@Bean也会越来越多。可以看到，我们已经模拟了mybatis这一核心功能。这个并不是mybatis最牛逼的地方，mybatis最牛逼的地方在于你可以加一个注解，可以扫描包下面的所有的mapper，全部注入到spring容器中，而不是通过现在这种方式。


# 第4章 MyBatis如何把多个mapper注入容器


抛出问题：mybatis如何把多个mapper注入到spring容器？


a. 假设配置FactoryBean，就是上一个章节分析的那样，只能一次配置一个，无法配置多个。
b. 假设你通过@Service注解，那就更不行了，因为mapper都无法被注入。


那么到底该怎么做呢？ 说白了，我们就是要把factoryBean放到spring容器中管理。有两个大的方向可以考虑：
一个是把类交给spring管理，另一个就是把对象交给spring管理。


显然如果把对象交给spring管理，是需要程序员参与的，所以这种方法不需要考虑。而把类交给spring管理，可以有xml配置的方式和注解的方式，显然刚才分析的也是行不通的，那还有没有其他的方法呢？那就是通过sring提供的扩展，即BeanDefinition。那么我们把factoryBean所代表的那个BeanDefinition交给spring就可以。

首先定义扫描器的注解：

![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581759129305-7d938ee2-7ce1-4bd2-93e2-e07a725cddbf.png)

其次定义 ImportBeanDefinitionRegistrar :
![image.png](https://cdn.nlark.com/yuque/0/2020/png/749466/1581759414570-b552b739-3426-478d-bf28-bae97b3250f6.png)

