package models;

public class StaffMember {
    private int staffId;
    private String name;
    private String email;

    private Equipment[] assignedEquipment;
    private int assignedCount;

    public StaffMember(int staffId, String name, String email) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.assignedEquipment = new Equipment[5];
        this.assignedCount = 0;
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAssignedEquipmentCount() { return assignedCount; }

    public boolean addAssignedEquipment(Equipment equipment) {
        if (assignedCount >= 5) return false;
        assignedEquipment[assignedCount] = equipment;
        assignedCount++;
        return true;
    }

    public boolean removeAssignedEquipment(String assetId) {
        for (int i = 0; i < assignedCount; i++) {
            if (assignedEquipment[i] != null && assignedEquipment[i].getAssetId().equals(assetId)) {
                for (int j = i; j < assignedCount - 1; j++) {
                    assignedEquipment[j] = assignedEquipment[j + 1];
                }
                assignedEquipment[assignedCount - 1] = null;
                assignedCount--;
                return true;
            }
        }
        return false;
    }
}
