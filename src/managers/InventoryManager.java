package managers;

import exceptions.AssignmentLimitExceededException;
import exceptions.EquipmentNotAvailableException;
import models.Equipment;
import models.StaffMember;

public class InventoryManager {

    // ================= ASSIGN EQUIPMENT =================
    public void assignEquipment(StaffMember staff, Equipment equipment)
            throws EquipmentNotAvailableException, AssignmentLimitExceededException {

        if (staff == null) {
            throw new AssignmentLimitExceededException("Staff member is null.");
        }

        if (equipment == null) {
            throw new EquipmentNotAvailableException("Equipment is null.");
        }

        if (!equipment.isAvailable()) {
            throw new EquipmentNotAvailableException(
                    "Equipment " + equipment.getAssetId() + " is not available."
            );
        }

        if (staff.getAssignedEquipmentCount() >= 5) {
            throw new AssignmentLimitExceededException(
                    "Staff already has 5 equipment items (limit reached)."
            );
        }

        if (!staff.addAssignedEquipment(equipment)) {
            throw new AssignmentLimitExceededException(
                    "Could not assign equipment (limit reached)."
            );
        }

        equipment.setAvailable(false);
        System.out.println("Assigned " + equipment.getAssetId() +
                " to " + staff.getName());
    }

    // ================= RETURN EQUIPMENT =================
    public void returnEquipment(StaffMember staff, String assetId)
            throws EquipmentNotAvailableException {

        if (staff == null) {
            throw new EquipmentNotAvailableException("Staff member is null.");
        }

        if (assetId == null || assetId.trim().isEmpty()) {
            throw new EquipmentNotAvailableException("Asset ID cannot be empty.");
        }

        if (!staff.removeAssignedEquipment(assetId)) {
            throw new EquipmentNotAvailableException(
                    "This staff member does not have equipment with assetId: " + assetId
            );
        }

        System.out.println("Returned equipment " + assetId +
                " from " + staff.getName());
    }

    // ================= MAINTENANCE FEE =================
    public double calculateMaintenanceFee(Equipment equipment, int daysOverdue) {
        if (equipment == null || daysOverdue <= 0) return 0;

        String category = equipment.getCategory();
        if (category == null) category = "";

        double ratePerDay;

        switch (category.toLowerCase()) {
            case "computer":
                ratePerDay = 5.0;
                break;
            case "lab":
            case "labequipment":
                ratePerDay = 10.0;
                break;
            case "furniture":
                ratePerDay = 2.0;
                break;
            default:
                ratePerDay = 3.0;
        }

        return ratePerDay * daysOverdue;
    }

    // ================= SEARCH METHODS (OVERLOADING) =================

    // Search by name
    public Equipment[] searchEquipment(Equipment[] inventory, String name) {
        if (inventory == null || name == null) return new Equipment[0];

        Equipment[] results = new Equipment[inventory.length];
        int count = 0;

        for (Equipment e : inventory) {
            if (e != null && e.getName() != null &&
                    e.getName().toLowerCase().contains(name.toLowerCase())) {
                results[count++] = e;
            }
        }

        return trimResults(results, count);
    }

    // Search by category + availability
    public Equipment[] searchEquipment(Equipment[] inventory,
                                       String category,
                                       boolean availableOnly) {

        if (inventory == null || category == null) return new Equipment[0];

        Equipment[] results = new Equipment[inventory.length];
        int count = 0;

        for (Equipment e : inventory) {
            if (e != null && e.getCategory() != null &&
                    e.getCategory().equalsIgnoreCase(category)) {

                if (!availableOnly || e.isAvailable()) {
                    results[count++] = e;
                }
            }
        }

        return trimResults(results, count);
    }

    // Search by warranty range
    public Equipment[] searchEquipment(Equipment[] inventory,
                                       int minWarranty,
                                       int maxWarranty) {

        if (inventory == null) return new Equipment[0];

        Equipment[] results = new Equipment[inventory.length];
        int count = 0;

        for (Equipment e : inventory) {
            if (e != null) {
                int w = e.getWarrantyMonths();
                if (w >= minWarranty && w <= maxWarranty) {
                    results[count++] = e;
                }
            }
        }

        return trimResults(results, count);
    }

    // ================= HELPER METHOD =================
    private Equipment[] trimResults(Equipment[] arr, int size) {
        Equipment[] trimmed = new Equipment[size];
        for (int i = 0; i < size; i++) {
            trimmed[i] = arr[i];
        }
        return trimmed;
    }
}
