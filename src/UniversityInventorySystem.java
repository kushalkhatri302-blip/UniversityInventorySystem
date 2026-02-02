import java.util.Scanner;

import exceptions.AssignmentLimitExceededException;
import exceptions.EquipmentNotAvailableException;
import managers.InventoryManager;
import managers.InventoryReports;
import models.Equipment;
import models.StaffMember;

public class UniversityInventorySystem {

    // Simple arrays (you can increase sizes)
    private static Equipment[] inventory = new Equipment[20];
    private static int inventoryCount = 0;

    private static StaffMember[] staffList = new StaffMember[20];
    private static int staffCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        InventoryManager manager = new InventoryManager();
        InventoryReports reports = new InventoryReports();

        int choice;

        do {
            System.out.println("\n=== University Inventory Management System ===");
            System.out.println("1. Add new equipment");
            System.out.println("2. Register a new staff member");
            System.out.println("3. Assign equipment to staff");
            System.out.println("4. Return equipment");
            System.out.println("5. Search inventory (by name)");
            System.out.println("6. Generate reports");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            choice = readInt(sc);

            try {
                switch (choice) {
                    case 1:
                        addEquipment(sc);
                        break;
                    case 2:
                        addStaff(sc);
                        break;
                    case 3:
                        assignEquipment(sc, manager);
                        break;
                    case 4:
                        returnEquipment(sc, manager);
                        break;
                    case 5:
                        searchByName(sc, manager);
                        break;
                    case 6:
                        showReports(sc, reports, manager);
                        break;
                    case 0:
                        System.out.println("Exiting... Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (EquipmentNotAvailableException | AssignmentLimitExceededException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    // ---------- MENU FUNCTIONS ----------

    private static void addEquipment(Scanner sc) {
        if (inventoryCount >= inventory.length) {
            System.out.println("Inventory is full!");
            return;
        }

        System.out.print("Enter assetId: ");
        String assetId = sc.nextLine().trim();

        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter brand: ");
        String brand = sc.nextLine().trim();

        System.out.print("Enter category (computer/lab/furniture/other): ");
        String category = sc.nextLine().trim();

        System.out.print("Enter warranty months (0 if expired): ");
        int warrantyMonths = readInt(sc);

        // New equipment is available by default
        Equipment e = new Equipment(assetId, name, brand, true, category, warrantyMonths);

        inventory[inventoryCount++] = e;
        System.out.println("Equipment added: " + e);
    }

    private static void addStaff(Scanner sc) {
        if (staffCount >= staffList.length) {
            System.out.println("Staff list is full!");
            return;
        }

        System.out.print("Enter staffId (number): ");
        int staffId = readInt(sc);

        System.out.print("Enter staff name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter staff email: ");
        String email = sc.nextLine().trim();

        StaffMember s = new StaffMember(staffId, name, email);
        staffList[staffCount++] = s;

        System.out.println("Staff registered: " + s.getName());
    }

    private static void assignEquipment(Scanner sc, InventoryManager manager)
            throws EquipmentNotAvailableException, AssignmentLimitExceededException {

        StaffMember staff = pickStaff(sc);
        if (staff == null) return;

        Equipment equipment = pickEquipment(sc);
        if (equipment == null) return;

        manager.assignEquipment(staff, equipment);
    }

    private static void returnEquipment(Scanner sc, InventoryManager manager)
            throws EquipmentNotAvailableException {

        StaffMember staff = pickStaff(sc);
        if (staff == null) return;

        System.out.print("Enter assetId to return: ");
        String assetId = sc.nextLine().trim();

        // StaffMember removes it; now we also mark equipment available again in inventory list
        boolean before = staff.removeAssignedEquipment(assetId);
        if (!before) {
            throw new EquipmentNotAvailableException("This staff member does not have equipment with assetId: " + assetId);
        }

        Equipment eq = findEquipmentById(assetId);
        if (eq != null) {
            eq.setAvailable(true);
        }

        System.out.println("Returned equipment " + assetId + " from " + staff.getName());
    }

    private static void searchByName(Scanner sc, InventoryManager manager) {
        System.out.print("Enter name keyword to search: ");
        String keyword = sc.nextLine().trim();

        Equipment[] results = manager.searchEquipment(inventory, keyword);

        System.out.println("\n--- SEARCH RESULTS ---");
        if (results.length == 0) {
            System.out.println("No equipment found.");
        } else {
            for (Equipment e : results) {
                System.out.println(e);
            }
        }
    }

    private static void showReports(Scanner sc, InventoryReports reports, InventoryManager manager) {
        System.out.println("\n1. Inventory report");
        System.out.println("2. Expired warranties");
        System.out.println("3. Assignments report");
        System.out.println("4. Utilisation rate");
        System.out.println("5. Maintenance schedule");
        System.out.print("Choose report: ");

        int r = readInt(sc);

        switch (r) {
            case 1:
                reports.generateInventoryReport(inventory);
                break;
            case 2:
                reports.findExpiredWarranties(inventory);
                break;
            case 3:
                reports.displayAssignmentsByDepartment(staffList);
                break;
            case 4:
                double rate = reports.calculateUtilisationRate(inventory);
                System.out.println("Utilisation rate: " + rate + "%");
                break;
            case 5:
                reports.generateMaintenanceSchedule(inventory);
                break;
            default:
                System.out.println("Invalid report option.");
        }
    }

    // ---------- HELPERS ----------

    private static int readInt(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static StaffMember pickStaff(Scanner sc) {
        if (staffCount == 0) {
            System.out.println("No staff registered yet.");
            return null;
        }

        System.out.println("\n--- STAFF LIST ---");
        for (int i = 0; i < staffCount; i++) {
            System.out.println((i + 1) + ". " + staffList[i].getName() + " (ID: " + staffList[i].getStaffId() + ")");
        }

        System.out.print("Pick staff number: ");
        int idx = readInt(sc) - 1;

        if (idx < 0 || idx >= staffCount) {
            System.out.println("Invalid staff selection.");
            return null;
        }

        return staffList[idx];
    }

    private static Equipment pickEquipment(Scanner sc) {
        if (inventoryCount == 0) {
            System.out.println("No equipment in inventory yet.");
            return null;
        }

        System.out.println("\n--- EQUIPMENT LIST ---");
        for (int i = 0; i < inventoryCount; i++) {
            System.out.println((i + 1) + ". " + inventory[i].getAssetId()
                    + " | " + inventory[i].getName()
                    + " | Available: " + inventory[i].isAvailable());
        }

        System.out.print("Pick equipment number: ");
        int idx = readInt(sc) - 1;

        if (idx < 0 || idx >= inventoryCount) {
            System.out.println("Invalid equipment selection.");
            return null;
        }

        return inventory[idx];
    }

    private static Equipment findEquipmentById(String assetId) {
        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i] != null && inventory[i].getAssetId().equals(assetId)) {
                return inventory[i];
            }
        }
        return null;
    }
}
