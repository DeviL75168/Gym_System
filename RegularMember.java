class RegularMember extends GymMember {
    private final int attendanceLimit = 30;
    private boolean isEligibleForUpgrade;
    private String removalReason;
    private String referralSource;
    private String plan;
    private double price;

    public RegularMember(int id, String name, String location, String phone, String email, 
                         String gender, String dob, String membershipStartDate, 
                         String referralSource, String plan) {
        super(id, name, location, phone, email, gender, dob, membershipStartDate);
        this.referralSource = referralSource;
        this.plan = plan;
        this.price = getPlanPrice(plan);
        this.isEligibleForUpgrade = false;
        this.removalReason = "";
    }

    public int getAttendanceCount() {
        return attendance;
    }

    // Getter for plan
    public String getPlan() {
        return this.plan;
    }

    // Static method to get plan price
    public static double getPlanPrice(String plan) {
        switch (plan.toLowerCase()) {
            case "basic": return 6500;
            case "standard": return 12500;
            case "deluxe": return 18500;
            default: return -1;
        }
    }

    public String upgradePlan(String newPlan) {
        if (newPlan.equalsIgnoreCase(plan)) {
            return "Member is already on this plan";
        }

        if (!isEligibleForUpgrade) {
            return "Member not eligible for upgrade";
        }

        double newPrice = getPlanPrice(newPlan);
        if (newPrice == -1) {
            return "Wrong plan selected";
        }

        this.plan = newPlan;
        this.price = newPrice;
        return "Plan upgraded to " + newPlan + " successfully";
    }

    public void revertRegularMember(String reason) {
        this.activeStatus = false;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.removalReason = reason;
        this.isEligibleForUpgrade = false;
        this.plan = "basic";
        this.price = 6500;
    }

    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 5;

            if (attendance >= attendanceLimit) {
                isEligibleForUpgrade = true;
            }
        }
    }

@Override
public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append("\nType: Regular Member")
      .append("\nReferral: ").append(referralSource)
      .append("\nPlan: ").append(plan)
      .append("\nPrice: ").append(price);
    if (!removalReason.isEmpty()) {
        sb.append("\nRemoval Reason: ").append(removalReason);
    }
    return sb.toString();
}

    @Override
    public String toFileString() {
        return "Regular," + id + "," + name + "," + location + "," + phone + "," + email + 
               "," + gender + "," + dob + "," + membershipStartDate + "," + referralSource + 
               "," + plan + "," + price + "," + attendance + "," + loyaltyPoints + "," + activeStatus;
    }
}
