package main;

import exception.InvalidDataException;
import exception.UserNotFoundException;
import model.*;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.enums.UserRole;
import repository.AlertRepository;
import service.AlertService;
import service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class main {
    private static final AlertRepository alertRepository = new AlertRepository();
    private static final AlertService alertService = new AlertService(alertRepository);
    private static final UserService userService = new UserService(null);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            int choice = getUserInput();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> exitApplication();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your option: ");
    }

    private static void login() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (username.equalsIgnoreCase("hacker")) {
            simulateHackerAttack();
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.authenticate(username, password);

            if (user.getRole() == UserRole.ADMIN) {
                adminMenu((AdminUser) user);
            } else if (user.getRole() == UserRole.REGULAR_USER) {
                userMenu((RegularUser) user);
            }
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void register() {
        System.out.print("\nEnter new username: ");
        String username = scanner.nextLine();

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        System.out.print("Choose role (ADMIN/REGULAR_USER): ");
        String role = scanner.nextLine();

        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            User newUser = (userRole == UserRole.ADMIN)
                    ? new AdminUser(0, username, password)
                    : new RegularUser(0, username, password);

            userService.createUser(newUser);

            System.out.println("Registration successful!");
        } catch (IllegalArgumentException | InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void adminMenu(AdminUser admin) {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. Create a new user");
            System.out.println("2. Get all users");
            System.out.println("3. Delete a user by ID");
            System.out.println("4. Search users by role");
            System.out.println("5. Create a new alert");
            System.out.println("6. Delete an alert by ID");
            System.out.println("7. Get alerts by status");
            System.out.println("8. Get sorted alerts by priority");
            System.out.println("9. Logout");
            System.out.print("Enter your option: ");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> register();
                case 2 -> {
                    List<User> users = userService.getAllUsers();
                    users.forEach(System.out::println);
                }
                case 3 -> deleteUser();
                case 4 -> searchUsersByRole();
                case 5 -> createAlert();
                case 6 -> deleteAlert();
                case 7 -> getAlertsByStatus();
                case 8 -> getSortedAlerts();
                case 9 -> {
                    System.out.println("Admin logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void userMenu(RegularUser user) {
        while (true) {
            System.out.println("\n==== USER MENU ====");
            System.out.println("1. Get all users");
            System.out.println("2. Get a user by ID");
            System.out.println("3. Search users by role");
            System.out.println("4. Get alerts by status");
            System.out.println("5. Get sorted alerts by priority");
            System.out.println("6. Logout");
            System.out.print("Enter your option: ");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> {
                    List<User> users = userService.getAllUsers();
                    users.forEach(System.out::println);
                }
                case 2 -> getUserById();
                case 3 -> searchUsersByRole();
                case 4 -> getAlertsByStatus();
                case 5 -> getSortedAlerts();
                case 6 -> {
                    System.out.println("User logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void deleteUser() {
        System.out.print("\nEnter user ID to delete: ");
        int userId = getUserInput();

        try {
            userService.deleteUserById(userId);
            System.out.println("User deleted");
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchUsersByRole() {
        System.out.print("\nEnter user role (ADMIN/REGULAR_USER): ");
        String roleInput = scanner.nextLine();
        try {
            UserRole role = UserRole.valueOf(roleInput.toUpperCase());
            List<User> users = userService.searchByRole(role);
            users.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role");
        }
    }

    private static void getUserById() {
        System.out.print("\nEnter user ID: ");
        int userId = getUserInput();
        try {
            User user = userService.getUserById(userId);
            System.out.println(user);
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createAlert() {
        System.out.println("Choose the type of alert:");
        System.out.println("1. Administrative Alert");
        System.out.println("2. Security Alert");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        Alert alert;

        if (choice == 1) {
            alert = createAdministrativeAlert();
        } else if (choice == 2) {
            alert = createSecurityAlert();
        } else {
            System.out.println("Invalid choice");
            return;
        }
        try {
            alertService.createAlert(alert);
            System.out.println("Alert created. Details:");
            System.out.println(alert);
        } catch (InvalidDataException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static AdministrativeAlert createAdministrativeAlert() {
        System.out.println("Creating an Administrative Alert:");

        System.out.println("Enter the priority (LOW, MEDIUM, HIGH, CRITICAL):");
        AlertPriority priority = AlertPriority.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter the status (NEW, IN_PROGRESS, CLOSED):");
        AlertStatus status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter the administrative issue:");
        String administrativeProblem = scanner.nextLine();

        return new AdministrativeAlert(0, priority, status, administrativeProblem);
    }

    private static SecurityAlert createSecurityAlert() {
        System.out.println("Creating a Security Alert:");

        System.out.println("Enter the priority (LOW, MEDIUM, HIGH, CRITICAL):");
        AlertPriority priority = AlertPriority.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter the status (NEW, IN_PROGRESS, CLOSED):");
        AlertStatus status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter the security issue:");
        String securityProblem = scanner.nextLine();

        return new SecurityAlert(0, priority, status, securityProblem);
    }

    private static void deleteAlert() {
        System.out.print("\nEnter alert ID to delete: ");
        int alertId = getUserInput();

        try {
            alertService.closeAlert(alertId);
            System.out.println("Alert deleted");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getAlertsByStatus() {
        System.out.print("\nEnter alert status (NEW/IN_PROGRESS/CLOSED): ");
        String statusInput = scanner.nextLine();

        try {
            AlertStatus status = AlertStatus.valueOf(statusInput.toUpperCase());
            List<Alert> alerts = alertService.getByStatus(status);
            alerts.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status entered.");
        }
    }

    private static void getSortedAlerts() {
        Set<Alert> sortedAlerts = alertService.getSortedAlertsByPriorityThenTime();
        sortedAlerts.forEach(System.out::println);
    }

    private static void exitApplication() {
        System.out.println("Exiting... Goodbye!");
        System.exit(0);
    }

    private static int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // invalid input
        }
    }

    private static void simulateHackerAttack() {
        // hacker and device
        HackerUser hacker = new HackerUser(1, "Hacker", "pa55word");
        Device hackerDevice = new Device("Hacker device", "192.168.1.99");
        // security alert
        SecurityAlert alert = new SecurityAlert(
                0,
                AlertPriority.CRITICAL,
                AlertStatus.NEW,
                "Unauthorized system access detected"
        );
        System.out.println(hacker + " is attempting an attack.");
        try {
            // associate the alert with the device
            alertService.createAlert(alert, hackerDevice);
        } catch (InvalidDataException e) {
            System.err.println("Error creating alert: " + e.getMessage());
        }
    }
}