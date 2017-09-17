package com.music.cms.model;

import com.music.cms.validator.ImageValidationMime;
import com.music.cms.validator.ImageValidationSize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Category Name cannot be empty!")
    private String name;

    @NotEmpty(message = "Category short description cannot be empty!")
    private String shortDescription;

    @NotNull(message = "At least one status must be selected!")
    private Boolean status;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(nullable=true)
    private byte[] image;

    @Transient
    @ImageValidationSize
    @ImageValidationMime
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", Name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
