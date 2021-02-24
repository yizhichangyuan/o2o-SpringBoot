package com.imooc.o2o.config.web;


import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.imooc.o2o.interceptors.ErrorPageInterceptor;
import com.imooc.o2o.interceptors.LoginInterceptor;
import com.imooc.o2o.interceptors.ShopPermissionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * 开启MVC，自动注入spring容器，WebMvcAutoConfigurationAdapter：配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类就可以方便的获得ApplicationContext中所有的Bean
 */
@Configuration
//@EnableWebMvc // 等价于<mvc:annotation-driven/>，不需要，会自动进行扫描
public class MvcConfiguration implements WebMvcConfigurer {
    // 都是String类型
    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String color;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.textproducer.char.string}")
    private String string;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String size;
    @Value("${kaptcha.noise.color}")
    private String noiseColor;
    @Value("${kaptcha.textproducer.char.length}")
    private String charLength;
    @Value("${kaptcha.textproducer.font.names}")
    private String fontNames;
    @Value("${interceptorPath}")
    public String interceptorPath;
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }

    /**
     * 由于web.xml不生效了，需要在这里配置Kaptcha验证码servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
        servlet.addInitParameter("kaptcha.border", border);
        servlet.addInitParameter("kaptcha.textproducer.font.color", color);
        servlet.addInitParameter("kaptcha.image.width", width);
        servlet.addInitParameter("kaptcha.textproducer.char.string", string);
        servlet.addInitParameter("kaptcha.image.height", height);
        servlet.addInitParameter("kaptcha.textproducer.font.size", size);
        servlet.addInitParameter("kaptcha.noise.color", noiseColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length", charLength);
        servlet.addInitParameter("kaptcha.textproducer.font.names", fontNames);
        return servlet;
    }

    /**
     * 因为有多个静态资源url配置，所以采用硬编码 <mvc:resources/>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/yizhichangyuan/image/upload/");
    }

    /**
     * 定义默认的请求处理器 <mvc:default-servlet-handler/>
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//        configurer.enable("test");
    }

//    /**
//     * 视图解析器<bean id="viewResolver"></bean>
//     * @return
//     */
//    @Bean
//    public ViewResolver createViewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        // 设置Spring容器
//        viewResolver.setApplicationContext(this.applicationContext);
//        // 取消缓存
//        viewResolver.setCache(false);
//        // 设置解析的前缀
//        viewResolver.setPrefix("/static/html/");
//        // 设置视图解析的后缀
//        viewResolver.setSuffix(".html");
//        return viewResolver;
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.viewResolver(createViewResolver());
    }

    /**
     * 文件上传解析器 <bean id="multipartResolver"></bean>
     * @return
     */
    @Bean
    public MultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new LoginInterceptor());
        // 配置拦截的路径
        loginIR.addPathPatterns(interceptorPath);
        // 拦截前台奖品兑换路径
        loginIR.addPathPatterns("/frontend/awardexchange");
        // 不拦截扫描二维码对员工进行授权的路径
        loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
        // 不拦截兑换二维码路径
        loginIR.excludePathPatterns("/shopadmin/awardexchange");
        // 不拦截商品兑换二维码
        loginIR.excludePathPatterns("/shopadmin/productexchange");
        // 在定义其他的拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        // 配置拦截的路径
        permissionIR.addPathPatterns(interceptorPath);
        // 配置不拦截的路径
        /**shoplist page，不拦截这的原因在于getshoplist才是将shopList放入到session**/
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");

        /**不拦截原因，在这里才开始将currentShop放入到session中**/
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
        // 不拦截注册店铺的路径
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
        // 不拦截扫描二维码对员工进行授权的路径
        permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
        // 不拦截兑换二维码路径
        permissionIR.excludePathPatterns("/shopadmin/awardexchange");
        // 不拦截商品兑换二维码
        permissionIR.excludePathPatterns("/shopadmin/productexchange");

        // 不设置路径默认拦截所有页面
        InterceptorRegistration errorPageInterceptor = registry.addInterceptor(new ErrorPageInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }
}
