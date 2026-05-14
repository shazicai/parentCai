package cn.com.springCai.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/6/20 3:31 PM
 * @Version 1.0
 */
public class ObjectMapperTest {
    public static void main(String[] args) {
        try {
            ObjectMapperTest.listObjectToString();
            ObjectMapperTest.objectToString();
            ObjectMapperTest.stringToObject();
            ObjectMapperTest.stringToListObject();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listObjectToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<PersonJson> personJsons = new ArrayList<>();
        PersonJson personJson = new PersonJson();
        personJson.setName("张三");
        personJson.setAge(18);
        PersonJson personJson1 = new PersonJson();
        personJson.setName("赵子龙");
        personJson.setAge(18);
        personJsons.add(personJson);
        personJsons.add(personJson1);

        System.out.println("listObjectToString打印:" + mapper.writeValueAsString(personJsons));
    }

    public static void objectToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PersonJson personJson = new PersonJson();
        personJson.setName("张三");
        personJson.setAge(18);
        System.out.println("objectToString打印:" + mapper.writeValueAsString(personJson));
    }

    public static void stringToObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"张三\",\"age\":18}";
        PersonJson personJson = mapper.readValue(json, PersonJson.class);
        System.out.println("stringToObject打印:" + personJson);
    }

    public static void stringToListObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"张三\",\"age\":18},{\"name\":\"赵子龙\",\"age\":18}]";
        /**
         * 先返回的是 List<LinkedHashMap<String,Object>>
         * 再通过mapper.getTypeFactory().constructParametricType(List.class, PersonJson.class) 转换成 List<PersonJson>
         * mapper.getTypeFactory().constructCollectionType(List.class, PersonJson.class);
         * mapper.getTypeFactory().constructParametricType(List.class, PersonJson.class);
         */

        List<PersonJson> personJsons = mapper.readValue(json, List.class);
        System.out.println("stringToListObject打印:" + personJsons);
    }
}



class PersonJson implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
