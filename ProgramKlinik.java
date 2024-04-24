import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class Patient {
    private String name;
    private String address;
    private long nik;
    private Date birthDate;

    public Patient(String name, String address, long nik, Date birthDate) {
        this.name = name;
        this.address = address;
        this.nik = nik;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getNik() {
        return nik;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String formattedBirthDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        return dateFormat.format(birthDate);
    }
}

public class ProgramKlinik{
    private ArrayList<Patient> patientList = new ArrayList<>();
    private int currentIndex = -1;

    private JTextField nameField, addressField, nikField, birthDateField;
    private JTextArea displayArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProgramKlinik programKlinik = new ProgramKlinik();
            programKlinik.createAndShowGUI();
        });
    }
    
    private void createAndShowGUI() {
        JFrame frame = new JFrame("ProgramKlinik");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        displayArea = new JTextArea(10, 40);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        nameField = new JTextField(20);
        addressField = new JTextField(50);
        nikField = new JTextField(15);
        birthDateField = new JTextField(12);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("NIK:"));
        inputPanel.add(nikField);
        inputPanel.add(new JLabel("Birth Date (YYYY-MMM-DD):"));
        inputPanel.add(birthDateField);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatient();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
            }
        });

        JButton prevButton = new JButton("Previous");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousPatient();
            }
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextPatient();
            }
        });

        JButton listButton = new JButton("Patient List");
        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPatientList();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(listButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }
    private boolean isNikUnique(long nik) {
        for (Patient patient : patientList) {
            if (patient.getNik() == nik) {
                return false; // NIK sudah ada, bukan unik
            }
        }
        return true; // NIK adalah unik
    }

    private void addPatient() {
        for (int i = 0; i < 3; i++) {
            String name = nameField.getText();
            String address = addressField.getText();
            long nik = 0;
            Date birthDate = null;
    
            try {
                nik = Long.parseLong(nikField.getText());
                birthDate = parseDate(birthDateField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid NIK format.");
                return;
            }
    
            if (isNikUnique(nik)) {
                Patient patient = new Patient(name, address, nik, birthDate);
                patientList.add(patient);
                displayPatients();
                displayPatientDetails(); // Menampilkan rincian pasien yang baru ditambahkan
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "NIK must be unique.");
                return; // Jika NIK tidak unik, hentikan penambahan pasien
            }
        }
    }
    
    private void updatePatient() {
        if (currentIndex >= 0) {
            String name = nameField.getText();
            String address = addressField.getText();
            long nik = 0;
            Date birthDate = null;

            try {
                nik = Long.parseLong(nikField.getText());
                birthDate = parseDate(birthDateField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid NIK format.");
                return;
            }

            Patient patient = new Patient(name, address, nik, birthDate);
            patientList.set(currentIndex, patient);
            displayPatients();
        } else {
            JOptionPane.showMessageDialog(null, "No patient selected.");
        }
    }

    private void deletePatient() {
        if (currentIndex >= 0) {
            patientList.remove(currentIndex);
            currentIndex = -1;
            displayPatients();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "No patient selected.");
        }
    }

    private void showPreviousPatient() {
        if (currentIndex > 0) {
            currentIndex--;
            displayPatientDetails();
        }
    }

    private void showNextPatient() {
        if (currentIndex < patientList.size() - 1) {
            currentIndex++;
            displayPatientDetails();
        }
    }

    private void showPatientList() {
        displayArea.setText("");
        int count = 1;
        for (Patient patient : patientList) {
            displayArea.append(count + "\t" + patient.getName() + "\t" + patient.getNik() + "\t" + patient.formattedBirthDate() + "\t" + patient.getAddress() + "\n");
            count++;
        }
    }

    private void displayPatients() {
        displayArea.setText("");
        for (Patient patient : patientList) {
            displayArea.append("Name: " + patient.getName() + "\n");
            displayArea.append("Address: " + patient.getAddress() + "\n");
            displayArea.append("NIK: " + patient.getNik() + "\n");
            displayArea.append("Birth Date: " + patient.formattedBirthDate() + "\n");
            displayArea.append("\n");
        }
    }

    private void displayPatientDetails() {
        if (currentIndex >= 0 && currentIndex < patientList.size()) {
            Patient patient = patientList.get(currentIndex);
            nameField.setText(patient.getName());
            addressField.setText(patient.getAddress());
            nikField.setText(Long.toString(patient.getNik()));
            birthDateField.setText(patient.formattedBirthDate());
        }
    }

    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        nikField.setText("");
        birthDateField.setText("");
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Tangani kesalahan parsing tanggal di sini
            e.printStackTrace();
            return null;  // atau kembalikan nilai yang sesuai dalam kasus kesalahan
        }
    }

}
