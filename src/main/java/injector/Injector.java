package injector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс инициализирующий свойства с анотацией @AutoInjectable
 */
public class Injector {

    private List<Inject> injects;
    private String pathName;

    /**
     * Конструктор, собирающий информации из файла
     * @param pathName путь к файлу, хранящемуся в ресурсах
     */
    public Injector(String pathName) {
        this.pathName = pathName;
        InputStream in = getClass().getClassLoader().getResourceAsStream(pathName);
        Stream<String> lines = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines();
        injects = lines.map(Inject::new).collect(Collectors.toList());
    }

    /**
     * Инициализация свойств объекта
     * @param value объект, с которым происходит работа
     * @param <T> тип объекта
     */
    public <T> T inject(T value) {
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                var implValue = getFieldValue(field);
                try {
                    field.set(value, implValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Ошибка задания значения для "+field.getName());
                }
            }
        }
        return value;
    }

    /**
     * Создание объекта для поля
     * @param field заданное поле
     * @return созданный объект
     */
    private Object getFieldValue(Field field) {
        String typeName = field.getType().getName();
        Inject inject = getInject(typeName);
        if (inject == null)
            throw new RuntimeException("В файле " + pathName + "  нет информации о " + typeName);
        Class<?> implClass;
        try {
            implClass = getClass().getClassLoader().loadClass(inject.getInitiator());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Класс "+typeName+" не существует");
        }
        try {
            return implClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("У класса "+typeName+" нет пустого конструктора");
        }catch (Exception e) {
            throw new RuntimeException("Не удалось создать новый объект класса "+typeName);
        }
    }

    /**
     * Поиск класса для инициализации
     * @param className имя класса
     * @return имя класса для инициализации
     */
    private Inject getInject(String className) {
        for (Inject inject : injects)
            if (className.equals(inject.getInitialization()))
                return inject;
        return null;
    }

}
