package validation;

public class CarValidator {
    private CarValidator(){}

    public static String modelValidation(String model) {
        if(model.isBlank()) {
            throw new IllegalArgumentException("Car model must not be blank");
        }
        return model;
    }

    public static int powerHpValidation(int powerHp) {
        if(powerHp < 30 || powerHp > 1200) {
            throw new IllegalArgumentException("Car powerHp must be between 30 and 1200 hp. Invalid value: " + powerHp);
        }
        return powerHp;
    }

    public static int productionYearValidation(int productionYear) {
        if(productionYear < 1900 || productionYear > 2026) {
            throw new IllegalArgumentException("Car productionYear must be between 1900 and 2026. Invalid value: " + productionYear);
        }
        return productionYear;
    }
}
