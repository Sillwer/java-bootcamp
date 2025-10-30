package edu.school21.reflection;

import java.io.File;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.diogonunes.jcolor.Ansi.colorize;

import com.diogonunes.jcolor.Attribute;

public class Program {
    static final String packageName = "edu.school21.classes";
    static final String line = "---------------------";
    static final Attribute COLOR_CONSTRUCTOR = Attribute.BRIGHT_GREEN_TEXT();
    static final Attribute COLOR_CLASS = Attribute.BRIGHT_RED_TEXT();
    static final Attribute COLOR_FIELD = Attribute.BRIGHT_BLUE_TEXT();
    static final Attribute COLOR_METHOD = Attribute.BRIGHT_YELLOW_TEXT();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Class<?> selectedClass = null;
        Object createdObject = null;

        try {
            printPackageClasses(packageName);
            System.out.println(line);
        } catch (Exception e) {
            System.out.print(e.getClass() + ": " + e.getLocalizedMessage() + "\n\n");
            System.exit(1);
        }

        while (selectedClass == null) {
            try {
                selectedClass = inputClass(sc);
            } catch (Exception e) {
                System.out.print(e.getClass() + ": " + e.getLocalizedMessage() + "\n\n");
            }
        }

        System.out.println(line);
        displayFieldsMethods(selectedClass);
        System.out.println(line);

        while (createdObject == null) {
            try {
                createdObject = createObject(sc, selectedClass);
            } catch (Exception e) {
                System.out.print(e.getClass() + ": " + e.getLocalizedMessage() + "\n\n");
            }
        }
        System.out.println(line);

        boolean updated = false;
        while (!updated) {
            try {
                updated = updateObject(sc, createdObject);
            } catch (Exception e) {
                System.out.print(e.getClass() + ": " + e.getLocalizedMessage() + "\n\n");
            }
        }
        System.out.println(line);

