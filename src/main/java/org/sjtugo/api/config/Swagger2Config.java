package org.sjtugo.api.config;


import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.sjtugo.api.entity.Strategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean("UsersApis")
    public Docket usersApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                // select()返回的是ApiSelectorBuilder对象，而非Docket对象
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                // build()返回的是Docket对象
                .build()
                // 测试API时的主机URL
                .host("api.ltzhou.com")
                // API前缀
                .pathProvider(new RelativePathProvider(null) {
                    @Override
                    public String getApplicationBasePath() {
                        return "/";
                    }
                })
                .apiInfo(apiInfo())
                .directModelSubstitute(Duration.class, int.class)
                .directModelSubstitute(LineString.class, ArrayList.class)
                .directModelSubstitute(Point.class, String.class);
    }

    public ApiInfo apiInfo() {
        // API负责人的联系信息
        final Contact contact = new Contact(
                "Litao Zhou", "https://ltzhou.com", "ltzhou@sjtu.edu.cn");
        return new ApiInfoBuilder()
                // API文档标题
                .title("SJTU-Go系统平台接口文档")
                // API文档描述
                .description("SJTU-Go公共服务API说明, 关注微信小程序SJTU-Go")
                // 服务条款URL
                .termsOfServiceUrl("https://github.com/ltzone/SJTU-Go")
                // API文档版本
                .version("1.0")
                // API负责人的联系信息
                .contact(contact)
                // API的许可证Url
                .licenseUrl("http://www.apache.org/licenses/")
                .license("Apache License 2.0")
                .build();
    }
}
