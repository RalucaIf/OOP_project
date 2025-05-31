package main;

import model.*;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.enums.UserRole;
import service.AlertService;
import service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        AlertService alertService = new AlertService(new repository.AlertRepository());
        UserService userService = new UserService(new repository.UserRepository());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Create a User");
            System.out.println("2. View All Users");
            System.out.println("3. Search Users by Role");
            System.out.println("4. Create an Alert");
            System.out.println("5. View All Alerts (Sorted by Priority)");
            System.out.println("6. Escalate an Alert");
            System.out.println("7. Close an Alert");
            System.out.println("8. View Alerts by Status");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // create user
                    System.out.print("Enter User ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter User Name: ");
                    String userName = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    System.out.println("Select User Role (1. ADMIN, 2. REGULAR_USER, 3. HACKER): ");
                    int roleChoice = scanner.nextInt();
                    UserRole role = (roleChoice == 1) ? UserRole.ADMIN :
                            (roleChoice == 2) ? UserRole.REGULAR_USER : UserRole.HACKER;

                    User user = (role == UserRole.ADMIN) ? new AdminUser(userId, userName, password) :
                            (role == UserRole.REGULAR_USER) ? new RegularUser(userId, userName, password) :
                                    new model.HackerUser(userId, userName, password);

                    userService.createUser(user);
                    System.out.println("User created successfully!");
                    break;

                case 2:
                    // view users
                    List<User> users = userService.getAllUsers();
                    System.out.println("All Users:");
                    for (User u : users) {
                        System.out.println("ID: " + u.getId() + ", Name: " + u.getName() + ", Role: " + u.getRole());
                    }
                    break;

                case 3:
                    // search user by role
                    System.out.println("Enter Role to Search (1. ADMIN, 2. REGULAR_USER, 3. HACKER): ");
                    int roleSearchChoice = scanner.nextInt();
                    UserRole searchRole = (roleSearchChoice == 1) ? UserRole.ADMIN :
                            (roleSearchChoice == 2) ? UserRole.REGULAR_USER : UserRole.HACKER;
                    List<User> roleUsers = userService.searchByRole(searchRole);
                    System.out.println("Users with role " + searchRole + ":");
                    for (User u : roleUsers) {
                        System.out.println("ID: " + u.getId() + ", Name: " + u.getName());
                    }
                    break;

                case 4:
                    // create alert
                    System.out.print("Enter Alert ID: ");
                    int alertId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Select Alert Type (1. Administrative, 2. Security): ");
                    int alertType = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Select Alert Priority (1. LOW, 2. MEDIUM, 3. HIGH, 4. CRITICAL): ");
                    int priorityChoice = scanner.nextInt();
                    scanner.nextLine();
                    AlertPriority priority = (priorityChoice == 1) ? AlertPriority.LOW :
                            (priorityChoice == 2) ? AlertPriority.MEDIUM :
                                    (priorityChoice == 3) ? AlertPriority.HIGH : AlertPriority.CRITICAL;

                    System.out.print("Enter Alert Issue: ");
                    String issue = scanner.nextLine();

                    if (alertType == 1) {
                        AdministrativeAlert adminAlert = new AdministrativeAlert(alertId, priority, AlertStatus.NEW, issue);
                        alertService.createAlert(adminAlert);
                    } else {
                        SecurityAlert securityAlert = new SecurityAlert(alertId, priority, AlertStatus.NEW, issue);
                        alertService.createAlert(securityAlert);
                    }
                    System.out.println("Alert created successfully!");
                    break;

                case 5:
                    // view alerts - priority
                    Set<Alert> sortedAlerts = alertService.getSortedAlertsByPriorityThenTime();
                    System.out.println("All Alerts (Sorted by Priority):");
                    for (Alert alert : sortedAlerts) {
                        System.out.println(alert.getDetails());
                    }
                    break;

                case 6:
                    // escalate
                    System.out.print("Enter Alert ID to Escalate: ");
                    int escalateId = scanner.nextInt();
                    try {
                        alertService.escalateAlert(escalateId);
                        System.out.println("Alert escalated successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 7:
                    // close an alert
                    System.out.print("Enter Alert ID to Close: ");
                    int closeId = scanner.nextInt();
                    try {
                        alertService.closeAlert(closeId);
                        System.out.println("Alert closed successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 8:
                    // alerts by status
                    System.out.println("Select Alert Status to View (1. NEW, 2. IN_PROGRESS, 3. CLOSED): ");
                    int statusChoice = scanner.nextInt();
                    AlertStatus status = (statusChoice == 1) ? AlertStatus.NEW :
                            (statusChoice == 2) ? AlertStatus.IN_PROGRESS : AlertStatus.CLOSED;
                    List<Alert> statusAlerts = alertService.getByStatus(status);
                    System.out.println("Alerts with status " + status + ":");
                    for (Alert alert : statusAlerts) {
                        System.out.println(alert.getDetails());
                    }
                    break;

                case 9:
                    // exit
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}