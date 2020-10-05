package org.pomo.toasterfx.demo.test;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public class ATest {

    @Test
    public void t01() throws IOException {

        ClassLoader classLoader = ATest.class.getClassLoader();

        System.out.println(classLoader);

        try (InputStream inputStream = classLoader
                .getResourceAsStream("org/pomo/toasterfx/demo/language/message_en.properties")) {
            System.out.println("ATest.class.getClassLoader(): "+inputStream);
        }

        try (InputStream inputStream = classLoader
                .getResourceAsStream("/org/pomo/toasterfx/demo/language/message_en.properties")) {
            System.out.println("ATest.class.getClassLoader():/ "+inputStream);
        }

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("org/pomo/toasterfx/demo/language/message_en.properties")) {
            System.out.println("Thread.currentThread().getContextClassLoader(): "+inputStream);
        }
    }

    @Test
    public void t02() {

        ClassLoader classLoader = ATest.class.getClassLoader();

        System.out.println(classLoader);

        ResourceBundle bundle = ResourceBundle.getBundle(
                "org.pomo.toasterfx.demo.language.message",
                Locale.ENGLISH,
                classLoader);
        System.out.println(bundle.getString("demo.title"));
    }
}
