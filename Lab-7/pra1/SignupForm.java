public class SignupForm {
    @NotBlank
    @MaxLength(40)
    private final String name;

    @NotBlank
    @MaxLength(80)
    private final String email;

    public SignupForm(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
