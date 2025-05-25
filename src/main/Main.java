package main;

import config.DBConfig;
import model.AdministrativeAlert;
import model.HackerUser;
import model.RegularUser;
import model.SecurityAlert;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.enums.UserRole;
import model.interfaces.InterfaceAlert;
import model.interfaces.InterfaceUser;
import service.AlertService;
import service.UserService;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AlertService alertService = new AlertService();
        UserService userService = new UserService(null);

        try (Connection connection = DBConfig.getConnection()) {
            boolean running = true;

            while (running) {
                System.out.println("===== MAIN MENU =====");
                System.out.println("1. Manage Users");
                System.out.println("2. Manage Alerts");
                System.out.println("3. Exit");
                System.out.print("Select an option: ");
                String mainChoice = scanner.nextLine();

                switch (mainChoice) {
                    case "1":
                        manageUsers(scanner, userService, connection);
                        break;
                    case "2":
                        manageAlerts(scanner, alertService);
                        break;
                    case "3":
                        System.out.println("Exiting program...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void manageUsers(Scanner scanner, UserService userService, Connection connection) {
        while (true) {
            System.out.println("\n===== USER MANAGEMENT =====");
            System.out.println("1. Create User");
            System.out.println("2. View User by ID");
            System.out.println("3. View All Users");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter User ID: ");
                        int userId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter User Name: ");
                        String userName = scanner.nextLine();
                        System.out.print("Enter User Password: ");
                        String userPassword = scanner.nextLine();
                        System.out.println("Select User Role (1 = ADMIN, 2 = REGULAR_USER, 3 = HACKER): ");
                        int roleChoice = Integer.parseInt(scanner.nextLine());
                        UserRole userRole = switch (roleChoice) {
                            case 1 -> UserRole.ADMIN;
                            case 2 -> UserRole.REGULAR_USER;
                            case 3 -> UserRole.HACKER;
                            default -> throw new IllegalArgumentException("Invalid role selected");
                        };

                        InterfaceUser newUser = switch (userRole) {
                            case ADMIN -> new RegularUser(userId, userName, userPassword);
                            case REGULAR_USER -> new RegularUser(userId, userName, userPassword);
                            case HACKER -> new HackerUser(userId, userName, "SQL Injection", userPassword);
                        };

                        if (userService.createUser(newUser, connection)) {
                            System.out.println("User created");
                        } else {
                            System.out.println("Failed to create user.");
                        }
                        break;

                    case "2":
                        System.out.print("Enter User ID to retrieve: ");
                        userId = Integer.parseInt(scanner.nextLine());
                        userService.getUser(userId, connection)
                                .ifPresentOrElse(
                                        user -> System.out.println("User: " + user),
                                        () -> System.out.println("User not found")
                                );
                        break;

                    case "3":
                        List<InterfaceUser> allUsers = userService.getAllUsers(connection);
                        allUsers.forEach(System.out::println);
                        break;

                    case "4":
                        System.out.print("Enter User ID to update: ");
                        userId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter New User Name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter New User Password: ");
                        String newPassword = scanner.nextLine();

                        InterfaceUser userToUpdate = new RegularUser(userId, newName, newPassword);
                        if (userService.updateUser(userToUpdate, connection)) {
                            System.out.println("User updated");
                        } else {
                            System.out.println("Failed to update user.");
                        }
                        break;

                    case "5":
                        System.out.print("Enter User ID to delete: ");
                        userId = Integer.parseInt(scanner.nextLine());
                        if (userService.deleteUser(userId, connection)) {
                            System.out.println("User deleted");
                        } else {
                            System.out.println("Failed to delete user.");
                        }
                        break;

                    case "6":
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void manageAlerts(Scanner scanner, AlertService alertService) {
        while (true) {
            System.out.println("\n===== ALERT MANAGEMENT =====");
            System.out.println("1. Create Alert");
            System.out.println("2. View Alert by ID");
            System.out.println("3. View All Alerts");
            System.out.println("4. Update Alert");
            System.out.println("5. Delete Alert");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");
            String choice2 = scanner.nextLine();

            try {
                switch (choice2) {
                    case "1":
                        System.out.print("Enter Alert ID: ");
                        int alertId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Alert Priority (LOW, MEDIUM, HIGH, CRITICAL): ");
                        AlertPriority priority = AlertPriority.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Enter Alert Status (NEW, IN_PROGRESS, CLOSED): ");
                        AlertStatus status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Enter Alert Type (ADMINISTRATIVE or SECURITY): ");
                        String type = scanner.nextLine().toUpperCase();

                        InterfaceAlert newAlert;
                        if (type.equals("ADMINISTRATIVE")) {
                            System.out.print("Enter Administrative Problem: ");
                            String problem = scanner.nextLine();
                            newAlert = new AdministrativeAlert(alertId, priority, status, problem);
                        } else if (type.equals("SECURITY")) {
                            System.out.print("Enter Security Problem: ");
                            String problem = scanner.nextLine();
                            newAlert = new SecurityAlert(alertId, priority, status, problem);
                        } else {
                            throw new IllegalArgumentException("Invalid alert type");
                        }

                        alertService.createAlert(newAlert);
                        System.out.println("Alert created");
                        break;

                    case "2":
                        System.out.print("Enter Alert ID to retrieve: ");
                        alertId = Integer.parseInt(scanner.nextLine());
                        InterfaceAlert retrievedAlert = alertService.getAlert(alertId);
                        System.out.println("Alert Details: " + retrievedAlert.getDetails());
                        break;

                    case "3":
                        List<InterfaceAlert> allAlerts = alertService.getAllAlerts();
                        allAlerts.forEach(alert -> System.out.println(alert.getDetails()));
                        break;

                    case "4":
                        System.out.print("Enter Alert ID to update: ");
                        alertId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter New Alert Priority (LOW, MEDIUM, HIGH, CRITICAL): ");
                        priority = AlertPriority.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Enter New Alert Status (NEW, IN_PROGRESS, CLOSED): ");
                        status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Enter Updated Alert Type (ADMINISTRATIVE or SECURITY): ");
                        type = scanner.nextLine().toUpperCase();

                        InterfaceAlert alertToUpdate;
                        if (type.equals("ADMINISTRATIVE")) {
                            System.out.print("Enter Updated Administrative Problem: ");
                            String updatedProblem = scanner.nextLine();
                            alertToUpdate = new AdministrativeAlert(alertId, priority, status, updatedProblem);
                        } else if (type.equals("SECURITY")) {
                            System.out.print("Enter Updated Security Problem: ");
                            String updatedProblem = scanner.nextLine();
                            alertToUpdate = new SecurityAlert(alertId, priority, status, updatedProblem);
                        } else {
                            throw new IllegalArgumentException("Invalid alert type");
                        }

                        alertService.updateAlert(alertToUpdate);
                        System.out.println("Alert updated");
                        break;

                    case "5":
                        System.out.print("Enter Alert ID to delete: ");
                        alertId = Integer.parseInt(scanner.nextLine());
                        alertService.deleteAlertById(alertId);
                        System.out.println("Alert deleted");
                        break;

                    case "6":
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }
}