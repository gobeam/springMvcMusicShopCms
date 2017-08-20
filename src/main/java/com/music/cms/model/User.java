package com.music.cms.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by alis on 8/3/17.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email(message = "Please provide valid email!")
    @NotEmpty(message = "Email cannot be empty!")
    private String email;

    @Size(min = 6,message = "Password cannot be less than 6 character!")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "First name cannot be blank!")
    private String first_name;

    @NotBlank(message = "Last name cannot be blank")
    private String last_name;

    @NotBlank(message = "Address cannot be blank!")
    private String address;

    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private String phone;

    @NotBlank(message = "Zip cannot be blank!")
    private String zip;

    @NotBlank(message = "Country cannot be blank!")
    private String country;

    @NotBlank(message = "City cannot be blank!")
    private String city;

    @NotNull(message = "Status must be specified!")
    private Boolean status;

    @Transient
//    @NotNull(message = "Role must be specified!")
    private Integer role_id;

   // @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", phone='" + phone + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", status=" + status +
                '}';
    }
}
