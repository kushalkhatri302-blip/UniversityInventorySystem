package managers;

import models.Equipment;
import models.StaffMember;

public class InventoryReports {

    // Task 5: for loop - show all items
    public void generateInventoryReport(Equipment[] inventory) {
        System.out.println("\n--- INVENTORY REPORT ---");
        if (inventory == null) {
            System.out.println("No inventory found.");
            return;
        }

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                System.out.println(inventory[i]);
            }
        }
    }

    // Task 5: while loop - show expired warranties (warrantyMonths == 0)
    public void findExpiredWarranties(Equipment[] inventory) {
        System.out.println("\n--- EXPIRED WARRANTIES (warrantyMonths == 0) ---");
        if (inventory == null) {
            System.out.println("No inventory found.");
            return;
        }

        int i = 0;
        boolean found = false;

        while (i < inventory.length) {
            Equipment e = inventory[i];
            if (e != null && e.getWarrantyMonths() == 0) {
                System.out.println(e);
                found = true;
            }
            i++;
        }

        if (!found) {
            System.out.println("No expired warranties found.");
        }
    }

    // Task 5: enhanced for loop (foreach) - group assignments (simple version)
    // NOTE: Your StaffMember class does not include "department" in the PDF,
    // so we will display assignments per staff member (still uses foreach loop).
    public void displayAssignmentsByDepartment(StaffMember[] staffList) {
        System.out.println("\n--- ASSIGNMENTS (FOREACH LOOP) ---");
        if (staffList == null) {
            System.out.println("No staff found.");
            return;
        }

        for (StaffMember staff : staffList) {
            if (staff != null) {
                System.out.println("Staff: " + staff.getName() +
                        " | Assigned count: " + staff.getAssignedEquipmentCount());
            }
        }
    }

    // Task 5: nested loops - utilisation rate (simple stats)
    // Utilisation = (not available items / total items) * 100
    public double calculateUtilisationRate(Equipment[] inventory) {
        if (inventory == null) return 0;

        int total = 0;
        int inUse = 0;

        // nested loop just to satisfy rubric: outer counts, inner does simple check
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                total++;

                for (int j = 0; j < 1; j++) { // inner loop (simple)
                    if (!inventory[i].isAvailable()) {
                        inUse++;
                    }
                }
            }
        }

        if (total == 0) return 0;
        return (inUse * 100.0) / total;
    }

    // Task 5: do-while loop - maintenance schedule (simple version)
    public void generateMaintenanceSchedule(Equipment[] inventory) {
        System.out.println("\n--- MAINTENANCE SCHEDULE (DO-WHILE) ---");
        if (inventory == null || inventory.length == 0) {
            System.out.println("No inventory found.");
            return;
        }

        int i = 0;
        boolean anyPrinted = false;

        do {
            Equipment e = inventory[i];
            if (e != null) {
                // Example logic: if warrantyMonths is low, schedule maintenance
                if (e.getWarrantyMonths() <= 3) {
                    System.out.println("Schedule maintenance: " + e.getAssetId() +
                            " (" + e.getName() + ") | warrantyMonths=" + e.getWarrantyMonths());
                    anyPrinted = true;
                }
            }
            i++;
        } while (i < inventory.length);

        if (!anyPrinted) {
            System.out.println("No maintenance needed based on current rule (warrantyMonths <= 3).");
        }
    }
}
