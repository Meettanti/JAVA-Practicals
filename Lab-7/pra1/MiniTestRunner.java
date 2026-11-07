import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class MiniTestRunner {
    public static int run(Object testClass) throws ReflectiveOperationException {
        if (testClass == null) {
            throw new IllegalArgumentException("Test class instance cannot be null.");
        }

        Method[] methods = testClass.getClass().getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        int testsRun = 0;
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Run.class)) {
                continue;
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalArgumentException(
                        "@Run method must have no parameters: " + method.getName());
            }

            method.setAccessible(true);
            method.invoke(testClass);
            System.out.println("Ran: " + method.getName());
            testsRun++;
        }
        return testsRun;
    }
}
