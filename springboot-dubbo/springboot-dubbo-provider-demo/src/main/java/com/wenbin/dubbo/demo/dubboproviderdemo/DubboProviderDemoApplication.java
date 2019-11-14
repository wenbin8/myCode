package com.wenbin.dubbo.demo.dubboproviderdemo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DubboProviderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderDemoApplication.class, args);

        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class)
                .getExtension("myprotocol");
        System.out.println("自动以协议端口号：" + protocol.getDefaultPort());

        Compiler compiler = ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        System.out.println(compiler.getClass());

        URL url = new URL("", "", 0);
//        url = url.addParameter("cache", "cache");
        List<Filter> list = ExtensionLoader.getExtensionLoader(Filter.class).getActivateExtension(url, "cache");
        System.out.println(list.size());
    }

}
