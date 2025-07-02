import javax.swing.*; //elements like JTextField and JButton
import java.awt.*;//BorderLayout, GridLayout
import java.awt.event.*;
import java.io.*;//FileWriter/PrintWriter = Save data to text file, FileReader/BufferedReader = Load data from text file

import java.util.ArrayList; // Dynamic member storage
import java.util.Calendar; // Date handling

public class GymGUI extends JFrame {
    private ArrayList<GymMember> members = new ArrayList<>();
    private static final String FILE_NAME = "MemberDetails.txt";

    // UI Components
    private JRadioButton regularButton = new JRadioButton("Regular Member", true);
    private JRadioButton premiumButton = new JRadioButton("Premium Member");
    private ButtonGroup memberTypeGroup = new ButtonGroup();

    private JTextField idField = new JTextField(10);
    private JTextField nameField = new JTextField(10);
    private JTextField locationField = new JTextField(10);
    private JTextField phoneField = new JTextField(10);
    private JTextField emailField = new JTextField(10);

    private JRadioButton maleRadio = new JRadioButton("Male");
    private JRadioButton femaleRadio = new JRadioButton("Female");
    private ButtonGroup genderGroup = new ButtonGroup();

    private JComboBox<Integer> yearCombo = new JComboBox<>();
    private JComboBox<String> monthCombo = new JComboBox<>();
    private JComboBox<Integer> dayCombo = new JComboBox<>();

    private JComboBox<Integer> msYearCombo = new JComboBox<>();
    private JComboBox<String> msMonthCombo = new JComboBox<>();
    private JComboBox<Integer> msDayCombo = new JComboBox<>();

    private JTextField referralField = new JTextField(10);
    private JComboBox<String> planCombo = new JComboBox<>(new String[] { "Basic" });
    private JTextField regularPriceField = new JTextField(10);

    private JTextField trainerField = new JTextField(10);
    private JTextField paidAmountField = new JTextField(10);
    private JTextField premiumPriceField = new JTextField("50000");
    private JTextField discountField = new JTextField(10);

    private JTextField memberIdField = new JTextField(10);
    private JTextArea displayArea = new JTextArea(10, 40);
    
    private JButton addRegularButton;
    private JButton addPremiumButton;

    public GymGUI() {
        setTitle("Gym GUI");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupDateComponents();
        setupUI();
        setVisible(true);
        readFromFile();
    }

