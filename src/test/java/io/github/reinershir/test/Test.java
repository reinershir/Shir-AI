package io.github.reinershir.test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

public class Test {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Work\\Software\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions"); // 禁用扩展
        options.addArguments("--disable-images");     // 禁用图片加载
        // 添加新的参数来模拟真实浏览器
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        
        // 添加实验性选项
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        
        // 保留原有的一些必要选项
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--headless"); // 暂时注释掉无头模式，便于调试
       
      

        try {
            WebDriver driver = new ChromeDriver(options);
             // 增加页面加载超时时间到30秒
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            // 增加脚本执行超时时间
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            // 增加隐式等待时间
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // 获取页面
            driver.get("https://partner.tiktokshop.com/docv2/page/6509d85b4a0bb702c057fdda?external_id=6509d85b4a0bb702c057fdda");
            
            // 等待页面加载完成
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // 等待id为"Response"的元素出现
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Response")));

             // 查找所有折叠面板的展开按钮（需要根据实际页面元素调整选择器）
            List<WebElement> expandButtons = driver.findElements(By.tagName("svg")); // 替换为实际的选择器
            
            // 点击所有展开按钮
            for (WebElement button : expandButtons) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                    // 添加短暂延迟，等待内容加载
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.out.println("点击展开按钮时出错: " + e.getMessage());
                }
            }

            // 获取渲染后的内容
            String content = driver.getPageSource();
            System.out.println("渲染后的页面："+content);
            Document doc = Jsoup.parse(content);
            
            // 移除script和style元素
            doc.select("script, style, img").remove();
            
            // 将HTML转换为Markdown
            FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder().build();
            String markdown = converter.convert(doc.body().html());
            
            System.out.println("Markdown格式内容===========================================");
            System.out.println(markdown);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
