package sg.edu.nus.iss.stocktrackerbackend.models;

import java.time.LocalDate;

public class Account {

    private String accountId;
    private String name;
    private String username;
    private String mobileNo;
    private String password;
    private String nationality;
    private String address;
    private LocalDate dateOfBirth;
    
    public Account() {
    }

    public Account(String name, String username, String mobileNo, String password, String nationality, String address,
            LocalDate dateOfBirth) {
        this.name = name;
        this.username = username;
        this.mobileNo = mobileNo;
        this.password = password;
        this.nationality = nationality;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    
    public Account(String accountId, String name, String username, String mobileNo, String password, String nationality,
            String address, LocalDate dateOfBirth) {
        this.accountId = accountId;
        this.name = name;
        this.username = username;
        this.mobileNo = mobileNo;
        this.password = password;
        this.nationality = nationality;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", name=" + name + ", username=" + username + ", mobileNo="
                + mobileNo + ", password=" + password + ", nationality=" + nationality + ", address=" + address
                + ", dateOfBirth=" + dateOfBirth + "]";
    }

    
}