    private void setupDateComponents() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearCombo.addItem(i);
            msYearCombo.addItem(i);
        }

        String[] months = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        for (String month : months) {
            monthCombo.addItem(month);
            msMonthCombo.addItem(month);
        }

        updateDayCombo(yearCombo, monthCombo, dayCombo);
        updateDayCombo(msYearCombo, msMonthCombo, msDayCombo);

        // listeners for date changes
        yearCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayCombo(yearCombo, monthCombo, dayCombo);
            }
        });
        monthCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayCombo(yearCombo, monthCombo, dayCombo);
            }
        });
        msYearCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayCombo(msYearCombo, msMonthCombo, msDayCombo);
            }
        });
        msMonthCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayCombo(msYearCombo, msMonthCombo, msDayCombo);
            }
        });

        premiumPriceField.setText("50000");
        regularPriceField.setEditable(false);
        premiumPriceField.setEditable(false);
        discountField.setEditable(false);
        updateRegularPrice();
    }

    private void updateRegularPrice() {
        String selectedPlan = (String) planCombo.getSelectedItem();
        double price = RegularMember.getPlanPrice(selectedPlan);
        regularPriceField.setText(String.valueOf(price));
    }

    private void updateDayCombo(JComboBox<Integer> yearBox, JComboBox<String> monthBox, JComboBox<Integer> dayBox) {
        int selectedDay = dayBox.getSelectedItem() == null ? 1 : (int) dayBox.getSelectedItem();
        dayBox.removeAllItems();

        int year = (int) yearBox.getSelectedItem();
        int month = monthBox.getSelectedIndex();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 1);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDay; i++) {
            dayBox.addItem(i);
        }

        if (selectedDay <= maxDay) {
            dayBox.setSelectedItem(selectedDay);
        } else {
            dayBox.setSelectedItem(maxDay);
        }
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Member type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberTypeGroup.add(regularButton);
        memberTypeGroup.add(premiumButton);
        typePanel.add(new JLabel("Member Type:"));
        typePanel.add(regularButton);
        typePanel.add(premiumButton);

        regularButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFieldAccess();
                addRegularButton.setEnabled(true);
                addPremiumButton.setEnabled(false);
                planCombo.setSelectedItem("Basic"); // Always ensure Basic is selected
            }
        });
        premiumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFieldAccess();
                addRegularButton.setEnabled(false);
                addPremiumButton.setEnabled(true);
            }
        });

        mainPanel.add(typePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Information"));

        // Add all form fields
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderPanel);

        // Date of Birth
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dobPanel.add(yearCombo);
        dobPanel.add(monthCombo);
        dobPanel.add(dayCombo);
        formPanel.add(new JLabel("Date of Birth:"));
        formPanel.add(dobPanel);

        // Membership Start Date
        JPanel msPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        msPanel.add(msYearCombo);
        msPanel.add(msMonthCombo);
        msPanel.add(msDayCombo);
        formPanel.add(new JLabel("Membership Start:"));
        formPanel.add(msPanel);

        // Regular member fields
        formPanel.add(new JLabel("Referral Source:"));
        formPanel.add(referralField);
        formPanel.add(new JLabel("Plan:"));
        formPanel.add(planCombo);
        formPanel.add(new JLabel("Regular Price:"));
        formPanel.add(regularPriceField);

        // Premium member fields
        formPanel.add(new JLabel("Trainer's Name:"));
        formPanel.add(trainerField);
        formPanel.add(new JLabel("Paid Amount:"));
        formPanel.add(paidAmountField);
        formPanel.add(new JLabel("Premium Price:"));
        formPanel.add(premiumPriceField);
        formPanel.add(new JLabel("Discount Amount:"));
        formPanel.add(discountField);
     

        // Add plan combo listener
        planCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRegularPrice();
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Add member buttons
        JPanel addButtonPanel = new JPanel();
        addRegularButton = createButton("Add Regular Member", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRegularMember();
            }
        });
        addPremiumButton = createButton("Add Premium Member", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPremiumMember();
            }
        });
        
        // Initially disable the premium button since regular is selected by default
        addPremiumButton.setEnabled(false);
        
        addButtonPanel.add(addRegularButton);
        addButtonPanel.add(addPremiumButton);
        buttonPanel.add(addButtonPanel);

        // Operations buttons
        JPanel operationButtonPanel = new JPanel();
        operationButtonPanel.add(new JLabel("Member ID:"));
        operationButtonPanel.add(memberIdField);
        operationButtonPanel.add(createButton("Activate", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activateMember();
            }
        }));
        operationButtonPanel.add(createButton("Deactivate", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deactivateMember();
            }
        }));
        operationButtonPanel.add(createButton("Mark Attendance", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAttendance();
            }
        }));
        operationButtonPanel.add(createButton("Upgrade Plan", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                upgradePlan();
            }
        }));
        operationButtonPanel.add(createButton("Calculate Discount", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateDiscount();
            }
        }));
        operationButtonPanel.add(createButton("Revert Regular", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revertRegularMember();
            }
        }));
        operationButtonPanel.add(createButton("Revert Premium", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revertPremiumMember();
            }
        }));
        operationButtonPanel.add(createButton("Pay Due Amount", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payDueAmount();
            }
        }));
        operationButtonPanel.add(createButton("Display All", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllMembers();
            }
        }));
          operationButtonPanel.add(createButton("Read From File", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllMembers();
            }
        }));
        operationButtonPanel.add(createButton("Clear", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        }));
        operationButtonPanel.add(createButton("Save to File", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        }));
        operationButtonPanel.add(createButton("Read from File", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFromFile();
            }
        }));
        buttonPanel.add(operationButtonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Display area
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        updateFieldAccess();
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

private void updateFieldAccess() {
    boolean isRegular = regularButton.isSelected();

    referralField.setEnabled(isRegular);
    planCombo.setEnabled(false); // Always disable the plan combo box
    regularPriceField.setEnabled(isRegular);

    trainerField.setEnabled(!isRegular);
    paidAmountField.setEnabled(false); // Always disabled
    premiumPriceField.setEnabled(!isRegular);
    discountField.setEnabled(!isRegular);
    
    // Clear and set appropriate prices when switching member types
    if (isRegular) {
        paidAmountField.setText("");
        updateRegularPrice();
    } else {
        premiumPriceField.setText("50000");
        paidAmountField.setText(""); // Keep empty until member is added
    }
}
private String getFormattedDate(JComboBox<Integer> yearBox, JComboBox<String> monthBox, JComboBox<Integer> dayBox) {
    int year = (int) yearBox.getSelectedItem();
    int month = monthBox.getSelectedIndex() + 1; // Calendar months are 0-based
    int day = (int) dayBox.getSelectedItem();

    // Format year with leading zeros
    String yearStr = String.valueOf(year);
    while (yearStr.length() < 4) {
        yearStr = "0" + yearStr;
    }
    
    // Format month with leading zero
    String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
    
    // Format day with leading zero
    String dayStr = (day < 10) ? "0" + day : String.valueOf(day);
    
    return yearStr + "-" + monthStr + "-" + dayStr;
}
    private void addRegularMember() {
    try {
        // Validate required fields
        if (idField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                locationField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                (!maleRadio.isSelected() && !femaleRadio.isSelected()) ||
                referralField.getText().trim().isEmpty()) {

            StringBuilder missingFields = new StringBuilder("Please fill the following fields:\n");
            if (idField.getText().trim().isEmpty())
                missingFields.append("- ID\n");
            if (nameField.getText().trim().isEmpty())
                missingFields.append("- Name\n");
            if (locationField.getText().trim().isEmpty())
                missingFields.append("- Location\n");
            if (phoneField.getText().trim().isEmpty())
                missingFields.append("- Phone\n");
            if (emailField.getText().trim().isEmpty())
                missingFields.append("- Email\n");
            if (!maleRadio.isSelected() && !femaleRadio.isSelected())
                missingFields.append("- Gender\n");
            if (referralField.getText().trim().isEmpty())
                missingFields.append("- Referral Source\n");

            JOptionPane.showMessageDialog(this, missingFields.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = maleRadio.isSelected() ? "Male" : "Female";
        String dob = getFormattedDate(yearCombo, monthCombo, dayCombo);
        String startDate = getFormattedDate(msYearCombo, msMonthCombo, msDayCombo);
        String referral = referralField.getText().trim();
        String plan = (String) planCombo.getSelectedItem();
        double price = RegularMember.getPlanPrice(plan);

        // Check for duplicate ID
        for (GymMember member : members) {
            if (member.getId() == id) {
                JOptionPane.showMessageDialog(this, "Member ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        RegularMember member = new RegularMember(id, name, location, phone, email,
                gender, dob, startDate, referral, plan);
        members.add(member);

        displayArea.append("Regular member added: " + name + " (ID: " + id + ")\n");
        paidAmountField.setText(String.valueOf((int)price));
        JOptionPane.showMessageDialog(this, "Regular member added successfully!\nPaid amount: Rs. " + (int)price, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID must be a number", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error adding member: " + e.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
 private void addPremiumMember() {
    try {
        // Check if all required fields are filled
        if (idField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                locationField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                (!maleRadio.isSelected() && !femaleRadio.isSelected()) ||
                trainerField.getText().trim().isEmpty()) {

            StringBuilder missingFields = new StringBuilder("Please fill the following fields:\n");
            if (idField.getText().trim().isEmpty())
                missingFields.append("- ID\n");
            if (nameField.getText().trim().isEmpty())
                missingFields.append("- Name\n");
            if (locationField.getText().trim().isEmpty())
                missingFields.append("- Location\n");
            if (phoneField.getText().trim().isEmpty())
                missingFields.append("- Phone\n");
            if (emailField.getText().trim().isEmpty())
                missingFields.append("- Email\n");
            if (!maleRadio.isSelected() && !femaleRadio.isSelected())
                missingFields.append("- Gender\n");
            if (trainerField.getText().trim().isEmpty())
                missingFields.append("- Trainer's Name\n");

            JOptionPane.showMessageDialog(this, missingFields.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = maleRadio.isSelected() ? "Male" : "Female";
        String dob = getFormattedDate(yearCombo, monthCombo, dayCombo);
        String startDate = getFormattedDate(msYearCombo, msMonthCombo, msDayCombo);
        String trainer = trainerField.getText().trim();
        
        // Set premium price
        double paidAmount = 50000;

        // Check for duplicate ID
        for (GymMember member : members) {
            if (member.getId() == id) {
                JOptionPane.showMessageDialog(this, "Member ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        PremiumMember member = new PremiumMember(id, name, location, phone, email,
                gender, dob, startDate, trainer, paidAmount);
        members.add(member);

        // Only set the paid amount field after successful addition
        paidAmountField.setText(String.valueOf((int)paidAmount));
        
        displayArea.append("Premium member added: " + name + " (ID: " + id + ")\n");
        JOptionPane.showMessageDialog(this, "Premium member added successfully!\nPaid amount: Rs. 50,000", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID", "Error",
                JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error adding member: " + e.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    private void activateMember() {
        String idText = memberIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memberId = Integer.parseInt(idText);
            for (GymMember member : members) {
                if (member.getId() == memberId) {
                    if (member.isActiveStatus()) {
                        JOptionPane.showMessageDialog(this, "Member is already active", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        member.activateMembership();
                        displayArea.append("Member " + memberId + " activated\n");
                        JOptionPane.showMessageDialog(this, "Member activated successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Member not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deactivateMember() {
        String idText = memberIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memberId = Integer.parseInt(idText);
            for (GymMember member : members) {
                if (member.getId() == memberId) {
                    if (!member.isActiveStatus()) {
                        JOptionPane.showMessageDialog(this, "Member is already inactive", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        member.deactivateMembership();
                        displayArea.append("Member " + memberId + " deactivated\n");
                        JOptionPane.showMessageDialog(this, "Member deactivated successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Member not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAttendance() {
        String idText = memberIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memberId = Integer.parseInt(idText);
            for (GymMember member : members) {
                if (member.getId() == memberId) {
                    if (!member.isActiveStatus()) {
                        JOptionPane.showMessageDialog(this, "Cannot mark attendance for inactive member", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    member.markAttendance();
                    displayArea.append("Attendance marked for member " + memberId + "\n");
                    JOptionPane.showMessageDialog(this, "Attendance marked successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Member not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void upgradePlan() {
    String idText = memberIdField.getText().trim();
    if (idText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int memberId = Integer.parseInt(idText);
        for (GymMember member : members) {
            if (member.getId() == memberId && member instanceof RegularMember) {
                RegularMember regMember = (RegularMember) member;
                
                String[] possiblePlans = {
                    "Basic - Rs. 6,500", 
                    "Standard - Rs. 12,500", 
                    "Deluxe - Rs. 18,500"
                };
                
                // Getting current plan with price
                String currentPlan = regMember.getPlan();
                String currentPlanWithPrice = currentPlan + " - Rs. " + 
                    (int)RegularMember.getPlanPrice(currentPlan);
                
                String newPlanWithPrice = (String) JOptionPane.showInputDialog(
                    this,
                    "Select new plan (Current: " + currentPlanWithPrice + "):",
                    "Upgrade Plan",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possiblePlans,
                    currentPlanWithPrice
                );
                
                if (newPlanWithPrice == null) {
                    return;
                }
                
                String newPlan = newPlanWithPrice.split(" - ")[0];
                
                // Checking attendance requirements
                int attendance = regMember.getAttendanceCount();
                if (newPlan.equals("Standard") && attendance < 30) {
                    JOptionPane.showMessageDialog(this, 
                        "Cannot upgrade to Standard plan: Minimum 30 attendance required", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (newPlan.equals("Deluxe") && attendance < 60) {
                    JOptionPane.showMessageDialog(this, 
                        "Cannot upgrade to Deluxe plan: Minimum 60 attendance required", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String result = regMember.upgradePlan(newPlan);
                double newPrice = RegularMember.getPlanPrice(newPlan);
                
                displayArea.append("Plan update for member " + memberId + ": " + result + 
                                 " (Paid price: Rs. " + (int)newPrice + ")\n");
                JOptionPane.showMessageDialog(this, 
                    result + "\nPaid price: Rs. " + (int)newPrice, 
                    "Plan Update", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Regular member not found", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void calculateDiscount() {
        String idText = memberIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memberId = Integer.parseInt(idText);
            for (GymMember member : members) {
                if (member.getId() == memberId && member instanceof PremiumMember) {
                    PremiumMember premMember = (PremiumMember) member;
                    premMember.calculateDiscount();
                    displayArea.append("Discount calculated for member " + memberId + ": "
                            + premMember.getDiscountAmount() + "\n");
                    JOptionPane.showMessageDialog(this, "Discount calculated: " + premMember.getDiscountAmount(),
                            "Discount", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Premium member not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 private void revertRegularMember() {
    String idText = memberIdField.getText().trim();
    if (idText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int memberId = Integer.parseInt(idText);
        for (GymMember member : members) {
            if (member.getId() == memberId && member instanceof RegularMember) {
              
                JOptionPane.showMessageDialog(this, "Regular member reverted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Regular member not found", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void revertPremiumMember() {
    String idText = memberIdField.getText().trim();
    if (idText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int memberId = Integer.parseInt(idText);
        for (GymMember member : members) {
            if (member.getId() == memberId && member instanceof PremiumMember) {
                // Show input dialog for removal reason
                String reason = JOptionPane.showInputDialog(
                    this, 
                    "Enter removal reason:", 
                    "Revert Premium Member", 
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (reason == null) {
                    return; // User cancelled
                }
                
                if (reason.trim().isEmpty()) {
                    reason = "No reason provided";
                }

                ((PremiumMember) member).revertPremiumMember(reason);
                displayArea.append("Premium member " + memberId + " reverted. Reason: " + reason + "\n");
                JOptionPane.showMessageDialog(this, "Premium member reverted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Premium member not found", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Wrong ID format", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void payDueAmount() {
        String idText = memberIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memberId = Integer.parseInt(idText);
            for (GymMember member : members) {
                if (member.getId() == memberId && member instanceof PremiumMember) {
                    PremiumMember premMember = (PremiumMember) member;
                    double amount = Double.parseDouble(paidAmountField.getText().trim());
                    String result = premMember.payDueAmount(amount);
                    displayArea.append("Payment for member " + memberId + ": " + result + "\n");
                    JOptionPane.showMessageDialog(this, result, "Payment", JOptionPane.INFORMATION_MESSAGE);
                    paidAmountField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Premium member not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong number format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAllMembers() {
        if (members.isEmpty()) {
            displayArea.setText("No members registered\n");
            return;
        }

        // Creating a new frame for displaying all members
        JFrame displayFrame = new JFrame("All Members");
        displayFrame.setSize(800, 600);

        JTextArea displayTextArea = new JTextArea();
        displayTextArea.setEditable(false);
        displayTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayTextArea);

        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL MEMBERS ===\n\n");

        for (GymMember member : members) {
            sb.append(member.toString()).append("\n\n");
        }

        displayTextArea.setText(sb.toString());
        displayFrame.add(scrollPane);
        displayFrame.setVisible(true);
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (GymMember member : members) {
                writer.println(member.toFileString());
            }
            JOptionPane.showMessageDialog(this, "Members saved to MemberDetails.txt successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("Regular")) {
                    RegularMember member = new RegularMember(
                            Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], parts[5],
                            parts[6], parts[7], parts[8], parts[9], parts[10]);
                    members.add(member);
                } else if (parts[0].equals("Premium")) {
                    PremiumMember member = new PremiumMember(
                            Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], parts[5],
                            parts[6], parts[7], parts[8], parts[9], Double.parseDouble(parts[10]));
                    members.add(member);
                }
            }

            displayArea.append("Members loaded from file\n");
            displayAllMembers();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

 private void clearFields() {
    idField.setText("");
    nameField.setText("");
    locationField.setText("");
    phoneField.setText("");
    emailField.setText("");
    genderGroup.clearSelection();
    yearCombo.setSelectedIndex(0);
    monthCombo.setSelectedIndex(0);
    dayCombo.setSelectedIndex(0);
    msYearCombo.setSelectedIndex(0);
    msMonthCombo.setSelectedIndex(0);
    msDayCombo.setSelectedIndex(0);
    referralField.setText("");
    trainerField.setText("");
    paidAmountField.setText("");
    planCombo.setSelectedItem("Basic");
}
  public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GymGUI();
            }
        });
    }
}
