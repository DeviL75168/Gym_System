class PremiumMember extends GymMember {
    private final double premiumCharge = 50000;
    private String personalTrainer;
    private String removalReason; 
    private boolean isFullPayment;
    private double paidAmount;
    private double discountAmount;
    
    public PremiumMember(int id, String name, String location, String phone, String email, 
                        String gender, String dob, String membershipStartDate, 
                        String personalTrainer, double paidAmount) {
        super(id, name, location, phone, email, gender, dob, membershipStartDate);
        this.personalTrainer = personalTrainer;
        this.paidAmount = paidAmount;
        this.isFullPayment = (paidAmount >= premiumCharge);
        this.discountAmount = isFullPayment ? premiumCharge * 0.1 : 0;
    }
    
    public String payDueAmount(double amount) {
        if (isFullPayment) {
            return "Payment already completed";
        }
        
        if (amount <= 0) {
            return "Wrong payment amount";
        }
        
        paidAmount += amount;
        if (paidAmount >= premiumCharge) {
            isFullPayment = true;
            discountAmount = premiumCharge * 0.1;
            return "Payment completed. Discount of " + discountAmount + " applied";
        }
        
        return "Payment received. Remaining amount: " + (premiumCharge - paidAmount);
    }
    
    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = premiumCharge * 0.1;
        } else {
            discountAmount = 0;
        }
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void revertPremiumMember(String reason) {
        this.activeStatus = false;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.personalTrainer = "";
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
        this.removalReason = reason;
    }
    
    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 10;
        }
    }
    
    @Override
public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append("\nType: Premium Member")
      .append("\nTrainer: ").append(personalTrainer)
      .append("\nPaid Amount: ").append(paidAmount)
      .append("\nPremium Charge: ").append(premiumCharge)
      .append("\nFull Payment: ").append(isFullPayment ? "Yes" : "No")
      .append("\nRemaining: ").append(premiumCharge - paidAmount);
    
    if (isFullPayment) {
        sb.append("\nDiscount: ").append(discountAmount);
    }
    
    if (removalReason != null && !removalReason.isEmpty()) {
        sb.append("\nRemoval Reason: ").append(removalReason);
    }
    
    return sb.toString();
}
    @Override
    public String toFileString() {
        return "Premium," + id + "," + name + "," + location + "," + phone + "," + email + 
               "," + gender + "," + dob + "," + membershipStartDate + "," + personalTrainer + 
               "," + paidAmount + "," + premiumCharge + "," + isFullPayment + "," + discountAmount + 
               "," + attendance + "," + loyaltyPoints + "," + activeStatus;
    }
}