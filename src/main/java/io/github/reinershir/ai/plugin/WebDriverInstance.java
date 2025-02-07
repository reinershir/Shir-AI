// package io.github.reinershir.ai.plugin;

// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;

// public class WebDriverInstance {

//     private static WebDriver driver;

//     public static WebDriver getWebDriver(ChromeOptions options) {
//         synchronized (WebDriverInstance.class) {
//             if (driver == null) {
//                 driver = new ChromeDriver(options);
//             }
//         }
//         return driver;
//     }
// }
