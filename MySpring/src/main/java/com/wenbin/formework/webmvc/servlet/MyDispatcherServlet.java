package com.wenbin.formework.webmvc.servlet;

import com.wenbin.formework.annotation.MyController;
import com.wenbin.formework.annotation.MyRequestMapping;
import com.wenbin.formework.context.MyApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MyDispatcherServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private MyApplicationContext context;

    private List<MyHandlerMapping> handlerMappings = new ArrayList<MyHandlerMapping>();

    private Map<MyHandlerMapping, MyHandlerAdapter> handlerAdapters
            = new ConcurrentHashMap<MyHandlerMapping, MyHandlerAdapter>();

    private List<MyViewResolver> viewResolvers = new ArrayList<MyViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1,初始化ApplicationContext
        context = new MyApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2,初始化Spring MVC九大组件
        initStrategies(context);
    }

    //初始化策略
    private void initStrategies(MyApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);


        //handlerMapping，必须实现
        initHandlerMappings(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);


        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(MyApplicationContext context) {
    }

    private void initViewResolvers(MyApplicationContext context) {
        // 拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();

        for (int i = 0; i < templates.length; i++) {
            // 这里主要是为了兼容多模板，所以模仿Spring用List
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new MyViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(MyApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(MyApplicationContext context) {

    }

    private void initHandlerAdapters(MyApplicationContext context) {
        //把一个request请求变成一个handler，参数都是字符串的，自动配到handler中的形参
        //需要拿到HandlerMapping
        for (MyHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new MyHandlerAdapter());
        }
    }

    private void initHandlerMappings(MyApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();

        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();

                if (!clazz.isAnnotationPresent(MyController.class)) {
                    continue;
                }
                String baseUrl = "";
                // 获取Controller的url配置
                if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                    MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                // 获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    // 没有加RequestMapping注解的直接忽略
                    if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                        continue;
                    }
                    // 映射URL
                    MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*"))
                            .replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);

                    this.handlerMappings.add(new MyHandlerMapping(controller, method, pattern));

                    log.info("Mapped " + regex + "," + method);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(MyApplicationContext context) {
    }

    private void initLocaleResolver(MyApplicationContext context) {
    }

    private void initMultipartResolver(MyApplicationContext context) {
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1, 通过request中拿到URL，去匹配一个HandlerMapping
        MyHandlerMapping handler = getHandler(req);

        if (handler == null) {
            processDispatchResult(req, resp, new MyModelAndView("404"));
        }

        //2, 准备调用前的参数
        MyHandlerAdapter ha = getHandlerAdapter(handler);

        //3, 真正的调用方法，返回ModelAndView存储了要传到页面的值，和页面模板的名称
        MyModelAndView mv = ha.handler(req, resp, handler);

        // 输出
        processDispatchResult(req, resp, mv);
    }

    private MyHandlerAdapter getHandlerAdapter(MyHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        MyHandlerAdapter ha = this.handlerAdapters.get(handler);

        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, MyModelAndView mv) throws Exception {
        // 把给我的ModelAndView变成一个HTML、OutputStream、json、freemark、veolcity
        if (null == mv) {
            return;
        }

        // 如果ModelAndView不为null，
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (MyViewResolver viewResolver : this.viewResolvers) {
            MyView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    private MyHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (MyHandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            // 如果没有匹配上继续下一个匹配
            if (!matcher.matches()) {
                continue;
            }

            return handler;
        }
        return null;
    }
}
