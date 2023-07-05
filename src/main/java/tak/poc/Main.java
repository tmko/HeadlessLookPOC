package tak.poc;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Main {
    private static final BrowserType.LaunchOptions HEADLESS = new BrowserType.LaunchOptions().setHeadless(true);
    private static final BrowserType.LaunchOptions HEADLIKE = new BrowserType.LaunchOptions().setHeadless(false);

    public static void main(String[] args) {
        new Main().takeScreenShot();
    }

    private byte[] takeScreenShot () {
        final String loginURL = "https://github.com/login";
        final String id = "tak_ko@hotmail.com";
        final String pw = "xxxxxxx";

        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(HEADLIKE)) {
                try (Page page = browser.newPage()) {

                    page.navigate(loginURL);
                    assertThat(page).hasTitle(Pattern.compile("Sign in"));

                    page.getByLabel("Username or email address").fill(id);
                    page.getByLabel("Password").fill(pw);

                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

                    page.waitForLoadState(LoadState.NETWORKIDLE);

                    page.screenshot(new Page.ScreenshotOptions()
                            .setPath(Paths.get("./target/screenshot.png"))
                            .setFullPage(true));

                    return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                }
            }
        }
    }
}