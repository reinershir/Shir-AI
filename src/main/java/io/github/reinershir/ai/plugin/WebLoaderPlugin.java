package io.github.reinershir.ai.plugin;

import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.unfbx.chatgpt.plugin.PluginAbstract;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

import io.github.reinershir.ai.plugin.entity.WebLoadParam;

public class WebLoaderPlugin extends PluginAbstract<WebLoadParam, String>{
    WebDriver driver;
    public WebLoaderPlugin(Class<?> r) {
        super(r);
        System.setProperty("webdriver.chrome.driver", "/app/software/chromedriver-linux64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions"); // 禁用扩展
        options.addArguments("--disable-images");     // 禁用图片加载
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // 使用EAGER加载策略
        options.addArguments("--headless"); // 无头模式
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        
        // 增加页面加载超时时间到30秒
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        // 增加脚本执行超时时间
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        // 增加隐式等待时间
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Override
    public String func(WebLoadParam args) {

        try {
            
            // 获取页面
            driver.get(args.getUrl());
            
            // 等待页面加载完成
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 等待javascript执行完成
            wait.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
            );

            // 获取渲染后的内容
            String content = driver.getPageSource();
            Document doc = Jsoup.parse(content);
            
            // 移除script和style元素
            doc.select("script, style, img").remove();
            
            // 将HTML转换为Markdown
            FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder().build();
            String markdown = converter.convert(doc.body().html());
            
            System.out.println("Markdown格式内容===========================================");
            System.out.println(markdown);
            return markdown;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            driver.close();
        }
        return "";
    }
    @Override
    public String content(String content) {
    //构建chatgpt需要的content参数
        return "该网页返回内容如下： \\n "+content;
    }
}
