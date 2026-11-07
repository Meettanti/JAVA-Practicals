public class MiniTestRunnerDemo {
    @Run
    private void additionTest() {
        if (2 + 3 != 5) {
            throw new AssertionError("Addition test failed.");
        }
    }

    @Run
    private void stringTest() {
        if (!"java".toUpperCase().equals("JAVA")) {
            throw new AssertionError("String test failed.");
        }
    }

    private void ignoredMethod() {
        throw new AssertionError("Unannotated methods must not run.");
    }

    public static void main(String[] args) throws ReflectiveOperationException {
        int testsRun = MiniTestRunner.run(new MiniTestRunnerDemo());
        System.out.println("Tests run: " + testsRun);
    }
}
