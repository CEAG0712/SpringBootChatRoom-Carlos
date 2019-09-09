package edu.udacity.java.nano.chat;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketChatServerTest {

    private static String TEST_USER = "tester";


    private static String APP_URL = "http://localhost:8080";
    private static String INDEX_URL = "http://localhost:8080/index?username=" + TEST_USER;
    private static String SEND_BUTTON_ID= "sendMsgToServer";
    private static String EXIT_BUTTON_ID= "exitChatRoom";
    private static String LOGIN_BUTTON_ID = "loginButton";
    private static String USERNAME_INPUT_ID= "username";
    private static String ONLINE_USER_SPAN_CLASS = "chat-num";
    private WebDriver webDriver;

    private static final Logger logger = LogManager.getLogger(WebSocketChatServerTest.class);


    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @Test
    public void chatRoomLoginPage() {
        webDriver.get(APP_URL);
        WebElement loginButton = webDriver.findElement(By.id(LOGIN_BUTTON_ID));
        WebElement inputUsername = webDriver.findElement(By.id(USERNAME_INPUT_ID));
        Assert.assertTrue(loginButton.isDisplayed());
        Assert.assertTrue(inputUsername.isDisplayed());
        webDriver.close();
    }

    @Test
    public void chatRoomJoinedPage() {
        webDriver.get(APP_URL);
        WebElement inputUsername = webDriver.findElement(By.id(USERNAME_INPUT_ID));
        assertNotNull(inputUsername);
        inputUsername.sendKeys(TEST_USER);
        WebElement loginButton = webDriver.findElement(By.id(LOGIN_BUTTON_ID));
        loginButton.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String getCurrentUrl = webDriver.getCurrentUrl();
        Assert.assertEquals(getCurrentUrl, String.format(INDEX_URL, TEST_USER));

        WebElement onlineUsers =  webDriver.findElement(By.className(ONLINE_USER_SPAN_CLASS));
        Assert.assertEquals(onlineUsers.getText(), "1");
        webDriver.close();
    }

    @Test
    public void chatRoomSendMessage() {
        String test_message = "TEST MESSAGE";
        webDriver.get(String.format(INDEX_URL, TEST_USER));
        WebElement msgContainer = webDriver.findElement(By.className("message-container"));
        int msgContainerCount = msgContainer.findElements(By.className("mdui-card")).size();
        WebElement msgInputField = webDriver.findElement(By.className("mdui-textfield-input"));
        msgInputField.sendKeys(test_message);
        webDriver.findElement(By.id(SEND_BUTTON_ID)).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("ERROR MESSAGE " + e.getMessage());
        }
        String expectedMessage = String.format("%s：%s", TEST_USER, test_message);
        List<WebElement> msgsList = msgContainer.findElements(By.className("message-content"));
        WebElement lastMessage = msgsList.get(msgsList.size() - 1);
        Assert.assertEquals(expectedMessage, lastMessage.getText());
        webDriver.close();
    }


    //Leave the app
    @Test
    public void chatRoomExit() {
        webDriver.get(String.format(INDEX_URL, TEST_USER));
        WebElement leave = webDriver.findElement(By.id(EXIT_BUTTON_ID));
        leave.click();
        webDriver.close();
    }
}
