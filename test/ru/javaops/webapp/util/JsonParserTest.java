package ru.javaops.webapp.util;

import org.junit.Assert;
import org.junit.Test;
import ru.javaops.webapp.model.AbstractSection;
import ru.javaops.webapp.model.Resume;
import ru.javaops.webapp.model.TextSection;

import static ru.javaops.webapp.TestData.RESUME_1;

public class JsonParserTest {

    @Test
    public void testResume() throws Exception {
        String json = JsonParser.write(RESUME_1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(RESUME_1, resume);
    }

    @Test
    public void write() {
        AbstractSection section1 = new TextSection("Objective1");
        String json = JsonParser.write(section1, AbstractSection.class);
        System.out.println(json);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assert.assertEquals(section1, section2);
    }
}