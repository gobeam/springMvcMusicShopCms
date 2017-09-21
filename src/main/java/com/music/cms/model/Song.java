package com.music.cms.model;

import com.music.cms.validator.ImageValidationMime;
import com.music.cms.validator.ImageValidationSize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Title cannot be empty!")
    private String title;

    @NotEmpty(message = "Singer name cannot be empty!")
    private String singer;

    @NotEmpty(message = "Status must be specified!")
    private Boolean status;

    @NotNull(message = "Price field cannot be empty!")
    private Long price;

    @NotNull(message = "Stock must be specified!")
    private Integer stock;

    @Column(nullable = true)
    private Long discount;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(nullable=true)
    private byte[] image;

    @Transient
    @ImageValidationSize
    @ImageValidationMime
    private MultipartFile file;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