        boolean methodUsed = false;
        while (!methodUsed) {
            try {
                methodUsed = useMethod(sc, createdObject);
            } catch (Exception e) {
                System.out.print(e.getClass() + ": " + e.getLocalizedMessage() + "\n\n");
            }
        }
        System.out.println(line);
        sc.close();
    }

    static void printPackageClasses(String packageName) throws URISyntaxException {
        packageName = packageName.replace(".", "/");

        URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
        if (url == null || !Files.isDirectory(Paths.get(url.toURI()))) {
            throw new RuntimeException("Package dir " + packageName + " not found");
        }

        System.out.printf("\nPackage: %s\n%s\nClasses:\n", packageName, line);
        File[] listFiles = new File(url.getFile()).listFiles();
        if (listFiles == null) {
            throw new RuntimeException("Can't get files list from package dir " + packageName);
        }

        for (File f : listFiles) {
            if (f.getName().endsWith(".class")) {
                String pkgName = f.getName().replace(".class", "");
                System.out.println("\t- " + colorize(pkgName, COLOR_CLASS));
            }
        }
    }

    static Class<?> inputClass(Scanner sc) throws ClassNotFoundException {
        System.out.print("Enter class name:\n"
                + colorize("-> ", COLOR_CLASS));
        String className = sc.nextLine().trim();
        return Class.forName(packageName + "." + className);
    }

    static void displayFieldsMethods(Class<?> cl) {
        System.out.println("fields:");
        for (Field field : cl.getDeclaredFields()) {
            System.out.printf("\t%s %s\n",
                    field.getType().getSimpleName(),
                    colorize(field.getName(), COLOR_FIELD)
            );
        }

        System.out.println("methods:");
        for (Method method : cl.getDeclaredMethods()) {
            String returnType = method.getReturnType().getSimpleName();
            String paramTypes =
                    Stream.of(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
            String nameAndParams = method.getName() + "(" + paramTypes + ")";

            System.out.printf("\t%s %s\n",
                    returnType,
                    colorize(nameAndParams, COLOR_METHOD)
            );
        }
    }

    static Object createObject(Scanner scanner, Class<?> cl) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.printf("Let's %s an object.\n",
                colorize("CREATE", COLOR_CONSTRUCTOR));

        Constructor<?>[] constructors = cl.getDeclaredConstructors();

        for (int i = 0; i < constructors.length; i++) {
            String name = constructors[i].getClass().getSimpleName();
            String params = Stream.of(constructors[i].getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", "));
            String coloredNameAndParams = colorize(name + "(" + params + ")", COLOR_CONSTRUCTOR);
            System.out.printf("\t%d. %s\n", i + 1, coloredNameAndParams);
        }

        System.out.print("Enter constructor number:\n"
                + colorize("-> ", COLOR_CONSTRUCTOR));
        int n = Integer.parseInt(scanner.nextLine().trim());

        Constructor<?> selectedConstructor = constructors[n - 1];
        Parameter[] parameters = selectedConstructor.getParameters();
        Object[] newInstanceParams = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            System.out.printf("enter %s %s:\n%s", cl.getSimpleName(), parameters[i].getName(),
                    colorize("-> ", COLOR_CONSTRUCTOR));
            String input = scanner.nextLine().trim();
            newInstanceParams[i] = parseObjectFromString(parameters[i].getType(), input);
        }

        Object newObj = selectedConstructor.newInstance(newInstanceParams);
        System.out.println("Object created: " + newObj);
        return newObj;
    }

    static Object parseObjectFromString(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == char.class || type == Character.class) {
            return Character.class;
        } else if (type == short.class || type == Short.class) {
            return Short.parseShort(value);
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        return null;
    }

    static boolean updateObject(Scanner scanner, Object object) throws NoSuchFieldException, IllegalAccessException {
        System.out.printf("Let's %s object.\nEnter name of the %s for changing:\n%s",
                colorize("UPDATE", COLOR_FIELD),
                colorize("field", COLOR_FIELD),
                colorize("-> ", COLOR_FIELD));
        String fieldName = scanner.nextLine().trim();
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        System.out.printf("Enter %s values:\n%s", field.getType().getSimpleName(), colorize("-> ", COLOR_FIELD));
        String fieldValue = scanner.nextLine().trim();
        field.set(object, parseObjectFromString(field.getType(), fieldValue));

        System.out.println("Object updated: " + object);
        return true;
    }

    static boolean useMethod(Scanner scanner, Object object)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.printf("Let's %s method of object.\n" +
                        "Enter %s of the %s like: toString() or print(String):\n%s",
                colorize("CALL", COLOR_METHOD),
                colorize("name (argument_types)", COLOR_METHOD),
                colorize("method", COLOR_METHOD),
                colorize("-> ", COLOR_METHOD));

        String[] inputSignature = scanner.nextLine().trim().split("[()]");

        if (inputSignature.length == 1) {
            Method method = object.getClass().getDeclaredMethod(inputSignature[0]);
            if (method.getReturnType() == void.class) {
                method.invoke(object);
            } else {
                System.out.println("Method returned:\n" + method.invoke(object));
            }
            return true;
        }

        String methodName = inputSignature[0].trim();
        String[] paramsStr = inputSignature[1].trim().split(",");

        Class<?>[] paramTypes =
                Stream.of(paramsStr)
                        .map(String::trim)
                        .map(className -> {
                            try {
                                return classByName(className);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(Class<?>[]::new);
        Method method = object.getClass().getMethod(methodName, paramTypes);

        Parameter[] params = method.getParameters();
        Object[] inputParams = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            System.out.printf("Enter %s type value of %s:\n%s",
                    params[i].getType().getSimpleName(), params[i].getName(),
                    colorize("-> ", COLOR_METHOD));

            String input = scanner.nextLine().trim();
            inputParams[i] = parseObjectFromString(params[i].getType(), input);
        }

        if (method.getReturnType() == void.class) {
            method.invoke(object, inputParams);
        } else {
            System.out.println("Method returned:\n" + method.invoke(object, inputParams));
        }
        return true;
    }

    static Class<?> classByName(String name) throws ClassNotFoundException {
        switch (name) {
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean":
                return boolean.class;
            case "char":
                return char.class;
            default:
                return Class.forName("java.lang." + name);
        }
    }
}

