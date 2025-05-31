package main;

import exception.AlertNotFoundException;
import exception.InvalidDataException;
import exception.UserNotFoundException;
import model.*;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.enums.UserRole;
import service.AlertService;
import service.UserService;

import repository.AlertRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService(new UserRepository());
        AlertService alertService = new AlertService(new AlertRepository());

        try (Scanner scanner = new Scanner(System.in)) {
            boolean isRunning = true;

            while (isRunning) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Create a user");
                System.out.println("2. Get a user by ID");
                System.out.println("3. Delete a user by ID");
                System.out.println("4. Get all users");
                System.out.println("5. Search for users by role");
                System.out.println("6. Create an alert");
                System.out.println("7. Escalate an alert by ID");
                System.out.println("8. Close an alert by ID");
                System.out.println("9. Get alerts by status");
                System.out.println("10. Get sorted alerts");
                System.out.println("0. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        try {
                            System.out.println("Enter user ID:");
                            int id = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter user name:");
                            String name = scanner.nextLine();

                            System.out.println("Enter user password:");
                            String password = scanner.nextLine();

                            System.out.println("Enter user role (ADMIN, REGULAR_USER, HACKER):");
                            String role = scanner.nextLine();
                            UserRole userRole = UserRole.valueOf(role.toUpperCase());

                            User user;
                            if (userRole == UserRole.ADMIN) {
                                user = new AdminUser(id, name, password);
                            } else if (userRole == UserRole.HACKER) {
                                user = new HackerUser(id, name, password);
                            } else {
                                user = new RegularUser(id, name, password);
                            }

                            userService.createUser(user);
                            System.out.println("User created successfully.");
                        } catch (InvalidDataException e) {
                            System.err.println("Error creating user: " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid role provided.");
                        }
                    }
                    case 2 -> {
                        try {
                            System.out.println("Enter user ID:");
                            int id = scanner.nextInt();

                            User user = userService.getUserById(id);
                            System.out.println("User found: " + user.getName());
                        } catch (UserNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                    case 3 -> {
                        try {
                            System.out.println("Enter user ID:");
                            int id = scanner.nextInt();

                            userService.deleteUserById(id);
                            System.out.println("User deleted successfully.");
                        } catch (UserNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        List<User> users = userService.getAllUsers();
                        System.out.println("All users:");
                        users.forEach(user -> System.out.println(user.getName() + " (" + user.getRole() + ")"));
                    }
                    case 5 -> {
                        System.out.println("Enter role to search (ADMIN, REGULAR_USER, HACKER):");
                        String role = scanner.nextLine();
                        try {
                            UserRole userRole = UserRole.valueOf(role.toUpperCase());
                            List<User> users = userService.searchByRole(userRole);
                            System.out.println("Users with role " + userRole + ":");
                            users.forEach(user -> System.out.println(user.getName()));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid role provided.");
                        }
                    }
                    case 6 -> {
                        try {
                            System.out.println("Enter alert ID:");
                            int id = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter alert priority (LOW, MEDIUM, HIGH, CRITICAL):");
                            AlertPriority priority = AlertPriority.valueOf(scanner.nextLine().toUpperCase());

                            System.out.println("Enter alert status (NEW, IN_PROGRESS, CLOSED):");
                            AlertStatus status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());

                            System.out.println("Enter alert type (SECURITY or ADMINISTRATIVE):");
                            String type = scanner.nextLine();

                            Alert alert;
                            if (type.equalsIgnoreCase("SECURITY")) {
                                System.out.println("Enter security problem description:");
                                String problem = scanner.nextLine();
                                alert = new SecurityAlert(id, priority, status, problem);
                            } else if (type.equalsIgnoreCase("ADMINISTRATIVE")) {
                                System.out.println("Enter administrative problem description:");
                                String problem = scanner.nextLine();
                                alert = new AdministrativeAlert(id, priority, status, problem);
                            } else {
                                throw new InvalidDataException("Invalid alert type.");
                            }

                            alertService.createAlert(alert);
                            System.out.println("Alert created successfully.");
                        } catch (InvalidDataException e) {
                            System.err.println("Error creating alert: " + e.getMessage());
                        }
                    }
                    case 7 -> {
                        try {
                            System.out.println("Enter alert ID:");
                            int id = scanner.nextInt();

                            alertService.escalateAlert(id);
                            System.out.println("Alert escalated successfully.");
                        } catch (AlertNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                    case 8 -> {
                        try {
                            System.out.println("Enter alert ID:");
                            int id = scanner.nextInt();

                            alertService.closeAlert(id);
                            System.out.println("Alert closed successfully.");
                        } catch (AlertNotFoundException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                    case 9 -> {
                        try {
                            System.out.println("Enter alert status (NEW, IN_PROGRESS, CLOSED):");
                            AlertStatus status = AlertStatus.valueOf(scanner.nextLine().toUpperCase());

                            List<Alert> alerts = alertService.getByStatus(status);
                            System.out.println("Alerts with status " + status + ":");
                            alerts.forEach(alert -> System.out.println(alert.getDetails()));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid status provided.");
                        }
                    }
                    case 10 -> {
                        Set<Alert> sortedAlerts = alertService.getSortedAlertsByPriorityThenTime();
                        System.out.println("Sorted alerts:");
                        sortedAlerts.forEach(alert -> System.out.println(alert.getDetails()));
                    }
                    case 0 -> {
                        System.out.println("Exiting...");
                        isRunning = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}