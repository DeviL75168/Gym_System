abstract class GymMember {
    protected int id;
    protected String name;
    protected String location;
    protected String phone;
    protected String email;
    protected String gender;
    protected String dob;
    protected String membershipStartDate;
    protected int attendance;
    protected double loyaltyPoints;
    protected boolean activeStatus;
    
    public GymMember(int id, String name, String location, String phone, String email, 
                    String gender, String dob, String membershipStartDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.membershipStartDate = membershipStartDate;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = false;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public String getMembershipStartDate() { return membershipStartDate; }
    public int getAttendance() { return attendance; }
    public double getLoyaltyPoints() { return loyaltyPoints; }
    public boolean isActiveStatus() { return activeStatus; }
    
    public void activateMembership() { activeStatus = true; }
    public void deactivateMembership() { activeStatus = false; }
    public abstract void markAttendance();
    public abstract String toFileString();
    
    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nLocation: " + location + 
               "\nPhone: " + phone + "\nEmail: " + email + "\nGender: " + gender + 
               "\nDOB: " + dob + "\nMembership Start: " + membershipStartDate + 
               "\nAttendance: " + attendance + "\nLoyalty Points: " + loyaltyPoints + 
               "\nActive: " + (activeStatus ? "Yes" : "No");
    }
}

