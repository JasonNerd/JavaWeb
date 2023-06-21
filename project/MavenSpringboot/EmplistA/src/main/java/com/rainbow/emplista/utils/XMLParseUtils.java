package com.rainbow.emplista.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

// 解析 emp.xml 数据文件
public class XMLParseUtils {
    public static <T> List<T> parse(String file, Class<T> targetClass){
        ArrayList<T> list = new ArrayList<T>();     // 待返回数据
        try{
            // 1. 获取一个解析器对象
            SAXReader saxReader = new SAXReader();
            // 2. 解析 xml 文档并返回 dom 对象
            Document document = saxReader.read(new File(file));
            // 3. 获取根节点
            Element rootElement = document.getRootElement();
            // 4. 获取所有子节点
            List<Element> elements = rootElement.elements("emp");
            // 5. 遍历子节点, 创建对象, 放入集合
            for(Element element: elements){
                // 6. 提取所有属性
                String name = element.element("name").getText();
                String age = element.element("age").getText();
                String image = element.element("image").getText();
                String gender = element.element("gender").getText();
                String job = element.element("job").getText();
                // 7. 组装数据, 创建一个对象放入结果列表
                Constructor<T> constructor = targetClass.getDeclaredConstructor(
                        String.class,
                        Integer.class,
                        String.class,
                        String.class,
                        String.class
                );
                constructor.setAccessible(true);
                T object = constructor.newInstance(name, Integer.parseInt(age), image, gender, job);
                list.add(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
